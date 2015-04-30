/**
 * 
 */
package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A collection of shared methods for persistence
 * @author yuvalzukerman
 *
 */
public class DoleJsonPersistenceUtils {

	/***
	 * Returns the highest numbered ID in the products table
	 * @param connection
	 * @param tableName - the table we want to get the maximum ID for
	 * @param idColumn - the column name containing the table's primary key
	 * @return the highest id used in the product table
	 * @throws SQLException
	 */
	public static int getMaxId(Connection connection, String tableName, String idColumn) throws SQLException
	{
		String queryString = "SELECT MAX(" + idColumn + ") AS max_id FROM " + tableName;
		PreparedStatement query = connection.prepareStatement(queryString);
		ResultSet rs = query.executeQuery();

		int maxProductId = 0;
		while(rs.next())
		{
			maxProductId = rs.getInt("max_id");
		}
		
		query.close();
		
		return maxProductId;
	}
}
