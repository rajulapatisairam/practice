package com.practice.errors;

public enum CellError implements ProjectError {
	ONLY_NUMERIC("Only Numeric Value allowed"),ONLY_DATE("Only Date Value Allowed");
private String error;


private CellError(String error){
	this.error = error;
}
	@Override
	public String getError() {
		// TODO Auto-generated method stub
		return this.error;
	}

}
