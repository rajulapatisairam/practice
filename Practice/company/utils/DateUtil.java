package com.sutisoft.roe.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.sutisoft.roe.setup.date.Pattern;
import com.sutisoft.roe.setup.date.DatePatterns;


/**
 * 
 *  @version 1.1.1 15,June 2015
 * @author sairamr
 *
 */
public class DateUtil {
	public static Date parseUserDate(String inputDate) throws Exception {
		Date paramDate = null;
		SimpleDateFormat dateFormator = new SimpleDateFormat();
			for (DatePatterns formate:DatePatterns.values()) {
			try {
				dateFormator.applyPattern(formate.getPattern());
				paramDate=dateFormator.parse(inputDate);
				break;
			} catch (Exception e) {
                   if(e instanceof ParseException) continue; 
                   else throw e;
			}
		}
			return paramDate;
	}
	
	public static String parseUserDate(String inputDate,Pattern requiredFormate) throws Exception {
		Date paramDate = null;
		String date=null;
		SimpleDateFormat dateFormator = new SimpleDateFormat();
		
		if(!StringUtils.isEmpty(inputDate)){
			paramDate=parseUserDate(inputDate);
			 dateFormator.applyPattern(requiredFormate.getPattern());
			 date=dateFormator.format(paramDate);
			return date;
		}else{
			return "";
		}
		
	}
	public static String parseUserDate(Pattern requiredFormate){
		SimpleDateFormat dateFormator = new SimpleDateFormat();
		dateFormator.applyPattern(requiredFormate.getPattern());
		return dateFormator.format(new Date());
	}
	
	/**
	 * 
	 * method getDateDifference the name it self specifies it's functionality.
	 * @param startDate 
	 * @param endDate
	 * @return  diffrence of @param beginDate and @param endDate
	 */
	public static String getDateDifference(Date startDate, Date endDate) {
		String timeDifference = "0:0s"; 
		try {
			long between = (endDate.getTime() - startDate.getTime());

			long millSecs = 0;
			long secs = 0;
			long mins = 0;
			
			if (between >= 60000) {
				mins = between / 60000;
				millSecs = between % 60000;
				secs = millSecs / 60;
				timeDifference = mins + "." + String.format("%03d", secs) + "m";
				
			} else {
				secs = between / 1000;
				millSecs = between % 1000;
				timeDifference = secs + "." + String.format("%03d", millSecs) + "s";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeDifference;	
	}
	
	/**
	 * 
	 * method getDateDifference the name it self specifies it's functionality.
	 * @param startDate 
	 * @return  diffrence of @param beginDate with  current Date 
	 */
	public static String getDateDifference(Date startDate) {
		return getDateDifference(startDate, new Date());
	}
	}

