package com.chtr.tmoauto.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This clas is used to perform database related functionalities like create connection,insert,delete,update operations.etc
 * @since: 11/06/2016
 * @author: Sangram Pisal
 */

public class OracleDb
{

	/**
	 * This function is used to connect to Oracle Database and returns
	 * connection object.
	 * 
	 * @param: dbConnectionString
	 * @param: userName
	 * @param: password
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */

	public Connection fw_DBOracle_MakeConnection(String dbConnectionString, String userName, String password)
			throws ClassNotFoundException, SQLException
	{
		Connection connection = null;
		System.out.println("Oracle JDBC Connection Testing ");
		Class.forName("oracle.jdbc.driver.OracleDriver");
		System.out.println("Oracle JDBC Driver Registered! ");
		connection = DriverManager.getConnection(dbConnectionString, userName, password);
		System.out.println("Connected");
		return connection;

	}

	/**
	 * This function is used to fetch the data from Database.
	 * 
	 * @param: connection
	 * @param: query
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */

	public ResultSet fw_DBOracle_GetRecordSet(Connection connection, String query)
	{
		ResultSet rs = null;
		System.out.println("[OUTPUT FROM SELECT]");
		try
		{
			Statement st = connection.createStatement();
			rs = st.executeQuery(query);

		} catch (SQLException ex)
		{
			System.err.println(ex.getMessage());
		}
		return rs;
	}

	/**
	 * This function is used to insert the data to Database.
	 * 
	 * @param: connection
	 * @param: query
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */

	public void fw_DBOracle_AddRecordSet(Connection connection, String query)
	{
		System.out.print("\n[Performing INSERT] ... ");
		try
		{
			Statement st = connection.createStatement();
			st.executeUpdate(query);
			System.out.println("data Inserted Successfully");
		} catch (SQLException ex)
		{
			System.err.println(ex.getMessage());
		}

	}

	/**
	 * This function is used to update the data in Database.
	 * 
	 * @param: connection
	 * @param: query
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */
	public void fw_DBOracle_UpdateRecordSet(Connection connection, String query)
	{

		System.out.print("\n[Performing UPDATE] ... ");
		try
		{
			Statement st = connection.createStatement();
			st.executeUpdate(query);
			System.out.println("data updated Successfully");
		} catch (SQLException ex)
		{
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * This function is used to delete the data in Database.
	 * 
	 * @param: connection
	 * @param: query
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */

	public void fw_DBOracle_DeleteRecordSet(Connection connection, String query)
	{
		System.out.println("\n[Performing DELETE] ... ");
		try
		{
			Statement st = connection.createStatement();
			st.executeUpdate("DELETE FROM  WHERE '");
		} catch (SQLException ex)
		{
			System.err.println(ex.getMessage());

		}

	}

	/**
	 * This function is used to close the database connection.
	 * 
	 * @param: connection
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */

	public void fw_DBOracle_Closeconnection(Connection connection)
	{
		try
		{
			connection.close();
			if (connection.isClosed())
				System.out.println("Connection closed.");

		} catch (SQLException ex)
		{
			// TODO Auto-generated catch block
			ex.getMessage();
		}

	}

}
