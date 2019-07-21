package com.hsbc.boardie.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "You cannot follow yourself! :)!")
public class BadFollowException extends IllegalArgumentException {
}
