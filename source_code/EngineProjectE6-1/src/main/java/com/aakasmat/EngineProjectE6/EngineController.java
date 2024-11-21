package com.aakasmat.EngineProjectE6;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EngineController {

	@Autowired
	RequestQueue reqQueue;
	
	/**
	 * Rest Api endpoint takes request from the driver and pushes into reqQueue
	 * @throws InterruptedException 
	 */
	@PostMapping("/process")
	@ResponseBody
	public ResponseEntity<Object> createEntryInQueue(@RequestParam String fileName,@RequestParam int startRow,@RequestParam int endRow) throws InterruptedException
	{
		System.out.println("Request came for file : " + fileName);
		Vector reqData = new Vector();
		reqData.add(fileName);
		reqData.add(startRow);
		reqData.add(endRow);
		
		if (reqQueue.size() > 10)
		{
			Thread.yield();
		}
		reqQueue.add(reqData);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
}