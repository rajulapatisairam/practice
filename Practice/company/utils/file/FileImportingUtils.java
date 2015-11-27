package com.mine.code.util.fileutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mine.code.exception.CellValidationException;
import com.mine.code.exception.FileImportingException;
import com.mine.code.exception.RowValidationException;
import com.mine.code.hibernate.JdbcConnection;
import com.mine.code.ratelibraries.RateLibrary;
import com.mine.code.setup.AppConstants;
import com.mine.code.setup.CellType;
import com.mine.code.setup.Condition;
import com.mine.code.setup.RowType;
import com.mine.code.setup.module.ModuleConstants.RateLibrary.SessionAttributes;
import com.mine.code.util.DateUtil;
import com.mine.code.util.DataBaseSpecification.RatePlanTypes;
import com.mine.code.util.collections.MapUtils;
import com.mine.code.util.sessionutil.SessionUtils;

/**
 * 
 * @author sairam rajulapati
 */
public class FileImportingUtils {
	private static final Logger LOGGER = Logger
			.getLogger(FileImportingUtils.class);
	private RateLibrary library = null;
	public Map<String, Object> importingData(File[] FILES,String COLUMN_INDEXS[],String FILE_NAMES[],RateLibrary library) throws FileImportingException{
		this.library = library;
		return this.importingData(FILES, FILE_NAMES, 
				COLUMN_INDEXS, library.getOnlyNumericFields(), library.getOnlyDecimalFields(),
				library.getOnlyStringFields(), library.getEmptyFields(),library.getYesOrNotFields(), 
				library.getSpecialCharaterAppendFields(), 
				library.getSpecialCharaterRemoveFields(), library.getOnlyDateValueFields(), 
				library.getCheckFirstDuplicateFields(), 
				library.getCheckSecondDuplicateFields(), library.getDataShouldBeIn(),
				library.getColumnsCount(), 
				library.isOverWriteDataBaseDuplicates(), 
				RowType.NO_DUPLICATE, library.getSelectQuery(),
library.getCheckDuplicateQuery(),library.getInsertQuery(),library.getUpdateQuery());
	}
	
