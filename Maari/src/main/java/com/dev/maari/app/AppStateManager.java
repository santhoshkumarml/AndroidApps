package com.dev.maari.app;

import android.content.Context;
import android.util.Log;
import com.dev.maari.exceptions.MaariException;
import com.dev.maari.model.StateInfo;
import com.dev.maari.model.TransactionLogInfo;
import com.dev.maari.util.SmsSender;
import com.dev.maari.util.TransactionLogInfoPollingHandler;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppStateManager {
  private static final String LOG_TAG = AppStateManager.class.getCanonicalName();
  private SmsSender smsSender;
  private ScheduledExecutorService ex;
  private Dao<TransactionLogInfo, Long> transactionLogDao;
  private StateInfo stateInfo;

  void initialize(Context context) throws MaariException {
    stateInfo = new StateInfo();
    DAOManager daoManager = new DAOManager(context);
    try {
      transactionLogDao = daoManager.getTransactionLogDao();
    } catch (SQLException e) {
      throw new MaariException(e);
    }
    //TODO: Pending intent for sms
    smsSender = new SmsSender(this, null, null);
    ex = Executors.newSingleThreadScheduledExecutor();
    ex.scheduleAtFixedRate(smsSender, 10, 10, TimeUnit.SECONDS);
  }

  public StateInfo getStateInfo() {
    return stateInfo;
  }

  public synchronized void offerToTransactionLogQueue(TransactionLogInfo logInfo) throws MaariException {
    try {
      transactionLogDao.create(logInfo);
    } catch (SQLException e) {
      throw new MaariException(e);
    }
  }

  public synchronized void pollTransactionLogInfoAndDoAction(TransactionLogInfoPollingHandler handler) throws MaariException {
    TransactionLogInfo transactionLogInfo = null;
    boolean isActionSuccess = false;
    Iterator<TransactionLogInfo> transactionLogInfoIterator = transactionLogDao.iterator();
    try {
      if (transactionLogInfoIterator.hasNext()) {
        transactionLogInfo = transactionLogInfoIterator.next();
        isActionSuccess  = handler.handleTransactionLogPollingAction(transactionLogInfo);
      }
    } catch (Exception e) {
      throw new MaariException(e);
    } finally {
      if (isActionSuccess) {
        try {
          transactionLogDao.delete(Collections.singletonList(transactionLogInfo));
        } catch (SQLException e) {
          Log.w(LOG_TAG, e);
        }
      }
    }
  }

  void destroy() {
    smsSender.stop();
    ex.shutdown();
  }
}
