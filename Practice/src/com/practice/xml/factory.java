package com.sutisoft.roe.ratelibraries;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.sutisoft.roe.setup.AppConstants;
import com.sutisoft.roe.setup.RowType;
import com.sutisoft.roe.util.DataBaseSpecification.RatePlanTypes;
import com.sutisoft.roe.util.sessionutil.SessionUtils;
/**
 * 
 * @author Sairam Rajulapati.
 *
 */
public class RateLibraryFactory {
							private final static String FILE_TEMPLATE = "D:/SAIRAM/myworkspace/SutiWEM/src/com/sutisoft/roe/xml/%s.xml";
							private static String FILE = "";
							private static SAXReader rateLibraryReader = new SAXReader();
							private static RateLibrary PRIMARY_RATE_PLAN_LIBRARY = null;
							private static RateLibrary DATA_FEATURE_RATE_LIBRARY = null;
							private static RateLibrary TEXT_RATE_PLAN_LIBRARY = null;
							private static RateLibrary RATE_PLAN_TYPE = null;
							private static RateLibraryFactory libraryFactory;
	
	public static RateLibraryFactory getInistance() {
							if (libraryFactory == null) {
												libraryFactory = new RateLibraryFactory();
												// PRIMARY_RATE_PLAN_LIBRARY =
												// prepareRateLibrary(RatePlanTypes.PRIMARY_RATE_PLAN_LIBRARY);
												// DATA_FEATURE_RATE_LIBRARY =
												// prepareRateLibrary(RatePlanTypes.DATA_FEATURE_RATE_LIBRARY);
												// TEXT_RATE_PLAN_LIBRARY =
												// prepareRateLibrary(RatePlanTypes.TEXT_RATE_PLAN_LIBRARY);
												RATE_PLAN_TYPE = prepareRateLibrary(RatePlanTypes.RATE_PLAN_TYPE);
							 }
		return libraryFactory;
	}
	
	private static RateLibrary prepareRateLibrary(RatePlanTypes ratePlanType) {
		GenerateRateLibrary generateRateLibrary = null;
							try {
										generateRateLibrary = new GenerateRateLibrary(ratePlanType);
							} catch (DocumentException e) {
										e.printStackTrace();
							}
				return generateRateLibrary.prepareRateLibrary();		
	}
	
	public RateLibrary getRatePlanLibrary(RatePlanTypes ratePlanType){
		switch(ratePlanType){
					case DATA_FEATURE_RATE_LIBRARY : return DATA_FEATURE_RATE_LIBRARY ;
					case PRIMARY_RATE_PLAN_LIBRARY : return PRIMARY_RATE_PLAN_LIBRARY;
					case TEXT_RATE_PLAN_LIBRARY : return TEXT_RATE_PLAN_LIBRARY;
					case RATE_PLAN_TYPE : return RATE_PLAN_TYPE;
					default : return null;
		}
		
	}
	private static class GenerateRateLibrary implements AppConstants{
		 			RateLibrary library = null;
					Node rateLibrary = null;
					List<Node> columns = null;
					RatePlanTypes ratePlanType = null;
					Node dbFields = null;
					Node validationNode = null;
					
		public GenerateRateLibrary(RatePlanTypes ratePlanType) throws DocumentException {
					FILE = String.format(FILE_TEMPLATE, ratePlanType.getType());
					this.ratePlanType = ratePlanType;
					System.out.println(" Rate Library  " + ratePlanType.getType() + " Is created");
					library = new RateLibrary();
					prepareRateLibraryNode();
					prepareColumnsNode();
					prepareDBFieldsNode();
					prepareValidationNode();
		}
		
		public RateLibrary prepareRateLibrary() {
					prepareLibraryBasicParameters();
					prepareColumnsData();
					prepareDataBaseFields();
					prepareQueries();
					prepareIfValidations();
					prepareOnlyOneColumnAmongColumnsValidation();
					return library;
		}

