package com.dev.maari.model;

import com.dev.maari.util.Utility;
import com.j256.ormlite.dao.Dao;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StateInfo {
  private ConcurrentLinkedQueue<TransactionLogInfo> transactionLogInfoQueue = new ConcurrentLinkedQueue<TransactionLogInfo>();
  private Map<ActorInfo.ActorType, Map<String, ActorPeriodInfo>> actorPeriodInfoMap;

  public ConcurrentLinkedQueue<TransactionLogInfo> getTransactionLogInfoQueue() {
    return this.transactionLogInfoQueue;
  }

  public void loadState(Dao<TransactionLogInfo, Long> transactionLogDao) throws MaariException{
    this.actorPeriodInfoMap = Utility.initializeAndReadData();
    //Load all rows from the SQL.
    for (TransactionLogInfo logInfo : transactionLogDao) {
      transactionLogInfoQueue.offer(logInfo);
    }
  }

  public ActorPeriodInfo getActorInfo(ActorInfo.ActorType type, String actorId) {
    return this.actorPeriodInfoMap.get(type).get(actorId);
  }
}
