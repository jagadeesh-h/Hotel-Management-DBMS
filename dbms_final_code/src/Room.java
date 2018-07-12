import java.sql.*;

/**
 * This class consists of the Room relation. It contains all functionalities that are
 * related to the Room relation
 * @author Team-D for CSC540 - Spring2018
 *
 */

public class Room {
	/**
	 * Method to create the relation Room
	 * @param conn
	 */
	public static void createRoom(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query = "CREATE TABLE Room " +
				     "(room_num INT(20) NOT NULL," +
				     "hotel_id_fk INT(20) NOT NULL," +
					 "availability BOOL NOT NULL," +
					 "max_occupancy VARCHAR(15) NOT NULL," +
					 "category VARCHAR(30) NOT NULL," +
					 "nightly_rate INT(10) NOT NULL," +
					 "FOREIGN KEY(hotel_id_fk) REFERENCES Hotel(hotel_id) ON UPDATE CASCADE ON DELETE CASCADE" +
					 "PRIMARY KEY (room_num, hotel_id_fk)" +
					 ");";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * To insert the values into the table Room
	 * @param conn
	 * @param roomID
	 * @param hotelID
	 * @param roomCategory
	 * @param maxAllowedOccupancy
	 * @param nightlyRate
	 * @param availability
	 */
	public void enterRoom(Connection conn, int roomID, int hotelID, String roomCategory, int maxAllowedOccupancy, int nightlyRate, int availability) {
		try {
			Statement s = conn.createStatement();
			String query= "INSERT INTO Room(room_num,hotel_id_fk, availability, max_occupancy, category, nightly_rate) VALUE ('" + roomID + "','" + hotelID + "','" + availability +"','" + maxAllowedOccupancy + "','" + roomCategory + "','" + nightlyRate + "')";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * To update the Room details
	 * @param conn
	 * @param roomID
	 * @param hotelID
	 * @param roomCategory
	 * @param maxAllowedOccupancy
	 * @param nightlyRate
	 * @param availability
	 */
	public void updateRoom(Connection conn, int roomID, int hotelID, String roomCategory, int maxAllowedOccupancy, int nightlyRate, int availability) {
		try {
			Statement s = conn.createStatement();
			String query= "UPDATE Room set category = '" + roomCategory + "', max_occupancy = '" + maxAllowedOccupancy + "', nightly_rate = '" + nightlyRate + "', availability = '" + availability + "' where room_num = '" + roomID + "' AND hotel_id_fk = '" + hotelID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * To delete a particular room
	 * @param conn
	 * @param roomID
	 * @param hotelID
	 */
	public void deleteRoom(Connection conn, int roomID, int hotelID) {
		try {
			Statement s = conn.createStatement();
			String query= "DELETE from Room where room_num = '" + roomID + "' AND hotel_id_fk = '" + hotelID + "'";;
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * To check the room availability with category, start date and end date
	 * @param conn
	 * @param hotelID
	 * @param roomCategory
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean checkRoomAvailability(Connection conn, int hotelID, String roomCategory, String startDate, String endDate) {
		boolean retval = false;
		try {
			Statement s = conn.createStatement();
			String query1 = "select room_num from Room where hotel_id_fk = '" + hotelID + "' and room_num NOT IN (Select distinct(room_num) from CheckinInfo Where hotel_id = '" + hotelID + "')";
			// String query2 = "SELECT DISTINCT(Room.room_num) from Room, CheckinInfo where Room.room_num = CheckinInfo.room_num AND availability = 1 and category = '" + roomCategory + "' and hotel_id_fk = '" + hotelID + "' and (CheckinInfo.end_date < '" + startDate + "' or CheckinInfo.start_date > '" + endDate + "')";
			String query2 = "Select Distinct(room_num) from CheckinInfo Where hotel_id = '" + hotelID + "' and room_num NOT IN (Select Distinct(room_num) from CheckinInfo Where hotel_id = '" + hotelID + "' and (('" + startDate  + "' BETWEEN start_date and end_date) or ('" + endDate + "' BETWEEN start_date and end_date)))";
			ResultSet rs1 = s.executeQuery(query1);
			ResultSet rs2 = s.executeQuery(query2);
			System.out.println("Available rooms in " + roomCategory + " type: ");
			while(rs1.next()) {
				if(retval == false) retval = true;
				int room_num = rs1.getInt("room_num");
				System.out.println(room_num);
			}
			while(rs2.next()) {
				if(retval == false) retval = true;
				int room_num = rs2.getInt("room_num");
				System.out.println(room_num);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
		return retval;
	}
	/**
	 * To assign a room and set the availability to 0
	 * @param conn
	 * @param roomNum
	 * @param hotelID
	 * @throws Exception
	 */
	public void assignRoom(Connection conn, int roomNum, int hotelID) throws Exception {
		try {
			Statement s = conn.createStatement();
			String query= "Update Room set availability = FALSE where room_num = '" + roomNum  + "' and hotel_id_fk = '" + hotelID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
			throw e;
		}
	}
	/**
	 * To release room and set the availability of the room to True
	 * @param conn
	 * @param roomNum
	 * @param hotelID
	 */
	public void releaseRoom(Connection conn, int roomNum, int hotelID) {
		try {
			Statement s = conn.createStatement();
			System.out.println("Releasing rooms");
			String query= "Update Room set availability = TRUE where room_num = '" + roomNum  + "' and hotel_id_fk = '" + hotelID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * To report the occupancy by Hotel
	 * @param conn
	 */
	public void reportOccupancyByHotel(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query= "select Hotel.hotel_id,coalesce(sum(num_guest),0) AS Occupancy " +
						  "from Hotel LEFT JOIN CheckinInfo ON CheckinInfo.hotel_id = Hotel.hotel_id " +
						  "group by Hotel.hotel_id";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				int hid = rs.getInt("hotel_id");
				int num_guest = rs.getInt("Occupancy");
				System.out.println("Hotel ID: " + hid);
				System.out.println("Occupancy: " + num_guest);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * To report the occupancy by Room category
	 * @param conn
	 */
	public void reportOccupancyByRoomCategory(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query= "select Room.hotel_id_fk as hotel_id,Room.category,coalesce(sum(num_guest),0) AS Occupancy " +
						   "from Room LEFT JOIN CheckinInfo ON CheckinInfo.hotel_id = Room.hotel_id_fk AND CheckinInfo.room_num = Room.room_num " +
					      "group by Room.category";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				int hid = rs.getInt("hotel_id");
				String roomCategory = rs.getString("category");
				int num_guest = rs.getInt("Occupancy");
				System.out.println("Hotel ID: " + hid);
				System.out.println("Room Category: " + roomCategory);
				System.out.println("Occupancy: " + num_guest);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * To report room occupancy by date
	 * @param conn
	 * @param startDate
	 * @param endDate
	 */
	public void reportOccupancyByDate(Connection conn, String startDate, String endDate) {
		try {
			Statement s = conn.createStatement();
			String query= "SELECT coalesce(sum(num_guest),0) AS Occupancy " +
						   "FROM CheckinInfo " +
						   "WHERE start_date > '" + startDate + "' AND end_date < '" + endDate + "'";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				int num_guest = rs.getInt("Occupancy");
				System.out.println("Start Date: " + startDate);
				System.out.println("End Date: " + endDate);
				System.out.println("Occupancy: " + num_guest);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * To report occupancy by City 
	 * @param conn
	 */
	public void reportOccupancyByCity(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query=  "SELECT Hotel.hotel_addr, coalesce(sum(num_guest),0) AS Occupancy " +
						   "from Hotel LEFT JOIN CheckinInfo ON CheckinInfo.hotel_id = Hotel.hotel_id " +
						   "group by Hotel.hotel_addr";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				String hotel_addr = rs.getString("hotel_addr");
				int num_guest = rs.getInt("Occupancy");
				System.out.println("City: " + hotel_addr);
				System.out.println("Occupancy: " + num_guest);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * To find the room category by hotel id and room num
	 * @param conn
	 * @param room_num
	 * @param hotelID
	 * @return
	 */
	public String findRoomCategory(Connection conn, int room_num, int hotelID) {
		String category = "Economy";
		try {
			Statement s = conn.createStatement();
			String query = "Select category from Room Where room_num = '" + room_num + "' and hotel_id_fk = '" + hotelID + "'";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				category = rs.getString("category");
			}
		} catch(Exception e) {
			System.out.print(e);
		}
		return category;
	}
	/**
     * To get the max occupancy by room number and hotel id
     * @param conn
     * @param room_num
     * @param hotelID
     * @return
     */
	public int getMaxOccupancy(Connection conn, int room_num, int hotelID) {
		int max_occupancy = 0;
		try {
			Statement s = conn.createStatement();
			String query = "Select max_occupancy from Room Where room_num = '" + room_num + "' and hotel_id_fk = '" + hotelID + "'";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				max_occupancy = rs.getInt("max_occupancy");
			}
		} catch(Exception e) {
			System.out.print(e);
		}
		return max_occupancy;
	}

}
