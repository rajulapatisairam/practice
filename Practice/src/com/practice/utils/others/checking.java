package com.practice.utils.others;

import com.practice.errors.CellError;
import com.practice.errors.RowError;
import com.practice.exceptions.CellException;
import com.practice.exceptions.RowException;



public class checking {

public static void main(){
	XlsReading sss=new XlsReading();
	
	try {
		sss.cellValidation(false);
	} catch (CellException e) {
		// TODO Auto-generated catch block
		System.out.println("\n Cell Validation is ; "+e.getMessage());
	}
}
}


class XlsReading{
	
	public void cellValidation(boolean type) throws CellException{
		if(type){
			throw new CellException(CellError.ONLY_NUMERIC);
		}else{
			throw new CellException(CellError.ONLY_DATE);
		}
		
	}
	public void RowValidataion() throws RowException{
		
		throw new RowException(RowError.DUPLICATE);
	}
	
}

