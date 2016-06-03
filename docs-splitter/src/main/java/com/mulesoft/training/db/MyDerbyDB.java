package com.mulesoft.training.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.stereotype.Controller;

@Controller
public class MyDerbyDB implements InitializingBean {

	private static Logger logger = LogManager.getLogger("com.mulesoft.training.americanDerbyDB.Logger");


	//This value must be available in a property placeholder or env variable
	@Value("${jdbc.derby.server.url}")
	private String derbyJdbcUrl;


	public String getDerbyJdbcUrl() { 
		return derbyJdbcUrl; 
	}

	public void afterPropertiesSet() throws Exception {

		

		//String dbURL = "jdbc:derby:memory:training;create=true";
		String dbURL=getDerbyJdbcUrl();
		
		logger.info("\n\n\n\n^^^^^^^JDBC Derby URL (not used currently): "+dbURL);
		Connection conn = null;

		try {

			// For in-memory JVM db
			// This only uses derby.jar
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			
			// Network connection
			// This requires derbyclient.jar or derbynet.jar
			//Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			conn = DriverManager.getConnection(dbURL);
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			logger.debug("&&&& - DB Init - &&&&");
			int i=0;

			while (rs.next()){
				logger.debug("\n\n^^^^^^^there is next " + rs.getString(3));
				if(rs.getString(3).equalsIgnoreCase("mystock")) i=1; //set a marker that this table already exists
			}

			logger.info("&&&& - DB Init - &&&&");
			Statement stmt = conn.createStatement();

			if(i!=1){

				//mystock("name", "date", "bookvalue") VALUES(#[xpath3('//Name').text], #[xpath3('//LastTradeDate').text], #[xpath3('//BookValue').text]);
				
				
				
				
				String createDerbyDBTable = "CREATE TABLE mystock (name varchar(80) NOT NULL,"
						+"date varchar(20), "
						+"bookvalue varchar(120) )";
				
				stmt.execute(createDerbyDBTable);

				logger.info("\n^^^^^^mystock Derby table created");
				//stmt.executeUpdate("INSERT INTO FLIGHTS(PRICE, DESTINATION, ORIGIN) VALUES (555, 'SFO','YYZ')");
				//stmt.executeUpdate("INSERT INTO FLIGHTS(PRICE, DESTINATION, ORIGIN) VALUES (450, 'LAX','YYZ')");
				//stmt.executeUpdate("INSERT INTO FLIGHTS(PRICE, DESTINATION, ORIGIN) VALUES (777, 'SEA','SQL')");
				//stmt.executeUpdate("INSERT INTO FLIGHTS(PRICE, DESTINATION, ORIGIN) VALUES (999, 'SFO','SQL')");


				String insertString = "INSERT INTO mystock ("
						+ "name, date, bookvalue"
						+ ") VALUES"
						+ "('IBM','2016-05-31','50.00')";
				
				stmt.executeUpdate(insertString);
				
				
				stmt.executeUpdate("INSERT INTO mystock (name, date, bookvalue) VALUES ('APPL', '2016-05-05','502.23')");
				stmt.executeUpdate("INSERT INTO mystock (name, date, bookvalue) VALUES ('GOOG', '2016-05-05','650.55')");


				logger.info("\n^^^^^^^^^^^^^mystock table populated with stocks.");

				ResultSet results = stmt.executeQuery("SELECT * FROM mystock");
				logger.info("\n^^^^^^^^^^^^^SELECT completed: "+results);

				while (results.next()){
					logger.debug("\n^^^^^^^^^^^^^"+results.getString("name"));

				}

			}

		}

		catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		}

	}

}

