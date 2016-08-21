package com.dev.maari.util;

import android.app.PendingIntent;
import android.util.Log;
import com.dev.maari.app.AppStateManager;
import com.dev.maari.exceptions.MaariException;
import com.dev.maari.model.TransactionLogInfo;

public final class SmsSender implements Runnable {
  private static final String LOG_TAG = SmsSender.class.getCanonicalName();
  private final PendingIntent si, di;
  private final AppStateManager appStateManager;
  private volatile boolean stop;

  public SmsSender(AppStateManager appStateManager, PendingIntent si, PendingIntent di) {
    this.appStateManager = appStateManager;
    this.si = si;
    this.di = di;
  }

  @Override
  public void run() {
    while (!stop) {
      try {
        appStateManager.pollTransactionLogInfoAndDoAction(new TransactionLogInfoPollingHandler() {
          @Override
          public boolean handleTransactionLogPollingAction(TransactionLogInfo logInfo) {
            Utility.sendSMS(
                logInfo,
                appStateManager.getStateInfo().getOwnerInfo(
                    logInfo.getActorId()
                ),
                appStateManager.getStateInfo().getAdminInfo(),
                //TODO: get the current logged in agent.
                appStateManager.getStateInfo().getAgentInfo(
                    logInfo.getActorId()
                ),
                si,
                di
            );
            return true;
          }
        });
      } catch (MaariException e) {
        Log.e(LOG_TAG, e.getMessage(), e);
        throw new RuntimeException(e);
      }
    }
  }

  public void stop() {
    this.stop = true;
  }

}