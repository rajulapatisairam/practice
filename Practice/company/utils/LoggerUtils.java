package com.sutisoft.roe.util;

/**
 * Date 8th June, 2015.
 * 
 *  
 *     
 * LoggerUtils.java  a util class for provide logger util methods which are useful to SutiWEM.
 *  
 *  1) This Utils class that simply display current executed method name along 
 *       with class name to standard output.
 *       
 * @author sairamr
 * @version SutiWEM1.0
 *
 */
public class LoggerUtils {
	private final static String STARTING_Messge ="Enter into  %s in %s";
	private final static String ENDING_Messge ="Ending of %s in %s";  
	
	/**
	 * Retrive the value of method enter message.
	 * @return A new string object that represents method staring message. 
	 */
public static String methodStartingMessage(){
	return getMessage(STARTING_Messge);
}
/**
 * Retrive the value of method ending message.
 * @return A new string message  that represents method ending message. 
 */
public static String methodEndingMessage(){
	 return getMessage(ENDING_Messge);
}

/**
 * Retrive the value of method message with respect to @param message .
 * @param message 
 * @return A new string message  that represents method dynamic message. 
 */
private static String getMessage(String message)
{
	StackTraceElement  stackElement = Thread.currentThread().getStackTrace()[3];
    String methoName = stackElement.getMethodName()+"()";
    String className    = stackElement.getClassName();
    return String.format(message,methoName,className);  
}
}
