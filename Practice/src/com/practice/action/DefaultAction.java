package com.practice.action;
import org.apache.log4j.Logger;

/**
 * @author Sairam Rajulapati.
 */
import com.opensymphony.xwork2.ActionSupport;

public class DefaultAction extends ActionSupport {
	private static final Logger LOGGER=Logger.getLogger(DefaultAction.class);
	public String execute(){
		LOGGER.info("\n DefaultAction execute()");
		return SUCCESS;
	}

}
