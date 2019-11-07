package com.example.demo;

public class JSONErrorMessage extends Throwable {
  private static final long serialVersionUID = -7586228912893121158L;

  public JSONErrorMessage(String string) {
    super(string);
  }
}