package com.solactive.cni.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVWriter;

@RestController
public class CNIController {
	
	private static final Logger log = LoggerFactory.getLogger(CNIController.class);
	
	@GetMapping(value = "/getCNIData", 
	        produces = { MediaType.APPLICATION_JSON_VALUE})
	public void getCNIData() {	
		 List<String[]> csvData = null;
	  	  try(Scanner sc = new Scanner( new File("D:\\CNI.txt"))) {
	  		  String[] header = {"Time", "TickPrice", "RIC","Currency"};
	  	      csvData = new ArrayList<>();
	  	      csvData.add(header);
	  	   // Check if there is another line of input
		  	  while(sc.hasNextLine()){
		  		  String str = sc.nextLine();
		  		  String[] data =  parseLine(str);
		  		  if(data[0] != null) {
		  			csvData.add(data);
		  		  }
		  	  }  	   
	  	  }
	  	  catch (IOException  exp) {  	   
		  	  exp.printStackTrace();
		  	  log.debug("Exception"+exp.getMessage());
	  	  }
	  	  exportToCSV(csvData);
		 }

	  	 private static String[]  parseLine(String str){  	 
		  	 String[] strFields = str.split("\\|");
		  	 String[] outputData = new String[5];
		  	 String closePrice = strFields[2].substring(strFields[2].indexOf("=")+1);
		  	 if(StringUtils.isNotEmpty(closePrice)) {
		  		outputData[0] = validateData(strFields[0].substring(strFields[0].indexOf("=")+1)) ;
		  		outputData[1] = validateData(strFields[1].substring(strFields[1].indexOf("=")+1)) ;
		  		outputData[2] = validateData(strFields[4].substring(strFields[4].indexOf("=")+1)) ;
		  		outputData[3] = validateData(strFields[3].substring(strFields[3].indexOf("=")+1)) ;
		  	 }
		  	 
		     return  outputData;
	  	 }
	  	 
	    private static String validateData(String data) {
	    	if(StringUtils.isNotEmpty(data)) {
	    		return data;
	    	}else {
	    		return "";
	    	}
	    }
	  	 
	  	private static void exportToCSV(List<String[]> csvData) {
	  		try (CSVWriter writer = new CSVWriter(new FileWriter("D:\\TestCNI\\CNIData"+ new Date().getTime() +".csv"))) {
	  	         writer.writeAll(csvData);
	  		}
	  		catch (IOException ex) {
	  			log.debug("Exception"+ex.getMessage());
	  		}
	  	}
	  	 
	  	
}
