/**
 * 
 */
package com.enavigo.doleloader.persistence;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author yuvalzukerman
 *
 */
public class MockPersistor implements DoleJsonPersistor {

	/* (non-Javadoc)
	 * @see com.enavigo.doleloader.persistence.DoleJsonPersistor#persist(java.sql.Connection, java.lang.Object, char)
	 */
	@Override
	public boolean persist(Connection connection, Object objToPersist,
			char sourceSite) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
