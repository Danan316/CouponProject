package com.couponProject.exceptions;

public class CouponAlreadyExpiredException extends Exception {

	private static final long serialVersionUID = 1L;

	public CouponAlreadyExpiredException(String message) {
		super(message);
	}
	
	

}
