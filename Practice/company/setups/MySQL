package com.sutisoft.roe.setup.date;

/**
 * 
 * @author sairamr
 *
 */
public enum MySQL implements Pattern{
	DATE(DatePatterns.yyyy_MM_dd),TIME(DatePatterns.HH0mm0ss),
	YEAR(DatePatterns.YEAR),DATETIME(DatePatterns.yyyy_MM_dd_s_HH0mm0ss),
	TIMESTAMP(DatePatterns.yyyy_MM_dd_s_HH0mm0ss);
	private DatePatterns pattern;
	private MySQL(final DatePatterns pattern)
	{
		this.pattern=pattern;
	}
	@Override
	public String getPattern(){
		return this.pattern.getPattern();
	}
}
