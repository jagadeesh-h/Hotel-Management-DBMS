import java.sql.*;

/**
 * This class consists of the Buys relation. It contains all functionalities that are
 * related to the Buys relation, like which checkin has used which service
 * @author Team-D for CSC540 - Spring2018
 *
 */

public class Buys {
	/**
	 * Create the table Buys
	 * @param conn
	 */
	public static void createBuys(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query = "CREATE TABLE Buys " +
				     "(checkin_id_fk INT(20) NOT NULL REFERENCES CheckinInfo(checkin_id)," +
					 "service_id_fk INT(20) NOT NULL REFERENCES Services(service_id)," +
					 "CONSTRAINT PRIMARY KEY(checkin_id_fk, service_id_fk)" +
					 ");";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Entering the service used by a particular checkin ID
	 * @param conn
	 * @param checkinID
	 * @param serviceID
	 */
	public void enterServiceRecords(Connection conn, int checkinID, int serviceID) {
		try {
			Statement s = conn.createStatement();
			String query= "INSERT INTO Buys (service_id_fk, checkin_id_fk) VALUES ('" + serviceID + "','" + checkinID + "')";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	
	/**
	 * Updating the service used by a particular checkin ID
	 * @param conn
	 * @param checkinID
	 * @param serviceID
	 */
	public void updateServiceRecords(Connection conn, int checkinID, int serviceID) {
		try {
			Statement s = conn.createStatement();
			String query= "UPDATE Buys SET service_id_fk = '" + serviceID + "' where checkin_id_fk = '" + checkinID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
}
