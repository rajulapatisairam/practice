package util.reflections;

import java.lang.reflect.Field;

/**
 *
 * 
 *   FieldUtils is reflection class.
 *   This class can provide util functionalities for calss field. 
 *     
 *  
 *  @version 1.1.1  06 Jun 2015
 *  @author sairam rajulapati 
 */
public class FieldUtils {
	/**
	 * 
	 * @param clas
	 * @return Array of Strings, it contains @clas member variable values.
	 */
public static String[] getClassMemberValues(Class<?> clas) {
	/* Get array of all  member variables of @param clas. */
	 Field[] classFields = clas.getFields();   
	
	 /*String array collect field values */
	String[] fieldValues = new String[classFields.length]; 
	
	/**   used in fieldValues array for represent current index while
	 *      assign field values to fieldValues array. */
	int index = 0; 
	
	/*  Iterator through classFields[] array list.  */
	for(Field field : classFields) { 
			try {
				/* True for allow accessing the private fields too. */
				field.setAccessible(Boolean.TRUE);  
				
				/** assign individual field value to fieldValues array 
				      with respective to current index*/
				fieldValues[index++] = "" + field.get(clas);
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	return fieldValues;
}
}
