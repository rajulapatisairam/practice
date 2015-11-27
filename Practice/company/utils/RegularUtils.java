package com.mine.code.util.regularutils;

import org.apache.commons.lang.xwork.StringUtils;
/**
 * RegularUtils.java is utils class.
 * This class can provide functionalities for regular expressions.
 * 
 * @version 1.1.0 11, June 2015
 * @author sairam rajulapati
 * 
 */
public class RegularUtils {
	private static final String DOT = ".";
	
	public static boolean equals(Boolean isCaseSensitive, String str1,String str2) {
		return isCaseSensitive ? StringUtils.equals(str1, str2)
													   : StringUtils.equalsIgnoreCase(str1, str2);
	}
	public static String convertIntoDecimal(String input)
    {
	
	if(StringUtils.isEmpty(input))
	{
		return "0.00";
	}else if(input.equals(DOT) ||!input.matches(RegularExpressions.DECIMAL)){
		return input;
     }
	String beforeDot = "";
	String afterDot = "00";
	
		if (StringUtils.contains(input, DOT))
			{
		      
			beforeDot = StringUtils.substringBefore(input, DOT);
			afterDot = StringUtils.substringAfter(input, DOT);
	
	    beforeDot = StringUtils.isEmpty(beforeDot)?"0":beforeDot;
			
			if (afterDot.length() == 1) {
				afterDot = afterDot + "0";
			} else if (afterDot.length() >= 2) {
				if (!afterDot.equalsIgnoreCase("00")) {
				double number = Double.parseDouble(input);
				return RegularExpressions.DECIMAL_FORMATER.format(number);
				}
				afterDot = StringUtils.substring(afterDot, 0, 2);
			}

			afterDot = StringUtils.isEmpty(afterDot) ? "00" : afterDot;
			input = beforeDot + DOT + afterDot;
		} else {
			input = input + DOT + afterDot;
				}
		return input;
   }

}
