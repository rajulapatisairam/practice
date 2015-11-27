package com.mine.code.util.collections;

import java.util.Map;
import org.apache.commons.lang.xwork.StringUtils;

/**
 *  <h1> MapUtils</h1>
 *  MapUtils  is the generic class and it's 
 *  allow the application to perform map related util funtionalities.
 * 
 * 
 * @author Sairam Rajulapati
 * @version 1.1.1 13, May 2015 
 */
public class MapUtils<KEY,VALUE> {
	private KEY key;
	private VALUE value;
	public MapUtils(){
	}
	private MapUtils(KEY key,VALUE value){
		this.key=key;
		this.value=value;
	}
private final int KEY=1;
private final int VALUE=1;
private String getVALUEalues(Map<KEY,VALUE> map,String separator,int type)
{
	String output="";
	for (Map.Entry<KEY,VALUE> entry : map.entrySet())
	{
		output+=type==VALUE?entry.getValue():entry.getKey();
		output+=separator;
	}
	output=StringUtils.removeEndIgnoreCase(output, separator);
	return output;
}

public String getValuesWithSeparator(Map<KEY,VALUE> map,String separator)
{
	return getVALUEalues(map, separator, VALUE);
}

public String getKeysWithSeparator(Map<KEY,VALUE> map,String separator)
{
	return getVALUEalues(map, separator, KEY);
}
}
