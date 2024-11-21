package com.aakasmat.driverProjectE6;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DriverApiService {

    private  RestTemplate restTemplate = new RestTemplate();;

    /**
     * Request the file to be processed by engine
     * @param fileName : fileName to be processed
     * @param startRow : dummy variables for future improvisations.
     * @param endRow : dummy variables for future improvisations.
     * @param port
     * @return
     */
    public ResponseEntity processRequestToEngine(String fileName,int startRow,int endRow,String port) 
    {
    	System.out.println("Attempt to request processing of file : " + fileName + " by engine at " + port);
        String url = "http://localhost:" + port +"/process";
        String uriWithParams = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("fileName", fileName)
                .queryParam("startRow", startRow)
                .queryParam("endRow", endRow)
                .toUriString();
        return restTemplate.postForObject(uriWithParams, null, ResponseEntity.class);
    }
}