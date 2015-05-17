package com.practice.exceptions;

import com.practice.errors.CellError;

public class CellException extends ProjectException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6401198506413218160L;
	public CellException(){
		super();
	}
	public CellException(CellError error){
		super(error);
	}
	public CellException(Exception exception){
		super(exception);
	}
	public CellException(CellError error,Exception exception){
		super(error,exception);
	}
	
}
