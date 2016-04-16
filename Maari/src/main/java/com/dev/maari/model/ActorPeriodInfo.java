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

  public class ActorInfo implements Serializable{
    private String name;
    private String phoneNo;
    private String emailId;
    private String org;
    private ActorType actorType;
    private AuthInfo authInfo = new AuthInfo();

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getPhoneNo() {
      return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
      this.phoneNo = phoneNo;
    }

    public String getEmailId() {
      return emailId;
    }

    public void setEmailId(String emailId) {
      this.emailId = emailId;
    }

    public String getOrg() {
      return org;
    }

    public void setOrg(String org) {
      this.org = org;
    }

    public ActorType getActorType() {
      return actorType;
    }

    public void setActorType(ActorType actorType) {
      this.actorType = actorType;
    }

    public AuthInfo getAuthInfo() {
      return authInfo;
    }

    public void setAuthInfo(AuthInfo authInfo) {
      this.authInfo = authInfo;
    }
  }

  public enum ActorType {
    ADMIN,
    AGENT,
    OWNER
  }

  public class AuthInfo implements Serializable {
    private String userName;
    private String password;

    public String getUserName() {
      return userName;
    }

    public void setUserName(String userName) {
      this.userName = userName;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
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
    }  else if (fieldName.equalsIgnoreCase("username")) {
      aP.getActorInfo().getAuthInfo().setUserName((String)val);
    } else if (fieldName.equalsIgnoreCase("password")) {
      aP.getActorInfo().getAuthInfo().setPassword((String)val);
    } else if (fieldName.equalsIgnoreCase("weekday")) {
      aP.setWeekday(Weekday.valueOf((String)val));
    }
  }

}
