import java.sql.*;

/**
 * This class consists of the Services relation. It contains all functionalities that are
 * related to the Services relation
 * @author Team-D for CSC540 - Spring2018
 *
 */
public class Services {
	/**
	 * To create the relation Services
	 * @param conn
	 */
	public static void createServices(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query = "CREATE TABLE Services " +
				     "(service_id INT(20) NOT NULL AUTO_INCREMENT," +
					 "price INT(20) NOT NULL," +
					 "service_type VARCHAR(30) NOT NULL," +
					 "PRIMARY KEY(service_id)"+
					 ");";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Enter the services that are available to the hotels
	 * @param conn
	 * @param serviceID
	 * @param price
	 * @param serviceType
	 */
	public void enterServices(Connection conn, int serviceID, int price, String serviceType) {
		try {
			Statement s = conn.createStatement();
			String query= "INSERT INTO Services(service_id, price, service_type) VALUES ('" + serviceID + "','" + price + "','" + serviceType + "')";
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	/**
	 * To find the service id using the service type
	 * @param conn
	 * @param serviceType
	 * @return
	 */
	public int findServiceID(Connection conn, String serviceType) {
		int id = 0;
		try {
			Statement s = conn.createStatement();
			String query= "Select service_id FROM Services WHERE service_type = '" + serviceType + "'";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				id = rs.getInt("service_id");
			}
		} catch(Exception e) {
			System.out.print(e);
		}	
		return id;
	}
	/**
	 * To print the list of Services
	 * @param conn
	 */
	public void printList(Connection conn) {
		try {
			Statement s = conn.createStatement();
			String query = "SELECT * FROM Services";
			ResultSet rs = s.executeQuery(query);
			while(rs.next()) {
				int service_id = rs.getInt("service_id");
				String type = rs.getString ("service_type");
				int price = rs.getInt("price");
				System.out.println("Service ID: " + service_id + " Service Type: " + type + " Price: " + price);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
