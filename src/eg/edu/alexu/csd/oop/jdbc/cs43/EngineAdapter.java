package eg.edu.alexu.csd.oop.jdbc.cs43;

import java.sql.SQLException;
import java.util.Map;

public interface EngineAdapter {
	public boolean CreateDatabase(String databaseName);

	public boolean executeStructureQuery(String sql) throws SQLException;

	public Object[][] executeQuery(String sql) throws SQLException;

	public int executeUpdateQuery(String sql) throws SQLException;

	public void closeEngine();

	public Map<String, Object> getCurrentTableMetaData();

	public Object execute(String sql) throws SQLException;

}
