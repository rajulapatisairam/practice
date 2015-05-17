package com.practice.exceptions;

import com.practice.errors.ProjectError;

public class ProjectException extends Exception
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public ProjectException(){
	super();
}
public ProjectException(ProjectError error){
	super(error.getError());
}
public ProjectException(Exception exception){
	super(exception);
}
public ProjectException(ProjectError error,Exception exception){
	super(error.getError(),exception);
}
}
