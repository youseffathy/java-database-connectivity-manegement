package eg.edu.alexu.csd.oop.jdbc.cs43;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;

import eg.edu.alexu.csd.oop.db.Database;

import eg.edu.alexu.csd.oop.db.cs43.concreteclass.MyDatabase;

// decorator
public class SingleDatabaseEngine implements EngineAdapter{
	
	private Database database;
	private boolean select = false;


	public SingleDatabaseEngine(String path) {
		
		database = MyDatabase.getInstance();
		MyDatabase.setParentFolder(path + System.getProperty("file.separator"));
		
	}

	public boolean CreateDatabase(String databaseName) {
		
		File file = new File(database.createDatabase(databaseName, false));
		return file.exists();
	
	}

	public boolean executeStructureQuery(String sql) throws SQLException {
		return database.executeStructureQuery(sql);
	}

	public Object[][] executeQuery(String sql) throws SQLException {
		select = true;
		return database.executeQuery(sql);
	}

	public int executeUpdateQuery(String sql) throws SQLException {
		return database.executeUpdateQuery(sql);
	}

	public void closeEngine() {
		MyDatabase.closeEngine();
		
	}

	// returns the current meta data of the table in case of select query.
	public Map<String, Object> getCurrentTableMetaData() {
		if (select) {
			Map<String, Object> map = MyDatabase.getCurrentTableMetaData();
			if (map != null) {	
				return map;
			}
		}
		return null;
	}
	public Object execute(String sql) throws SQLException {
		String pattern = "(\\s+)";
		Pattern pat = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		String[] strs = pat.split(sql.trim()); // removed trailing and leading spaces

		if (strs[0].equalsIgnoreCase("create") || strs[0].equalsIgnoreCase("drop")) {
			if (strs[0].equalsIgnoreCase("create") && strs[1].equalsIgnoreCase("database")) {
				if (strs.length == 3) {
					return CreateDatabase(strs[2]);
				}
			} else if (strs[0].equalsIgnoreCase("drop") && strs[1].equalsIgnoreCase("database")) {

			//	CreateDatabase( path+ System.getProperty("file.separator")+strs[2]);			
				return Boolean.valueOf(executeStructureQuery(sql));

			} else if (strs[1].equalsIgnoreCase("table")) {

				return Boolean.valueOf(executeStructureQuery(sql));

			}

		} else if (strs[0].equalsIgnoreCase("delete") || strs[0].equalsIgnoreCase("insert")
				|| (strs[0].equalsIgnoreCase("update"))) {
			int UpdateCount =  Integer.valueOf(executeUpdateQuery(sql));

			return UpdateCount;

		} else if (strs[0].equalsIgnoreCase("select")) {

			return (Object[][]) executeQuery(sql);

		}
		return null;

	}
}
