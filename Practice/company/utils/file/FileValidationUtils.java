package com.mine.code.util.fileutil;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;

import com.mine.code.exception.FileImportingException;
import com.mine.code.setup.FileValidationType;

public class FileValidationUtils {
public static void validateParameters(final File[] FILES,final String FILE_NAMES[],
		    final String COLUMN_INDEXS[],final Map<Integer,String> checkFirstDuplicateFields,Map<Integer,String> checkSecondDuplicateFields,
		    int NUMBER_OF_CELLS,final String COLUMNS_TYPE_QUERY,
			final String CHECK_DUPLICATE_QUERY, final String INSERT_QUERY,
			final String UPDATE_QUERY) throws FileImportingException{
	FileValidationType validationType=null;
	if(FILES==null) validationType=FileValidationType.FILES_NOT_EMPTY;
	else if(FILE_NAMES==null||FILE_NAMES.length==0) validationType=FileValidationType.FILE_NAMES_NOT_EMPTY;
	else if(COLUMN_INDEXS==null||COLUMN_INDEXS.length==0) validationType=FileValidationType.COLUMN_INDEXES_NOT_EMPTY;
	else if(checkFirstDuplicateFields==null||checkFirstDuplicateFields.size()==0) validationType=FileValidationType.CHECK_DUPLICATE_CONDITION_NOT_EMPTY;
	else if(NUMBER_OF_CELLS==0) validationType=FileValidationType.NUMBER_OF_CELLS;
	else if(StringUtils.isEmpty(COLUMNS_TYPE_QUERY)) validationType=FileValidationType.COLUMNS_TYPE_QUERY;
	else if(StringUtils.isEmpty(CHECK_DUPLICATE_QUERY)) validationType=FileValidationType.DUPLICATE_QUERY;
	else if(StringUtils.isEmpty(INSERT_QUERY)) validationType=FileValidationType.INSERT_QUERY;
	else if(StringUtils.isEmpty(UPDATE_QUERY)) validationType=FileValidationType.UPDATE_QUERY;
	if(validationType!=null) throw new FileImportingException(validationType);
}
}
