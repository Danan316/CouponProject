package com.couponProject.exceptions;

public class CouponAlreadyPurchasedException extends Exception {

	private static final long serialVersionUID = 1L;

	public CouponAlreadyPurchasedException(String message) {
		super(message);
	}

	
}
