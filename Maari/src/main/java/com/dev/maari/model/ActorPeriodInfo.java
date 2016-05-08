package com.dev.maari.model;

import java.io.Serializable;

public class ActorPeriodInfo implements Serializable{
  ActorInfo actorInfo = new ActorInfo();
  Weekday weekday = Weekday.SUNDAY;

  public enum Weekday {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THRUSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
  }

  public ActorInfo getActorInfo() {
    return actorInfo;
  }

  public void setActorInfo(ActorInfo actorInfo) {
    this.actorInfo = actorInfo;
  }

  public Weekday getWeekday() {
    return weekday;
  }

  public void setWeekday(Weekday weekday) {
    this.weekday = weekday;
  }

  public static void set(ActorPeriodInfo aP, String fieldName, Object val) {
    if (fieldName.equalsIgnoreCase("name")) {
      aP.getActorInfo().setName((String) val);
    } else if (fieldName.equalsIgnoreCase("phoneno")) {
      aP.getActorInfo().setPhoneNo((String)val);
    } else if (fieldName.equalsIgnoreCase("emailid")) {
      aP.getActorInfo().setEmailId((String)val);
    } else if (fieldName.equalsIgnoreCase("org")) {
      aP.getActorInfo().setOrg((String)val);
    } else if (fieldName.equalsIgnoreCase("username")) {
      aP.getActorInfo().getAuthInfo().setUserName((String)val);
    } else if (fieldName.equalsIgnoreCase("password")) {
      aP.getActorInfo().getAuthInfo().setPassword((String)val);
    } else if (fieldName.equalsIgnoreCase("weekday")) {
      aP.setWeekday(Weekday.valueOf((String)val));
    }
  }
}
