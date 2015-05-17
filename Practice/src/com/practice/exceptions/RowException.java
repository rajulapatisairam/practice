package com.practice.exceptions;

import com.practice.errors.RowError;

public class RowException extends ProjectException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2966146156115010447L;
	public RowException(){
		super();
	}
	public RowException(RowError error){
		super(error);
	}
	public RowException(Exception exception){
		super(exception);
	}
	public RowException(RowError error,Exception exception){
		super(error,exception);
	}
}
