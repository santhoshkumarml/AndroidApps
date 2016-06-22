package com.dev.maari.model;

public class ActorPeriodInfo extends ActorInfo {
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
