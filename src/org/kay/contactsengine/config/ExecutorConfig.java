package org.kay.contactsengine.config;

import javax.sql.DataSource;

import org.kay.contactsengine.actions.AddContactAction;
import org.kay.contactsengine.actions.Executor;
import org.kay.contactsengine.actions.ExportContactsAction;
import org.kay.contactsengine.actions.ImportContactsAction;
import org.kay.contactsengine.actions.SearchContactAction;
import org.kay.contactsengine.actions.ViewContactsAction;
import org.kay.contactsengine.aspects.MethodMetrics;
import org.kay.contactsengine.engine.ContactsEngine;
import org.kay.contactsengine.formatters.ContactCSVFormatter;
import org.kay.contactsengine.formatters.ContactFormatter;
import org.kay.contactsengine.store.ContactsDao;
import org.kay.contactsengine.store.sql.SQLTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@EnableAspectJAutoProxy
public class ExecutorConfig {
	
	@Bean(name="SEARCH")
	public Executor searchAction() {
		return new SearchContactAction();
	}
	
	@Bean(name="ADD")
	public Executor add() {
		return new AddContactAction();
	}
	
	@Bean(name="VIEW")
	public Executor view() {
		return new ViewContactsAction();
	}
	
	@Bean(name="EXPORT")
	public Executor export() {
		return new ExportContactsAction();
	}
	
	@Bean(name="IMPORT")
	public Executor importExecutor() {
		return new ImportContactsAction();
	}
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		//TODO read data source values from config json/ini
		String className = "org.sqlite.JDBC";
		String dbFilename = System.getProperty("user.dir") + "/contactsengine.db";
		
		String url = "jdbc:sqlite:" + dbFilename;
		String username = "";
		String password =  "";
		ds.setDriverClassName(className);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}
	
	@Bean
	public ContactsDao contactsDao() {
		return new SQLTemplate();
	}
	
	@Bean 
	public ContactsEngine contactsEngine() {
		ContactsEngine engine = new ContactsEngine();
		engine.Init();
		return engine;
	}
	
	@Bean
	public ContactFormatter getFormatter() {
		return new ContactCSVFormatter();
	}

	@Bean 
	public MethodMetrics metricsAcpect() {
		return new MethodMetrics();
	}
}
