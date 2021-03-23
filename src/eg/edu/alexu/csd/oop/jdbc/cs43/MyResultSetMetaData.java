package eg.edu.alexu.csd.oop.jdbc.cs43;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MyResultSetMetaData implements ResultSetMetaData {
	private String table;
	private String[] types;
	private String[] columns;

	public MyResultSetMetaData(String database, String[] types, String[] columns) {
		this.table = database;
		this.columns = columns;
		this.types = types;
	}

	@Override
	public int getColumnCount() throws SQLException {

		return columns.length;
	}

	@Override
	public String getColumnLabel(int arg0) throws SQLException {

		return getColumnName(arg0);
	}

	@Override
	public String getColumnName(int arg0) throws SQLException {

		try {
			return columns[arg0-1];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new SQLException();
		}
	}

	@Override
	public int getColumnType(int arg0) throws SQLException {
		try {
			String s = types[arg0-1];
			if (s.equals("integer")) {
				return java.sql.Types.INTEGER;
			} else if (s.equals("string")) {
				return java.sql.Types.VARCHAR;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new SQLException();
		}
		return 0;
	}

	@Override
	public String getTableName(int arg0) throws SQLException {

		return table;
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
	public String getCatalogName(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getColumnClassName(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnDisplaySize(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPrecision(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getScale(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSchemaName(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAutoIncrement(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCaseSensitive(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCurrency(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDefinitelyWritable(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int isNullable(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReadOnly(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSearchable(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSigned(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWritable(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getColumnTypeName(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

}
