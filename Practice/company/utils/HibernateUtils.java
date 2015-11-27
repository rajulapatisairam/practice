package com.mine.code.util.hibernate;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.mine.code.hibernate.HibernateSessionFactory;
import com.mine.code.util.LoggerUtils;

public class HibernateUtils<Entity> {
	public static final Logger LOGGER = Logger.getLogger(HibernateUtils.class);
        private Entity entity;
        private Session session;
    	private Criteria criteria;

			private HibernateUtils() {
				super();
			}
			public HibernateUtils(Entity entity) {
				if(entity == null) throw new NullPointerException();
				session = HibernateSessionFactory.getSession();
				criteria = session.createCriteria(entity.getClass());
				this.entity = entity;
			}
 
			@SuppressWarnings("unchecked")
			public List<Entity> getEntityData(Map<String,Object> criterias){
				LOGGER.info(LoggerUtils.methodStartingMessage());
				for (Map.Entry<String, Object> condition : criterias.entrySet()) {
					criteria.add(Restrictions.eq(condition.getKey(), condition.getValue()));
			}
				List<Entity> result = criteria.list(); 
				session.clear();
				session.close();
				LOGGER.info(LoggerUtils.methodEndingMessage());
				return result;
			}
        
}
