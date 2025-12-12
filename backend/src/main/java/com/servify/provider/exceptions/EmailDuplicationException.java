package com.servify.provider.exceptions;

public class EmailDuplicationException extends RuntimeException {
  public EmailDuplicationException(String message) {
    super(message);
  }
}
