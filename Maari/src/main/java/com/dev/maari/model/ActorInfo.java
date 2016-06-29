package com.dev.maari.model;

import java.io.Serializable;

public class ActorInfo implements Serializable {
  private String actorId;
  private String name;
  private String phoneNo;
  private String emailId;
  private String org;
  private ActorType actorType;
  private AuthInfo authInfo = new AuthInfo();

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

  public String getActorId() {
    return this.actorId;
  }

  public void setActorId(String actorId) {
    this.actorId = actorId;
  }
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


