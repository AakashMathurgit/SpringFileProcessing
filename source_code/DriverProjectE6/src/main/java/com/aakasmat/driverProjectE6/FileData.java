package com.aakasmat.driverProjectE6;

import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

public class FileData {
	String fileName;
	Map<Integer, Vector<Double>> fileSummary;

	/**
	 * Constructor with fileName
	 * @param fileName
	 */
	FileData(String fileName) {
		this.fileName = fileName;
		this.fileSummary = new HashMap<Integer, Vector<Double>>();
	}

	/**
	 * Constructor with fileName and yearlyData
	 * @param fileName
	 * @param yearlyData
	 */
	FileData(String fileName, Map<Integer, Vector<Double>> yearlyData) {
		this.fileName = fileName;
		this.fileSummary = yearlyData;
	}

	/**
	 * Getter for fileName
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Setter for fileName
	 * @return
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Getter for fileSummary
	 * @return
	 */
	public Map<Integer, Vector<Double>> getFileSummary() {
		return fileSummary;
	}

	/**
	 * Setter for fileSummary
	 * @param fileSummary
	 */
	public void setFileSummary(Map<Integer, Vector<Double>> fileSummary) {
		this.fileSummary = fileSummary;
	}

	/**
	 * Merge fileData objects to create a cumulative output file
	 * 
	 * @param file2
	 */
	void mergeFileData(FileData file2) {
		
		Map<Integer, Vector<Double>> file2Summary = file2.getFileSummary();

		for (Map.Entry<Integer, Vector<Double>> entry : file2Summary.entrySet()) {
			Integer year = entry.getKey();
			Vector<Double> file2Data = entry.getValue();
			Vector<Double> file1Data = fileSummary.get(year);

			if (file1Data == null || file1Data.size() == 0) {
				file1Data = file2Data;
			} else {
				file1Data.set(0, Math.min(file1Data.get(0), file2Data.get(0)));
				file1Data.set(1, Math.max(file1Data.get(1), file2Data.get(1)));
				file1Data.set(2, file1Data.get(2) + file2Data.get(2));
				file1Data.set(3, file1Data.get(3) + file2Data.get(3));
				file1Data.set(4, file1Data.get(2) / file1Data.get(3));
			}
			
			fileSummary.put(year, file1Data);
		}
	}

	public void printData() {
		for (Map.Entry<Integer, Vector<Double>> entry : fileSummary.entrySet()) {
			System.out.println("YEAR == " + entry.getKey() + " ");

			for (Double d : entry.getValue()) {
				System.out.println(" " + d + " ");
			}

			System.out.println("\n");
		}
	}

}
