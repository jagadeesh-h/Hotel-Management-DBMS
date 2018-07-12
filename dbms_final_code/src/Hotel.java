import java.sql.*;

/**
 * This class consists of the Hotel relation. It contains all functionalities that are
 * related to the Hotel relation
 * @author Team-D for CSC540 - Spring2018
 *
 */
public class Hotel {
	/**
	 * Method to create the relation Hotel
	 * @param conn
	 */
	public void createHotel(Connection conn) {
		try {
			Statement s = conn.createStatement();  
			String query = "CREATE TABLE Hotel " +
				     "(hotel_id INT(20) NOT NULL," +
					 "hotel_name VARCHAR(30) NOT NULL," +
					 "hotel_addr VARCHAR(50) NOT NULL," +
					 "manager_id INT(20) NOT NULL," +
					 "hotel_ph_num VARCHAR(15) NOT NULL," +
					 "PRIMARY KEY (hotel_id)" +
					 ");";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Entering the information about a new hotel
	 * @param conn
	 * @param hotelID
	 * @param hotelName
	 * @param hotelAddress
	 * @param hotelPh
	 * @param managerID
	 */
	public void enterHotel(Connection conn, int hotelID, String hotelName, String hotelAddress, String hotelPh, int managerID) {
		try {
			
			Statement s = conn.createStatement();
			String query= "INSERT INTO Hotel(hotel_id,hotel_name, hotel_addr, hotel_ph_num, manager_id) VALUE ('" + hotelID + "','" + hotelName + "','" + hotelAddress + "','" + hotelPh + "','" + managerID + "')";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	
	/**
	 * Updating any wrong entry of a hotel
	 * @param conn
	 * @param hotelID
	 * @param hotelName
	 * @param hotelAddress
	 * @param hotelPh
	 * @param managerID
	 */
	public void updateHotel(Connection conn, int hotelID, String hotelName, String hotelAddress, String hotelPh, int managerID) {
		try {
			Statement s = conn.createStatement();
			String query= "UPDATE Hotel SET hotel_name = '" + hotelName + "', hotel_addr = '" + hotelAddress + "', manager_id = '" + managerID + "', hotel_ph_num = '" + hotelPh + "' WHERE hotel_id = '" + hotelID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	
	/**
	 * Delete the hotel which is no longer in business
	 * @param conn
	 * @param hotelID
	 */
	public void deleteHotel(Connection conn, int hotelID) {
		try {
			Statement s = conn.createStatement();
			String query= "DELETE FROM Hotel WHERE hotel_id = '" + hotelID + "'";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	
	/**
	 * Returns the total occupancy and percentage occupied in all hotels
	 * @param conn
	 */
	public void reportTotalOccupancy(Connection conn, String startDate, String endDate) {
		try {
			Statement s = conn.createStatement();
			String query= "SELECT sum(num_guest) AS Total_occupancy FROM CheckinInfo WHERE checkin_id IN (Select checkin_id from CheckinInfo Where ((start_date BETWEEN '" + startDate + "' and '" + endDate + "') and (end_date BETWEEN '" + startDate + "' and '" + endDate + "')))" ;
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				int sum = rs.getInt("Total_occupancy");
				System.out.println("Total_occupancy: " + sum);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * Returns the percentage occupied of rooms
	 * @param conn
	 */
	public void reportPercentageOfRooms(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query= "select ((value1/value2)*100) as Percentage_Occupied from (SELECT COUNT(room_num) as value1 FROM Room WHERE availability = 0) as temp, (SELECT COUNT(room_num) as value2 FROM Room) as temp2";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				float percentage = rs.getFloat("Percentage_Occupied");
				System.out.println("Percentage_Occupied: " + percentage);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
	}
}
