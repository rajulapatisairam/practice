/**
 * 
 */
package utils.pagination;

import hibernate.BaseHibernateDAO;
import hibernate.HibernateSessionFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
import org.hibernate.criterion.Expression;
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
			caluclateTotalRecordsCount();
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

	private void caluclateTotalRecordsCount() {
		ScrollableResults results = criteria.scroll();
		results.last();
		this.totalRecordsCount = results.getRowNumber() + 1;
		results.close();
	    int numberOfPages = (totalRecordsCount / numberOfRecords);
		if (numberOfPages % numberOfRecords == 0) numberOfPages--;
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
				
				if("PO Created".equals(requisitionStatus)){
					criteria.add(Restrictions.eq("requisitionStatus","PO Created"));
				}
			}
			
			if(table.equals("Address")){
				criteria.add(Restrictions.eq("addressType","BillTo"));
			}
			if (table.equalsIgnoreCase("pendingRequisitions")){
				List<String> status = new ArrayList<String>();
				status.add("Draft");
				status.add("Approval Process");
				status.add("Approved");
				status.add("PO Created");
				status.add("Rejected");								
				criteria.add(Restrictions.in("requisitionStatus", status));
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
				criteria.add(Restrictions.eq("status", "Approval Process"));
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
			caluclateTotalRecordsCount();
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
		//criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	}

	public void prepareBasicRequirements() {
		this.currentPage = this.startIndex;
		displayPageNumber = startIndex+1;
		paginationIndex = startIndex;
		this.startIndex *= numberOfRecords;
		prepareCriteria();
	}
	
	public void getPaginationData(Map<String, Object> restrictions, int startIndex, int numberOfRecords) {
		LOGGER.info(LoggerUtils.methodStartingMessage());
		this.startIndex = startIndex;
		this.numberOfRecords = numberOfRecords;
		this.restrictions = restrictions;
		//String separator = ".";
		try{
			
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
		Query query = getQuery(fields,stringBuffer,cls,restrictions);
		caluclateTotalRecordsCount(query);
		query.setFirstResult(this.startIndex);
		query.setMaxResults(this.numberOfRecords);
		results = query.list();
	}
	private void caluclateTotalRecordsCount(Query query) {
		ScrollableResults results = query.scroll();
		results.last();
		this.totalRecordsCount = results.getRowNumber() + 1;
		results.close();
	    int numberOfPages = (totalRecordsCount / numberOfRecords);
		if (numberOfPages % numberOfRecords == 0) numberOfPages--;
		totalPages = numberOfPages +1;
		if (startIndex < 0) {
			startIndex =  numberOfPages * numberOfRecords;
			currentPage = totalPages;
			displayPageNumber = totalPages;
			paginationIndex = totalPages - PAGINATION_LIMIT;
		}
	}
	
	
	public Query getQuery(Field[] fields,StringBuffer stringBuffer,Class<?> cls,Map<String, Object> restrictions){
		Query query = null;
		this.restrictions = restrictions;
		stringBuffer.append(" from "+cls.getName()+" t where 1=1 ");
		try{
			for(Field field : fields){
				field.setAccessible(true);
				String propetyName = field.getName();
				Object value = field.get(entity);
				String dataType = field.getType().getName();
				if((!StringUtils.contains(dataType, "java.lang.Object")) && (StringUtils.contains(dataType, "java.lang") || isPrimitive(dataType) || isDateDataType(dataType))){
					if(value != null && !value.toString().isEmpty()){
						if(StringUtils.equalsIgnoreCase(dataType, "java.lang.String")){
							stringBuffer.append(" and t."+propetyName+" like lower('%"+value+"%')");// '"+value+"%'");
						}else{
							stringBuffer.append(" and t."+propetyName+" = "+value+"");
						}
					}
						
				}else{
					if(value != null && !value.toString().isEmpty()){
						Field[] fields2 = value.getClass().getDeclaredFields();
						for(Field field2 : fields2){
							field2.setAccessible(Boolean.TRUE);
							Object associatedPropertyValue = field2.get(value);
							String property = field2.getName();
							String dataType2 = field2.getType().getName();
							if((!StringUtils.contains(dataType2, "java.lang.Object")) && (StringUtils.contains(dataType2, "java.lang") || isPrimitive(dataType2) || isDateDataType(dataType2))){
								if(associatedPropertyValue != null && !associatedPropertyValue.toString().isEmpty()){
									if(StringUtils.equalsIgnoreCase(dataType2, "java.lang.String") || isDateDataType(dataType2)){
										stringBuffer.append(" and t."+propetyName+"."+property+" like lower('%"+field2.get(value)+"%')");//'%"+field2.get(value)+"%'");
									}else{
										stringBuffer.append(" and t."+propetyName+"."+property+" = "+field2.get(value)+"");
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
								field3.setAccessible(Boolean.TRUE);
								Object value = field3.get(object);
								if(value != null && !value.toString().isEmpty()){
									if(StringUtils.contains(dataType3, "java.lang") || isPrimitive(dataType3) || isDateDataType(dataType3)){
										if(StringUtils.equalsIgnoreCase(dataType3, "java.lang.String") || isDateDataType(dataType3)){
											stringBuffer.append(" and t."+map.getKey()+"."+property+" like lower('%"+value+"%')");
										}else{
											stringBuffer.append(" and t."+map.getKey()+"."+property+" = "+value+"");
										}
									}
								}
							}
							
						}
					}
					
				}
			}
			session = getSession();
			//String s = "select costCenterCode,wirelessNumber,userName from com.sutisoft.inventory.WipMaster t where 1=1  and t.company.companyId = 7 and t.company.companyName like lower('%IBT%') and t.company.companyTitle like lower('%IBT%') and t.company.contactNumber1 like lower('%985632532%') and t.company.companyTimeZone like lower('%Etc/GMT+12%') and t.company.companyEmail like lower('%madhavaraoc%') and t.company.address1 like lower('%Sai Nagar%') and t.company.createdBy like lower('%superadmin%') and t.company.lastUpdatedBy like lower('%superadmin%') and t.company.city like lower('%Hyderabad%') and t.company.state like lower('%T.S%') and t.company.pincode like lower('%500081%') and t.company.flag1 like lower('%false%') and t.company.sutiSign = 0 and t.company.ultimateApproverId = 4 and t.company.currencyId = 1 and t.company.workFlowApprovalType like lower('%default%') and t.company.numOfUsers like lower('%10%') and t.company.isDefaultCarrier = 1 and t.company.companyId = 7 and t.company.statusFlag = hibernate.StatusFlag@1495510 and t.company.countries = hibernate.Countries@95a342 and t.company.companyName like lower('%IBT%') and t.company.companyTitle like lower('%IBT%') and t.company.contactNumber1 like lower('%985632532%') and t.company.companyTimeZone like lower('%Etc/GMT+12%') and t.company.companyEmail like lower('%madhavaraoc@ibt.example.com%') and t.company.address1 like lower('%Sai Nagar%') and t.company.createdBy like lower('%superadmin%') and t.company.lastUpdatedBy like lower('%superadmin%') and t.company.city like lower('%Hyderabad%') and t.company.state like lower('%T.S%') and t.company.pincode like lower('%500081%') and t.company.flag1 like lower('%false%') and t.company.sutiSign = 0 and t.company.ultimateApproverId = 4 and t.company.currencyId = 1 and t.company.workFlowApprovalType like lower('%default%') and t.company.numOfUsers like lower('%10%') and t.company.isDefaultCarrier = 1";
			query = session.createQuery(stringBuffer.toString());
			
		}catch(RuntimeException re){
			re.printStackTrace();
			throw re;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return query;
		
	}
	
	boolean isPrimitive(String dataType){
		boolean isPrimitive = false;
		if(StringUtils.isEmpty(dataType)){
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
		if(StringUtils.isEmpty(dataType)){
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
