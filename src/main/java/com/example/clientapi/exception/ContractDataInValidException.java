package com.example.clientapi.exception;

public class ContractDataInValidException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ContractDataInValidException() {
		super();
	}
	
	public ContractDataInValidException(String message) {
		super(message);
	}
}
