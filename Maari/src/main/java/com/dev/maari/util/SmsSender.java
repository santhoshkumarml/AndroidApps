package com.dev.maari.util;

import android.app.PendingIntent;
import com.dev.maari.app.AppStateManager;
import com.dev.maari.model.StateInfo;
import com.dev.maari.model.TransactionLogInfo;

public class SmsSender implements Runnable {
  private final PendingIntent si, di;
  private final StateInfo stateInfo;
  private volatile boolean stop;

  public SmsSender(StateInfo stateInfo, PendingIntent si, PendingIntent di) {
    this.stateInfo = stateInfo;
    this.si = si;
    this.di = di;
  }

  @Override
  public void run() {
    while(!stop){
      TransactionLogInfo logInfo = stateInfo.getTransactionLogInfoQueue().poll();
      Utility.sendSMS(
          logInfo,
          stateInfo.getOwnerInfo(
              logInfo.getActorId()
          ),
          stateInfo.getAdminInfo(),
          //TODO: get the current logged in agent.
          stateInfo.getAgentInfo(
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
