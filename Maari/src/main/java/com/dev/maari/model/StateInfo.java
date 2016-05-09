package com.dev.maari.model;

import com.dev.maari.util.Utility;
import com.j256.ormlite.dao.Dao;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StateInfo {
  private ConcurrentLinkedQueue<TransactionLogInfo> transactionLogInfoQueue = new ConcurrentLinkedQueue<TransactionLogInfo>();
  private Map<ActorInfo.ActorType, Map<String, ActorInfo>> actorInfoMap;

  public ConcurrentLinkedQueue<TransactionLogInfo> getTransactionLogInfoQueue() {
    return this.transactionLogInfoQueue;
  }

  public void loadState(Dao<TransactionLogInfo, Long> transactionLogDao) throws MaariException{
    this.actorInfoMap = Utility.initializeAndReadData();
    //Load all rows from the SQL.
    for (TransactionLogInfo logInfo : transactionLogDao) {
      transactionLogInfoQueue.offer(logInfo);
    }
  }

  public ActorPeriodInfo getOwnerInfo(String actorId) {
    return (ActorPeriodInfo)(actorInfoMap.get(ActorInfo.ActorType.OWNER).get(actorId));
  }

  public ActorInfo getAgentInfo(String actorId) {
    return actorInfoMap.get(ActorInfo.ActorType.AGENT).get(actorId);
  }

  public ActorInfo getAdminInfo() {
    return actorInfoMap.get(ActorInfo.ActorType.ADMIN).values().iterator().next();
  }
}