		private void prepareOnlyOneColumnAmongColumnsValidation() {
			if(validationNode != null){
				List<Node> amongColumns = validationNode.selectNodes(ONLY_ONE_COLUMN_AMONG_COLUMNS);
	        	if (amongColumns != null) {
	        		 library.getValidations().add(RowType.AMONG_COLUMNS);
	        		for(Node amongColumn : amongColumns ){
							   String value = amongColumn.valueOf(ATR_COLUMNS);
											value = value.concat(":");
											value = value.concat(amongColumn.valueOf(ATR_EQUALTO));
											value = value.concat(":");
											value = value.concat(amongColumn.valueOf(ATR_CASE_SENSISTIVE));
	        				library.getAmongColumns().add(value);
	        		} // End of for(Node amongColumn : amongColumns )
	        	} // End of if (amongColumns != null)
			}// End of if(validationNode != null)
		}

		private void prepareIfValidations() {
	         		if(validationNode != null){
				        List<Node> ifNodes = validationNode.selectNodes(IF);
				        	if (ifNodes != null) {
				        		 library.getValidations().add(RowType.DEPENDENT);
							      for(Node ifNode : ifNodes){  		
				         			     List<Node> isNodes = ifNode.selectNodes(IS);
							         			for(Node isNode : isNodes){
							         				       String key = "";
							         			           String value = "";
												         				key = ifNode.valueOf(ATR_COLUMN);
												         				key = key.concat(":");
												         				key = key.concat(isNode.valueOf(ATR_EQUALTO));
												         				key = key.concat(":");
												         				key = key.concat(isNode.valueOf(ATR_CASE_SENSISTIVE));
						
													          Node thenNode = isNode.selectSingleNode(THEN);
						
												         				value = value.concat(thenNode.valueOf(ATR_COLUMNS));
												         				value = value.concat(":");
												         				value = value.concat(thenNode.valueOf(ATR_EQUALTO));
												         				value = value.concat(":");
												         				value = value.concat(thenNode.valueOf(ATR_CASE_SENSISTIVE));
									                      library.getIfValidation().put(key, value); 
							         			      } // for(Node isNode : isNodes)
							               } // End of for(Node ifNode : ifNodes)
	         			        } // if if (ifNode != null) {
	         		} // if(validationNode != null)
		}// End of prepareValidations

		private void prepareQueries() {
					prepareSelectQuery();
					prepareUpdateQuery();
					prpeareInsertionQuery();
					prepareCheckDuplicatesQuery();
		}

	private void prepareCheckDuplicatesQuery() {
				String query = "SELECT %s  FROM " + library.getTableName() + " WHERE (%s) AND %s";
				String kama = ",";
				String columns = "";
				String questionMark = "=? ";
				String _1stCondition = "";
				String _2ndCondition = "";
				String condition = "";
				String and = " AND ";
				String quotes = "`";
			columns = quotes +library.getPrimaryKey() + quotes + kama;
			
			for (String column : library.getCheckFirstDuplicateFields() .values()) {
					columns = columns + quotes + column + quotes + kama;
					_1stCondition = _1stCondition + quotes + column + quotes + questionMark + and;
			 }
					_1stCondition = StringUtils.removeEnd(_1stCondition, and);
					columns = StringUtils.removeEnd(columns, kama);
         
			if (library.getCheckSecondDuplicateFields().size() > 0) {
					_1stCondition = "( " + _1stCondition + " ) or (%s) ";
					for (String column : library.getCheckSecondDuplicateFields().values()) {
						_2ndCondition = _2ndCondition +  quotes + column + quotes + questionMark + and;
				     }
						_2ndCondition = StringUtils.removeEnd(_2ndCondition, and);
						_1stCondition = String.format(_1stCondition, _2ndCondition);
			   }
						condition =  quotes + library.getUserId()+ quotes +"="+SessionUtils.getUserId()+and;
						condition = condition+ quotes +library.getCompanyId()+ quotes +"="+SessionUtils.getCompanyId();
						
			if (StringUtils.isNotEmpty(library.getCarrier())) {
					condition = condition+and+ quotes +library.getCarrier()+ quotes +" =  "+SessionUtils.getRequestParameter(CARRIER_ID)+and;
					condition = condition+ quotes +library.getCompanyId()+ quotes +" = "+SessionUtils.getCompanyId();
			}
					query = String.format(query, columns, _1stCondition, condition);
					library.getCheckDuplicateQuery().append(query);
					
		} // End of prepareCheckDuplicatesQuery()

