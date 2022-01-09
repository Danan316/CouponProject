package com.couponProject.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couponProject.entities.Administrator;
import com.couponProject.entities.Company;
import com.couponProject.entities.Coupon;
import com.couponProject.entities.Customer;
import com.couponProject.exceptions.CompanyAlreadyExistsException;
import com.couponProject.exceptions.CompanyNotFoundException;
import com.couponProject.exceptions.CustomerAlreadyExistsException;
import com.couponProject.exceptions.CustomerNotFoundException;

@Service
@Transactional
public class AdminService extends ClientService{

	@Autowired
	private Administrator admin;

	public AdminService() {
	}

	@Override
	public boolean login(String email, String password) {
		if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
			return true;
		}
		return false;
	}

	public void addCompany(Company company) throws CompanyAlreadyExistsException {
		List<Company> check = companyRepository.findByEmailOrName(company.getEmail(), company.getName());
		if (!check.isEmpty()) {
			throw new CompanyAlreadyExistsException("The company that you're trying to create already exists.");
		}
		companyRepository.save(company);
		System.out.println("The company " + company.getName() + " has been added successfully");
	}

	public void updateCompany(Company company) throws CompanyNotFoundException, CompanyAlreadyExistsException {
		List<Company> check = companyRepository.findByIdAndName(company.getId(), company.getName());
		if (check.isEmpty()) {
			throw new CompanyNotFoundException("The company that you're trying to update doesn't exist.");
		}
		List<Company> checkEmail = companyRepository.findByEmail(company.getEmail());
		if(!checkEmail.isEmpty()) {
			throw new CompanyAlreadyExistsException("There is already a company with the same email!");
		}
		companyRepository.save(company);
		System.out.println("The company " + company.getName() + " has been updated successfully");
	}

	public void deleteCompany(int companyID) throws CompanyNotFoundException {
		Company company = getOneCompany(companyID);
		companyRepository.delete(company);
		System.out.println("The company with ID " + company.getId() + " has been deleted successfully");
	}

	public List<Company> getAllCompanies() {
		return companyRepository.findAll();
	}

	public Company getOneCompany(int companyID) throws CompanyNotFoundException {
		List<Company> companies = companyRepository.findById(companyID);
		if (companies == null) {
			throw new CompanyNotFoundException("The company that you're trying to find doesn't exist.");
		}
		return companies.get(0);
	}

	public void addCustomer(Customer customer) throws CustomerAlreadyExistsException {
		List<Customer> check = customerRepository.findByEmail(customer.getEmail());
		if (!check.isEmpty()) {
			throw new CustomerAlreadyExistsException("The customer that you're trying to create already exists.");
		}
		customerRepository.save(customer);
		System.out.println("The customer " + customer.getFirstName() + " " + customer.getLastName()
				+ " has been added successfully");
	}

	public void updateCustomer(Customer customer) throws CustomerNotFoundException, CustomerAlreadyExistsException {
		List<Customer> check = customerRepository.findById(customer.getId());
		if (check == null) {
			throw new CustomerNotFoundException("The customer that you're trying to update doesn't exist.");
		}
		List<Customer> checkEmail = customerRepository.findByEmail(customer.getEmail());
		if(!checkEmail.isEmpty()) {
			throw new CustomerAlreadyExistsException("There is already a customer with the same email!");
		}
		customerRepository.save(customer);
		System.out.println("The customer with the ID " + customer.getId() + " has been updated successfully");
	}

	public void deleteCustomer(int customerID) throws CustomerNotFoundException {
		Customer customer = getOneCustomer(customerID);
		for(Coupon coupon:customer.getCoupons()) {
			coupon.deleteCouponPurchase(customer);
		}
		customerRepository.delete(customer);
		System.out.println("The customer with ID " + customer.getId() + " has been deleted successfully");
	}

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public Customer getOneCustomer(int customerID) throws CustomerNotFoundException {
		List<Customer> customers = customerRepository.findById(customerID);
		if (customers == null) {
			throw new CustomerNotFoundException("The customer that you're trying to find doesn't exist.");
		}
		return customers.get(0);
	}

}
