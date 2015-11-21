package com.sutisoft.roe.util.fileutil;

import java.util.Map;
import java.util.TreeSet;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import com.sutisoft.roe.exception.CellValidationException;
import com.sutisoft.roe.exception.RowValidationException;
import com.sutisoft.roe.ratelibraries.RateLibrary;
import com.sutisoft.roe.setup.AppConstants;
import com.sutisoft.roe.setup.Condition;
import com.sutisoft.roe.setup.RowType;
import com.sutisoft.roe.util.regularutils.RegularUtils;

/**
 * 
 * @author sairam rajulapati
 * 
 */
public class RowUtils {
	/**
	 * 
	 * @param row
	 *            xls row
	 * @param rowType
	 *            Validation Type
	 * @param checkDuplicateFields
	 * @param checkDuplicateData
	 * @return
	 * @throws RowValidationException
	 */
	private static final String comma = ",";

	public synchronized static String[] rowValidation(Row row, RowType rowType,
			Map<Integer, String> checkFirstDuplicateFields,
			Map<Integer, String> checkSecondDuplicateFields,
			TreeSet<String> checkDuplicateData, final String COLUMN_INDEXS[])
			throws RowValidationException {
		boolean validationFailed = false;
		String[] columnNames = new String[checkFirstDuplicateFields.size()];
		// String firstIndexValue=null;
		StringBuffer firstValue = new StringBuffer();
		StringBuffer secondValue = new StringBuffer();
		String value = null;
		int index = 0;
		for (Map.Entry<Integer, String> entry : checkFirstDuplicateFields
				.entrySet()) {
			int currentCellIndex = Integer.parseInt(COLUMN_INDEXS[entry
					.getKey()]);
			value = ColumnUtils.getCellContent(row, currentCellIndex);
			columnNames[index++] = value;
			firstValue.append(value);
			// if(firstIndexValue==null) firstIndexValue=firstValue.toString();
		}
		for (Map.Entry<Integer, String> entry : checkSecondDuplicateFields
				.entrySet()) {
			int currentCellIndex = Integer.parseInt(COLUMN_INDEXS[entry
					.getKey()]);
			value = ColumnUtils.getCellContent(row, currentCellIndex);
			secondValue.append(value);
		}
		switch (rowType) {
		case NO_DUPLICATE:
			if (validationFailed = !checkDuplicateData.add(firstValue
					.toString()))
				rowType.setCondition(Condition.FIRST_TYPE);
			else if (checkSecondDuplicateFields.size() != 0
					&& (validationFailed = !checkDuplicateData.add(secondValue
							.toString())))
				rowType.setCondition(Condition.SECOND_TYPE);
			break;
		default:
			break;
		}
		if (validationFailed) {
			throw new RowValidationException(rowType);
		}
		return columnNames;
	}

	public synchronized static void rowValidation(Row row, RateLibrary library)
			throws RowValidationException, CellValidationException {
		if (library == null)
			return;
		StringBuffer validationErrors = new StringBuffer();
		for (RowType validation : library.getValidations()) {
			switch (validation) {
			case DEPENDENT:
				dependentValidation(validation, row, library, validationErrors);
				break;
			case AMONG_COLUMNS:
				amongColumnsValidation(validation, row, library, validationErrors);
				break;
			case COLUMNS_SUM:
				columnsSumValidation(validation, row, library, validationErrors);
				break;
			default:
				break;
			}
		}
		String errors = validationErrors.toString();
		if (StringUtils.isNotEmpty(errors)) {
			throw new RowValidationException(errors);
		}
	}
	private static void columnsSumValidation(RowType validation, Row row,
			RateLibrary library, StringBuffer validationErrors) {
         for( String columnsTag : library.getColumnsSum() ){
			String[] data = columnsTag.split(":");
			String columnTag = data[0].trim();
			String[] columns = columnTag.split(" ");
			String columnsAlias = getColumnAlias(library, columns);
			String target = data[1];
			String targetValue = ColumnUtils.getCellContent(row,
					library.getColumnIndex(target));
			targetValue = RegularUtils.convertIntoDecimal(targetValue);
			double columnsTotal = 0.00;
			for( String column : columns ){
				    String columnValue = ColumnUtils.getCellContent(row,library.getColumnIndex(column));
				    columnValue = RegularUtils.convertIntoDecimal(columnValue);
				    try{
				    	columnsTotal += Double.parseDouble(columnValue);
				    }catch(NumberFormatException exp){
				    	return;
				    }
			}
			if(! StringUtils.equalsIgnoreCase(targetValue, RegularUtils.convertIntoDecimal(""+columnsTotal))){
				String error = String.format(validation.getMessage(), columnsAlias, target);
				validationErrors.append(error);
			}
         }
	}

