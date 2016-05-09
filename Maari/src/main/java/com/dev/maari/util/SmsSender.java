package com.dev.maari.util;

import android.app.PendingIntent;
import com.dev.maari.app.StateInfoManager;
import com.dev.maari.model.ActorInfo;
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
          this.stateInfoManager.getStateInfo().getActorInfo(
              ActorInfo.ActorType.OWNER,
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
