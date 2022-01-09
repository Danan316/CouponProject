package com.couponProject.exceptions;

public class UnsuccessfulLoginException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public UnsuccessfulLoginException(String message) {
		super(message);
	}

}
