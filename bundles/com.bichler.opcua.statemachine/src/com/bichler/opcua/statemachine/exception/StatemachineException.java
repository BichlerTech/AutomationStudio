package com.bichler.opcua.statemachine.exception;

public class StatemachineException extends Exception{

	private static final long serialVersionUID = -1836773311013353102L;

	public StatemachineException(Throwable cause) {
		super(cause);
	}
	
	public StatemachineException(String message) {
		super(message);
	}
}