	private void prpeareInsertionQuery() {
			String query = " INSERT INTO  %s  (%s)  VALUES (%s)";
			String columns = "";
			String kama = ",";
			String questionMark = "?" + kama;
			String values = "";
			String quotes = "`";
			
			for (String column : library.getColumns().values()) {
							columns = columns + quotes + column + quotes + kama;
							values = values + questionMark;
			}
			
			columns = columns + quotes + library.getUserId() + quotes + kama;
			columns = columns + quotes + library.getCompanyId() + quotes;
			values = values + SessionUtils.getUserId() + kama;
			values = values + SessionUtils.getCompanyId();
			
				if (StringUtils.isNotEmpty(library.getCarrier())) {
								columns = kama + columns + quotes + library.getCarrier() + quotes;
								columns = kama + columns + quotes + library.getCustomer() + quotes;
								values = kama + values + SessionUtils.getRequestParameter(CARRIER_ID);
								values = kama + values + SessionUtils.getCustomerId();
		        }
			query = String.format(query, library.getTableName(), columns,	values);
			library.getInsertQuery().append(query);				
	}

	private void prepareUpdateQuery() {
		String query = " UPDATE  %s  SET %s WHERE %s=?";
		String columns = "";
		String kama = ",";
		String questionMark = "=?";
		String quotes = "`";
		for (String column : library.getColumns().values()) {
			columns = columns + quotes + column + quotes + questionMark + kama;
		}
		columns = StringUtils.removeEnd(columns, kama);
		query = String.format(query,library.getTableName(),columns,quotes +library.getPrimaryKey()+ quotes);
		library.getUpdateQuery().append(query);
	}

	private void prepareSelectQuery() {
			String query = "SELECT %s  FROM %s WHERE 0";
			String columns = "";
			String kama = ",";
			String quotes = "`";
			for (String column : library.getColumns().values()) {
				columns = columns + quotes + column + quotes + kama;
			}
			columns = StringUtils.removeEnd(columns, kama);
			query = String.format(query, columns,
                    library.getTableName());
        	library.getSelectQuery().append(query);
	}

	private void prepareDataBaseFields() {
		library.setUserId(dbFields.selectSingleNode(USERID).valueOf(ATR_COLUMNNAME));
		library.setCompanyId(dbFields.selectSingleNode(COMPANYID).valueOf(ATR_COLUMNNAME));
		library.setCarrier(dbFields.selectSingleNode(CARRIER).valueOf(ATR_COLUMNNAME));
		library.setCustomer(dbFields.selectSingleNode(CUSTOMER).valueOf(ATR_COLUMNNAME));
		}

	private void prepareColumnsData() {
		   int index = 0;
			for (Node column : columns) {
					library.getColumns().put(index, column.valueOf(ATR_NAME));
					library.getColumnAlias().put(index,column.valueOf(ATR_EXCEL_COLUMN_NAME));
					collectUniqueKeys(index, column);
					checkAllowEmptyOrNot(column);
					checkColumnType(column);
					checkColumnHasRemovableCharaters(column);
					checkColumnHasMandatoryData(column);
					index++;
			}
			library.setColumnsCount(index);
		}

