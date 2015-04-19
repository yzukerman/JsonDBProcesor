package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.SQLException;

/***
 * This interface specifies the contract all classes handling data persistence must comply with
 * @author yuvalzukerman
 *
 */
public interface DoleJsonPersistor {

	public boolean persist(Connection connection, Object objToPersist, char sourceSite) throws SQLException;
	
}
