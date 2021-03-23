package eg.edu.alexu.csd.oop.jdbc.cs43;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ConnectionManager implements ConnectionPool {
	private static ConnectionManager connectionManager;
	private static Map<String, Connection> runningConnections;
	private static Map<String, Long> ConnectionTimeStamp;
	private static Map<String, Connection> unClosedConnections;
	private static Connection CurrentConnection;

	private ConnectionManager() {
		runningConnections = new HashMap<>();
		unClosedConnections = new HashMap<>();
		ConnectionTimeStamp = new HashMap<>();

	}

	public static ConnectionManager getInstance() {
		if (connectionManager == null) {
			connectionManager = new ConnectionManager();
		}
		return connectionManager;
	}

	@Override
	public Connection acquireConnection(String path) {
		CurrentConnection = null;

		for (Entry<String, Connection> e : unClosedConnections.entrySet()) {
			if (System.currentTimeMillis() - ConnectionTimeStamp.get(e.getKey()) > 60000) { // expired
				if (e.getKey().equalsIgnoreCase(path)) { // revive it after expiration
					revive(e.getKey());
				} else {
					unClosedConnections.remove(e.getKey()); // collected by the GC
				}
			} else { // not expired
				if (e.getKey().equalsIgnoreCase(path)) { // revive it before expiration
					revive(e.getKey());
				}
			}
		}

		for (Entry<String, Connection> e : runningConnections.entrySet()) {
			if (e.getKey().equalsIgnoreCase(path)) {
				CurrentConnection = runningConnections.get(e.getKey());
				ConnectionTimeStamp.put(path, System.currentTimeMillis());
				break;
			}
		}

		if (CurrentConnection == null) {

			CurrentConnection = new MyConnection(path, "jdbc:xmldb://localhost");
			runningConnections.put(path, CurrentConnection);
			ConnectionTimeStamp.put(path, System.currentTimeMillis());

		}

		return CurrentConnection;
	}

	private void revive(String path) {
		CurrentConnection = unClosedConnections.remove(path); // remove it from the unused
		runningConnections.put(path, CurrentConnection); // add it to the running.
		ConnectionTimeStamp.put(path, System.currentTimeMillis());
	}

	@Override
	public void releaseConnection(String path) {
		ConnectionTimeStamp.put(path, System.currentTimeMillis());
		unClosedConnections.put(path, runningConnections.remove(path));
	}

	@Override
	public void CloseConnection(String path) {
		if (runningConnections.get(path) != null) {
			runningConnections.remove(path);
		} else if (unClosedConnections.get(path) != null) {
			unClosedConnections.remove(path);
		}

	}

	@Override
	public String[] getAllUsedConnections() {
		String[] paths = new String[runningConnections.size()];
		int i = 0;
		for (Entry<String, Connection> e : runningConnections.entrySet()) {
			paths[i] = e.getKey();
			i++;
		}
		return paths;
	}

	@Override
	public Connection getCurrentConnection() {
		return CurrentConnection;

	}
}
