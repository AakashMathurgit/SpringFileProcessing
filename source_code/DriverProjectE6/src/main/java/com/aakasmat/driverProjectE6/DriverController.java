package com.aakasmat.driverProjectE6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverController {

	@Autowired
	DriverLogic driver;

	// Engine use the api endpoint to send data
	@PostMapping("/fileData")
	@ResponseBody
	public ResponseEntity<Object> reqCompleted(@RequestBody FileData fileData) {
		
		System.out.println("Recieved fileData for file : " + fileData.getFileName());
		driver.filesNotProcessed.remove(fileData.getFileName());
		driver.filesProcessed.put(fileData.getFileName(), fileData);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
}
