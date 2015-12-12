/**
 * 
 */
package utils.pagination;

import hibernate.BaseHibernateDAO;
import hibernate.HibernateSessionFactory;
import hibernate.Users;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import reusable.AppConstants;
import reusable.LoggerUtils;
import reusable.session.SessionUtils;

/**
 * @author Sairam Rajulapati.
 *
 */
public class PaginationUtils<Entity> extends BaseHibernateDAO {
	public static final Logger LOGGER = Logger.getLogger(PaginationUtils.class);
	
	private final int PAGINATION_LIMIT = 5;
	private Entity entity;
	private Session session;
	private Criteria criteria;
	private Integer totalRecordsCount;
	private int startIndex;
	private int numberOfRecords;
	private List<Entity> results;
	private Map<String, Object> restrictions;
	private int totalPages;
	private int currentPage;
	private int displayPageNumber;
	private int paginationIndex;

	private PaginationUtils() {
		super();
	}

	public PaginationUtils(Entity entity) {
		this.entity = entity;
	}

	public void getPaginationData1(Map<String, Object> restrictions, int startIndex, int numberOfRecords) {
		LOGGER.info(LoggerUtils.methodStartingMessage());
		this.startIndex = startIndex;
		this.numberOfRecords = numberOfRecords;
		this.restrictions = restrictions;
		try 
		{
			Example example = Example.create(entity).ignoreCase().enableLike(MatchMode.ANYWHERE);
			prepareBasicRequirements();
			if(!restrictions.isEmpty()){
				for(Map.Entry<String,Object> map : restrictions.entrySet()){
					if(StringUtils.equals(map.getValue().toString(),AppConstants.EXCLUDE_PROPERTY)){
						example.excludeProperty(map.getKey());
					}else{
						criteria.createCriteria(map.getKey())
						.add(Example.create(map.getValue()).ignoreCase().enableLike(MatchMode.ANYWHERE));
					}
					
				}
			}
			criteria.add(example);
			//c2aluclateTotalRecordsCount();
			if(numberOfRecords != 0){
				criteria.setFirstResult(this.startIndex);
				criteria.setMaxResults(this.numberOfRecords);
			}
			results = criteria.list();
			prepareResponseData();
				} catch (RuntimeException exp) {
					LOGGER.error(exp);
					exp.printStackTrace();
				} finally {
					HibernateSessionFactory.clearSession(session);
				}
				LOGGER.info(LoggerUtils.methodEndingMessage());
	}

	private void prepareResponseData() {
		restrictions.clear();
		restrictions.put("results", results);
		restrictions.put("totalPages", totalPages);
		restrictions.put("currentPage", currentPage);
		restrictions.put("displayPageNumber", displayPageNumber);
		restrictions.put("paginationIndex", paginationIndex);
		restrictions.put("maximumRowCount", totalRecordsCount);
		for (Map.Entry<String, Object> results : restrictions.entrySet()) {
			SessionUtils.setRequestAttribute(results.getKey(), results.getValue());
		}
	}

	private void caluclateTotalRecordsCount(ScrollableResults results) {
		results.last();
		this.totalRecordsCount = results.getRowNumber() + 1;
		results.close();
	    int numberOfPages = (totalRecordsCount / numberOfRecords);
		if ( (numberOfPages % numberOfRecords == 0)  || (totalRecordsCount % numberOfRecords == 0) ) numberOfPages--;
		totalPages = numberOfPages +1;
		if (startIndex < 0) {
			startIndex =  numberOfPages * numberOfRecords;
			currentPage = totalPages;
			displayPageNumber = totalPages;
			paginationIndex = totalPages - PAGINATION_LIMIT;
		}
	}
	
