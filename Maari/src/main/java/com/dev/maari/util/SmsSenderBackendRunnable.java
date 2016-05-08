/**
 * Copyright 2016 StreamSets Inc.
 * <p>
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dev.maari.util;

import android.app.PendingIntent;
import com.dev.maari.app.StateInfoManager;
import com.dev.maari.model.ActorInfo;
import com.dev.maari.model.TransactionLogInfo;

public class SmsSenderBackendRunnable implements Runnable {
  private PendingIntent si, di;
  private volatile boolean stop;
  private StateInfoManager stateInfoManager;

  public SmsSenderBackendRunnable(StateInfoManager stateInfoManager, PendingIntent si, PendingIntent di) {
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
