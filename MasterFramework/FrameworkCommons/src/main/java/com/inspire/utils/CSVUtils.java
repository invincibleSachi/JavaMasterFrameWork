package com.inspire.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.inspire.abstestbase.MasterLogger;

import au.com.bytecode.opencsv.CSVWriter;
import net.minidev.json.JSONArray;

/**
 * @author sachi
 *
 */
public class CSVUtils {
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private String csvFileName = null;
	Logger log = MasterLogger.getInstance();

	public CSVUtils(String csvFileName) {
		this.csvFileName = csvFileName;
	}

	public void append2CSV(String s) {
		CSVWriter csvWriter = null;
		try {
			csvWriter = new CSVWriter(new FileWriter(csvFileName, true));
		} catch (IOException e) {
			log.info("IO Exception occurred " + e.getMessage());
		}
		String tmp = s.replace("null", "empty");
		log.info("inserting row in csv " + tmp);
		String[] row = tmp.split(",");
		csvWriter.writeNext(row);
		try {
			csvWriter.close();
		} catch (IOException e) {
			log.info("IO Exception occurred " + e.getMessage());
		}
	}

	public void createNewCSV(String csvFileName) {
		FileIOUtility.deleteFileIfExists(csvFileName);
		CSVWriter csvWriter = null;
		try {
			csvWriter = new CSVWriter(new FileWriter(csvFileName), ',', CSVWriter.NO_QUOTE_CHARACTER);
			csvWriter.close();
		} catch (IOException e) {
			log.info("IO Exception occurred " + e.getMessage());
		}

	}
	
	public JSONObject convertCsv2Json(String JsonName) throws JSONException{
		BufferedReader br=null;
		String line = "";
		JSONArray jarr=new JSONArray();
		try {
			Map < String, String > obj = new LinkedHashMap < > ();
            br = new BufferedReader(new FileReader(csvFileName));
            String[] header=br.readLine().split(",");
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                for (int i=0;i<fields.length;i++){
                	obj.put(header[i], fields[i]);
                }
                jarr.add(obj);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		JSONObject obj=new JSONObject();
		obj.put(JsonName, jarr);
		return obj;

	}
	
}
