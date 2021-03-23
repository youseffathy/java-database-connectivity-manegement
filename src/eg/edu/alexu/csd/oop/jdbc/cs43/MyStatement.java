package eg.edu.alexu.csd.oop.jdbc.cs43;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.sound.midi.MidiDevice.Info;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

public class MyStatement implements Statement {
	// columns are one based
	private TimeLimitedCodeBlock block;
	private List<String> batches;
	private Connection connection;
	private SingleDatabaseEngine engine;
	private int timeout = 2;
	private String path;
	private Object object;
	private Object objectInt;
	private Object objectBoolean;
	private Object objectArray;
	private boolean sqlEx = false;

	public MyStatement(Connection connection, String path) {
		engine = new SingleDatabaseEngine(path);
		this.connection = connection;
		batches = new LinkedList<>(); // list of sql commands to be executed
		this.path = path;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		if (connection == null) {
			MyLogger.getLogger().log(Level.SEVERE, "connection is closed , query cannnot be executed");
			throw new SQLException();
		}
		MyLogger.getLogger().log(Level.INFO, "a sql query is added to the batch");
		batches.add(sql);
	}

	@Override
	public void clearBatch() throws SQLException {
		if (connection == null) {
			MyLogger.getLogger().log(Level.SEVERE, "connection is closed , query cannnot be executed");
			throw new SQLException();
		}
		MyLogger.getLogger().log(Level.INFO, "sql batch is cleared");
		batches.clear();
	}

	@Override
	public void close() throws SQLException {
		MyLogger.getLogger().log(Level.INFO, "a statment is closed");
		connection = null;
		engine.closeEngine();
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		if (connection == null) {
			MyLogger.getLogger().log(Level.SEVERE, "connection is closed , query cannnot be executed");
			throw new SQLException();
		}

		TimeLimitedCodeBlock.runWithTimeout(new Runnable() {
			@Override
			public void run() {

				try {
					object = engine.execute(sql);
				} catch (SQLException e) {
					sqlEx = true;
				}

			}
		}, timeout, TimeUnit.SECONDS);
		if (sqlEx) {
			sqlEx = false;
			throw new SQLException();
		}
		if (object instanceof Boolean) {
			MyLogger.getLogger().log(Level.INFO, "a structure query is executed");
			return (Boolean) object;
		} else if (object instanceof Integer) {
			MyLogger.getLogger().log(Level.INFO, "an update query is executed");
			if ((Integer) object > 0) {
				return true;
			} else {
				return false;
			}
		} else if (object instanceof Object[][]) {
			MyLogger.getLogger().log(Level.INFO, "a select query is executed");
			if (object == null || ((Object[][]) object).length > 0) {
				return true;
			}
			return false;
		}

		return false;

	}

	@Override
	public int[] executeBatch() throws SQLException {
		if (connection == null) {
			MyLogger.getLogger().log(Level.SEVERE, "connection is closed , query cannnot be executed");
			throw new SQLException();
		}

		int[] RowsAffected = new int[batches.size()];
		if (batches.isEmpty()) {
			MyLogger.getLogger().log(Level.WARNING, "batch is empty");
		} else {
			MyLogger.getLogger().log(Level.INFO, "batch of queries will be executed");
		}

		for (int i = 0; i < RowsAffected.length; i++) {
			String sql = batches.get(i);
			try {
				TimeLimitedCodeBlock.runWithTimeout(new Runnable() {
					@Override
					public void run() {

						try {
							object = engine.execute(sql);
						} catch (SQLException e) {
							sqlEx = true;
						}

					}
				}, timeout, TimeUnit.SECONDS);
				if (sqlEx) {
					sqlEx = false;
					throw new SQLException();
				}
				MyLogger.getLogger().log(Level.INFO, "a query from the batch is executed successfully");
				if (object instanceof Integer) {
					RowsAffected[i] = Integer.valueOf(String.valueOf(object));
				} else {

					RowsAffected[i] = SUCCESS_NO_INFO;
				}
			} catch (Exception e) {
				MyLogger.getLogger().log(Level.SEVERE, "query failure");
				RowsAffected[i] = EXECUTE_FAILED;
			}

		}
		return RowsAffected;
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		if (connection == null) {
			MyLogger.getLogger().log(Level.SEVERE, "connection is closed , query cannnot be executed");
			throw new SQLException();
		}

		TimeLimitedCodeBlock.runWithTimeout(new Runnable() {
			@Override
			public void run() {

				try {
					objectArray = engine.executeQuery(sql);
				} catch (SQLException e) {
					sqlEx = true;
				}

			}
		}, timeout, TimeUnit.SECONDS);
		if (sqlEx) {
			sqlEx = false;
			throw new SQLException();
		}
		Object[][] result = (Object[][]) objectArray;
		Map<String, Object> map = engine.getCurrentTableMetaData();
		String[] columns = (String[]) map.get("columns");
		String[] types = (String[]) map.get("types");
		ResultSetMetaData data = new MyResultSetMetaData((String) map.get("tablename"), types, columns);
		MyLogger.getLogger().log(Level.INFO, "a select query is executed");
		ResultSet resultSet = new MyResultset(data, result, columns, this);
		return resultSet;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		if (connection == null) {
			MyLogger.getLogger().log(Level.SEVERE, "connection is closed , query cannnot be executed");
			throw new SQLException();
		}

		TimeLimitedCodeBlock.runWithTimeout(new Runnable() {
			@Override
			public void run() {

				try {
					objectInt = engine.executeUpdateQuery(sql);
					MyLogger.getLogger().log(Level.INFO, "an update query is executed");
				} catch (SQLException e) {
					sqlEx = true;
				}

			}
		}, timeout, TimeUnit.SECONDS);
		if (sqlEx) {
			sqlEx = false;
			throw new SQLException();
		}
		return (int) objectInt;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (connection == null) {
			MyLogger.getLogger().log(Level.SEVERE, "connection is closed , query cannnot be executed");
			throw new SQLException();
		}

		return connection;
	}

	public int getQueryTimeout() throws SQLException {
		if (connection == null) {
			MyLogger.getLogger().log(Level.SEVERE, "connection is closed , query cannnot be executed");
			throw new SQLException();
		}

		return timeout;
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		if (connection == null) {
			MyLogger.getLogger().log(Level.SEVERE, "connection is closed , query cannnot be executed");
			throw new SQLException();
		}
		MyLogger.getLogger().log(Level.INFO, "query timeout is set");
		timeout = seconds;

	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void cancel() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchDirection() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxRows() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetType() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getUpdateCount() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isClosed() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPoolable() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		throw new UnsupportedOperationException();
	}

}
