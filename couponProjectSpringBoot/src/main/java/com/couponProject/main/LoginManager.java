package com.couponProject.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import com.couponProject.exceptions.UnsuccessfulLoginException;
import com.couponProject.services.AdminService;
import com.couponProject.services.ClientService;
import com.couponProject.services.CompanyService;
import com.couponProject.services.CustomerService;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class LoginManager {

	@Autowired
	private ApplicationContext ctx;
	@Autowired
	private AdminService adminService;

	
	/*
	 * Checking the email and password in the argument according to the case
	 * that matches the client type and returning the appropriate service.
	 */
	public ClientService login(String email, String password, ClientType clientType) throws UnsuccessfulLoginException {
		ClientService clientService = null;
		switch (clientType) {
		case ADMINISTRATOR:
			if(adminService.login(email, password)) {
				clientService = (ClientService) adminService;
			}
			break;
		case COMPANY:
			clientService = (ClientService) ctx.getBean(CompanyService.class);
			if(!clientService.login(email, password)) {
				clientService = null;
			}
			break;
		case CUSTOMER:
			clientService = (ClientService) ctx.getBean(CustomerService.class);
			if(!clientService.login(email, password)) {
				clientService = null;
			}
			break;
		}
		if(clientService == null) {
			throw new UnsuccessfulLoginException("Unsuccessful Login, Try Again!");
		}
		return clientService;
	}
}