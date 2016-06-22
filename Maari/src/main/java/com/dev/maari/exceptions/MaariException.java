package com.dev.maari.exceptions;

public class MaariException extends Exception{
  public MaariException(String msg, Throwable e) {
    super(msg, e);
  }
  public MaariException(Throwable e) {
    super(e);
  }
}
