package com.practice.action;

import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public class SessionExpire extends StrutsResultSupport {
private static final Logger LOGGER=Logger.getLogger(SessionExpire.class);
	
	@Override
	protected void doExecute(String target_URL, ActionInvocation actionInvocation)
			throws Exception {
		LOGGER.info("Session Going to Expire");
		ActionContext actionContext=actionInvocation.getInvocationContext();
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		HttpSession session=request.getSession();
		session=null;
		response.sendRedirect("."+target_URL);

	}

}
