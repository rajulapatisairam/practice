package com.mine.code.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.star.uno.RuntimeException;
import com.mine.code.hibernate.JdbcConnection;
import com.mine.code.setup.AppConstants;

/**
 * 
 * @author sairamr
 *
 */
public class DataBaseUtil {
	private static final Logger LOGGER = Logger.getLogger(DataBaseUtil.class.getName());
	
	public String executeDMLQuery(final String sql) {
		LOGGER.info(LoggerUtils.methodStartingMessage());
		String status = AppConstants.ERROR;
		Connection connection = null;
		Statement statement = null;
		 if(StringUtils.isEmpty(sql)) throw new RuntimeException("query Should not be empty");
		 connection = JdbcConnection.getJdbcConnection();
			try {
				statement = connection.createStatement();
				statement.executeUpdate(sql);
				status = AppConstants.SUCCESS;
			} catch (SQLException exp) {
				exp.printStackTrace();
			}finally{
				try {
					connection.commit();
				} catch (SQLException exp) {
					exp.printStackTrace();
				}
			}
			LOGGER.info(LoggerUtils.methodEndingMessage());
			return status;
	}
	/**
 *  util method, for execute the Sql Query
 *  ( passed as input to  this method through @param jsonObject ) and 
 *   preparing the query results in j-son format. 
 * @throws JSONException
 * @author sairam rajulapati
 * Added on Mar 10, 2015
 */
public  void executeQuery(final JSONObject jsonObject) throws JSONException{
	LOGGER.info(LoggerUtils.methodStartingMessage());
	Connection connection = null;
	JSONArray parent = new JSONArray();
	  JSONObject child = null;
	Statement statement = null;
	ResultSet resultSet = null;
	String query = jsonObject.getString(AppConstants.QUERY);
   if(StringUtils.isEmpty(query)) throw new JSONException("query parameter not existed");
   jsonObject.remove(AppConstants.QUERY);
	connection = JdbcConnection.getJdbcConnection();
	try {
		statement = connection.createStatement();
	resultSet = statement.executeQuery(query);
	ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
	int columnCount = resultSetMetaData.getColumnCount();
	while(resultSet.next())
	   {
	    child = new JSONObject();
	    for(int index = 1; index <= columnCount; index++)
	    {
	     String columnName = resultSetMetaData.getColumnName( index );
	     child.put(columnName,resultSet.getString( index ));
	    }
	    if(child.length() == 0) continue;
	    parent.put( child );
	   }
	jsonObject.put(AppConstants.RESULT, parent);
	jsonObject.put(AppConstants.ROWCOUNT, parent.length());
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} finally {
			if (connection !=  null)
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LOGGER.info(LoggerUtils.methodEndingMessage());
		}
	
	}
	
public void saveLogs(String description,Integer userId,Integer customerId,String type,Integer carrierId,String importedFrom){
	String userLogsQuery = "insert into user_logs(description,created_on,type,user_id,customer_id,carrier_id,imported_from) values(?,?,?,?,?,?,?)";
	PreparedStatement preparedStatement = null;
	Connection conn = null;
	try{
		conn = JdbcConnection.getJdbcConnection();
		preparedStatement = conn.prepareStatement(userLogsQuery);
		preparedStatement.setString(1, description);
		preparedStatement.setTimestamp(2,new Timestamp(new Date().getTime()));
		preparedStatement.setString(3,type);
		preparedStatement.setInt(4,userId);
		preparedStatement.setInt(5,customerId);
		preparedStatement.setInt(6,carrierId);
		preparedStatement.setString(7,importedFrom);
		int insertStatus = preparedStatement.executeUpdate();
		if(insertStatus == 1){
			conn.commit();
			LOGGER.info("Logs Inserted into DB");
		}
		else{
			LOGGER.info("Logs Failed to insert into Data Base ");
		}
	}catch(SQLException se){
		se.printStackTrace();
	}finally{
		if(preparedStatement != null){
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
}
	
