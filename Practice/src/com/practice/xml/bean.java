package ratelibraries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.xwork.StringUtils;

import setup.AppConstants;
import setup.RowType;
import util.sessionutil.SessionUtils;

public class RateLibrary {
	private String ratePlanName = "";
	private int ratePlanTypeId;
	private String tableName = "";
	private String primaryKey = "";
	private String userId = "";
	private String companyId = "";
	private String carrier = "";
	private String customer = "";
	private int columnsCount = 0;
	private boolean overWriteDataBaseDuplicates = Boolean.FALSE;
	private Map<Integer, String> columns = new HashMap<Integer, String>();
	private Map<Integer, String> columnAlias = new HashMap<Integer, String>();
	private List<String> yesOrNotFields = null;
	private List<String> onlyNumericFields = null;
	private List<String> onlyDecimalFields = null;
	private List<String> onlyDateValueFields = null;
	private List<String> onlyStringFields = null;
	private List<String> emptyFields = null;
	private Map<Integer, String> checkFirstDuplicateFields = new HashMap<Integer, String>();
	private Map<Integer, String> checkSecondDuplicateFields = new HashMap<Integer, String>();
	private Map<String, String> specialCharaterAppendFields = null;
	private Map<String, String> specialCharaterRemoveFields = null;
	private Set<RowType> validations = new TreeSet<RowType>();
	private Map<String, List<String>> dataShouldBeIn = null;
	private StringBuffer selectQuery = new StringBuffer();
	private StringBuffer updateQuery = new StringBuffer();
	private StringBuffer insertQuery = new StringBuffer();
	private StringBuffer checkDuplicateQuery = new StringBuffer();
	private Map<String, String> ifValidation = new HashMap<String, String>();
    private List<String> amongColumns = new ArrayList<String>();
	public String getRatePlanName() {
		return ratePlanName;
	}

	public void setRatePlanName(String ratePlanName) {
		this.ratePlanName = ratePlanName;
	}

	public int getRatePlanTypeId() {
		return ratePlanTypeId;
	}

	public void setRatePlanTypeId(int ratePlanTypeId) {
		this.ratePlanTypeId = ratePlanTypeId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public int getColumnsCount() {
		return columnsCount;
	}

	public void setColumnsCount(int columnsCount) {
		this.columnsCount = columnsCount;
	}

	public boolean isOverWriteDataBaseDuplicates() {

		return StringUtils.equals(
				SessionUtils.getRequestParameter(AppConstants.CURRENT_ACTION),
				AppConstants.UPDATE);
	}

	public Map<Integer, String> getColumns() {
		return columns;
	}

	public void setColumns(Map<Integer, String> columns) {
		this.columns = columns;
	}

	public Map<Integer, String> getColumnAlias() {
		return columnAlias;
	}

	public void setColumnAlias(Map<Integer, String> columnAlias) {
		this.columnAlias = columnAlias;
	}

	public List<String> getYesOrNotFields() {
		return yesOrNotFields;
	}

	public void setYesOrNotFields(List<String> yesOrNotFields) {
		this.yesOrNotFields = yesOrNotFields;
	}

	public List<String> getOnlyNumericFields() {
		return onlyNumericFields;
	}

	public void setOnlyNumericFields(List<String> onlyNumericFields) {
		this.onlyNumericFields = onlyNumericFields;
	}

	public List<String> getOnlyDecimalFields() {
		return onlyDecimalFields;
	}

	public void setOnlyDecimalFields(List<String> onlyDecimalFields) {
		this.onlyDecimalFields = onlyDecimalFields;
	}

	public List<String> getEmptyFields() {
		return emptyFields;
	}

	public void setEmptyFields(List<String> emptyFields) {
		this.emptyFields = emptyFields;
	}

	public Map<Integer, String> getCheckFirstDuplicateFields() {
		return checkFirstDuplicateFields;
	}

	public void setCheckFirstDuplicateFields(
			Map<Integer, String> checkFirstDuplicateFields) {
		this.checkFirstDuplicateFields = checkFirstDuplicateFields;
	}

	public Map<Integer, String> getCheckSecondDuplicateFields() {
		return checkSecondDuplicateFields;
	}

	public void setCheckSecondDuplicateFields(
			Map<Integer, String> checkSecondDuplicateFields) {
		this.checkSecondDuplicateFields = checkSecondDuplicateFields;
	}

	public Map<String, String> getSpecialCharaterRemoveFields() {
		return specialCharaterRemoveFields;
	}

	public void setSpecialCharaterRemoveFields(
			Map<String, String> specialCharaterRemoveFields) {
		this.specialCharaterRemoveFields = specialCharaterRemoveFields;
	}
	
	public Map<String, String> getSpecialCharaterAppendFields() {
		return specialCharaterAppendFields;
	}

	public Map<String, List<String>> getDataShouldBeIn() {
		return dataShouldBeIn;
	}

	public void setDataShouldBeIn(Map<String, List<String>> dataShouldBeIn) {
		this.dataShouldBeIn = dataShouldBeIn;
	}

	public StringBuffer getSelectQuery() {
		return selectQuery;
	}

	public void setSelectQuery(StringBuffer selectQuery) {
		this.selectQuery = selectQuery;
	}

	public StringBuffer getUpdateQuery() {
		return updateQuery;
	}

	public void setUpdateQuery(StringBuffer updateQuery) {
		this.updateQuery = updateQuery;
	}

	public StringBuffer getInsertQuery() {
		return insertQuery;
	}

	public void setInsertQuery(StringBuffer insertQuery) {
		this.insertQuery = insertQuery;
	}

	public StringBuffer getCheckDuplicateQuery() {
		return checkDuplicateQuery;
	}

	public void setCheckDuplicateQuery(StringBuffer checkDuplicateQuery) {
		this.checkDuplicateQuery = checkDuplicateQuery;
	}

	public List<String> getOnlyDateValueFields() {
		return onlyDateValueFields;
	}

	public void setOnlyDateValueFields(List<String> onlyDateValueFields) {
		this.onlyDateValueFields = onlyDateValueFields;
	}

	public List<String> getOnlyStringFields() {
		return onlyStringFields;
	}

	public void setOnlyStringFields(List<String> onlyStringFields) {
		this.onlyStringFields = onlyStringFields;
	}

	public Map<String, String> getIfValidation() {
		return ifValidation;
	}

	public List<String> getAmongColumns() {
		return amongColumns;
	}

	public void setAmongColumns(List<String> amongColumns) {
		this.amongColumns = amongColumns;
	}

    public Set<RowType> getValidations() {
		return validations;
	}

	public void setValidations(Set<RowType> validations) {
		this.validations = validations;
	}

	/**
     * It's an RateLibrary Utils method for get Columns index based on its name.
     * @param column
     * @return
     */
	public int getColumnIndex(String column) {
		for (Map.Entry<Integer, String> rColumn : this.getColumns().entrySet()) {
			if (StringUtils.endsWithIgnoreCase(column, rColumn.getValue()))
				return rColumn.getKey();
		}
		return 0;
	}
	
	
}
