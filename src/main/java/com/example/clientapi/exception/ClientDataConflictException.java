package com.example.clientapi.exception;

public class ClientDataConflictException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ClientDataConflictException() {
		super();
	}
	
	public ClientDataConflictException(String message) {
		super(message);
	}
}