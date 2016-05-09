package com.dev.maari.app;

import android.content.Context;
import com.dev.maari.model.MaariException;
import com.dev.maari.model.StateInfo;
import com.dev.maari.util.SmsSender;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppStateManager {
  SmsSender smsSender;
  ScheduledExecutorService ex;
  StateInfo stateInfo;
  DAOManager daoManager;

  public void initialize(Context context) throws MaariException{
    stateInfo = new StateInfo();
    daoManager = new DAOManager(context);
    try {
      stateInfo.loadState(this.daoManager.getTransactionLogDao());
    } catch (SQLException e) {
      throw new MaariException(e);
    }
    smsSender = new SmsSender(stateInfo, null, null);
    ex = Executors.newSingleThreadScheduledExecutor();
  }

  public void destroy() {
    smsSender.stop();
    ex.shutdown();
  }
}
