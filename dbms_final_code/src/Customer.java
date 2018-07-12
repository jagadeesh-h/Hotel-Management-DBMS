import java.sql.*;

/**
 * This class consists of the Customer relation. It contains all functionalities that are
 * related to the Customer relation
 * @author Team-D for CSC540 - Spring2018
 *
 */
public class Customer {
	/**
	 * The function is used to create the table Customer
	 * @param conn
	 */
	public static void createCustomer(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query = "CREATE TABLE Customer " +
				     "( cust_id INT(20) NOT NULL," +
					 "cust_name VARCHAR(30) NOT NULL," +
					 "cust_dob DATE," +
					 "cust_ph_num VARCHAR(15) NOT NULL," +
					 "cust_email VARCHAR(30)," +
					 "PRIMARY KEY (cust_id)" +
					 ");";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * Insert information about a new customer
	 * @param conn
	 * @param customerID
	 * @param custName
	 * @param dob
	 * @param phNum
	 * @param emailID
	 */
	public void insertCustomer(Connection conn, int customerID, String custName, String dob, String phNum, String emailID) {
		try {
			Statement s = conn.createStatement();
			String query= "INSERT INTO Customer(cust_id,cust_name, cust_dob, cust_ph_num, cust_email) VALUES ('" + customerID + "','" + custName + "','" + dob + "','" + phNum + "','" + emailID + "')";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * Update any wrong information entered about a new customer
	 * @param conn
	 * @param customerID
	 * @param custName
	 * @param dob
	 * @param phNum
	 * @param emailID
	 */
	public static void updateCustomer(Connection conn, int customerID, String custName, String dob, String phNum, String emailID) {
		try {
			Statement s = conn.createStatement();
			String query= "UPDATE Customer SET cust_name = '" + custName + "', cust_dob = '" + dob + "', cust_email = '" + emailID + "', cust_ph_num = '" + phNum + "' WHERE cust_id = '" + customerID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * Delete a customer who no longer wants to be associated with the hotel
	 * @param conn
	 * @param customerID
	 */
	public static void deleteCustomer(Connection conn, int customerID) {
		try {
			Statement s = conn.createStatement();
			String query= "DELETE FROM Customer WHERE cust_id = '" + customerID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	
}

