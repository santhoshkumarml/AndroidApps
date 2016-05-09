package com.dev.maari.model;

import java.io.Serializable;

public class ActorPeriodInfo extends ActorInfo{
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

  public Weekday getWeekday() {
    return weekday;
  }

  public void setWeekday(Weekday weekday) {
    this.weekday = weekday;
  }
}
