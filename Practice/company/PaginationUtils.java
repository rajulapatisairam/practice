/**
 * 
 */
package utils.pagination;

import hibernate.BaseHibernateDAO;
import hibernate.HibernateSessionFactory;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;

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

	public void getPaginationData(Map<String, Object> restrictions, int startIndex, int numberOfRecords) {
		LOGGER.info(LoggerUtils.methodStartingMessage());
		this.startIndex = startIndex;
		this.numberOfRecords = numberOfRecords;
		this.restrictions = restrictions;
		try 
		{
			prepareBasicRequirements();
			criteria.add(Example.create(entity).ignoreCase().enableLike(MatchMode.ANYWHERE).excludeZeroes());
			if(!restrictions.isEmpty()){
				for(Map.Entry<String,Object> map : restrictions.entrySet()){
					criteria.createCriteria(map.getKey())
					.add(Example.create(map.getValue()).ignoreCase().enableLike(MatchMode.ANYWHERE));
				}
			}
			
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
					LOGGER.info(LoggerUtils.methodEndingMessage());
				}
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
			caluclateTotalRecordsCount();
			criteria.setFirstResult(this.startIndex);
			criteria.setMaxResults(this.numberOfRecords);
			/*Code for showing last updated Record*/
			
			//if(table.equals("ShippingTerms")|| table.equals("Departments") || table.equals("Warehouse") || table.equals("Users") || table.equals("Supplier") || table.equals("Accessories") || table.equals("BudgetPeriod") || table.equals("BudgetLines") || table.equals("Taxcode") || table.equals("PaymentTerms")){
			if(table.equals("Role")|| table.equals("CompanyCarriers") || table.equals("GeographicLocation") || table.equals("GlCodes")){
				criteria.addOrder(Order.desc("updatedDate"));

			}else
			
			if(table.equals("CostCenter")){
				criteria.addOrder(Order.desc("modifiedOn"));
			}
			
			else{
				criteria.addOrder(Order.desc("lastUpdatedDate"));
			}
			
			
			
			results = criteria.list();
			prepareResponseData();
				} catch (RuntimeException exp) {
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
}