	/*Method for showing the last updated record in list*/
	public void getPaginationResult(String table,Map<String, Object> restrictions, int startIndex, int numberOfRecords) {
		LOGGER.info(LoggerUtils.methodStartingMessage());
		this.startIndex = startIndex;
		this.numberOfRecords = numberOfRecords;
		this.restrictions = restrictions;
		try 
		{
			prepareBasicRequirements();
			criteria.add(Example.create(entity).ignoreCase().enableLike(MatchMode.ANYWHERE));
			for(Map.Entry<String,Object> map : restrictions.entrySet()){
				criteria.createCriteria(map.getKey())
				.add(Example.create(map.getValue()).ignoreCase().enableLike(MatchMode.ANYWHERE));
			}

			if(table.equals("RequisitionHeader")){
				HttpServletRequest request=ServletActionContext.getRequest();
				String requisitionStatus=(String) request.getAttribute("status");
				if("Rejected".equals(requisitionStatus)){
					criteria.add(Restrictions.eq("requisitionStatus","Rejected"));
				}
				
				if("Contract Created".equals(requisitionStatus)){
					criteria.add(Restrictions.eq("requisitionStatus","Contract Created"));
				}
			}

			if(table.equals("Address")){
				criteria.add(Restrictions.eq("addressType","BillTo"));
			}
			if (table.equalsIgnoreCase("pendingRequisitions")){
				List<String> status = new ArrayList<String>();
				status.add("Draft");
				status.add("Pending Approval");
				status.add("Approved");
				status.add("Contract Created");
				status.add("Rejected");								
				criteria.add(Restrictions.in("requisitionStatus", status));
				Users user =SessionUtils.getUser();
				if (!user.getCreatedBy().equalsIgnoreCase("superadmin")){
					criteria.add(Restrictions.eq("requestorUserId", user));
				}
			}
			
			
			/*Code for showing last updated Record*/
			if(table.equals("Role")|| table.equals("CompanyCarriers") || table.equals("GeographicLocation") || table.equals("GlCodes") || table.equals("WorkFlowHeader")){
				criteria.addOrder(Order.desc("updatedDate"));
			}
			
			if(table.equals("Role")|| table.equals("CompanyCarriers") || table.equals("GeographicLocation") || table.equals("GlCodes") || table.equals("WorkFlowHeader")){
				criteria.addOrder(Order.desc("updatedDate"));
			}
			
			if(table.equals("CostCenter")){
				criteria.addOrder(Order.desc("modifiedOn"));
			}else if(table.equals("RequisitionApprovals")){
				criteria.add(Restrictions.eq("status", "Pending Approval"));
				criteria.add(Restrictions.eq("users.userId", SessionUtils.getUser().getUserId()));
				criteria.addOrder(Order.desc("updatedDate"));
				}else if(table.equals("RequisitionApprovalHeader")){
					criteria.add(Restrictions.eq("requisitionStatus", "Approved"));
					criteria.add(Restrictions.eq("requisitionApproverId", SessionUtils.getUser().getUserId()));
					criteria.addOrder(Order.desc("updatedDate"));
					
				}else if(table.equals("RateLibraries")){
					criteria.addOrder(Order.desc("createdTime"));

				}
				else if(table.equals("Supplier")){
					criteria.addOrder(Order.desc("lastUpdatedDate"));

				}
			
			/*else if(table.equals("Departments") || table.equals("Accessories") || table.equals("Supplier")){
					criteria.addOrder(Order.desc("lastUpdatedDate"));
				}*/
			caluclateTotalRecordsCount(criteria.scroll());
			criteria.setFirstResult(this.startIndex);
			criteria.setMaxResults(this.numberOfRecords);
			results = criteria.list();
			prepareResponseData();
				} catch (Exception exp) {
					LOGGER.error(exp);
					exp.printStackTrace();
				} finally {
					HibernateSessionFactory.clearSession(session);
					LOGGER.info(LoggerUtils.methodEndingMessage());
				}
	}

	private void prepareCriteria() {
		session = getSession();
		criteria = session.createCriteria(entity.getClass());
	}
	
	public void preparePaginationIndex() {
		this.currentPage = this.startIndex;
		displayPageNumber = startIndex+1;
		paginationIndex = startIndex;
		this.startIndex *= numberOfRecords;
	}
	public void prepareBasicRequirements() {
		preparePaginationIndex();
		prepareCriteria();
	}
	/**
	 * 
	 * @author Sairam Rajulapati.
	 * @date Dec 10, 2015
	 * @param hqlQuery should be hqlQuery than sql query.
	 * @param startIndex      pagination index.
	 * @param numberOfRecords  count of record to be display to the end user.
	 * 
	 *  This method is very useful to prepare pagination, if we have and hql Query.
	 *  HqlQuery prepration is very easy to developers.
	 */
	public void getPaginationData(String hqlQuery, int startIndex, int numberOfRecords) {
		LOGGER.info(LoggerUtils.methodStartingMessage());
		this.startIndex = startIndex;
		this.numberOfRecords = numberOfRecords;
		this.restrictions = new HashMap<String,Object>();
		try{
			preparePaginationIndex();
			preparePagination(hqlQuery);
			prepareResponseData();
		}catch(RuntimeException re){
			re.printStackTrace();
			throw re;
		}  finally {
			HibernateSessionFactory.clearSession(session);
			LOGGER.info(LoggerUtils.methodEndingMessage());
		}
		
	}
	
