package com.hsbc.boardie.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Login or user you follow not found!")
public class UserNotFoundException extends IllegalArgumentException {
}
