package com.couponProject.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.couponProject.repositories.CompanyRepository;
import com.couponProject.repositories.CouponRepository;
import com.couponProject.repositories.CustomerRepository;

@Service
@Transactional
public abstract class ClientService {
	
	@Autowired
	protected CouponRepository couponRepository;
	@Autowired
	protected CompanyRepository companyRepository;
	@Autowired
	protected CustomerRepository customerRepository;
	
	public ClientService() {}
	
	public abstract boolean login(String email, String password);
}
