package com.mine.code.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mine.code.hibernate.JdbcConnection;

public class DataBase {

/**
 *  util method, for execute the Sql Query
 *  ( passed as input to  this method through @param jsonObject ) and 
 *   preparing the query results in j-son format. 
 * @throws JSONException
 * @author sairam rajulapati
 * Added on Mar 10, 2015
 */
public static void executeQuery(final JSONObject jsonObject) throws JSONException{
	Connection connection = null;
	JSONArray parent=new JSONArray();
	  JSONObject child=null;
	Statement statement=null;
	ResultSet resultSet=null;
	String query=jsonObject.getString("query");
	
   if(query==null) throw new JSONException("query parameter not existed");
   jsonObject.remove(query);
	connection = JdbcConnection.getJdbcConnection();
	try {
		statement=connection.createStatement();
	resultSet=statement.executeQuery(query);
	ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
	int columnCount=resultSetMetaData.getColumnCount();
	while(resultSet.next())
	   {
	    child=new JSONObject();
	    for(int i=1;i<=columnCount;i++)
	    {
	     String columnName=resultSetMetaData.getColumnName(i);
	     
	     child.put(columnName,resultSet.getString(columnName));
	    }
	    parent.put(child);
	   }

	jsonObject.put("result", parent);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		if(connection!=null)
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
}
}
