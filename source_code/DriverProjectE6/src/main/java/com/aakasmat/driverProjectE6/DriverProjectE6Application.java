package com.aakasmat.driverProjectE6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DriverProjectE6Application {

	//List of portNumbers
	static List<String> portNumbers = new ArrayList<String>(Arrays.asList());
		
	public static void main(String[] args) {
		
		if (args.length > 0)
		{
			List<String> portNums = Arrays.asList(args[0].split(","));
			for (String s : portNums)
			{
				String port = s.trim();
				if (port.length() != 0)
				{
					portNumbers.add(port);
				}
			}			
		}
		
		ApplicationContext context = SpringApplication.run(DriverProjectE6Application.class, args);
		
		DriverLogic driver = context.getBean(DriverLogic.class);
		driver.dataRequestStrategyUtils(portNumbers);
		
		//Exit the driver method after completion 
		int exitCode = SpringApplication.exit(context);
        System.exit(exitCode);
	}
}
