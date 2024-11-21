package com.aakasmat.EngineProjectE6;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EngineApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    private String driverPort = "8082";
    private String hostString = "http://localhost:";
    /**
     * Sends the FileData object containing the analyzed info the the file.
     * @param fData FileData object
     */
    public ResponseEntity submitFileData(FileData fData) 
    {
    	System.out.println("Sending the fileData to driver for fileName : " + fData.fileName);
        String url = hostString + driverPort +"/fileData";
        return restTemplate.postForObject(url, fData, ResponseEntity.class);   
    }
}
