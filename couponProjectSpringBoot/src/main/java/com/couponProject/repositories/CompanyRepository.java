package com.couponProject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import com.couponProject.entities.Company;

@Component
public interface CompanyRepository extends JpaRepository<Company, Integer> {
	
	Company findByEmailAndPassword(String email, String password);
	
	List<Company> findByEmailOrName(String email, String name);
	
	List<Company> findByIdAndName(int ID, String name);
	
	List<Company> findById(int ID);
	
	List<Company> findByEmail(String email);
	
}
