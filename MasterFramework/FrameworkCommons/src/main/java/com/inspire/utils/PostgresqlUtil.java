package com.inspire.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
public class PostgresqlUtil {
	String dbConnString = "";
	Connection connection = null;
	static int liveDBConnections=0;
	public static Logger log = MasterLogger.getInstance();
	static final String JDBC_DRIVER = UtilConstants.POSTGRESQL_JDBC_DRIVER;

	public PostgresqlUtil(String dbConnString, String user, String pwd) {
		this.dbConnString = dbConnString;

		try {
			Class.forName(JDBC_DRIVER);
			log.info(dbConnString +"/"+ user +":"+ pwd);
			connection = DriverManager.getConnection(dbConnString, user, pwd);
			liveDBConnections++;
			log.info("db connection successful, total live DB Connections : "+liveDBConnections);
		} catch (SQLException | ClassNotFoundException e) {
			log.info("connection to db has been failed " + e.getMessage());
		}
	}
	
	public synchronized Object getExplainPlan(String query) {
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("explain analyze "+query);
			while (rs.next())
			{
			   return rs.getObject(1);
			}
		} catch (SQLException e) {
			log.info(e.getMessage());
		}
		return null;
		
	}

	public synchronized ResultSet executeQuery(String query) {
		log.info(query);
		Statement st;
		ResultSet rs = null;
		try {
			st = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			log.info("postgresql Connection failed " + e.getMessage());
		}

		return rs;
	}

	public synchronized String executeSelectQuery(String query) {
		log.info(query);
		Statement st;
		ResultSet rs = null;
		String ret = "";
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			log.info("postgresql Connection failed " + e.getMessage());
		}

		try {
			if (rs.next()) {
				ret = rs.getString(1);
			}
		} catch (SQLException e) {
			log.info("resultset does not fetch the column " + e.getMessage());
		}

		return ret;
	}

	public synchronized String executeSelectQuery(String query, String columnName) {
		Statement st;
		ResultSet rs = null;
		log.info(query);
		log.info(columnName);
		String ret = "";
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			log.info("postgresql Connection failed" + e.getMessage());
		}

		try {
			if (rs.next()) {
				ret = rs.getString(columnName);
			}
		} catch (SQLException e) {
			log.info("resultset does not fetch the column " + columnName + " " + e.getMessage());
		}

		return ret;
	}

	public void executeUpdateQuery(String updateQuery) {
		log.info(updateQuery);
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(updateQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeDBConnection() {
		try {
			if (connection != null) {
				connection.close();
				liveDBConnections--;
				log.info("DB Connection destroyed, total live DB connections : "+liveDBConnections);
			}
		} catch (SQLException e) {
			log.info("issue while closing the db connections " + e.getMessage());
		}
	}

}
