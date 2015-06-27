package com.practice.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.practice.beans.StudentBean;
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
	
public void studentAction() throws IOException, JSONException{
	HttpServletResponse response = ServletActionContext.getResponse();
	HttpServletRequest request = ServletActionContext.getRequest();
	String refferencName = (String) request.getParameter("refference");
	
	/**
	 *  Data Prepration Logic Part ....
	 */
	
	response.setContentType("application/json");
	
    JSONObject jsonObject = new JSONObject();
    
    ApplicationLogic logic = new ApplicationLogic();
    JSONObject studentData = null;
    JSONArray students = new JSONArray();
    JSONArray numbers = new JSONArray();
    int index=1; 
    /**
     * 
     * private int messageType;
	private String refferenceName;
	private String studentName checking.
     */
    
    for(StudentBean bean : logic.getStudentData())
    {
    	numbers.put(index++);
    	studentData = new JSONObject();
    	studentData.put("messageType", bean.getMessageType());
    	studentData.put("refferenceName", bean.getRefferenceName());
    	studentData.put("studentName", bean.getStudentName());
    	students.put(studentData);
    	
    }
    jsonObject.put("result", refferencName);
    jsonObject.put("studentsRecordss", students);
    jsonObject.put("numbers", numbers);
    
	PrintWriter out = response.getWriter();
	out.println(jsonObject.toString());
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
		System.out.println("\n Student Recordes here");
		ApplicationLogic logic = new ApplicationLogic();
		ServletActionContext.getRequest().setAttribute("studentRecords", logic.getStudentData());
		return STUDENT;
	}


}
