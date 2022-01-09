package com.couponProject.main;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.couponProject.entities.Category;
import com.couponProject.entities.Company;
import com.couponProject.entities.Coupon;
import com.couponProject.entities.Customer;
import com.couponProject.exceptions.CompanyAlreadyExistsException;
import com.couponProject.exceptions.CompanyNotFoundException;
import com.couponProject.exceptions.CouponAlreadyExistsException;
import com.couponProject.exceptions.CouponAlreadyExpiredException;
import com.couponProject.exceptions.CouponAlreadyPurchasedException;
import com.couponProject.exceptions.CouponNotFoundException;
import com.couponProject.exceptions.CouponNotInStockException;
import com.couponProject.exceptions.CustomerAlreadyExistsException;
import com.couponProject.exceptions.CustomerNotFoundException;
import com.couponProject.exceptions.UnsuccessfulLoginException;
import com.couponProject.services.AdminService;
import com.couponProject.services.ClientService;
import com.couponProject.services.CompanyService;
import com.couponProject.services.CustomerService;

@Component
public class Test {

	@Autowired
	private LoginManager loginManager;
	@Autowired
	private CouponExpirationDailyJob job;

	/**
	 * This function activates the entire project and the daily job thread.
	 */
	public void testAll() {
		Scanner s = new Scanner(System.in);
		boolean status = true;
		Thread t = new Thread(job);
		t.setDaemon(true);
		t.start();

		while (status) {
			try {
				int choice = 0;
				ClientService service = loginUser();
				userMenus(service);
				System.out.println(
						"Do you want to log in from another account or exit the program? (1 - Log in, 2 - Exit)");
				while (choice < 1 || choice > 2) {
					try {
						choice = s.nextInt();
						while (choice < 1 || choice > 2) {
							System.out.println("Invalid choice, try again!");
							choice = s.nextInt();
						}
						if (choice == 2) {
							System.out.println("Goodbye!");
							status = false;
						}
					} catch (InputMismatchException e) {
						System.err.println("Enter digits only for the choice!");
						s.nextLine();
					}
				}
			} catch (InputMismatchException e) {
				System.err.println("Enter digits only for the choice!");
				s.nextLine();
			}

		}
		synchronized (job) {
			job.stop();
		}
	}

	/**
	 * Returns the service inheritance object by getting the login parameters
	 * from the user and using the LoginManager "login" function.
	 */
	public ClientService loginUser() {
		Scanner s = new Scanner(System.in);
		ClientService service = null;
		while (service == null) {
			try {
				System.out.println("Enter E-mail: ");
				String email = s.nextLine();
				System.out.println("Enter Password: ");
				String password = s.nextLine();
				System.out.println("Enter choice of client type (1 - Administrator, 2 - Company, 3 - Customer): ");
				int choice = s.nextInt();
				while (choice < 1 || choice > 3) {
					System.out.println("Invalid choice, try again! (1 - Administrator, 2 - Company, 3 - Customer) ");
					choice = s.nextInt();
				}
				service = loginManager.login(email, password, ClientType.values()[choice - 1]);
				if (service != null) {
					System.out.println("You are logged in!");
				}
			} catch (InputMismatchException e) {
				System.err.println("Enter digits only for the client type!");
				s.nextLine();
			} catch (UnsuccessfulLoginException e) {
				System.err.println(e.getMessage());
				s.nextLine();
			}
		}
		return service;
	}

	/**
	 * Acts as a crossroads between the 3 kinds of services for the ClientService
	 * object that is returned from the "loginUser" function.
	 */
	public static void userMenus(ClientService service) {
		if (service instanceof AdminService) {
			AdminServiceMenu((AdminService) service);
		} else if (service instanceof CompanyService) {
			CompanyServiceMenu((CompanyService) service);
		} else if (service instanceof CustomerService) {
			CustomerServiceMenu((CustomerService) service);
		} else {
			System.out.println("Unsuccessful login attempt!");
		}
	}

