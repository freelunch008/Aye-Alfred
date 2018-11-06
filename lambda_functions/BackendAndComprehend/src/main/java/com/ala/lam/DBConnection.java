package com.ala.lam;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://calprodev.cai7cl6csqyt.us-east-2.rds.amazonaws.com:3306/calprodev";
	   
	   //  Database credentials
	   static final String USER = "calprodev";
	   static final String PASS = "calprodev";
	
	public static Connection getConnection() throws Exception
	{
	      Class.forName("com.mysql.jdbc.Driver");

	      //STEP 3: Open a connection
	      System.out.println("Connecting to database...");
	      Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
	      return conn;
	}
}
