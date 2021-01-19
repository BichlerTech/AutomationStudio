package opc.client.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnector {
	private Connection dbConnection = null;
	private String drv;
	private String connection;
	private String user;
	private String password;

	public DBConnector(String databaseDriver, String databaseConnection, String user, String password) {
		this.drv = databaseDriver;
		this.connection = databaseConnection;
		this.user = user;
		this.password = password;
	}

	public void connect() {
		try {
			this.dbConnection = DriverManager.getConnection(toDbString());
			this.dbConnection.setAutoCommit(false);
		} catch (SQLException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
	}

	public void initializeDBDriver() {
		try {
			Class.forName(this.drv);
		} catch (ClassNotFoundException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
	}

	public String getDrv() {
		return drv;
	}

	public String getConnection() {
		return connection;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public Connection getDBConnection() {
		return this.dbConnection;
	}

	public String toDbString() {
		return "jdbc:" + this.connection + "?user=" + this.user + "&password=" + this.password;
	}
}
