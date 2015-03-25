package com.practice.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.resources.message.MessageResources;
import org.apache.log4j.Logger;

import com.practice.resources.Resources;
import com.practice.resources.Resources.Project;

/**
 * Application Lifecycle Listener implementation class ApplicationListener
 *
 */
public class ApplicationListener implements ServletContextListener {

	private static final Logger LOGGER=Logger.getLogger(ApplicationListener.class);
	private MessageResources messageResources;
    /**
     * Default constructor. 
     */
    public ApplicationListener() {
        // TODO Auto-generated constructor stub
    	LOGGER.info(" Application Listener Constructor");
    	messageResources=MessageResources.getMessageResources("project");
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    	LOGGER.info(" Application Listener contextDestroy ");
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    	LOGGER.info(" Application Listener COntextInitialized ");
    	Resources.DEVELOPER.setName(messageResources.getMessage(""));
    	Resources.DEVELOPER.setContact(messageResources.getMessage(""));
    	Resources.DEVELOPER.setContact(messageResources.getMessage(""));
    	Resources.PROJECT.setName(messageResources.getMessage(""));
    	Resources.PROJECT.setJavaVersion(messageResources.getMessage(""));
    	Resources.PROJECT.setApacheVersion(messageResources.getMessage(""));
    }
	
}
