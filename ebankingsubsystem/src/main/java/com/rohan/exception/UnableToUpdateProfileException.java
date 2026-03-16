package com.rohan.exception;

public class UnableToUpdateProfileException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnableToUpdateProfileException(String msg) {
		super(msg);
	}
}
