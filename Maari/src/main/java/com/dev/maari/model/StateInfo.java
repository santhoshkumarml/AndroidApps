package com.dev.maari.model;

import com.dev.maari.util.Utility;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StateInfo {
  private ConcurrentLinkedQueue<TransactionLogInfo> transactionLogInfoQueue = new ConcurrentLinkedQueue<TransactionLogInfo>();
  private Map<ActorInfo.ActorType, Map<String, ActorPeriodInfo>> actorPeriodInfoMap;

  public ConcurrentLinkedQueue<TransactionLogInfo> getTransactionLogInfoQueue() {
    return this.transactionLogInfoQueue;
  }

  public void loadState() {
    this.actorPeriodInfoMap = Utility.initializeAndReadData();
    //TODO: add initialization for loading transactionLog;
    //transactionLogInfoQueue
  }

  public ActorPeriodInfo getActorInfo(ActorInfo.ActorType type, String actorId) {
    return this.actorPeriodInfoMap.get(type).get(actorId);
  }
}
