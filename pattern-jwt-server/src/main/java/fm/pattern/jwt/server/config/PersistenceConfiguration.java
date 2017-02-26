package fm.pattern.jwt.server.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
@EnableTransactionManagement
public class PersistenceConfiguration {

	private static final String MODEL_PACKAGE = "fm.pattern.jwt";

	@Autowired
	private DatabaseProperties databaseProperties;

	@Bean(name = "dataSource")
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(databaseProperties.getDriver());
		dataSource.setUrl(databaseProperties.getUrl() + "/" + databaseProperties.getSchema() + "?useSSL=false&requireSSL=false");
		dataSource.setUsername(databaseProperties.getUsername());
		dataSource.setPassword(databaseProperties.getPassword());
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setTestOnBorrow(true);
		dataSource.setTestOnReturn(true);
		return dataSource;
	}

	@Bean(name = "flyway")
	public Flyway flyway() {
		Flyway flyway = new Flyway();
		flyway.setSchemas(databaseProperties.getSchema());
		flyway.setDataSource(databaseProperties.getUrl() + "?useSSL=false&requireSSL=false", databaseProperties.getUsername(), databaseProperties.getPassword());
		flyway.setLocations(databaseProperties.getMigrations().split(","));
		if (databaseProperties.shouldMigrate()) {
			flyway.migrate();
		}
		return flyway;
	}

	@DependsOn("flyway")
	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { MODEL_PACKAGE });
		sessionFactory.setAnnotatedPackages(new String[] { MODEL_PACKAGE });
		sessionFactory.setHibernateProperties(getHibernateProperties());
		return sessionFactory;
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionFactory().getObject());
	}

	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", databaseProperties.getDialect());
		properties.setProperty("javax.persistence.validation.mode", "none");
		return properties;
	}

}
