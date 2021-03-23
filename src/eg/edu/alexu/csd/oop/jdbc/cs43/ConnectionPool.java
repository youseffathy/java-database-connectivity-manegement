package eg.edu.alexu.csd.oop.jdbc.cs43;

import java.sql.Connection;

public interface ConnectionPool {
	public static ConnectionManager getInstance() {
		return null;
	}
	public Connection acquireConnection(String path);

	public void releaseConnection(String path); // if the user does not close the connection but connected to a new one

	public void CloseConnection(String path); // when the user closed the connection

	public String[] getAllUsedConnections();

	public Connection getCurrentConnection();
}
