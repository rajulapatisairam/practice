package com.practice.errors;

public enum RowError implements ProjectError {
	DUPLICATE("Duplicate Not Allowed");
	private String error;
	private RowError(String error){
		this.error = error;
	}

	@Override
	public String getError() {
		// TODO Auto-generated method stub
		return this.error;
	}

}
