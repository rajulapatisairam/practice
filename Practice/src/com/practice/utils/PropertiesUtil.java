package com.practice.utils;

import org.apache.commons.resources.message.MessageResources;
/**
 * 
 * @author Sairam Rajulapati.
 *
 */
public class PropertiesUtil {
	private static MessageResources resources;
	
	public static String getPropertie(final String properteis,final String propertie)
	{
		resources=MessageResources.getMessageResources(properteis);
		return resources.getMessage(propertie);
	}
	
	public static String getPropertie_String(final String propertie)
	{
		return getPropertie(Thread.currentThread().getStackTrace()[2].getClassName(), propertie);
	}
	

}
