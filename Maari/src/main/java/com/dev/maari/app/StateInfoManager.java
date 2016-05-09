package com.dev.maari.app;

import android.content.Context;
import com.dev.maari.model.MaariException;
import com.dev.maari.model.StateInfo;
import com.dev.maari.util.DAOManager;
import com.dev.maari.util.SmsSender;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class StateInfoManager {
  SmsSender smsSender;
  ScheduledExecutorService ex;
  StateInfo stateInfo;
  DAOManager daoManager;

  public StateInfo getStateInfo() {
    return this.stateInfo;
  }

  public void initialize(Context context) throws MaariException{
    this.stateInfo = new StateInfo();
    this.daoManager = new DAOManager(context);
    try {
      this.stateInfo.loadState(this.daoManager.getTransactionLogDao());
    } catch (SQLException e) {
      throw new MaariException(e.getMessage());
    }
    this.smsSender = new SmsSender(this, null, null);
    this.ex = Executors.newSingleThreadScheduledExecutor();
  }

  public void destroy() {
    this.smsSender.stop();
    this.ex.shutdown();
  }
}
