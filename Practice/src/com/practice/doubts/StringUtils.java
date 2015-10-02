package com.practice.doubts;

public class StringUtils {
public static void main(String...str) {
	String checkLists[]={"1:123","2:345","3:785"};
	updatQuery("A", 789, checkLists);
}
private static void updatQuery(String status,int userId,String...chekLists){
	String query="SET @STATUS= "+status +";"
			+ " Update xyz set status =@STATUS, userId ="+userId+" where id in(%s) and billNo in(%s) ";
	String ids="";
	String billNumbers="";
	String coma=",";
	
	for(String checkList : chekLists)
	{
		String[] data = checkList.split(":");
		ids = ids+data[0]+coma;
		billNumbers = billNumbers+data[1]+coma;
	}
	ids = org.apache.commons.lang3.StringUtils.removeEndIgnoreCase(ids, coma);
	billNumbers = org.apache.commons.lang3.StringUtils.removeEndIgnoreCase(billNumbers, coma);
	query = String.format(query, ids,billNumbers);
	System.out.println(query);
	
}
}
