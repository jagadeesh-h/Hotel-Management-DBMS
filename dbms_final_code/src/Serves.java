import java.sql.*;

/**
 * This class consists of the Serves relation. It contains all functionalities that are
 * related to the Serves relation, like which checkin has been served by which staff
 * @author Team-D for CSC540 - Spring2018
 *
 */
public class Serves {
	/**
	 * To create the relation Serves
	 * @param conn
	 */
	public static void createServes(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query = "CREATE TABLE Serves " +
				     "(staff_id_fk INT(20) NOT NULL REFERENCES Staff(staff_id)," +
					 "checkin_id_fk INT(20) NOT NULL REFERENCES CheckinInfo(checkin_id)," +
					 "staff_name VARCHAR(30)," +
					 "CONSTRAINT PRIMARY KEY(staff_id_fk, checkin_id_fk)"+
					 ");";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * Method to insert data into the Serves table
	 * @param conn
	 * @param staff_id
	 * @param staff_name
	 * @param checkin_id
	 */
	public void insertServes(Connection conn, int staff_id, String staff_name, int checkin_id) {
		try {
			Statement s = conn.createStatement();
			String query = "INSERT INTO Serves(staff_id_fk, checkin_id_fk, staff_name) VALUES ('" + staff_id + "','" + staff_name + "','" + checkin_id + "')";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Returns the staff serving the particular checkin ID
	 * @param conn
	 */
	public void staffServed(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query=  "SELECT checkin_id_fk, staff_id_fk, staff_name " +
						   "FROM Serves " +
						   "UNION " + 
 						   "Select checkin_id_fk, dedicated_staff_id, dedicated_staff_name " +
						   "FROM DedicatedTo " +
						   "ORDER BY checkin_id_fk";
			ResultSet rs = s.executeQuery(query);
			System.out.println("CheckinID \t staff ID \t Name");
			while(rs.next()) {
				int checkin_ID = rs.getInt("checkin_id_fk");
				int staff_id = rs.getInt("staff_id_fk");
				String staff_name = rs.getString("staff_name");
				System.out.println(checkin_ID + "  \t   " + staff_id + "    \t   " + staff_name);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
	}
}