	/**
	 * A menu for all of the functions that the administrator can execute in the project.
	 */
	public static void AdminServiceMenu(AdminService service) {
		Scanner s = new Scanner(System.in);
		boolean status = true;

		while (status) {
			try {
				System.out.println("Admin Menu: \n" + "1 - Add Company \n" + "2 - Update Company \n"
						+ "3 - Delete Company \n" + "4 - Get All Companies \n" + "5 - Get One Company \n"
						+ "6 - Add Customer \n" + "7 - Update Customer \n" + "8 - Delete Customer \n"
						+ "9 - Get All Customers \n" + "10 - Get One Customer \n" + "11 - Exit \n");
				int pick = s.nextInt();
				switch (pick) {

				case 1:
					System.out.println("Enter the company's name: ");
					s.nextLine();
					String name1 = s.nextLine();
					System.out.println("Enter the company's E-Mail address: ");
					String email1 = s.nextLine();
					System.out.println("Enter the company's password: ");
					String password1 = s.nextLine();
					Company company1 = new Company(name1, email1, password1, null);
					service.addCompany(company1);
					break;

				case 2:
					System.out.println("Enter the company's ID that you want to update: ");
					int id2 = s.nextInt();
					s.nextLine();
					Company testComp = service.getOneCompany(id2);
					System.out.println("Enter the company's name that you want to update: ");
					String name2 = s.nextLine();
					if(!name2.equals(testComp.getName())) {
						throw new CompanyNotFoundException("The company that you're trying to update doesn't exist.");
					}
					System.out.println("Enter the company's new E-Mail address: ");
					String email2 = s.nextLine();
					System.out.println("Enter the company's new password: ");
					String password2 = s.nextLine();
					Company company2 = new Company(id2, name2, email2, password2, null);
					service.updateCompany(company2);
					break;

				case 3:
					System.out.println("Enter the company's ID that you want to delete: ");
					int id3 = s.nextInt();
					service.deleteCompany(id3);
					break;

				case 4:
					List<Company> allCompanies4 = service.getAllCompanies();
					if (allCompanies4.isEmpty()) {
						System.out.println("There are no companies!");
					} else {
						System.out.println(allCompanies4);
					}
					break;

				case 5:
					System.out.println("Enter the company's ID that you want to show: ");
					int id5 = s.nextInt();
					Company company5 = service.getOneCompany(id5);
					System.out.println(company5);
					break;

				case 6:
					System.out.println("Enter the customer's first name: ");
					s.nextLine();
					String firstName6 = s.nextLine();
					System.out.println("Enter the customer's last name: ");
					String lastName6 = s.nextLine();
					System.out.println("Enter the customer's E-Mail address: ");
					String email6 = s.nextLine();
					System.out.println("Enter the customer's password: ");
					String password6 = s.nextLine();
					Customer customer6 = new Customer(firstName6, lastName6, email6, password6);
					service.addCustomer(customer6);
					break;

				case 7:
					System.out.println("Enter the customer's ID that you want to update: ");
					int id7 = s.nextInt();
					s.nextLine();
					Customer testCustomer = service.getOneCustomer(id7);
					System.out.println("Enter the customer's new first name: ");
					String firstName7 = s.nextLine();
					System.out.println("Enter the customer's new last name: ");
					String lastName7 = s.nextLine();
					System.out.println("Enter the customer's new E-Mail address: ");
					String email7 = s.nextLine();
					System.out.println("Enter the customer's new password: ");
					String password7 = s.nextLine();
					Customer customer7 = new Customer(id7, firstName7, lastName7, email7, password7);
					service.updateCustomer(customer7);
					break;

				case 8:
					System.out.println("Enter the customer's ID that you want to delete: ");
					int id8 = s.nextInt();
					service.deleteCustomer(id8);
					break;

				case 9:
					List<Customer> allCustomers9 = service.getAllCustomers();
					if (allCustomers9.isEmpty()) {
						System.out.println("There are no customers!");
					} else {
						System.out.println(allCustomers9);
					}
					System.out.println();
					break;

				case 10:
					System.out.println("Enter the customer's ID that you want to show: ");
					int id10 = s.nextInt();
					Customer customer10 = service.getOneCustomer(id10);
					System.out.println(customer10);
					break;

				case 11:
					status = false;
					break;

				default:
					System.out.println("Invalid pick, try again!");
				}
			} catch (InputMismatchException e) {
				System.err.println("Enter only digits for the numeric objects!");
				s.nextLine();
			} catch (CustomerNotFoundException | CustomerAlreadyExistsException | CompanyNotFoundException
					| CompanyAlreadyExistsException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * A menu for all of the functions that a company can execute in the project.
	 */
	public static void CompanyServiceMenu(CompanyService service) {
		Scanner s = new Scanner(System.in);
		boolean status = true;

		while (status) {
			try {
				System.out.println("Company Menu: \n" + "1 - Add Coupon \n" + "2 - Update Coupon \n"
						+ "3 - Delete Coupon \n" + "4 - Get Company Coupons \n"
						+ "5 - Get Company Coupons (By Category) \n" + "6 - Get Company Coupons (By Max Price) \n"
						+ "7 - Get Company Coupons (By Min Price) \n" + "8 - Get Company Details \n" + "9 - Exit \n");
				int pick = s.nextInt();
				switch (pick) {

				case 1:
					System.out.println("Enter the coupon's category ID: ");
					s.nextLine();
					int categoryID1 = s.nextInt();
					while (categoryID1 < 1 || categoryID1 > Category.values().length) {
						System.out.println("Invalid choice, try again!");
						categoryID1 = s.nextInt();
					}
					Category category1 = Category.values()[categoryID1 - 1];
					s.nextLine();
					System.out.println("Enter the coupon's title: ");
					String title1 = s.nextLine();
					System.out.println("Enter the coupon's description: ");
					String description1 = s.nextLine();
					System.out.println("Enter the coupon's start date: (YYYY-MM-DD) ");
					String stringStartDate1 = s.nextLine();
					Date startDate1 = Date.valueOf(stringStartDate1);
					while (startDate1.before(Calendar.getInstance().getTime())) {
						System.out.println("The start date can't be earlier than or equal to today! try again.");
						stringStartDate1 = s.nextLine();
						startDate1 = Date.valueOf(stringStartDate1);
					}
					System.out.println("Enter the coupon's end date: (YYYY-MM-DD) ");
					String stringEndDate1 = s.nextLine();
					Date endDate1 = Date.valueOf(stringEndDate1);
					while (endDate1.before(startDate1)) {
						System.out.println("The end date can't be earlier than the start date! try again.");
						stringEndDate1 = s.nextLine();
						endDate1 = Date.valueOf(stringEndDate1);
					}
					System.out.println("Enter the coupon's amount: ");
					int amount1 = s.nextInt();
					while (amount1 <= 0) {
						System.out.println("The amount can't be lower than 1! try again.");
						amount1 = s.nextInt();
					}
					System.out.println("Enter the coupon's price: ");
					double price1 = s.nextDouble();
					while (price1 <= 0.0) {
						System.out.println("The price can't be lower than 1! try again.");
						price1 = s.nextDouble();
					}
					s.nextLine();
					System.out.println("Enter the coupon's image: ");
					String image1 = s.nextLine();
					Coupon coupon1 = new Coupon(service.getCompanyDetails(), category1, title1, description1, startDate1,
							endDate1, amount1, price1, image1);
					service.addCoupon(coupon1);
					break;

				case 2:
					System.out.println("Enter the coupon's ID that you want to update:  ");
					int id2 = s.nextInt();
					System.out.println("Enter the coupon's new category ID: ");
					int categoryID2 = s.nextInt();
					while (categoryID2 < 1 || categoryID2 > Category.values().length) {
						System.out.println("Invalid choice, try again!");
						categoryID2 = s.nextInt();
					}
					Category category2 = Category.values()[categoryID2 - 1];
					s.nextLine();
					System.out.println("Enter the coupon's new title: ");
					String title2 = s.nextLine();
					System.out.println("Enter the coupon's new description: ");
					String description2 = s.nextLine();
					System.out.println("Enter the coupon's new start date: (YYYY-MM-DD) ");
					String stringStartDate2 = s.nextLine();
					Date startDate2 = Date.valueOf(stringStartDate2);
					while (startDate2.before(Calendar.getInstance().getTime())) {
						System.out.println("The start date can't be earlier than or equal to today! try again.");
						stringStartDate2 = s.nextLine();
						startDate2 = Date.valueOf(stringStartDate2);
					}
					System.out.println("Enter the coupon's new end date: (YYYY-MM-DD) ");
					String stringEndDate2 = s.nextLine();
					Date endDate2 = Date.valueOf(stringEndDate2);
					while (endDate2.before(startDate2)) {
						System.out.println("The end date can't be earlier than the start date! try again.");
						stringEndDate2 = s.nextLine();
						endDate2 = Date.valueOf(stringEndDate2);
					}
					System.out.println("Enter the coupon's new amount: ");
					int amount2 = s.nextInt();
					while (amount2 <= 0) {
						System.out.println("The amount can't be lower than 1! try again.");
						amount2 = s.nextInt();
					}
					System.out.println("Enter the coupon's new price: ");
					double price2 = s.nextDouble();
					while (price2 <= 0.0) {
						System.out.println("The price can't be lower than 1! try again.");
						price2 = s.nextDouble();
					}
					s.nextLine();
					System.out.println("Enter the coupon's new image: ");
					String image2 = s.nextLine();
					Coupon coupon2 = new Coupon(id2, service.getCompanyDetails(), category2, title2, description2,
							startDate2, endDate2, amount2, price2, image2);
					service.updateCoupon(coupon2);
					break;

				case 3:
					System.out.println("Enter the coupon's ID that you want to delete: ");
					int id3 = s.nextInt();
					service.deleteCoupon(id3);
					break;

				case 4:
					List<Coupon> companyCoupons4 = service.getCompanyCoupons();
					if (companyCoupons4.isEmpty()) {
						System.out.println("The company " + service.getCompanyDetails().getName() + " has no coupons");
					} else {
						System.out.println(companyCoupons4);
					}
					break;

				case 5:
					System.out.println("Enter the coupon's category ID: (1-4)");
					int categoryID5 = s.nextInt();
					while (categoryID5 < 1 || categoryID5 > Category.values().length) {
						System.out.println("Invalid choice, try again!");
						categoryID5 = s.nextInt();
					}
					Category category5 = Category.values()[categoryID5 - 1];
					List<Coupon> companyCoupons5 = service.getCompanyCoupons(category5);
					if (companyCoupons5.isEmpty()) {
						System.out.println("The company " + service.getCompanyDetails().getName()
								+ " has no coupons in the category " + category5.getName());
					} else {
						System.out.println(companyCoupons5);
					}
					break;

				case 6:
					System.out.println("Enter the coupon's max price: ");
					double maxPrice6 = s.nextDouble();
					List<Coupon> companyCoupons6 = service.getCompanyCoupons(maxPrice6);
					if (companyCoupons6.isEmpty()) {
						System.out.println("The company " + service.getCompanyDetails().getName()
								+ " has no coupons below the price " + maxPrice6);
					} else {
						System.out.println(companyCoupons6);
					}
					break;

				case 7:
					System.out.println("Enter the coupon's min price: ");
					double minPrice7 = s.nextDouble();
					List<Coupon> companyCoupons7 = service.getCompanyCouponsByMinPrice(minPrice7);
					if (companyCoupons7.isEmpty()) {
						System.out.println("The company " + service.getCompanyDetails().getName()
								+ " has no coupons over the price " + minPrice7);
					} else {
						System.out.println(companyCoupons7);
					}
					break;

				case 8:
					System.out.println(service.getCompanyDetails());
					break;

				case 9:
					status = false;
					break;

				default:
					System.out.println("Invalid pick, try again!");
				}
			} catch (InputMismatchException e) {
				System.err.println("Enter only digits for the numeric objects!");
				s.nextLine();
			} catch (IllegalArgumentException e) {
				System.err.println("Enter dates only in the right template!");
			} catch (CouponAlreadyExistsException | CouponNotFoundException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * A menu for all of the functions that a customer can execute in the project.
	 */
	public static void CustomerServiceMenu(CustomerService service) {
		Scanner s = new Scanner(System.in);
		boolean status = true;
		

		while (status) {
			try {
				System.out.println("Customer Menu: \n" + "1 - Purchase Coupon \n" + "2 - Get Customer Coupons \n"
						+ "3 - Get Customer Coupons (By Category) \n" + "4 - Get Customer Coupons (By Max Price) \n"
						+ "5 - Get Customer Coupons (By Min Price) \n" + "6 - Get Customer Details \n" + "7 - Exit \n");
				int pick = s.nextInt();
				switch (pick) {

				case 1:

					System.out.println("Enter the desired coupon ID: ");
					int id1 = s.nextInt();
					service.PurchaseCoupon(id1);
					break;

				case 2:

					List<Coupon> customerCoupons2 = service.getCustomerCoupons();
					if (customerCoupons2.isEmpty()) {
						System.out.println("The customer " + service.getCustomerDetails().getFirstName() + " "
								+ service.getCustomerDetails().getLastName() + " has no coupons");
					} else {
						System.out.println(customerCoupons2);
					}
					break;

				case 3:
					System.out.println("Enter the coupon's category ID: (1-4)");
					int categoryID3 = s.nextInt();
					while (categoryID3 < 1 || categoryID3 > Category.values().length) {
						System.out.println("Invalid choice, try again!");
						categoryID3 = s.nextInt();
					}
					Category category3 = Category.values()[categoryID3 - 1];
					List<Coupon> customerCoupons3 = service.getCustomerCoupons(category3);
					if (customerCoupons3.isEmpty()) {
						System.out.println("The customer " + service.getCustomerDetails().getFirstName() + " "
								+ service.getCustomerDetails().getLastName() + " has no coupons in the category "
								+ category3.getName());
					} else {
						System.out.println(customerCoupons3);
					}
					break;

				case 4:
					System.out.println("Enter the coupon's max price: ");
					double maxPrice4 = s.nextDouble();
					List<Coupon> customerCoupons4 = service.getCustomerCoupons(maxPrice4);
					if (customerCoupons4.isEmpty()) {
						System.out.println("The customer " + service.getCustomerDetails().getFirstName() + " "
								+ service.getCustomerDetails().getLastName() + " has no coupons below the price "
								+ maxPrice4);
					} else {
						System.out.println(customerCoupons4);
					}
					break;

				case 5:
					System.out.println("Enter the coupon's min price: ");
					double minPrice5 = s.nextDouble();
					List<Coupon> customerCoupons5 = service.getCustomerCouponsByMinPrice(minPrice5);
					if (customerCoupons5.isEmpty()) {
						System.out.println("The customer " + service.getCustomerDetails().getFirstName() + " "
								+ service.getCustomerDetails().getLastName() + " has no coupons over the price "
								+ minPrice5);
					} else {
						System.out.println(customerCoupons5);
					}
					break;

				case 6:
					System.out.println(service.getCustomerDetails());
					break;

				case 7:
					status = false;
					break;

				default:
					System.out.println("Invalid pick, try again!");
				}
			} catch (InputMismatchException e) {
				System.err.println("Enter only digits for the numeric objects!");
				s.nextLine();
			} catch (CouponNotFoundException | CouponNotInStockException | CouponAlreadyPurchasedException
					| CouponAlreadyExpiredException e) {
				System.err.println(e.getMessage());
			}
		}
	}

}
