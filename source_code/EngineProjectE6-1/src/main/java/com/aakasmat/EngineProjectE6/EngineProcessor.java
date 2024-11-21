package com.aakasmat.EngineProjectE6;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EngineProcessor 
{
	//Using Concurrent HashMap to keep information of filesProcessed by the engine, if a file is requested again by Driver maybe 
	//because of loss of request data we don't need to calculate again.
	static ConcurrentHashMap<String, FileData> filesProcessed = new ConcurrentHashMap<String, FileData>();
		
	/**
	 * Process all the requests present in the reqQueue passed by the driver.
	 * @param reqQueue : requestQueue containing all requests
	 * @param apiCaller : bean to call driverApis
	 * @throws InterruptedException
	 */
	public static void processRequests(RequestQueue reqQueue, EngineApiService apiCaller) throws InterruptedException
	{
		 //Till all the request are processed.
		while (true)
		{
			if (!reqQueue.isEmpty())
			{
				Vector request = (Vector)reqQueue.poll();
				
				if (request == null || request.size() == 0)
				{
					continue;
				}
				
				String fileName = (String) request.elementAt(0);
				
				//If we have the file already processed, don't process again
				if (!filesProcessed.contains(fileName))
				{
					processCSVFile(request);
				}
			
				 //Send the fileData to the driver for final processing
				 try 
				 {
					apiCaller.submitFileData(filesProcessed.get(fileName));
				 }
				 catch (Exception e) 
				 {
					System.out.println("Unable to complete sumbitFileData request, driver might be down");
				 }		
			}
		}
	}
	
	/**
	 * Takes a CSV file from the fileStore specified by the request and process it.
	 * @param request : Vector containing fileName
	 */
	public static void processCSVFile(Vector request)
	{
		String fileName = (String) request.elementAt(0);
		//We can in future improve this if the CSV file are too big by processing in between range
		// As of now redundant , not using it any where/
		int startRow = (int)request.elementAt(1);
		int endRow = (int)request.elementAt(2);
		
		Path driverProjecteDirectory =  Paths.get("").toAbsolutePath();
		Path projectDir = driverProjecteDirectory.getParent().getParent();
		
		Path filePath = projectDir.resolve("sample_dataset").resolve("student_scores").resolve(fileName);
		
        try
        {
            // format :  year, minScore, maxScore, scoreSum, count, avgScore
            Map<Integer, Vector<Double>> totalYearlyData = new HashMap<Integer, Vector<Double>>();
            // Read CSV file. For each row, instantiate.
            BufferedReader reader = Files.newBufferedReader(filePath);
            Iterable < CSVRecord > records = CSVFormat.RFC4180.parse( reader );
            
            //Loop over the records in the .csv file
            for ( CSVRecord record : records )
            {
                Integer year = Integer.parseInt(record.get( 1 ));
                Double score = Double.parseDouble(record.get(2));
                
                if (!totalYearlyData.containsKey(year))
                {
                	Vector<Double> yearData = new Vector<Double>();
                	yearData.add(score); yearData.add(score); yearData.add(score); yearData.add((double) 1); 
                	yearData.add(score);
                	totalYearlyData.put(year, yearData);
                }
                else
                {
                	Vector<Double> yearData = totalYearlyData.get(year);
                	yearData.set(0, Math.min(yearData.get(0), score));  //min value for the year
                	yearData.set(1, Math.max(yearData.get(1), score));  //max value for the year
                	yearData.set(2, yearData.get(2) + score);  //total sum value for the year
                	yearData.set(3, yearData.get(3)+1); //total num of row processed
                	yearData.set(4, yearData.get(2)/yearData.get(3)); // avg till now
                	totalYearlyData.put(year, yearData);
                }
            }
            
            //Create fileData and put it in local map for later reference
            FileData fData = new FileData(fileName, totalYearlyData);
            filesProcessed.put(fileName, fData);
        } 
        catch ( IOException e )
        {
        	System.out.println("Unable to process request for file : " + fileName);
            e.printStackTrace();
        }	
	}
}
