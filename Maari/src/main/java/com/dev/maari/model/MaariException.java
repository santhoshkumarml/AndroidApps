package com.dev.maari.model;

public class MaariException extends Exception{
  public MaariException(String msg, Throwable e) {
    super(msg, e);
  }
  public MaariException(Throwable e) {
    super(e);
  }
}
