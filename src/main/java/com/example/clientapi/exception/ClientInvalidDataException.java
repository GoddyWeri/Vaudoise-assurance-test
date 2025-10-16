package com.example.clientapi.exception;

public class ClientInvalidDataException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ClientInvalidDataException() {
		super();
	}
	
	public ClientInvalidDataException(String message) {
		super(message);
	}
}
