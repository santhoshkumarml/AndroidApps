package com.dev.maari.model;

import com.dev.maari.exceptions.MaariException;
import com.dev.maari.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StateInfo {
  private Map<ActorInfo.ActorType, Map<String, ActorInfo>> actorInfoMap;
  private Map<ActorPeriodInfo.Weekday, Set<String>> weekdayToOwnerIdMap;

  public void StateInfo() throws MaariException {
    actorInfoMap = Utility.initializeAndReadData();
    weekdayToOwnerIdMap = new HashMap<ActorPeriodInfo.Weekday, Set<String>>();
    for (ActorInfo actorInfo : actorInfoMap.get(ActorInfo.ActorType.OWNER).values()) {
      ActorPeriodInfo actorPeriodInfo =  (ActorPeriodInfo) actorInfo;
      Set<String> ownerIds = weekdayToOwnerIdMap.get(actorPeriodInfo.getWeekday());
      if (ownerIds == null) {
        ownerIds = new HashSet<String>();
      }
      ownerIds.add(actorPeriodInfo.getActorId());
      weekdayToOwnerIdMap.put(actorPeriodInfo.getWeekday(), ownerIds);
    }
  }

  public ActorPeriodInfo getOwnerInfo(String actorId) {
    return (ActorPeriodInfo)(actorInfoMap.get(ActorInfo.ActorType.OWNER).get(actorId));
  }

  public List<ActorInfo> getOwnersForWeekday(ActorPeriodInfo.Weekday weekday) {
    Set<String> ownerIds = weekdayToOwnerIdMap.get(weekday);
    List<ActorInfo> actorInfos = new ArrayList<ActorInfo>();
    for (String ownerId : ownerIds) {
      actorInfos.add(actorInfoMap.get(ActorInfo.ActorType.OWNER).get(ownerId));
    }
    return actorInfos;
  }

  public ActorInfo getAgentInfo(String actorId) {
    return actorInfoMap.get(ActorInfo.ActorType.AGENT).get(actorId);
  }

  public ActorInfo getAdminInfo() {
    return actorInfoMap.get(ActorInfo.ActorType.ADMIN).values().iterator().next();
  }
}
