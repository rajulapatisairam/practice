package com.sutisoft.roe.util.sessionutil;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.ServletActionContext;
import com.sutisoft.roe.setup.AppConstants;
import com.sutisoft.roe.setup.module.ModuleAttributes;
/**
 *  SessionUtils.java is a utils class.
 *   This calss can provide util functionalities for session activities. 
 * 
 * @version 1.1.1  06 Jun 2015
 *  @author sairam rajulapati 
 */
public class SessionUtils {
	/**
	 *  clear session attributes, whic are not under current requested action.
	 * @param request current live request attribute create from 
	 */
	public static String  getRequestParameter(String key){
		String value = ServletActionContext.getRequest().getParameter(key);
		if(StringUtils.isEmpty(value)){
			value = (String) getRequestAttribute(key);
		}
		value = StringUtils.isEmpty(value)? "":value;
		return value;
	}
	public static Object  getRequestAttribute(String key){
		return   ServletActionContext.getRequest().getAttribute(key);
	}
	public static void setRequestAttribute(String key,Object value){
		ServletActionContext.getRequest().setAttribute(key,value);
	}
	public static int getCompanyId() {
		return (Integer) getAttribute(AppConstants.COMPANY_ID);
	}
	public static int getUserId() {
		return (Integer) getAttribute(AppConstants.USER_ID);
	}
	public static int getCustomerId() {
		Integer customerId = (Integer) getAttribute(AppConstants.CUSTOMER_ID);
		return customerId == null ? -1 : customerId;
	}
	public static String getCarrierId(){
		return SessionUtils.getRequestParameter(AppConstants.CARRIER_ID);
	}
	public static Object getAttribute(String key){
		return ServletActionContext.getRequest().getSession().getAttribute(key);
	}
	public static void setAttribute(String key,String value){
		ServletActionContext.getRequest().getSession().setAttribute(key,value);
	}
	public static void removeAttribute(String key){
		ServletActionContext.getRequest().getSession().removeAttribute(key);
	}
	public static String getRealPath(){
		return ServletActionContext.getRequest().getSession().getServletContext().getRealPath("/");
	}
	public static String getRateLibrariesPath(){
			return StringUtils.replace(getRealPath(),  "/",File.separator)+"RateLibraries//%s.xml";
	}
	
	public static HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	
public static void clearSession(HttpServletRequest request)
{
	HttpSession session = request.getSession();
	String requestURL = request.getRequestURL().toString();
	boolean requestExists = false;
	for(ModuleAttributes module : ModuleAttributes.values()) {
             for(String requestAttribute : module.getRequestAttributes())
             {
            	 if(requestExists = StringUtils.contains(requestURL, requestAttribute))
            	 {
            	 break;
            	 }
             }
  if(!requestExists) {
	  for(String sessionAttribute : module.getSessionAttributes())
        {
        	session.removeAttribute(sessionAttribute);
        }
	 }
 	}
}
}
