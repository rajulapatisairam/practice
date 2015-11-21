package com.sutisoft.roe.util.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.sutisoft.roe.action.importsAccount.AccountWireLessChargesAction;
import com.sutisoft.roe.ratelibraries.RateLibrary;
import com.sutisoft.roe.util.LoggerUtils;
import com.sutisoft.roe.util.sessionutil.SessionUtils;
public class ExcelUtils {
	public static final Logger LOGGER = Logger.getLogger(ExcelUtils.class);
	Workbook workBook = null;
	Sheet sheet = null;
	Row row = null;
	RateLibrary rateLibrary;
	CellStyle cellStyle ; 
	Font font;
	public ExcelUtils(RateLibrary rateLibrary) {
		super();
		this.rateLibrary = rateLibrary;
		workBook = new HSSFWorkbook();
		sheet = workBook.createSheet(getFileDescription());
		createCellStyle();
		fixColumnWidths();
	}
private void fixColumnWidths() {
		Map<Integer, String> columns = getColumns();
		for (Map.Entry<Integer, String> column : columns.entrySet()) {
			int length = 4500;
			int columnLength =column.getValue().length(); 
			if(columnLength>22){
				length+=(columnLength * 150);
			}else{
				length+=(columnLength* 100);
			}
			sheet.setColumnWidth(column.getKey(),length);
		}
	}
private void createCellStyle(){
	cellStyle = workBook.createCellStyle();
	font = workBook.createFont();
	font.setBoldweight(Font.BOLDWEIGHT_BOLD);
	font.setFontName("calibri");
	font.setBoldweight((short)2);
	font.setFontHeightInPoints((short)12);
	cellStyle.setFont(font);
	cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
    cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
    cellStyle.setBorderRight(CellStyle.BORDER_THIN);
    cellStyle.setBorderTop(CellStyle.BORDER_THIN);
    cellStyle.setTopBorderColor(HSSFColor.WHITE.index);
    cellStyle.setBottomBorderColor(HSSFColor.WHITE.index);
    cellStyle.setRightBorderColor(HSSFColor.WHITE.index);
    cellStyle.setLeftBorderColor(HSSFColor.WHITE.index);;
	}

private String getFileName() {
		return getLibrary().getRatePlanName();
	}

private String getFileDescription() {
	return getLibrary().getRatePlanDescription();
}

private RateLibrary getLibrary() {
	return this.rateLibrary;
}

public void generateRateLibraryTemplate() throws IOException{
	LOGGER.info(LoggerUtils.methodStartingMessage());
	Map<Integer, String>  columns = getColumns(); 
	row = sheet.createRow(0);
	for(Map.Entry<Integer, String> column: columns.entrySet()){
		Cell cell = row.createCell(column.getKey());
		cell.setCellValue(column.getValue());
		cell.setCellStyle(cellStyle);
	}
	prepareOutPutFile();
LOGGER.info(LoggerUtils.methodEndingMessage());
}

private void prepareOutPutFile() throws IOException {
	LOGGER.info(LoggerUtils.methodStartingMessage());
	String rootPath = SessionUtils.getRealPath();
	OutputStream outputStream = null;
	File file = null;
	 AccountWireLessChargesAction accWirChar = new AccountWireLessChargesAction();
	try{
	file = new File(rootPath+getFileDescription()+".xls");
	if (file.exists()) {
		file.delete();
	}
	outputStream = new FileOutputStream(file);
	workBook.write(outputStream);
	accWirChar.downloadFile(file, getFileDescription()+".xls");
	file.delete();
	}finally{
		outputStream.close();
	}
	LOGGER.info(LoggerUtils.methodEndingMessage());
}

private Map<Integer, String> getColumns() {
	return rateLibrary.getColumnAlias();
}
}
