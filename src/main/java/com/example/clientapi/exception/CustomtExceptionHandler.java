package com.example.clientapi.exception;

import java.time.OffsetDateTime;

import org.hibernate.QueryTimeoutException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.UnexpectedTypeException;

@RestControllerAdvice
public class CustomtExceptionHandler {
		
	private ProblemDetail problem(HttpStatus status, String detail) {
		ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
		pd.setProperty("timestamp", OffsetDateTime.now());
		return pd;
		}
	
	@ExceptionHandler(ClientInvalidDataException.class)
	ResponseEntity<ProblemDetail> handleClientInvalid(ClientInvalidDataException ex) {
	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(problem(HttpStatus.NOT_ACCEPTABLE, ex.getMessage()));
	}

	@ExceptionHandler(ClientDataBaseInfoException.class)
	ResponseEntity<ProblemDetail> handleClientDb(ClientDataBaseInfoException ex) {
	return ResponseEntity.status(HttpStatus.CONFLICT).body(problem(HttpStatus.CONFLICT, ex.getMessage()));
	}


	@ExceptionHandler(DataIntegrityViolationException.class)
	ResponseEntity<ProblemDetail> handleIntegrity(DataIntegrityViolationException ex) {
	return ResponseEntity.status(HttpStatus.CONFLICT)
	.body(problem(HttpStatus.CONFLICT, rootMessage(ex, "Integrity constraint violation")));
	}

	@ExceptionHandler({ QueryTimeoutException.class})
	ResponseEntity<ProblemDetail> handleTransient(DataAccessException ex) {
	return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	.body(problem(HttpStatus.SERVICE_UNAVAILABLE, "Temporary database issue. Please try again."));
	}


	@ExceptionHandler(DataAccessException.class)
	ResponseEntity<ProblemDetail> handleDataAccess(DataAccessException ex) {
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	.body(problem(HttpStatus.INTERNAL_SERVER_ERROR, "Database error"));
	}


	@ExceptionHandler(Exception.class)
	ResponseEntity<ProblemDetail> handleUnknown(Exception ex) {
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	.body(problem(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error"));
	}
	
	
	private String rootMessage(Throwable ex, String fallback) {
	Throwable root = ex;
	while (root.getCause() != null) root = root.getCause();
	return (root.getMessage() != null && !root.getMessage().isBlank()) ? root.getMessage() : fallback;
	}
	
}
