package com.practice.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface FileImporting  {
	public String tableName();
	public String primaryKey();
	@Target(value = {ElementType.FIELD })
	public @interface COLUMN{
		
		public String columnName() ;
		public String excelField();
		public DATA_TYPE dataType() default DATA_TYPE.STRING;
	}
	public enum DATA_TYPE{
		STRING,NUMERIC,DATE,DOUBLE,YES_NO;
	}
	class a {
		public final static String name =" variable inner value";
	}
	 
	public String checking() default a.name;
}
