import java.sql.*;
import java.util.*;

/**
 * This class consists of the Staff relation. It contains all functionalities that are
 * related to the Staff relation
 * @author Team-D for CSC540 - Spring2018
 *
 */

public class Staff {
	/**
	 * To create the relation Staff
	 * @param conn
	 */
	public void createStaff(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query = "CREATE TABLE Staff " +
				     "(staff_id INT(20) NOT NULL," +
					 "staff_dept VARCHAR(30) NOT NULL," +
					 "staff_title VARCHAR(50) NOT NULL," +
					 "staff_age INT(5) NOT NULL," +
					 "staff_name VARCHAR(15) NOT NULL," +
					 "hotel_serving INT(20) NOT NULL," +
					 "staff_ph_num VARCHAR(15) NOT NULL," +
					 "staff_addr VARCHAR(50) NOT NULL," +
					 "staff_availability BOOLEAN NOT NULL," + 
					 "PRIMARY KEY (staff_id)" +
					 "FOREIGN KEY(hotel_serving) REFERENCES Hotel(hotel_id) ON UPDATE CASCADE ON DELETE CASCADE" +
					 ");";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Manager creates a new entry for any new staff
	 * @param conn
	 * @param staffID
	 * @param dept
	 * @param title
	 * @param age
	 * @param staffName
	 * @param hotelID
	 * @param phoneNum
	 * @param staffAddr
	 * @param staffAvailability
	 */
	public void enterStaff(Connection conn, int staffID, String dept, String title, int age, String staffName, int hotelID, String phoneNum, String staffAddr, int staffAvailability) {
		try {
			Statement s = conn.createStatement();
			String query = "INSERT INTO Staff(staff_id,staff_dept, staff_title, staff_age, staff_name, hotel_serving, staff_ph_num, staff_addr,staff_availability) VALUES ('" + staffID + "','" + dept + "','" + title + "','" + age + "','" + staffName + "','" + hotelID + "','" + phoneNum +  "','" + staffAddr + "','" + staffAvailability + "')";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	
	/**
	 * Updating any wrong entry for a particular staff
	 * @param conn
	 * @param staffID
	 * @param dept
	 * @param title
	 * @param age
	 * @param staffName
	 * @param hotelID
	 * @param phoneNum
	 * @param staffAddr
	 * @param staffAvailability
	 */
	public void updateStaff(Connection conn, int staffID, String dept, String title, int age, String staffName, int hotelID, String phoneNum, String staffAddr, int staffAvailability) {
		try {
			Statement s = conn.createStatement();
			String query= "UPDATE Staff set staff_dept = '" + dept + "', staff_title = '" + title + "', staff_age = '" + age + "', staff_name = '" + staffName + "', staff_addr = '" + staffAddr + "', staff_availability = '" + staffAvailability + "' where staff_id = '" + staffID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	
	/**
	 * Deleting a staff who is no longer with the hotel
	 * @param conn
	 * @param staffID
	 */
	public void deleteStaff(Connection conn, int staffID) {
		try {
			Statement s = conn.createStatement();
			String query= "DELETE from Staff where staff_id = '" + staffID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	
	/**
	 * Reporting the staff of a particular job title as requested by the manager
	 * @param conn
	 */
	public void reportStaffByGroup(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query=  "SELECT * " +
						   "FROM Staff " +
						   "ORDER BY staff_title";
			ResultSet rs = s.executeQuery(query);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
				for(int i = 1; i <= columnCount; i++) {
					Object object = rs.getObject(i);
			        System.out.printf("%s, ", object == null ? "NULL" : object.toString());
				}
				System.out.print("\n");
			}
		} catch(Exception e) {
			System.out.print(e);
		}
	}

	/**
	 * Returns true if there are any staff that are free for the requested job title
	 * @param conn
	 * @param title
	 * @return
	 */
	public boolean chooseFreeStaff(Connection conn, String title) {
		int flag = 0;
		try {
			Statement s = conn.createStatement();
			String query = "SELECT staff_id FROM Staff WHERE staff_availability = TRUE AND staff_title = '" + title + "'";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				if(flag == 0) flag = 1;
				int staff_id = rs.getInt("staff_id");
				System.out.println(staff_id);
			}
		} 
		catch(Exception e) {
			System.out.println(e);
		}
		if(flag == 0) return false;
		return true;
	}

	/**
	 * Returns the staff name of the person with given staff id
	 * @param conn
	 * @param id
	 * @return
	 */
	public String getStaffName(Connection conn, int id) {
		String res = "";
		try {
			Statement s = conn.createStatement();
			String query = "Select staff_name from Staff Where staff_id = '" + id + "'";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				res = rs.getString("staff_name");
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return res;
	}

	/**
	 * Assigns a staff to a service, which means that staff is currently unavailable
	 * @param conn
	 * @param staffID
	 * @throws Exception
	 */
	public void assignStaff(Connection conn, int staffID) throws Exception{
		try {
			Statement s = conn.createStatement();
			String query= "Update Staff set staff_availability = FALSE where staff_id = '" + staffID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
			throw e;
		}
	}

	/**
	 * Releases a staff from a service, which means the staff is now available for more service
	 * @param conn
	 * @param staffID
	 */
	public void releaseStaff(Connection conn, int staffID) {
		try {
			Statement s = conn.createStatement();
			String query= "Update Staff set staff_availability = TRUE where staff_id = '" + staffID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}	

	/**
	 * Returns the list of all the staff who have served for that checkin ID (customer)
	 * @param conn
	 * @param checkinID
	 * @return
	 */
	public ArrayList<Integer> getStaffServed(Connection conn, int checkinID) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
			Statement s = conn.createStatement();
			String query=  "SELECT staff_id_fk " +
						   "FROM Serves " +
						   "Where checkin_id_fk = '" + checkinID + "'" +
						   "UNION " + 
 						   "Select dedicated_staff_id " +
						   "FROM DedicatedTo " +
						   "Where checkin_id_fk = '" + checkinID + "'";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				int id = rs.getInt("staff_id_fk");
				list.add(id);
			}
		}
		catch(Exception e) {
			System.out.print(e);
		}
		return list;
	}
}
