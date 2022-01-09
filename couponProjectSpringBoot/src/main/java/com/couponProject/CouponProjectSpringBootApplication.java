package com.couponProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.couponProject.main.Test;

@SpringBootApplication
public class CouponProjectSpringBootApplication {
	   
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(CouponProjectSpringBootApplication.class, args);
		Test test = ctx.getBean(Test.class);
		test.testAll();
	}
}