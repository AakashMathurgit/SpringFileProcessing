package com.aakasmat.EngineProjectE6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
@SpringBootApplication
public class EngineProjectE61Application {

	public static void main(String[] args) throws InterruptedException 
	{
		ApplicationContext context = SpringApplication.run(EngineProjectE61Application.class, args);
		//Initialize the reqQueue bean which will keep all the requests.
		RequestQueue reqQueue = context.getBean(RequestQueue.class);
		// Initialize ApiCaller bean which will be used to send FileData back to driver.
		EngineApiService apiCaller = context.getBean(EngineApiService.class);
		
		EngineProcessor processor = context.getBean(EngineProcessor.class);
		processor.processRequests(reqQueue, apiCaller);
	}
	
}
