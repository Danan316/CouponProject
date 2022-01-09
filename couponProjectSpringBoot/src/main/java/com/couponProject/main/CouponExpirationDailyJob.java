package com.couponProject.main;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.couponProject.entities.Coupon;
import com.couponProject.repositories.CouponRepository;

@Component
public class CouponExpirationDailyJob implements Runnable {

	private final long DAY_TIME = 24 * 60 * 60 * 1000; // 24 hours in miliseconds
	@Autowired
	private CouponRepository couponRepository;
	private boolean quit;

	public CouponExpirationDailyJob() {}

	public CouponExpirationDailyJob(boolean quit) {
		this.quit = false;
	}

	public boolean isQuit() {
		return quit;
	}

	public void setQuit(boolean quit) {
		this.quit = quit;
	}

	/**
	 * The thread is checking all the expired coupons and deletes
	 * them and their purchase histories, then sleeps for 24 hours.
	 */
	@Override
	public void run() {
		try {
			while (!quit) {
//				couponRepository.deleteExpiredCoupons(Calendar.getInstance().getTime());
				for (Coupon coupon : couponRepository.findByEndDateBefore(Calendar.getInstance().getTime())) {
					couponRepository.delete(coupon);
					System.out.println("The coupon with ID " + coupon.getId() + " has been deleted successfully");
				}
				System.out.println("Job has deleted expired coupons!");
				Thread.sleep(DAY_TIME);
			}
		} catch (InterruptedException e) {}
	}

	/**
	 * Stops the threat and notifying about it.
	 */
	public void stop() {
		setQuit(true);
		this.notify();
	}
}