package com.practice.action;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.practice.utils.PropertiesUtil;

public class ApplicationAction extends ActionSupport implements IApplicationAction{
private static final Logger LOGGER=Logger.getLogger(ApplicationAction.class);
	
	public String inValidAcess(){
	    LOGGER.info("\n Struts Message is: "+getText("unAuthorize"));     
		return PropertiesUtil.getPropertie_String(unAuthorized);
	}
	

	public String exception() {
		
		throw new NullPointerException();
		//return "exception";
	}
	
	public String error(){
		return ERROR;
	}
	
	
	
}
