/**
 * 
 */
package com.practice.resources.tlds;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.jasper.el.JspELException;
import org.apache.log4j.Logger;

import com.practice.utils.PropertiesUtil;

/**
 * @author Sairam Rajulapati.
 *
 */
public class ApplicationTag extends SimpleTagSupport {
private final Logger logger=Logger.getLogger(ApplicationTag.class);
private String propertie;
public void doTag() throws JspELException,IOException{
	PageContext context=(PageContext)getJspContext();
	HttpServletRequest request=(HttpServletRequest) context.getRequest();
	String properteis=request.getRequestURI();
	properteis=properteis.substring(properteis.lastIndexOf("/")+1, properteis.indexOf("."));
	JspWriter out=getJspContext().getOut();
	out.print(PropertiesUtil.getPropertie(properteis, propertie));
}
public String getPropertie() {
	return propertie;
}
public void setPropertie(String propertie) {
	this.propertie = propertie;
}

}
