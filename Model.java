import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Model class store and retrieve data from the database 
 * @author Ziyu Huang, Zhining Qi, Tri Ninh
 *
 */
public class Model 
{
	View view;
	DefaultTableModel TableModel;
	DefaultTableModel tmodel;
	
	public Model(View aview)
	{
		this.view = aview;
//		TableModel = getTableModel("person");
		TableModel = null;
		tmodel = getTableModel("violation");
		
		view.setAdminSearchTable(tmodel);
		view.setTable(TableModel);
	}

	/**
	 * turn a database table into a java type represented table
	 * @param selectTable database table to be used
	 * @return table model
	 */
	public DefaultTableModel getTableModel(String selectTable) {
			DefaultTableModel tableModel = null;
			try
			{
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectmars", "root", "TRIninh123");
			Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from " + selectTable);
			tableModel = constrTableModel(rs);
			}
			catch (Exception ex) {
	        	System.out.println("errorrrr");

	            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
			return tableModel;
			
	}
	
	/**
	 * generate a table model based on parameter resultset 
	 * @param rs database table resultset
	 * @return table model
	 * @throws SQLException
	 */
	public DefaultTableModel constrTableModel(ResultSet rs) throws SQLException
	{
        Vector<String> colNames = new Vector<String>();
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();

		//retrieve the column names into vector
        ResultSetMetaData rsmd = rs.getMetaData();
        int totalColumns = rsmd.getColumnCount();
        for(int column = 1; column <= totalColumns; column++)
        	colNames.add(rsmd.getColumnName(column));
        
        while(rs.next())
        {
        	Vector<Object> v = new Vector<Object>();
        	for(int column = 1; column <= totalColumns; column++)
        		v.add(rs.getObject(column)); //store a column data
        	data.add(v);	//store the entire table
        }
        return new DefaultTableModel(data, colNames);
	}
	
	/**
	 * generate a table model based on the query
	 * @param targetname use input
	 * @param table	table name
	 * @param selectColumn select column
	 * @param where where
	 * @param model 
	 * @return
	 */
	public DefaultTableModel search(String targetname, String table, String selectColumn,
			String where, DefaultTableModel model)
	{
		try
		{
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectmars", "root", "TRIninh123");
			Statement st = con.createStatement();

			ResultSet rs =
				st.executeQuery("select name, dl, pcn,dob, balance from person "
						+ " inner join car on dl = dlicense"
						+ " inner join violation on lp = lplate "
						+ " where paid = 0 and " + where + " =   '"+targetname+"'");
	
			int row = 0;
			if(rs.last())
			{
				row = rs.getRow();
				rs.beforeFirst();
			}		
			return constrTableModel(rs);
		}
		catch(SQLException e)
		{
			 e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * generate a table model based on the query
	 * @param query query to search
	 * @return table model
	 */
	public DefaultTableModel SearchByQuery(String query)
	{
		try
		{
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectmars", "root", "TRIninh123");
			Statement st = con.createStatement();
			
			ResultSet rs = st.executeQuery(query);
			return constrTableModel(rs);
		}
		catch(SQLException e)
		{
			 e.printStackTrace();
		}
		return null;
	}
	
	 public void  addUser(String driverLicense, String name, String gender, String dob)
	 {
	        String sqlInsert = "insert into person values('" + 
	                       driverLicense + "','" + name + "','" + gender + "','" + dob + "')";
	        try{
	        	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectmars", "root", "TRIninh123");
				Statement st = con.createStatement();
	            st.executeUpdate(sqlInsert);

	        }
	        catch(SQLException e)
			{
				 e.printStackTrace();
			}  
	 }
	 
	 /**
	  * add a violation to the database
	  * @param pcn
	  * @param issuedate
	  * @param location
	  * @param contravention
	  * @param chargelevel
	  * @param fine
	  * @param paid
	  * @param lplate
	  * @param balance
	  */
	 public void addViolation( String pcn, String issuedate,String location, String contravention,String chargelevel,float fine,int paid,String lplate,float balance)
	 {
		
		  String sqlInsert = "insert into violation values('" + 
                  pcn + "','" + issuedate + "','" + location + "','" + contravention + "','" + chargelevel +"','" + fine + "','" + paid + "','" + lplate + "','" + balance +"')";
		   
		  try{
		   	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectmars", "root", "TRIninh123");
				Statement st = con.createStatement();
				
		       st.executeUpdate(sqlInsert);
		
		   }
		   catch(SQLException e)
			{
				 e.printStackTrace();
			}
	 }
	 
	 /**
	  * add a car to the database
	  * @param licensePlate
	  * @param driverLicense
	  * @param year
	  * @param make
	  * @param model
	  * @param color
	  */
	 public void addCar(String licensePlate, String driverLicense, int year, String make, String model, String color){
		 String sqlInsert1 = "insert into car values('" + licensePlate + "','" + driverLicense + "','" + year + "','" + make + "','" + model + "','" + color + "')";
		 try{
			 Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectmars", "root", "TRInin123");
			 Statement st = con.createStatement();
			 st.executeUpdate(sqlInsert1);
		 }
		 catch (SQLException e)
		 {
			 e.printStackTrace();
		 }
	 }

	 /**
	  * execute the query
	  * @param query query to execute
	  */
	 public void executeQuery(String query)
	 {
		 try{
	        	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectmars", "root", "TRIninh123");
				Statement st = con.createStatement();
				
	            st.executeUpdate(query);

	        }
	        catch(SQLException e)
			{
				 e.printStackTrace();
			}
	 }
}
