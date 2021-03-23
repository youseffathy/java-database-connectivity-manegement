package eg.edu.alexu.csd.oop.jdbc.cs43;

import java.util.*;
import java.io.File;
import java.sql.Connection;
import java.sql.Driver;

import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JDBCDriver implements Driver {
	private ConnectionManager connectionManager;
	public JDBCDriver() {
		
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		if (url.equals("jdbc:xmldb://localhost")) {
			MyLogger.getLogger().log(Level.INFO,"url is accepted , jdbc driver is active now");
			return true;
		}
		return false;
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		if(url == null) {
			throw new SQLException();
		}
		if (acceptsURL(url)) {
		
			File dir = (File) info.get("path");
			String path = dir.getAbsolutePath();
			MyLogger.getLogger().log(Level.INFO, "driver is connected to the path" + path);
			 connectionManager = ConnectionManager.getInstance();
			return connectionManager.acquireConnection(path);
		}
		throw new SQLException();
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		if (acceptsURL(url)) {
			DriverPropertyInfo[] infos = new DriverPropertyInfo[info.size() + 1];
			DriverPropertyInfo info2 = new DriverPropertyInfo("url", url);
			infos[0] = info2;
			int i = 1;
			for (Entry<Object,Object> e:info.entrySet()) {
				infos[i] = new DriverPropertyInfo(String.valueOf(e.getKey()),String.valueOf(e.getValue()));
				i++;
			}

			return infos;
		}

		return null;
	}

	@Override
	public int getMajorVersion() {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getMinorVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean jdbcCompliant() {
		throw new UnsupportedOperationException();
	}

}
