package com.mobile.group.tlu_contact_be.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomException extends RuntimeException {

  private HttpStatus status = HttpStatus.BAD_REQUEST;

  private Object data;

  private String[] errors = new String[]{};

  public CustomException(String message) {
        super(message);
  }

  public CustomException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public CustomException(String message, HttpStatus status, String[] errors) {
    super(message);
    this.status = status;
    this.errors = errors;
  }

  public CustomException(Object data, String message) {
    super(message);
    this.data = data;
  }

  public CustomException(Object data, String message, HttpStatus status) {
    super(message);
    this.status = status;
    this.data = data;
  }

  public CustomException(Object data, String message, HttpStatus status, String[] errors) {
    super(message);
    this.status = status;
    this.errors = errors;
    this.data = data;
  }
}
