import java.sql.*;
import java.util.*;

/**
 * This class consists of the CheckinInfo relation. It contains all functionalities that are
 * related to the Checkininfo relation, like which checkin has used which service
 * @author Team-D for CSC540 - Spring2018
 *
 */
public class CheckinInfo {
	/**
	 * Function to create the CheckinInfo table
	 * @param conn
	 */
	public static void createCheckinInfo(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query = "CREATE TABLE CheckinInfo " +
				     "(checkin_id INT(20) NOT NULL AUTO_INCREMENT," +
					 "in_time TIME NOT NULL," +
					 "out_time TIME NOT NULL," +
					 "num_guest INT(10) NOT NULL," +
					 "start_date DATE NOT NULL," +
					 "end_date DATE NOT NULL," +
					 "free_services BOOL NOT NULL," +
					 "services_offered VARCHAR(20) NOT NULL," + 
					 "room_num INT(20) NOT NULL," +
					 "hotel_id INT(30) NOT NULL," +
					 "cust_id INT(20) NOT NULL," +
					 "CONSTRAINT PRIMARY KEY (checkin_id), " +
					 "FOREIGN KEY(cust_id) REFERENCES Customer(cust_id), " +
					 "FOREIGN KEY(hotel_id) REFERENCES Hotel(hotel_id), " +
					 "FOREIGN KEY(room_num) REFERENCES Room(room_num) " +
					 ");";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * This information is entered by the front desk staff. This method is called when the customer has checked into a room.
	 * @param conn
	 * @param in_time
	 * @param out_time
	 * @param num_guest
	 * @param start_date
	 * @param end_date
	 * @param free_services
	 * @param services_offered
	 * @param room_num
	 * @param hotel_id
	 * @param cust_id
	 * @return
	 * @throws Exception
	 */
	public int enterCheckinInfo(Connection conn, String in_time, String out_time, int num_guest, String start_date, String end_date, int free_services, String services_offered, int room_num, int hotel_id, int cust_id) throws Exception {
		int checkin_id = 0;
		try {
			String query= "INSERT INTO CheckinInfo (in_time, out_time, num_guest, start_date, end_date, free_services, services_offered, room_num, hotel_id, cust_id) VALUES ('" + in_time + "','" + out_time + "','" + num_guest + "','" + start_date + "','" + end_date + "','" + free_services + "','" + services_offered + "','" + room_num + "','" + hotel_id + "','" + cust_id + "')";
			PreparedStatement s = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			s.executeUpdate();
			ResultSet rs = s.getGeneratedKeys();
			rs.next();
			checkin_id = rs.getInt(1);
			System.out.println("New Checkin ID is: " + checkin_id);
		} catch(Exception e) {
			System.out.print(e);
			throw e;
		}
		return checkin_id;
	}
	/**
	 * The function is used to find the room number, hotel id
	 * @param conn
	 * @param checkinID
	 * @return
	 */
	public ArrayList<Integer> findRoomNumber(Connection conn, int checkinID) {
		ArrayList<Integer> checkin_id = new ArrayList<Integer>();
		try {
			Statement s = conn.createStatement();
			String query= "Select room_num, hotel_id from CheckinInfo where checkin_id = '" + checkinID + "'";
			ResultSet rs = s.executeQuery(query);		
			while(rs.next()) {
				int room = rs.getInt("room_num");
				int hotel_id = rs.getInt("hotel_id");
				checkin_id.add(room);
				checkin_id.add(hotel_id);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
		return checkin_id;
	}
}
