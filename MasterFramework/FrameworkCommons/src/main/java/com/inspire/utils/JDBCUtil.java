package com.inspire.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;
import com.inspire.constants.UtilConstants;


/**
 * @author sachi
 *
 */
public class JDBCUtil {
	static final String JDBC_DRIVER = UtilConstants.MYSQL_JDBC_DRIVER;
	public static Connection conn = null;
	public static Statement stmt = null;
	private String DB_URL, schemaName;
	private String userId, password;
	public static Logger log = MasterLogger.getInstance();

	public JDBCUtil(String connectionString) {
		String[] strArr = connectionString.split(":");
		String authDetails = strArr[3];
		// String connectionUrl=authDetails
		setDBConnection();

	}

	private void setDBConnection() {
		String databaseURL = DB_URL + "/" + schemaName;
		log.info("DBURL-->" + databaseURL);
		/*
		 * log.info("DBUserID-->"+userId); log.info("DBPassword--->"+ password);
		 */
		try {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			conn = DriverManager.getConnection(databaseURL, userId, password);
			log.info("Database Connection is successful");
		} catch (SQLException e) {
			log.info("Connection Failed due to " + e.getMessage());
			e.printStackTrace();
		}
	}

	public ResultSet executeSQL(String sqlStatement) {
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlStatement);
		} catch (SQLException e) {
			log.info("SQL Query Not executed due to SQL Exception" + e.getMessage());
		}
		return rs;
	}

}
