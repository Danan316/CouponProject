package com.couponProject.exceptions;

public class CustomerAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public CustomerAlreadyExistsException(String message) {
		super(message);
	}
	

}
