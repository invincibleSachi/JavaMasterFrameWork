package com.inspire.utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.inspire.abstestbase.MasterLogger;

/**
 * @author sachi
 *
 */
public class ExcelUtility {
    private static XSSFSheet ExcelWSheet;    
    private static XSSFWorkbook ExcelWBook;
    private static XSSFCell Cell;
    private static XSSFRow Row;
    private static XSSFRow previousRow;
    private XSSFCellStyle CellStyle;
    public static ArrayList<String> query;
    public static CellStyle style;
    static Logger log = MasterLogger.getInstance();
    
    public static ArrayList<String> readSheet (String Path, int RowNum, String SheetName) throws Exception {
        ArrayList<String> data = new ArrayList<String>();
            try {
            // Open the Excel file
            FileInputStream ExcelFile = new FileInputStream(Path);
            // Access the required test data sheet
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            for(int i=RowNum;i<=ExcelWSheet.getLastRowNum();i++) {
                Row = ExcelWSheet.getRow(i);
                Cell = Row.getCell(0);
                String value = Cell.getStringCellValue();
                data.add(value);
                ExcelFile.close();
            }
            } catch (Exception e){
                throw (e);
            }           
            return data;
    }
    
    public void writeHeader(int RowNum, String Path, String ...headers) throws IOException {
    	writeBuildDate();
        Row = ExcelWSheet.getRow(RowNum);       
        int lastColumnWithData = Row.getLastCellNum();
        System.out.println("Last Column of Row "+RowNum + " is "+lastColumnWithData);
        for(String s : headers) {
            Cell = Row.createCell(lastColumnWithData++);
            Cell.setCellValue(s);
            setBorderStyle();
        }
        FileOutputStream fileOut = new FileOutputStream(Path);
        ExcelWBook.write(fileOut);
        fileOut.close();
    }
    
    public static void writeData(int RowNum, String Path, double s1, double s2, int s3, int s4) throws IOException {
        style = ExcelWBook.createCellStyle();
        Row = ExcelWSheet.getRow(RowNum);
        int previousRowNum = RowNum-1;
        previousRow = ExcelWSheet.getRow(previousRowNum);
        int lastColumn = Row.getLastCellNum();
        System.out.println("Last Column of Row "+RowNum + " is "+lastColumn);
        if(lastColumn==1) {
            lastColumn = previousRow.getLastCellNum()-5;
            System.out.println("Last Column of Row "+previousRowNum + " is "+lastColumn);
            setCellValue(s1,lastColumn++);
            setCellValue(s2,lastColumn++);
            setCellValue(s3,lastColumn++);
            setCellValue(s4,lastColumn++);
            
            System.out.println("currentTotalCost is: "+s2);
            Cell = Row.createCell(lastColumn++);            
            Cell.setCellValue("NA");
            setBorderStyle();
        }
        else {
        double previousTotalCost = getPreviousColumnValue(lastColumn, 4);
        setCellValue(s1,lastColumn++);
        setCellValue(s2,lastColumn++);
        setCellValue(s3,lastColumn++);
        setCellValue(s4,lastColumn++);
        
        System.out.println("currentTotalCost is: "+s2);
        System.out.println("previous total cost is: "+previousTotalCost);     
        double diff = s2 - previousTotalCost;
        System.out.println("Difference is: "+diff);
        setCellValue(diff,lastColumn++);
        
        }
        
        FileOutputStream fileOut = new FileOutputStream(Path);
        ExcelWBook.write(fileOut);
        fileOut.close();
        
    }
    
    public static void writeBuildDate() {
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
    	LocalDateTime now = LocalDateTime.now();
    	Row = ExcelWSheet.getRow(1);
        int lastColumn = Row.getLastCellNum();
        int RowNum = Row.getRowNum();
        int previousRowNum = RowNum-1;
        XSSFRow previousRow = ExcelWSheet.getRow(previousRowNum);
    	Cell = previousRow.createCell(lastColumn);
        Cell.setCellValue(dtf.format(now));
        CellRangeAddress mergedCell = new CellRangeAddress(
                0, 
                0, 
                lastColumn,  
                lastColumn+4  
        );
        ExcelWSheet.addMergedRegion(mergedCell);
    	setBorderStyle();
    }
    
    public static void setCellValue(double data, int previousColumn) {
        Cell = Row.createCell(previousColumn);
        Cell.setCellValue(data);
        setBorderStyle();
    }
    
    public static double getPreviousColumnValue(int currentColumn, int previousColumn) {
        int previousColumnNumber = currentColumn - previousColumn;      
        XSSFCell cell = Row.getCell(previousColumnNumber);
        int previousColumnValue = (int) cell.getNumericCellValue();
        System.out.println(previousColumnValue);
        return previousColumnValue;
    }
    
    public static void setBorderStyle() {
        style = ExcelWBook.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        Cell.setCellStyle(style);
    }
    
   /* public static JSONArray readSheetAsJSONObject(String FileNameWithPath, String sheetName) {
    	JSONArray jarr=new JSONArray();
    	XSSFSheet excelWSheet=null;
    	XSSFWorkbook excelWBook=null;
    	XSSFRow row =null;
    	Map<Integer,String> header=new HashMap<Integer,String>();
        // Access the required test data sheet
        try {
        	FileInputStream excelFile = new FileInputStream(FileNameWithPath);
        	excelWBook = new XSSFWorkbook(excelFile);
        	excelWSheet = excelWBook.getSheet(sheetName);
            for(int i=0;i<=excelWSheet.getLastRowNum();i++) {
            	JSONObject rowObj=new JSONObject();
            	row=excelWSheet.getRow(i);
            	for (int j=0;j<row.getLastCellNum();j++) {
            		if (i==0) {
            			if (row.getCell(j).getCellTypeEnum().equals(CellType.NUMERIC)){
            				header.put(j, NumberToTextConverter.toText(row.getCell(j).getNumericCellValue()));
            			}else {
            				header.put(j, row.getCell(j).getStringCellValue());
            			}
            			
            		}else {
            			if(row.getCell(j).getCellTypeEnum().equals(CellType.NUMERIC)) {
            				rowObj.put(header.get(j), NumberToTextConverter.toText(row.getCell(j).getNumericCellValue()));
            			}else {
            				rowObj.put(header.get(j), row.getCell(j).getStringCellValue());
            			} 			
            		}
            	}
            	jarr.put(rowObj);
            }
		} catch (IOException e) {
			log.info(e.getMessage());
		}
        
    	return jarr;
    }*/
    
    
    
    
    
    
    
}