	/**
	 * 
	 * @param FILES
	 *            attached Files details.
	 * @param FILE_NAMES
	 *            collection of file names.
	 * @param COLUMN_INDEXS
	 *            represents the column mapping indexes order.
	 * @param onlyNumericFields
	 *            collection of column details which are only allowed to numeric
	 *            type.
	 * @param onlyStringFields
	 *            collection of column details which are only allowed to String
	 *            type.
	 * @param emptyFields
	 *            collection of column details which are only allowed to empty
	 *            value.
	 * @param yesOrNotFields
	 *            collection of column details which are either 'YES' or 'NO'.
	 * @param onlyDateValueFields
	 *            collection of column details which are only allowed to Date
	 *            type.
	 * @param checkDuplicateFields
	 *            collection of column details which are only allowed only one
	 *            time .
	 * @param specialCharaterAppendFields
	 *            collection of column details which are having extra data
	 *            append to those. Exg: 7001 to $7001.
	 * @param specialCharaterRemoveFields
	 *            collection of column details which are having unncessary data.
	 *            Exg: 7001.0 to 7001.
	 * @param dataShouldBeIn
	 *            collection of column details which are having speified data
	 *            only. Exg: Network only 'OPEN' or '3G'.
	 * @param NUMBER_OF_CELLS
	 *            count of number of cell for cell iteration.
	 * @param overwriteDatabaseDuplicates
	 *            it's boolean variable and specifies duplicates are override in
	 *            data base or not.
	 * @param COLUMNS_TYPE_QUERY
	 *            query for checking dataBase existed column details along with
	 *            those dataTypes.
	 * @param CHECK_DUPLICATE_QUERY
	 *            query for checking unique fields already existed in dataBase
	 *            or not.
	 * @param INSERT_QUERY
	 *            query for data Insertion Query.
	 * @param UPDATE_QUERY
	 *            query for existed data updation query.
	 * @return
	 * @throws FileImportingException
	 */
	public Map<String, Object> importingData(final File[] FILES,
			final String FILE_NAMES[], final String COLUMN_INDEXS[],
			List<String> onlyNumericFields, List<String> onlyDecimalFields,
			List<String> onlyStringFields, List<String> emptyFields,
			List<String> yesOrNotFields,
			Map<String, String> specialCharaterAppendFields,
			Map<String, String> specialCharaterRemoveFields,
			List<String> onlyDateValueFields,
			Map<Integer, String> checkFirstDuplicateFields,
			Map<Integer, String> checkSecondDuplicateFields,
			Map<String, List<String>> dataShouldBeIn,
			final int NUMBER_OF_CELLS, boolean overwriteDatabaseDuplicates,
			RowType rowType, final String COLUMNS_TYPE_QUERY,
			final String CHECK_DUPLICATE_QUERY, final String INSERT_QUERY,
			final String UPDATE_QUERY) throws FileImportingException {
		LOGGER.info(" Starting of Fie Importing Importing.... ");
		// Validate Required parameters if those are the meats the requirements
		// or not.
		FileValidationUtils.validateParameters(FILES, FILE_NAMES,
				COLUMN_INDEXS, checkFirstDuplicateFields,
				checkSecondDuplicateFields, NUMBER_OF_CELLS,
				COLUMNS_TYPE_QUERY, CHECK_DUPLICATE_QUERY, INSERT_QUERY,
				UPDATE_QUERY);
		// DataBase Related Components
		Connection connection = null;
		Statement columnTypeStatement = null; // for dataBase Column Type.
		PreparedStatement checkDuplicateStatement = null;
		PreparedStatement insertionStatement = null;
		PreparedStatement updateStatement = null;
		ResultSetMetaData columnsTYPE = null;
		ResultSet dataRecords = null;
        // execution cout 
		int executionCount = 0;
		// to identify process starting time .
		Date processStaringTime = null;
		// EXSheet Components
		Workbook workBook = null;
		Sheet sheet = null;
		Row row = null;
		// Predefined Variables Block
		int firstRowNumber = 0;
		int lastRowNumber = 0;
		final Map<String, Object> operationStatus = new LinkedHashMap<String, Object>();
		// Specifies the status default value as fail
		operationStatus.put("status", "fail");
		// Specifies the duplicateExists default value as inDataBase
		operationStatus.put("duplicateExists", "DataBase");
		// Collect the errors form each row mean while file iteration.
		Map<String, String> rowErrors = new LinkedHashMap<String, String>();
		// Collect the exceptions.
		List<String> exceptions = new LinkedList<String>();
		// Collect the duplicateRows which are already existed on dataBase.
		Map<String, String> duplicateRows = new LinkedHashMap<String, String>();
		Map<String, String> dataBaseDuplicateRows = new LinkedHashMap<String, String>();
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		decimalFormat.setMaximumFractionDigits(2);
		decimalFormat.setMinimumFractionDigits(2);
		// Check Duplicate Records
		TreeSet<String> checkDuplicateData = new TreeSet<String>();
		// Check Row is Empty
		Set<String> rowEmptyCellsCollection = new HashSet<String>();

		/**
		 *  Collect the RPA plan ids . 
		 */
		List<Integer> trgRatePlanIds = null;
		boolean atLeastOneTRGRowExists = false;
		/**
		 * Get Target Rate Group name and notes are geting from session scope.
		 */
		String trgName = (String) SessionUtils.getAttribute(SessionAttributes.TARGET_RATE_GROUP_NAME);
		boolean prepareTRG = StringUtils.isNotEmpty(trgName);
		/**
		 * initialize the rpaIndex List only if we go through Rate Plan Analysis.  
		 */
		if(prepareTRG) {
			trgRatePlanIds = new LinkedList<Integer>();
		}

		// Acual Logic Begins
		try {
			// DataBase Connection establish
			connection = JdbcConnection.getJdbcConnection();
			if (connection.getAutoCommit()) {
				connection.setAutoCommit(Boolean.FALSE);
			}
			// Append Query to relevent prepare statements.
			checkDuplicateStatement = connection
					.prepareStatement(CHECK_DUPLICATE_QUERY.toString());
		/**
		 *  Add parameter PreparedStatement.RETURN_GENERATED_KEYS 
		 *  to get Rate Plan Library Id (it's generated by database auto increment).
		 */
				insertionStatement = connection.prepareStatement(INSERT_QUERY
					.toString(),PreparedStatement.RETURN_GENERATED_KEYS);
			updateStatement = connection.prepareStatement(UPDATE_QUERY
					.toString());
			// Execute the Select Query for Getting dataBase Columns details
			// along with those data types.
			columnTypeStatement = connection.createStatement();
			dataRecords = columnTypeStatement.executeQuery(COLUMNS_TYPE_QUERY);
			columnsTYPE = dataRecords.getMetaData();
			dataRecords.close();
			columnTypeStatement.close();
			String firstTypeDuplicateFields = "";
			String secondTypeDuplicateFields = "";
			// String firstConcatination="";
			// String secondConcatination="";
			MapUtils<Integer, String> mapUtils = new MapUtils<Integer, String>();
			firstTypeDuplicateFields = mapUtils.getValuesWithSeparator(
					checkFirstDuplicateFields, " and ");
			secondTypeDuplicateFields = mapUtils.getValuesWithSeparator(
					checkSecondDuplicateFields, " and ");
			// firstConcatination=checkFirstDuplicateFields.size()>1?" are ":" is ";
			// secondConcatination=checkSecondDuplicateFields.size()>1?" are ":" is ";

			String checkDuplicateIndexFirstValue[] = null;

			// Actuall file importing processing star now.
			processStaringTime = new Date();
			for (int fileIndex = 0; fileIndex < FILES.length; fileIndex++) {
				if (FILES[fileIndex].exists()) {

					// Get the file extentions type.
					String fileExtendType = FILE_NAMES[fileIndex].substring(
							FILE_NAMES[fileIndex].lastIndexOf("."),
							FILE_NAMES[fileIndex].length());
					if (fileExtendType.equalsIgnoreCase(".xls")) {
						workBook = new HSSFWorkbook(new FileInputStream(
								FILES[fileIndex]));
					} else {
						workBook = new XSSFWorkbook(new FileInputStream(
								FILES[fileIndex]));
					}
					sheet = workBook.getSheetAt(0);
					firstRowNumber = sheet.getFirstRowNum() + 1;
					lastRowNumber = sheet.getLastRowNum();
					int cellIndex = 0;
					int currentCellIndex = 0;
					int correctExecutionCount;

					for (int rowIndex = firstRowNumber; rowIndex <= lastRowNumber; rowIndex++) {
						// Get the current row.
						row = sheet.getRow(rowIndex);
						if (row == null)
							continue;
						try {
							// Specifies the perameter index in
							// duplicatePreparedStatement.
							int checkDuplicateIndex = 1;
							checkDuplicateStatement.clearParameters();
							insertionStatement.clearParameters();
							updateStatement.clearParameters();
							rowEmptyCellsCollection.clear();
							String cellContent = "";
							for (cellIndex = 0, correctExecutionCount = 0; cellIndex < NUMBER_OF_CELLS; cellIndex++) {
								try {
									boolean allowNullable = Boolean.FALSE;
									currentCellIndex = Integer
											.parseInt(COLUMN_INDEXS[cellIndex]);
									// get the cellContent regarding to current
									// cell index
									int columnType = columnsTYPE
											.getColumnType(cellIndex + 1);
									cellContent = ColumnUtils.getCellContent(
											row, currentCellIndex, columnType);
									// if(cellContent==null) continue;
									String columnName = columnsTYPE
											.getColumnName(cellIndex + 1);
									CellType cellType = null;
									// Remove Special Character from content.
									// Lets Suppose if cellcontent value is like
									// 7932.0 then we romove and replace it as
									// 7932
									if (specialCharaterRemoveFields != null
											&& specialCharaterRemoveFields
													.containsKey(columnName)) {
										cellContent = StringUtils.remove(
												cellContent,
												specialCharaterRemoveFields
														.get(columnName));
									}
									if (emptyFields == null
											|| firstTypeDuplicateFields
													.contains(columnName)
											|| secondTypeDuplicateFields
													.contains(columnName)
											|| !emptyFields
													.contains(columnName)) {
										ColumnUtils
												.cellValidation(
														CellType.NOT_EMPTY,
														cellContent);
									} else {
										allowNullable = true;
									}
									if (yesOrNotFields != null
											&& yesOrNotFields
													.contains(columnName)) {
										cellType = CellType.YES_OR_NO;
									} else if (onlyStringFields != null
											&& onlyStringFields
													.contains(columnName)) {
										cellType = CellType.ONLY_STRING;
									} else if (onlyNumericFields != null
											&& onlyNumericFields
													.contains(columnName)) {
										cellType = CellType.ONLY_NUMERIC;
									} else if (onlyDecimalFields != null
											&& onlyDecimalFields
													.contains(columnName)) {
										cellType = CellType.ONLY_DECIMAL;
									} else if (onlyDateValueFields != null
											&& onlyDateValueFields
													.contains(columnName)) {
										cellType = CellType.ONLY_DATE;
									} else if (dataShouldBeIn != null
											&& dataShouldBeIn
													.containsKey(columnName)) {
										ColumnUtils.cellValidation(cellContent,
												dataShouldBeIn,columnName);
									}
									// Validate cell value satisfies the
									// validation or not.
									if (allowNullable
											&& StringUtils.isEmpty(cellContent)) {
										cellType = null;
										columnType = Types.NULL;
									}
									ColumnUtils.cellValidation(cellType,
											cellContent);
									if (!rowErrors.isEmpty()) {
										correctExecutionCount++;
										continue;
									}
									// it's append specila character to cell
									// content
									// lets suppose it have set $ value to 10.00
									// then it's possible here $10.00
									if (specialCharaterAppendFields != null
											&& specialCharaterAppendFields
													.containsKey(columnName)) {
										cellContent = specialCharaterAppendFields
												.get(columnName) + cellContent;
									}
									if (checkFirstDuplicateFields
											.containsValue(columnName)
											|| checkSecondDuplicateFields
													.containsValue(columnName)) {
										checkDuplicateStatement.setString(
												checkDuplicateIndex++,
												cellContent);
									}

									switch (columnType) {
									case Types.INTEGER:
										int value = StringUtils
												.isEmpty(cellContent) ? 0
												: Integer.parseInt(cellContent);
										insertionStatement.setInt(
												cellIndex + 1, value);
										updateStatement.setInt(cellIndex + 1,
												value);
										break;
									case Types.DOUBLE:
										double doubleValue = StringUtils
												.isEmpty(cellContent) ? 0.00
												: Double.parseDouble(cellContent);
										insertionStatement.setDouble(
												cellIndex + 1, doubleValue);
										updateStatement.setDouble(
												cellIndex + 1, doubleValue);
										break;
									case Types.TIMESTAMP:
										java.sql.Timestamp timeStampValue = java.sql.Timestamp
												.valueOf(cellContent);
										insertionStatement.setTimestamp(
												cellIndex + 1, timeStampValue);
										updateStatement.setTimestamp(
												cellIndex + 1, timeStampValue);
										break;
									case Types.TIME:
										java.sql.Time timeValue = java.sql.Time
												.valueOf(cellContent);
										insertionStatement.setTime(
												cellIndex + 1, timeValue);
										updateStatement.setTime(cellIndex + 1,
												timeValue);
										break;
									case Types.DATE:
										java.sql.Date dateValue = java.sql.Date
												.valueOf(cellContent);
										// Date date =
										// DateUtil.parseUserDate(cellContent);
										insertionStatement.setDate(
												cellIndex + 1, dateValue);
										updateStatement.setDate(cellIndex + 1,
												dateValue);
										break;
									case Types.VARCHAR:
										insertionStatement.setString(
												cellIndex + 1, cellContent);
										updateStatement.setString(
												cellIndex + 1, cellContent);
										break;
									default:
										insertionStatement.setString(
												cellIndex + 1, cellContent);
										updateStatement.setString(
												cellIndex + 1, cellContent);
										break;
									}
									correctExecutionCount++;
								} catch (CellValidationException e) {
									rowErrors
											.put("Cell  "
													+ CellReference
															.convertNumToColString(currentCellIndex)
													+ "" + (rowIndex + 1), e
													.getMessage());
									// columnsTYPE.getColumnName(cellIndex+1)
									if (correctExecutionCount == 0)
										rowEmptyCellsCollection
												.add("Cell  "
														+ CellReference
																.convertNumToColString(currentCellIndex)
														+ "" + (rowIndex + 1));
								} catch (Exception exp) {
									exp.printStackTrace();
								}
							}// End of Cells Iteration
							if (correctExecutionCount == 0) {
								rowErrors.keySet().removeAll(
										rowEmptyCellsCollection);
								continue;
							}
							// Validate the row for checking row contains
							// duplicate data or not.
							checkDuplicateIndexFirstValue = RowUtils
									.rowValidation(row, rowType,
											checkFirstDuplicateFields,
											checkSecondDuplicateFields,
											checkDuplicateData, COLUMN_INDEXS);
							RowUtils.rowValidation(row, library);
							/*
							 * if(StringUtils.isEmpty(checkDuplicateIndexFirstValue
							 * )
							 * ||checkDuplicateIndexFirstValue.equalsIgnoreCase(
							 * "null")) { continue; }
							 */String duplicatesExistedIn = (String) operationStatus
									.get("duplicateExists");
							if (!(correctExecutionCount == NUMBER_OF_CELLS
									&& rowErrors.isEmpty() && !duplicatesExistedIn
									.equalsIgnoreCase("XLS"))) {
								continue;
							}
							// Almost all cell validation is done to here.
							/**
							 *  Check if current row RPA column has Yes/No values, base on @param prepareRPA .
							 *   Yes -> Means Current rate paln should be place in Rate Plan Analysis.
							 *   No  -> Means Leave current rate plan for Rate Plan Analysis. 
							 */
							boolean isTRGRow = false;
							if (prepareTRG) {
								isTRGRow = StringUtils.equalsIgnoreCase(ColumnUtils.getCellContent(row,cellIndex), "YES");
								if(isTRGRow) atLeastOneTRGRowExists = true;
							}
							cellIndex--;
							dataRecords = checkDuplicateStatement
									.executeQuery();
							if (dataRecords.next()) {
								int index = 1;
								int rowId = dataRecords.getInt(index++);
								// String
								// firstIndexValue=dataRecords.getString(checkFirstDuplicateFields.get(0));
								updateStatement.setInt(cellIndex + 2, rowId);
								updateStatement.addBatch();
								String errorMessage = "";
								int fistListDuplicateCount = 0;
								for (String value : checkDuplicateIndexFirstValue) {
									if (value.equalsIgnoreCase(dataRecords
											.getString(index++))) {
										fistListDuplicateCount++;
									}
								}
								if (fistListDuplicateCount == checkDuplicateIndexFirstValue.length) {

									errorMessage = String.format(
											RowType.NO_DUPLICATE.getMessage(),
											firstTypeDuplicateFields);
									// errorMessage=firstTypeDuplicateFields+errorMessage;
								} else {
									errorMessage = String.format(
											RowType.NO_DUPLICATE.getMessage(),
											secondTypeDuplicateFields);
									// errorMessage=secondTypeDuplicateFields+errorMessage;
								}
								dataBaseDuplicateRows.put("Row "
										+ (rowIndex + 1), errorMessage);
								// duplicateRows.put("Cell "+CellReference.convertNumToColString(cellIndex-1)+""+(rowIndex+1),
								// RowType.NO_DUPLICATE.getMessage());
                             
								//Collect RPA rate plan ids.
								if(isTRGRow) trgRatePlanIds.add(rowId);
							} else {
									//Collect RPA rate plan ids.
									if (isTRGRow) {
										insertionStatement.execute();
										ResultSet resultSet = insertionStatement
										                                          .getGeneratedKeys();
									       if (resultSet.next()) {
										              trgRatePlanIds.add(resultSet.getInt(1));
									              }
								    } else {
									            insertionStatement.addBatch();
								     }
							 }
							   executionCount++;
						} catch (RowValidationException e) {
							String errorMessage = "";
							if(e.getRowType() == RowType.NO_DUPLICATE){
								
									if (rowType.getCondition() == Condition.FIRST_TYPE) {
										errorMessage = String.format(e.getMessage(),
												firstTypeDuplicateFields);
										// errorMessage=firstTypeDuplicateFields+errorMessage;
									} else if (rowType.getCondition() == Condition.SECOND_TYPE) {
										errorMessage = String.format(e.getMessage(),
												secondTypeDuplicateFields);
										// errorMessage=secondTypeDuplicateFields+errorMessage;
									}
								
							} else{
								     errorMessage = StringUtils.replace(e.getMessage(),";",".\n\r");
							}
							duplicateRows.put("Row " + (rowIndex + 1), errorMessage);
						}
					}// End of Rows Iteration

				}// Ending of Checking of file Existing
			}// End of files iteration
			if (duplicateRows.isEmpty() && dataBaseDuplicateRows.isEmpty())
				overwriteDatabaseDuplicates = true;
			
			/**
			 *      lastRowNumber represent rows count of importing file,
			 *  it's reduced by one because 1st row represent column heading, so that is not be counted.
			 */
			lastRowNumber = executionCount;
			if (overwriteDatabaseDuplicates && rowErrors.isEmpty()) {
				int[] insertedRecords = insertionStatement.executeBatch();
				int[] updatedRecords = updateStatement.executeBatch();
				int failedRecordsCount = lastRowNumber
						- (insertedRecords.length + updatedRecords.length);
				operationStatus.put("trgID",0);
				if(atLeastOneTRGRowExists) {
					operationStatus.put("trgID",createTargetRateGroups(connection,trgRatePlanIds));
				}
				// connection.commit();
				operationStatus.put("status", AppConstants.SUCCESS);
				operationStatus.put("processingTime",
						DateUtil.getDateDifference(processStaringTime));
				operationStatus.put("totalRecords", lastRowNumber);
				operationStatus.put("insertCount", insertedRecords.length);
				operationStatus.put("updateCount", updatedRecords.length);
				operationStatus.put("failedRecordsCount", failedRecordsCount);
				operationStatus.put("atLeastOneTRGRowExists", atLeastOneTRGRowExists);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			exceptions.add(e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			exceptions.add(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			exceptions.add(e.getMessage());
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					exceptions.add(e.getMessage());
				}
		}
		// Actual Logic Ending
		LOGGER.info(" Ending of Text Rate Plan Library Importing.... ");
		if (rowErrors.size() != 0 || duplicateRows.size() != 0)
			operationStatus.put("duplicateExists", "XLS");
		operationStatus.put("duplicateRows", duplicateRows);
		operationStatus.put("dataBaseDuplicateRows", dataBaseDuplicateRows);
		operationStatus.put("rowErrors", rowErrors);
		operationStatus.put("exceptions", exceptions);
		return operationStatus;
	}
	/**
	 * @author Sairam Rajulapati
	 * @param connection 
	 * @param trgRatePlanIds 
	 * @return 
	 * @throws SQLException 
	 * 
	 */
	private int createTargetRateGroups(Connection connection, List<Integer> trgRatePlanIds) throws SQLException{
		int trgID = 0;
		String trgName = (String) SessionUtils.getAttribute(SessionAttributes.TARGET_RATE_GROUP_NAME);
		String trgNotes =  (String) SessionUtils.getAttribute(SessionAttributes.TARGET_RATE_GROUP_NOTES);	
		String ratePlanType = (String) SessionUtils.getAttribute(SessionAttributes.RATE_PLAN_TYPE);
		Integer trgGroupTypeId = RatePlanTypes.getTargetRatePlanID(ratePlanType);
		Integer companyID = SessionUtils.getCompanyId();
		Integer customerID = SessionUtils.getCustomerId();
		Integer userID = SessionUtils.getUserId();
		trgName = StringUtils.replace(trgName, "'","");
		trgNotes = StringUtils.replace(trgNotes, "'","");
          StringBuffer query  = new StringBuffer("INSERT INTO `target_rate_group` (`trg_name`, `trg_notes`, `created_by_user_id`, `customer_id`, `company_id`,`target_rate_group_type`) ");
          query.append(" VALUES ('"+trgName+"', '"+trgNotes+"',"+userID+", "+customerID+", "+companyID+","+trgGroupTypeId+"); ");
          //query.append(" SELECT LAST_INSERT_ID() into @trgId; ");
          Statement statement = connection.createStatement();
          statement.execute(query.toString(),Statement.RETURN_GENERATED_KEYS);
          ResultSet resultSet = statement.getGeneratedKeys();
          if(resultSet.next()){
        	  trgID = resultSet.getInt(1);
          }
          query.delete(0, query.length());
          query.append(" INSERT INTO `target_rate_plans` (`target_rate_group_id`, `rate_plan_id`)  VALUES");
          String ratePlanIdQuery = " ("+trgID+", %s)";
          String cama = ",";
          for(int ratePlanId : trgRatePlanIds)
          {
        	  query.append(String.format(ratePlanIdQuery,ratePlanId));
       		  query.append(cama);
          }
       String sql = StringUtils.removeEnd(query.toString(), cama);
       statement = connection.createStatement();
       statement.execute(sql);
       return trgID;
	}
}
