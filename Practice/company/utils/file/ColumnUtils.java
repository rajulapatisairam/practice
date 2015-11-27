package com.mine.code.util.fileutil;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import com.mine.code.exception.CellValidationException;
import com.mine.code.setup.CellType;
import com.mine.code.setup.date.MySQL;
import com.mine.code.util.DateUtil;
/**
 * 
 * @author sairam rajulapati
 *
 */
public class ColumnUtils {
public static void cellValidation(final CellType cellType,final String cellContent) throws CellValidationException,Exception{
	
	if(cellType==null) return;
	boolean validationFailed=false;
	switch(cellType)
	{
	case YES_OR_NO:
		validationFailed=!(cellContent.equalsIgnoreCase("YES") || cellContent.equalsIgnoreCase("NO"));
		break;
	case ONLY_NUMERIC: 
		validationFailed=!NumberUtils.isDigits(cellContent);
		break;
	case ONLY_DECIMAL:
		validationFailed=!cellContent.matches("^-?\\d*\\.\\d+$");
		break;
	case NOT_EMPTY:
		validationFailed=StringUtils.isEmpty(cellContent);
		break;
	case ONLY_STRING:
		validationFailed=!cellContent.matches("[a-zA-Z]*");
		break;
	case ONLY_DATE:
			validationFailed=(DateUtil.parseUserDate(cellContent)==null);
		break;
		default: return;
	}
	if(validationFailed) {
		throw new CellValidationException(cellType);
	}
}

public static void cellValidation(final String cellConString,Map<String, List<String>> dataShouldBeIn, String columnName) throws CellValidationException
{
		List<String> dataParameters = dataShouldBeIn.get(columnName);
	if(!dataParameters.contains(cellConString.toUpperCase()))
	{
		String dataInOnly = "";
       if(dataParameters.size() < 6){
		 dataInOnly = " must be either ";
			final String OR = "/";
			for (String data : dataParameters) {
				dataInOnly += "'" + data + "'" + OR;
			}
			dataInOnly = StringUtils.removeEndIgnoreCase(dataInOnly, OR);
       }else{
    	   dataInOnly = columnName+" '"+cellConString +"' doesn't exists. ";
       }
		throw new CellValidationException(dataInOnly);
	}
}
public static String getCellContent(Row row,final Integer index)
{
String cellContent=null;
Cell cell=null;
	try{
		cell=row.getCell(index);
		if(cell==null) return null;
		cellContent=cell.getStringCellValue().trim();
		if(cellContent.equalsIgnoreCase("null")) return null;
	}catch(java.lang.IllegalStateException e){
	if(e.getMessage().contains("Cannot get a text value from a numeric")){
		   cellContent=""+cell.getNumericCellValue();
	   }else{
		   throw e;
	   }
	}
	return cellContent;
}

public static String getCellContent(Row row,final Integer index,int columnType) throws CellValidationException
{
String cellContent=null;
BigDecimal bigDecimal = null;
Cell cell=null;
cell=row.getCell(index);
if(cell==null) return null;
/*if(columnType==Types.DOUBLE)
{
	columnType=Types.NUMERIC;
}*/
switch(columnType)
{
case Types.NUMERIC:
	try{
	cellContent=""+cell.getNumericCellValue();
	}catch(java.lang.IllegalStateException e){
		throw new CellValidationException(CellType.ONLY_NUMERIC);
	}
break;
case Types.TIME:
	try {
		cellContent=""+cell.getDateCellValue();
		cell.getNumericCellValue();
		cellContent=DateUtil.parseUserDate(cellContent, MySQL.TIME);
		if(StringUtils.isEmpty(cellContent) && !cellContent.equalsIgnoreCase("unspecified"))
			throw new CellValidationException(CellType.ONLY_DATE);
	} catch (Exception e) {
		throw new CellValidationException(CellType.ONLY_DATE);
	}
	break;
case Types.TIMESTAMP:
	try {
		cellContent=""+cell.getDateCellValue();
		cellContent=DateUtil.parseUserDate(cellContent, MySQL.TIMESTAMP);
		if(StringUtils.isEmpty(cellContent) && !cellContent.equalsIgnoreCase("unspecified"))
			throw new CellValidationException(CellType.ONLY_DATE);
	} catch (Exception e) {
		
		throw new CellValidationException(CellType.ONLY_DATE);
	}
	break;
case Types.DATE:
	
	try {
		cellContent=""+cell.getDateCellValue();
		cellContent=DateUtil.parseUserDate(cellContent, MySQL.DATE);
		if(StringUtils.isEmpty(cellContent) && !cellContent.equalsIgnoreCase("unspecified"))
			throw new CellValidationException(CellType.ONLY_DATE);
	} catch (Exception e) {
		throw new CellValidationException(CellType.ONLY_DATE);
	}
	break;
case Types.VARCHAR:
	try{
		cellContent=cell.getStringCellValue().trim();
		if(cellContent.equalsIgnoreCase("null")) return null;
	}catch(java.lang.IllegalStateException e){
		if(e.getMessage().contains("Cannot get a text value from a numeric")){
		   cellContent=""+cell.getNumericCellValue();
		   /**
			 * For converting exponential numbers.
			 * especially for wireless numbers
			 */
		   try{
				if(cellContent.matches(".*[eE].*")){
					bigDecimal = new BigDecimal(cellContent);
					cellContent = new Long(bigDecimal.longValue()).toString();
				}
		   }catch(NumberFormatException nfe){
			   nfe.getMessage();
		   }
	   }else{
		   throw e;
	   }
	}
	break;
default :
	try{
		cellContent=cell.getStringCellValue().trim();
		if(cellContent.equalsIgnoreCase("null")) return null;
	}catch(java.lang.IllegalStateException e){
		if(e.getMessage().contains("Cannot get a text value from a numeric")){
		   cellContent=""+cell.getNumericCellValue();
	   }else{
		   throw e;
	   }
	}
	break;
}
	return cellContent;
}
}