	public void getPaginationData(Map<String, Object> restrictions, int startIndex, int numberOfRecords) {
		LOGGER.info(LoggerUtils.methodStartingMessage());
		this.startIndex = startIndex;
		this.numberOfRecords = numberOfRecords;
		this.restrictions = restrictions;
		//String separator = ".";
		try{
			preparePaginationIndex();
			getResult();
			prepareResponseData();
		}catch(RuntimeException re){
			re.printStackTrace();
			throw re;
		}  finally {
			HibernateSessionFactory.clearSession(session);
			LOGGER.info(LoggerUtils.methodEndingMessage());
		}
		
	}
	
	private void getResult(){
		Class<?> cls = entity.getClass();
		Field[] fields = cls.getDeclaredFields();
		StringBuffer stringBuffer = new StringBuffer();
		String hqlQuery = getQuery(fields,stringBuffer,cls,restrictions);
		preparePagination(hqlQuery);
	}
	private void preparePagination(String hqlQuery ) {
		session = getSession();
		Query query = session.createQuery(hqlQuery);
		caluclateTotalRecordsCount(query.scroll());
		query.setFirstResult(this.startIndex);
		query.setMaxResults(this.numberOfRecords);
		results = query.list();
	}
	
	
	
	public String getQuery(Field[] fields,StringBuffer stringBuffer,Class<?> cls,Map<String, Object> restrictions){
		this.restrictions = restrictions;
		stringBuffer.append(" from "+cls.getName()+" t where 1=1 ");
		try{
			for(Field field : fields){
				field.setAccessible(true);
				String propetyName = field.getName();
				
				/**
			     * Some Pojo Class are may be implements java.io.Serializable 
			     * so jvm will add serialVersionUID to pojo classes so this property is not be 
			     * required in query preparation so it is negligible
			     */
				if(StringUtils.equalsIgnoreCase(propetyName,"serialVersionUID")) {
					continue;
				}
				
				Object value = field.get(entity);
				String dataType = field.getType().getName();
				boolean isDateType = isDateDataType(dataType);
				if((!StringUtils.contains(dataType, "java.lang.Object")) && (StringUtils.contains(dataType, "java.lang") || isPrimitive(dataType) || isDateType)){
					if(value != null && !value.toString().isEmpty() && !excludeProperty(restrictions,propetyName)){
						if(StringUtils.equalsIgnoreCase(dataType, "java.lang.String")){
							if(disableLike(restrictions, propetyName)){
								stringBuffer.append(" and t."+propetyName+" = '"+value+"'");// '"+value+"%'");
							}else{
								stringBuffer.append(" and t."+propetyName+" like lower('%"+value+"%')");// '"+value+"%'");
							}
							
						}else if(isDateType){
							value = StringUtils.substringBefore(value.toString(),".");
							if(disableLike(restrictions, propetyName)){
								stringBuffer.append(" and t."+propetyName+" = '"+value+"'");// '"+value+"%'");
							}else{
								stringBuffer.append(" and t."+propetyName+" like '%"+value+"%'");// '"+value+"%'");
							}
							
						}
						
						else{
							stringBuffer.append(" and t."+propetyName+" = "+value+"");
						}
					}
						
				}else{
					if(value != null && !value.toString().isEmpty()){
						if(!(value instanceof java.util.Collection)){
							Field[] fields2 = value.getClass().getDeclaredFields();
							for(Field field2 : fields2){
								field2.setAccessible(Boolean.TRUE);
								Object associatedPropertyValue = field2.get(value);
								String property = field2.getName();
								
								/**
							     * Some Pojo Class are may be implements java.io.Serializable 
							     * so jvm will add serialVersionUID to pojo classes so this property is not be 
							     * required in query preparation so it is negligible
							     */
								if(StringUtils.equalsIgnoreCase(property,"serialVersionUID")) {
									continue;
								}
								
								String dataType2 = field2.getType().getName();
								if((!StringUtils.contains(dataType2, "java.lang.Object")) && (StringUtils.contains(dataType2, "java.lang") || isPrimitive(dataType2) || isDateDataType(dataType2))){
									if(associatedPropertyValue != null && !associatedPropertyValue.toString().isEmpty() && !excludeProperty(restrictions,propetyName+"."+property)){
										if(StringUtils.equalsIgnoreCase(dataType2, "java.lang.String") ){
											if(disableLike(restrictions, propetyName+"."+property)){
												stringBuffer.append(" and t."+propetyName+"."+property+" = '"+associatedPropertyValue+"'");
											}else{
												stringBuffer.append(" and t."+propetyName+"."+property+" like lower('%"+associatedPropertyValue+"%')");//'%"+field2.get(value)+"%'");
											}
											
										}else if(isDateDataType(dataType2)){
											String fieldValue = StringUtils.substringBefore(associatedPropertyValue.toString(),".");
											if(disableLike(restrictions, propetyName+"."+property)){
												stringBuffer.append(" and t."+propetyName+"."+property+" = '"+fieldValue+"'");//'%"+field2.get(value)+"%'");
											}else{
												stringBuffer.append(" and t."+propetyName+"."+property+" like '%"+fieldValue+"%'");//'%"+field2.get(value)+"%'");
											}
											
										}
										
										else{
											stringBuffer.append(" and t."+propetyName+"."+property+" = "+associatedPropertyValue+"");
										}
										
									}
								}
								
							}
						}
					}
				}
			}
			if(restrictions != null && !restrictions.isEmpty()){
				for(Map.Entry<String,Object> map : restrictions.entrySet()){
					Object object = map.getValue();
					String objectDataType = object.getClass().getCanonicalName();
					if(!StringUtils.contains(objectDataType, "java.lang")){
						Field[] fields2 = object.getClass().getDeclaredFields();
						for(Field field3 : fields2){
							String dataType3 = field3.getType().getName();
							if(!StringUtils.contains(dataType3, "java.util") || isDateDataType(dataType3)){
								String property = field3.getName();
								
								/**
							     * Some Pojo Class are may be implements java.io.Serializable 
							     * so jvm will add serialVersionUID to pojo classes so this property is not be 
							     * required in query preparation so it is negligible
							     */
								if(StringUtils.equalsIgnoreCase(property,"serialVersionUID")) {
									continue;
								}
								
								field3.setAccessible(Boolean.TRUE);
								Object value = field3.get(object);
								if(value != null && !value.toString().isEmpty() && !excludeProperty(restrictions,map.getKey()+"."+property)){
									if(StringUtils.contains(dataType3, "java.lang") || isPrimitive(dataType3) || isDateDataType(dataType3)){
										if(StringUtils.equalsIgnoreCase(dataType3, "java.lang.String")){
											if(disableLike(restrictions, map.getKey()+"."+property)){
												stringBuffer.append(" and t."+map.getKey()+"."+property+" = '"+value+"'");
											}else{
												stringBuffer.append(" and t."+map.getKey()+"."+property+" like lower('%"+value+"%')");
											}
											
										}else if(isDateDataType(dataType3)){
											value = StringUtils.substringBefore(value.toString(),".");
											if(disableLike(restrictions, map.getKey()+"."+property)){
												stringBuffer.append(" and t."+map.getKey()+"."+property+" = '"+value+"'");
											}else{
												stringBuffer.append(" and t."+map.getKey()+"."+property+" like '%"+value+"%'");
											}
											
										}
										else{
											stringBuffer.append(" and t."+map.getKey()+"."+property+" = "+value+"");
										}
									}
								}
							}
							
						}
					}
					
				}
			}
			addRestrictions(restrictions,stringBuffer);
			//String s = "select costCenterCode,wirelessNumber,userName from com.sutisoft.inventory.WipMaster t where 1=1  and t.company.companyId = 7 and t.company.companyName like lower('%IBT%') and t.company.companyTitle like lower('%IBT%') and t.company.contactNumber1 like lower('%985632532%') and t.company.companyTimeZone like lower('%Etc/GMT+12%') and t.company.companyEmail like lower('%madhavaraoc%') and t.company.address1 like lower('%Sai Nagar%') and t.company.createdBy like lower('%superadmin%') and t.company.lastUpdatedBy like lower('%superadmin%') and t.company.city like lower('%Hyderabad%') and t.company.state like lower('%T.S%') and t.company.pincode like lower('%500081%') and t.company.flag1 like lower('%false%') and t.company.sutiSign = 0 and t.company.ultimateApproverId = 4 and t.company.currencyId = 1 and t.company.workFlowApprovalType like lower('%default%') and t.company.numOfUsers like lower('%10%') and t.company.isDefaultCarrier = 1 and t.company.companyId = 7 and t.company.statusFlag = hibernate.StatusFlag@1495510 and t.company.countries = hibernate.Countries@95a342 and t.company.companyName like lower('%IBT%') and t.company.companyTitle like lower('%IBT%') and t.company.contactNumber1 like lower('%985632532%') and t.company.companyTimeZone like lower('%Etc/GMT+12%') and t.company.companyEmail like lower('%madhavaraoc@ibt.example.com%') and t.company.address1 like lower('%Sai Nagar%') and t.company.createdBy like lower('%superadmin%') and t.company.lastUpdatedBy like lower('%superadmin%') and t.company.city like lower('%Hyderabad%') and t.company.state like lower('%T.S%') and t.company.pincode like lower('%500081%') and t.company.flag1 like lower('%false%') and t.company.sutiSign = 0 and t.company.ultimateApproverId = 4 and t.company.currencyId = 1 and t.company.workFlowApprovalType like lower('%default%') and t.company.numOfUsers like lower('%10%') and t.company.isDefaultCarrier = 1";
			//query = session.createQuery(stringBuffer.toString());
		}catch(RuntimeException re){
			re.printStackTrace();
			throw re;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
	
	StringBuffer addRestrictions(Map<String, Object> restrictions,StringBuffer stringBuffer){
		Object orderObject = null;
		if(restrictions.get(AppConstants.DESC_ORDER) != null){
			orderObject = restrictions.get(AppConstants.DESC_ORDER);
		}else{
			orderObject = restrictions.get(AppConstants.ASC_ORDER);
		}
		if(orderObject != null){
			stringBuffer.append(" order by t."+orderObject.toString()+" "+AppConstants.DESC_ORDER);
		}
		
		return stringBuffer;
	}
	
	boolean excludeProperty(Map<String, Object> restrictions,String propertyName){
		Object object = restrictions.get(AppConstants.EXCLUDE_PROPERTY);
		boolean excludeProperty = false;
		if(object != null){
			if(StringUtils.equalsIgnoreCase(propertyName, object.toString())){
				excludeProperty = true;
			}
		}
		return excludeProperty;
	}
	
	boolean disableLike(Map<String, Object> restrictions,String propertyName){
		Object object = restrictions.get(AppConstants.DISABLE_LIKE);
		boolean disableLike = false;
		if(object != null){
			if(StringUtils.equalsIgnoreCase(propertyName, object.toString())){
				disableLike = true;
			}
		}
		return disableLike;
	}
	
	
	boolean isPrimitive(String dataType){
		boolean isPrimitive = false;
		if(!StringUtils.isEmpty(dataType)){
			if(dataType.equalsIgnoreCase("int")){
				isPrimitive = true;
			}
			else if(dataType.equalsIgnoreCase("double")){
				isPrimitive = true;
			}
			else if(dataType.equalsIgnoreCase("float")){
				isPrimitive = true;
			}
			else if(dataType.equalsIgnoreCase("char")){
				isPrimitive = true;
			}
			else if(dataType.equalsIgnoreCase("byte")){
				isPrimitive = true;
			}
			else if(dataType.equalsIgnoreCase("boolean")){
				isPrimitive = true;
			}
			else if(dataType.equalsIgnoreCase("short")){
				isPrimitive = true;
			}
			else if(dataType.equalsIgnoreCase("long")){
				isPrimitive = true;
			}
			
		}
		return isPrimitive;
	}
	
	public boolean isDateDataType(String dataType){
		boolean isDateDataType = false;
		if(!StringUtils.isEmpty(dataType)){
			if(dataType.equalsIgnoreCase("java.util.Date")){
				isDateDataType = true;
			}
			else if(dataType.equalsIgnoreCase("java.sql.Date")){
				isDateDataType = true;
			}
			else if(dataType.equalsIgnoreCase("java.sql.Timestamp")){
				isDateDataType = true;
			}
		}
		return isDateDataType;
	}
}
