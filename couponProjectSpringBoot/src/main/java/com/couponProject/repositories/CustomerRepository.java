package com.couponProject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.couponProject.entities.Customer;

@Component
public interface CustomerRepository extends JpaRepository<Customer, Integer>  {
		
	Customer findByEmailAndPassword(String email, String password);
	
	List<Customer> findByEmail(String email);
	
	List<Customer> findById(int id);
	
}
