package eg.edu.alexu.csd.oop.jdbc.cs43;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.spi.DirStateFactory.Result;

import eg.edu.alexu.csd.oop.db.cs43.ExecuteStructureQuerys;


public class test {

	public static void main(String[] args) throws SQLException {
		
		Driver driver = new JDBCDriver();
		Properties info =new Properties();
		info.put("path",new File("C:\\Users\\SHIKO\\Desktop\\"+"sample" + System.getProperty("file.separator") +"database1"));
		Connection connection = driver.connect("jdbc:xmldb://localhost", info);
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(1);
		statement.execute("create database dbas");
		statement.execute("drop database dbas ");
		
		statement.execute("create table table1 ( column1 int ,column2 varchar )");
		statement.executeUpdate("insert into table1 values ( 12 , 'sd')");
		statement.executeUpdate("insert into table1 values ( 13 , 'sd' )");
		statement.executeUpdate("insert into table1 values ( 14 , 'sd' )");
	//	ResultSet set = statement.executeQuery("select * from table1 where column1 > 11 ");
		
	/*	while (set.next()) {
			System.out.println("value " +set.getInt(1) +" " +set.getString(2));
			
		}	System.out.println();
		set.absolute(-1);
		System.out.println("value " +set.getInt(1));
		while (set.previous()) {
			System.out.println("value " +set.getInt(1));
		}
		*/
		statement.close();
	}	
	
}
