import hibernate.HibernateSessionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import org.hibernate.Session;
import org.hibernate.connection.C3P0ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
/**
 * 
 * @author Sairam Rajulapati
 *
 */
public class DataBase {
	public Connection getJdbcConnection() throws SQLException {
		Connection jdbcConn = null;
		SessionFactoryImplementor sessionFactoryImplementor = null;
		Session session = HibernateSessionFactory.getSession();
		sessionFactoryImplementor = (SessionFactoryImplementor) session.getSessionFactory();
		sessionFactoryImplementor = (SessionFactoryImplementor) HibernateSessionFactory.getSessionFactory();
		C3P0ConnectionProvider  connectionProvider = (C3P0ConnectionProvider) sessionFactoryImplementor.getConnectionProvider();
	    jdbcConn = connectionProvider.getConnection();
	    jdbcConn.setAutoCommit(false);
	   return jdbcConn;
	}
}
