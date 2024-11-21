package com.aakasmat.EngineProjectE6;

import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

/*
 * File Data keeps the summary of each file
 */
public class FileData {
	String fileName;
	Map<Integer, Vector<Double>> fileSummary;

	FileData(String fileName) {
		this.fileName = fileName;
		this.fileSummary = new HashMap<Integer, Vector<Double>>();
	}

	FileData(String fileName, Map<Integer, Vector<Double>> yearlyData) {
		this.fileName = fileName;
		this.fileSummary = yearlyData;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<Integer, Vector<Double>> getFileSummary() {
		return fileSummary;
	}

	public void setFileSummary(Map<Integer, Vector<Double>> fileSummary) {
		this.fileSummary = fileSummary;
	}
}
