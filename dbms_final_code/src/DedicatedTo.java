import java.sql.*;

/**
 * This class consists of the DedicatedTo relation. It contains all functionalities that are
 * related to the DedicatedTo relation
 * @author Team-D for CSC540 - Spring2018
 *
 */
public class DedicatedTo {
	/**
	 * Method to create the relation DedicatedTo
	 * @param conn
	 */
	public void createDedicatedTo(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query = "CREATE TABLE DedicatedTo " +
				     "(dedicated_id INT(20) NOT NULL AUTO_INCREMENT," +
					 "dedicated_staff_id INT(20) NOT NULL REFERENCES Staff(staff_id)," +
					 "dedicated_staff_name VARCHAR(20) NOT NULL REFERENCES Staff(staff_name)," +
					 "checkin_id_fk INT(20) NOT NULL REFERENCES CheckinInfo(checkin_id)," +
					 "CONSTRAINT PRIMARY KEY(dedicated_id)" +
					 ");";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * This function is used to insert all staff who are going to serve presidential customers
	 * @param conn
	 * @param staff_id
	 * @param staff_name
	 * @param checkin_id
	 * @throws Exception
	 */
	public void insertDedicatedTo(Connection conn, int staff_id, String staff_name, int checkin_id) throws Exception {
		try {
			Statement s = conn.createStatement();
			String query = "INSERT INTO DedicatedTo(dedicated_staff_id, dedicated_staff_name, checkin_id_fk) VALUES ('" + staff_id + "','" + staff_name + "','" + checkin_id + "')";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
			throw e;
		}
	}
}
