package com.practice.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet Filter implementation class ApplicationFilter
 */
public class ApplicationFilter implements Filter {
private static final Logger LOGGER=Logger.getLogger(ApplicationFilter.class);
	
    /**
     * Default constructor. 
     */
    public ApplicationFilter() {
        // TODO Auto-generated constructor stub
    	LOGGER.info("in ApplicationFilter");
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		LOGGER.info("in Application Filter");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		LOGGER.info("in doFIlter Before doFIlter");
		chain.doFilter(request, response);
		LOGGER.info("in DOFIlter After doFIlter");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		LOGGER.info("in application FIlter init()");
	}

}
