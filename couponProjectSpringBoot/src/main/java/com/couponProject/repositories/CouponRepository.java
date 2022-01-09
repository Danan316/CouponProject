package com.couponProject.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.couponProject.entities.Category;
import com.couponProject.entities.Coupon;

@Component
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
	
	List<Coupon> findById(int ID);
	
	List<Coupon> findByCompanyId(int companyID);
	
	List<Coupon> findByTitle(String title);
	
//	@Query(value = "DELETE coupon FROM Coupons where coupon.endDate < ?1", nativeQuery = true)
//	List<Coupon> deleteExpiredCoupons(Date date);
	
	List<Coupon> findByIdAndCompanyId(int ID, int companyID);
	
	List<Coupon> findByCategory(Category category);
	
	List<Coupon> findByCompanyIdAndCategory(int companyID, Category category);
	
	List<Coupon> findByPriceLessThanEqual(double maxPrice);

	List<Coupon> findByCompanyIdAndPriceLessThanEqual(int companyID, double maxPrice);
	
	List<Coupon> findByPriceGreaterThanEqual(double minPrice);
	
	List<Coupon> findByCompanyIdAndPriceGreaterThanEqual(int companyID, double minPrice);
	
	List<Coupon> findByEndDateBefore(Date date);
	
}
