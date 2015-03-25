package com.practice.interceptor;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class ApplicationInterceptor extends ActionSupport implements Interceptor {

	private static final Logger LOGGER=Logger.getLogger(ApplicationInterceptor.class);
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		LOGGER.info("in Applciation Lister destroy Method");
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		LOGGER.info("in Applciation Lister inti()");
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info("in Applciation Lister befroe INvocation");
		String result=invocation.invoke();
		LOGGER.info("in Applciation Lister After INvocation");
		return result;
	}

}
