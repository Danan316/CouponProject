package com.couponProject.exceptions;

public class CouponNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public CouponNotFoundException(String message) {
		super(message);
	}

}
