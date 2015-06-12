package com.practice.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.practice.logic.ApplicationLogic;
import com.practice.utils.PropertiesUtil;

public class ApplicationAction extends ActionSupport implements IApplicationAction{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static final Logger LOGGER=Logger.getLogger(ApplicationAction.class);
	
	public String inValidAcess(){
	    LOGGER.info("\n Struts Message is: "+getText("unAuthorize"));     
		return PropertiesUtil.getPropertie_String(unAuthorized);
	}
	
public void studentAction() throws IOException{
	HttpServletResponse response = ServletActionContext.getResponse();
	HttpServletRequest request = ServletActionContext.getRequest();
	String refferencName = (String) request.getParameter("refference");
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	out.println("Your Refference Number is : "+refferencName);
	out.flush();
	return;
}
	public String exception() {
		
		throw new NullPointerException();
		//return "exception";
	}
	
	public String error(){
		return ERROR;
	}
	public String studentrecords(){
		ApplicationLogic logic = new ApplicationLogic();
		ServletActionContext.getRequest().setAttribute("studentRecords", logic.getStudentData());
		return STUDENT;
	}


}
