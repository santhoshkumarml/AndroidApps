package com.dev.maari.app;

import com.dev.maari.model.StateInfo;
import com.dev.maari.util.SmsSenderBackendRunnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class StateInfoManager {
  SmsSenderBackendRunnable smsSender;
  ScheduledExecutorService ex;
  StateInfo stateInfo;

  public StateInfo getStateInfo() {
    return this.stateInfo;
  }

  public void initialize() {
    this.stateInfo = new StateInfo();
    this.stateInfo.loadState();
    this.smsSender = new SmsSenderBackendRunnable(this, null, null);
    this.ex = Executors.newSingleThreadScheduledExecutor();
  }

  public void destroy() {
    this.smsSender.stop();
    this.ex.shutdown();
  }
}
