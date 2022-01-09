package com.couponProject.services;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.couponProject.entities.Category;
import com.couponProject.entities.Company;
import com.couponProject.entities.Coupon;
import com.couponProject.exceptions.CouponAlreadyExistsException;
import com.couponProject.exceptions.CouponNotFoundException;

@Scope("prototype")
@Service
@Transactional
public class CompanyService extends ClientService {
	
	private int companyID;
	
	public CompanyService(int companyID) {
		this.companyID = companyID;
	}
	
	public CompanyService() {}
	
	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}
	
	@Override
	public boolean login(String email, String password) {
		Company company = companyRepository.findByEmailAndPassword(email, password);
		if(company == null) {
			return false;
		}
		setCompanyID(company.getId());
		return true;
	}
	
	
	public void addCoupon(Coupon coupon) throws CouponAlreadyExistsException, CouponNotFoundException {
		List<Coupon> check = couponRepository.findByTitle(coupon.getTitle());
		if(!check.isEmpty()) {
			throw new CouponAlreadyExistsException("There is already a coupon with the same title!");
		}
		couponRepository.save(coupon);
		System.out.println("The coupon with ID " + coupon.getId() + " has been added successfully to the company "
				+ getCompanyDetails().getName());
	}
	
	public void updateCoupon(Coupon coupon) throws CouponNotFoundException, CouponAlreadyExistsException {
		List<Coupon> check = couponRepository.findByIdAndCompanyId(coupon.getId(), coupon.getCompany());
		if(check.isEmpty()) {
			throw new CouponNotFoundException("The coupon that you tried to update doesn't exist.");
		}
		List<Coupon> checkTitle = couponRepository.findByTitle(coupon.getTitle());
		if(!checkTitle.isEmpty()) {
			throw new CouponAlreadyExistsException("There is already a coupon with the same title!");
		}
		couponRepository.save(coupon);
		System.out.println("\nThe coupon with ID " + coupon.getId() + " has been updated successfully"
						 + " in the company " + getCompanyDetails().getName() + "\n");
	}
	
	public void deleteCoupon(int couponID) throws CouponNotFoundException {
		Coupon coupon = getOneCoupon(couponID);
		couponRepository.delete(coupon);
		System.out.println("The coupon with ID " + coupon.getId() + " has been deleted successfully "
						 + "in the company " + getCompanyDetails().getName());
	}
	
	public Coupon getOneCoupon(int couponID) throws CouponNotFoundException {
		List<Coupon> check = couponRepository.findByIdAndCompanyId(couponID, companyID);
		if (check.isEmpty()) {
			throw new CouponNotFoundException("The coupon that you tried to get doesn't exist in this company.");
		}
		return couponRepository.getById(couponID);
	}
	
	public List<Coupon> getCompanyCoupons() {
		return couponRepository.findByCompanyId(companyID);
	}

	public List<Coupon> getCompanyCoupons(Category category) {
		return couponRepository.findByCompanyIdAndCategory(companyID, category);
	}

	public List<Coupon> getCompanyCoupons(double maxPrice) {
		return couponRepository.findByCompanyIdAndPriceLessThanEqual(companyID, maxPrice);
	}

	public List<Coupon> getCompanyCouponsByMinPrice(double minPrice) {
		return couponRepository.findByCompanyIdAndPriceGreaterThanEqual(companyID, minPrice);
	}
	
	public Company getCompanyDetails() {
		return companyRepository.getById(companyID);
	}
}