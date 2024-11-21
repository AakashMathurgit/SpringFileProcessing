package com.aakasmat.driverProjectE6;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DriverBean {

	// A HashMap containing fileData received successfully by the driver, used to do
	// final merging.
	public ConcurrentHashMap<String, FileData> filesProcessed = new ConcurrentHashMap<String, FileData>();
	// A ConcurrentHashMap containing files not processed, used a hashMap as
	// ConcurrentHashSet are not present.
	public ConcurrentHashMap<String, Integer> filesNotProcessed = new ConcurrentHashMap<String, Integer>();
	// List of portNumbers
	private List<String> portNumbers;
	//
	private Map<String, Integer> divertReqFromPorts = new HashMap<String, Integer>();
	private Map<String, Integer> failureCount = new HashMap<String, Integer>();
	
	Integer failureTolerance = 5;
	Integer portSkip = 10;
	@Autowired
	DriverApiService apiCaller;

	/**
	 * Looks for all csv files and sends them to be processed
	 * 
	 * @param portNumbers
	 */
	public void dataRequestStrategyUtils(List<String> portNumbers) {

		this.portNumbers = portNumbers;
		// Looks for files in the location
		Path driverProjecteDirectory = Paths.get("").toAbsolutePath();
		Path projectDir = driverProjecteDirectory.getParent().getParent();
		Path sampleDataSetPath = projectDir.resolve("sample_dataset").resolve("student_scores");
		File studentScoreDir = new File(sampleDataSetPath.toString());
		if (studentScoreDir.exists() && studentScoreDir.isDirectory()) {
			File[] files = studentScoreDir.listFiles();

			if (files != null) {
				for (File file : files) {
					if (!file.getName().endsWith("csv")) {
						continue;
					}
					filesNotProcessed.put(file.getName(), 0);
				}
			} else {
				System.out.println("No files found in the directory.");
			}
		} else {
			System.out.println("Directory does not exist.");
		}

		dataDistributionStrategy();
	}

	/**
	 * Method responsible for balancing load between engines.
	 * 
	 */
	public void dataDistributionStrategy() {
		Long callNumber = (long) 0;
		
		
		// Till all files are not processed keep looping
		while (!filesNotProcessed.isEmpty()) {
			// System.out.println("FILE NOT PROCESSED BEFORE CALLING THE EGNIER" +
			// filesNotProcessed.size());
			int portOffSet = (int) (Math.random() * 3);
			Set<String> fileNameSet = filesNotProcessed.keySet();
			
			for (String fileName : fileNameSet) {
				// If a file was required to be processed by an engine and was not processed,
				// send it to another engine.
				callNumber++;
				int port = calculatePort(portOffSet, callNumber);
				 
				
				// Send the file processing request to engine
				try {
					// startRow and endRow are dummy variables for now
					apiCaller.processRequestToEngine(fileName, 0, -1, portNumbers.get(port));
					
				} catch (Exception e) 
				{
					int currFailCount = 0;
					if (failureCount.get(portNumbers.get(port)) != null) {
						currFailCount = failureCount.get(portNumbers.get(port));
						if (currFailCount == failureTolerance) 
						{
							System.out.println("Divert " + portSkip + " requests from "  + portNumbers.get(port));
							divertReqFromPorts.put(portNumbers.get(port), portSkip);
						}
				}
					failureCount.put(portNumbers.get(port), currFailCount + 1);

					System.out.println("Unable to send request to engine for File = " + fileName + " at port " + portNumbers.get(port));
				}
			}
		}

		mergeFileDataAndWrite();
		System.out.println(
				"############################################ ALL FILES ARE PROCESSED #####################################");

	}

	private int calculatePort(int portOffSet, Long callCount) {
		
		int portCount = portNumbers.size();
		int portIdx = (int) ((portOffSet)% portCount + (callCount) % portCount);
		
		while (true)
		{
			int port = (portIdx % portCount + portCount) % portCount;
			
			if (divertReqFromPorts.get(portNumbers.get(port))!= null && divertReqFromPorts.get(portNumbers.get(port))>0)
			{
				divertReqFromPorts.put(portNumbers.get(port), divertReqFromPorts.get(portNumbers.get(port))-1);
				
				if (divertReqFromPorts.get(portNumbers.get(port)) == 0)
				{
					failureCount.put(portNumbers.get(port), 0);
				}
			}
			else
			{
				return port;
			}
			
			portIdx++;
		}
	}

	/*
	 * Merge all fileData beans and write into output.txt file
	 */
	private void mergeFileDataAndWrite() {
		FileData Output = new FileData("Output");
		for (Map.Entry<String, FileData> entry : filesProcessed.entrySet()) {
			// Merge the output
			Output.mergeFileData(entry.getValue());
		}

		// sort the entires on year
		List<Map.Entry<Integer, Vector<Double>>> entries = new ArrayList<>(Output.getFileSummary().entrySet());
		Collections.sort(entries, Comparator.comparing(Map.Entry::getKey));

		Path driverProjecteDirectory = Paths.get("").toAbsolutePath();
		Path projectDir = driverProjecteDirectory.getParent().getParent();
		Path outputFile = projectDir.resolve("output.txt");

		try {
			File file = new File(outputFile.toAbsolutePath().toString());

			file.delete();
			file.createNewFile();
			try {
				FileWriter myWriter = new FileWriter(file);
				System.out.println("File created: " + file.getAbsolutePath());
				for (Map.Entry<Integer, Vector<Double>> entry : entries) {
					Integer year = entry.getKey();
					Integer minScore = (int) Math.round(entry.getValue().get(0));
					Integer maxScore = (int) Math.round(entry.getValue().get(1));
					Integer avgScore = (int) Math.floor(entry.getValue().get(4));
					String row = year + "," + minScore + "," + maxScore + "," + avgScore + "\n";
					myWriter.write(row);
				}
				myWriter.close();
				System.out.println("Successfully wrote to the file.");
			} catch (IOException e) {

				System.out.println("Error writing to the file");
				e.printStackTrace();
			}

		} catch (IOException e) {
			System.err.println("Error writing to the file" + e.getMessage());
		}
	}
}
