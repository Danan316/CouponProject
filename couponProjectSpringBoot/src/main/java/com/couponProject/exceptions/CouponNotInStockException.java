package com.couponProject.exceptions;

public class CouponNotInStockException extends Exception{

	private static final long serialVersionUID = 1L;

	public CouponNotInStockException(String message) {
		super(message);
	}

	
}
