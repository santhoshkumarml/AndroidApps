package com.dev.maari.util;

import android.app.PendingIntent;
import com.dev.maari.app.StateInfoManager;
import com.dev.maari.model.TransactionLogInfo;

public class SmsSender implements Runnable {
  private final PendingIntent si, di;
  private final StateInfoManager stateInfoManager;
  private volatile boolean stop;

  public SmsSender(StateInfoManager stateInfoManager, PendingIntent si, PendingIntent di) {
    this.stateInfoManager = stateInfoManager;
    this.si = si;
    this.di = di;
  }

  @Override
  public void run() {
    while(!stop){
      TransactionLogInfo logInfo = this.stateInfoManager.getStateInfo().getTransactionLogInfoQueue().poll();
      Utility.sendSMS(
          logInfo,
          stateInfoManager.getStateInfo().getOwnerInfo(
              logInfo.getActorId()
          ),
          stateInfoManager.getStateInfo().getAdminInfo(),
          //TODO: get the current logged in agent.
          stateInfoManager.getStateInfo().getAgentInfo(
              logInfo.getActorId()
          ),
          si,
          di
      );
    }
  }

  public void stop() {
    this.stop = true;
  }
}