	private synchronized static void amongColumnsValidation(RowType validation,
			Row row, RateLibrary library, StringBuffer validationErrors) {
		for (String columnsTag : library.getAmongColumns()) {
			String[] data = columnsTag.split(":");
			String columnTag = data[0].trim();
			String[] columns = columnTag.split(" ");
			String columnsAlias = getColumnAlias(library, columns);
			String target = data[1];

			boolean isCaseSensitive = StringUtils.equalsIgnoreCase(
					AppConstants.YES, data[2]);
			int targetHitCount = 0;
			for (String column : columns) {
				String columnValue = ColumnUtils.getCellContent(row,
						library.getColumnIndex(column));
				if (RegularUtils.equals(isCaseSensitive, columnValue, target)) {
					targetHitCount++;
				}
			}
			if (targetHitCount > 1) {
				// Only one among %s must be - %s
				String error = String.format(validation.getMessage(),
						columnsAlias, target);
				validationErrors.append(error);
				return;
			}
		}
	}
	private synchronized static void dependentValidation(RowType validation,
			Row row, RateLibrary library, StringBuffer validationErrors) throws CellValidationException {
		childs: for (Map.Entry<String, String> column : library
				.getIfValidation().entrySet()) {
			String[] key = column.getKey().split(":");
			String[] values = column.getValue().split(":");
			String parent = key[0];
			int parentIndex = library.getColumnIndex(parent);
			String parentAlias = library.getAliasName(parent);
			String parentTarget = key[1];
			String parentValue = ColumnUtils.getCellContent(row, parentIndex);
			boolean isParenetCaseSensitive = StringUtils.equalsIgnoreCase(AppConstants.YES, key[2]);
			parentValue = StringUtils.equalsIgnoreCase(parentValue, "0.0") ? "0"
					: parentValue;
			String childsTag = values[0].trim();
			String[] childs = childsTag.split(" ");
			String columnsAlias = getColumnAlias(library, childs);
			String childTarget = values[1];
			boolean isChildCaseSensitive = StringUtils.equalsIgnoreCase(
					AppConstants.YES, values[2]);
			if (RegularUtils.equals(isParenetCaseSensitive, parentValue,
					parentTarget)) {
				for (String child : childs) {
					String childValue = ColumnUtils.getCellContent(row,
							library.getColumnIndex(child));
					childValue = StringUtils.equalsIgnoreCase(childValue, "0.0") ? "0"
							: childValue;
					if (!RegularUtils.equals(isChildCaseSensitive, childValue,
							childTarget)) {
						// " Plan with %s - %s, %s Must be % ;"
						String error = String.format(validation.getMessage(),
								parentAlias, parentTarget, columnsAlias, childTarget);
						validationErrors.append(error);
						continue childs;
					}
				}
			}
		}
	}
	private static String getColumnAlias( RateLibrary library,String...columns) {
		String columnsAlias = "";
		for( String column : columns ){
			columnsAlias = columnsAlias.concat(library.getAliasName(column));
			columnsAlias = columnsAlias.concat(comma);
		}
		columnsAlias = StringUtils.removeEndIgnoreCase(columnsAlias, comma);
		return columnsAlias;
	}
}
