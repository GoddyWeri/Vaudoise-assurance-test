package com.example.clientapi.exception;

public class ClientDataBaseInfoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ClientDataBaseInfoException() {
		super();
	}
	
	public ClientDataBaseInfoException(String message) {
		super(message);
	}
}