	private void checkColumnHasMandatoryData(Node column) {
			Node dataIn = column.selectSingleNode(DATA_IN);
			String columnName = column.valueOf(ATR_NAME);
			if (dataIn != null) {
					if (library.getDataShouldBeIn() == null) {
							library.setDataShouldBeIn(new HashMap<String, List<String>>());
					}
					List<Node> valueNodes = dataIn.selectNodes(VALUE);
					List<String> shouldBe = new LinkedList<String>();
					for (Node value : valueNodes) {
						shouldBe.add(value.getStringValue());
				   }
					library.getDataShouldBeIn().put(columnName, shouldBe);
		  }
	}

	private void checkColumnHasRemovableCharaters(Node column) {
		Node extraData = column.selectSingleNode(EXTRADATA_REMOVE);
		String columnName = column.valueOf(ATR_NAME);
		if(extraData!=null)
		{
			if(library.getSpecialCharaterRemoveFields() == null ){
				library.setSpecialCharaterRemoveFields(new LinkedHashMap<String,String>());
			}
				library.getSpecialCharaterRemoveFields().put(columnName,".0");
		}
	}

	private void checkColumnType(Node column) {
		String type =  column.valueOf(ATR_TYPE);
		String columnName = column.valueOf(ATR_NAME);
		if(StringUtils.equals(NUMERIC, type))
		{
			if(library.getOnlyNumericFields() == null ){
				library.setOnlyNumericFields(new LinkedList<String>());
			}
			library.getOnlyNumericFields().add(columnName);
		}else if(StringUtils.equals(DECIMAL, type))
		{
			if(library.getOnlyDecimalFields() == null ){
				library.setOnlyDecimalFields(new LinkedList<String>());
			}
			library.getOnlyDecimalFields().add(columnName);
		}else if(StringUtils.equals(DATE, type))
		{
			if(library.getOnlyDateValueFields() == null ){
				library.setOnlyDateValueFields(new LinkedList<String>());
			}
			library.getOnlyDateValueFields().add(columnName);
		}else if(StringUtils.equals(YESORNO, type))
		{
			if(library.getYesOrNotFields() == null)
			{
				library.setYesOrNotFields(new LinkedList<String>());
			}
			library.getYesOrNotFields().add(columnName);
		}
	}

	private void checkAllowEmptyOrNot(Node column) {
			if(StringUtils.equals(YES, column.valueOf(ATR_EMPTY))){
				if(library.getEmptyFields() == null){
					library.setEmptyFields(new LinkedList<String>());
				}
				library.getEmptyFields().add(column.valueOf(ATR_NAME));
			}
	}

	private void collectUniqueKeys(int index, Node column) {
		String uniqueSet = column.valueOf(ATR_UNIQE_COLUMNS);
		if(StringUtils.isNotEmpty(uniqueSet))
		{
			if(StringUtils.equals(uniqueSet, SET1))
			{
				library.getCheckFirstDuplicateFields().put(index,column.valueOf(ATR_NAME));
			}else{
				library.getCheckSecondDuplicateFields().put(index,column.valueOf(ATR_NAME));
			}
		}
	}

	private void prepareLibraryBasicParameters() {
		   library.setTableName(rateLibrary.valueOf(ATR_TABLE));
		   library.setPrimaryKey(rateLibrary.valueOf( ATR_PRIMARYKEY));
		   library.setRatePlanName(ratePlanType.getType());
		   library.setRatePlanTypeId(ratePlanType.getTypeId());
		}
	
	   private void prepareRateLibraryNode() throws DocumentException{
		   rateLibrary = rateLibraryReader.read(new File(FILE)).getDocument().getRootElement().selectSingleNode(RATELIBRARY);
	   }
	   private void prepareColumnsNode() {
			columns = rateLibrary.selectSingleNode(COLUMNS).selectNodes(COLUMN);
		}
	   private void prepareDBFieldsNode() {
		   dbFields = rateLibrary.selectSingleNode(DATABASE_COLUMNS);
		 	}
	   private void prepareValidationNode(){
		   validationNode = rateLibrary.selectSingleNode(VALIDATION);
	   }
	}
}

