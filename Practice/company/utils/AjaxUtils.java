package com.mine.code.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
/**
 * 
 * @author sairamr
 *
 * @version 1.1.1 3-July-2015.
 */
public class AjaxUtils {
	/**
	 *  it's an util method for ajax response.
	 * @param responseData is used to response to requested client.
	 * @throws IOException runtime exception
	 */
	private static final String AJAX = "text/html";
	private static final String JSON = "application/json";
	public static void ajaxResponse(final String responseData) throws IOException{
		response(responseData, AJAX);
	}
	public static void jsonResponse(final String responseData) throws IOException{
		response(responseData, JSON);
	}
		private static void response(final String responseData,String responseType) throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType(responseType);
		// To remove the cache memory in browser.
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.println(responseData);
		out.flush();
	}
}
