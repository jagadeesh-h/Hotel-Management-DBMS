import java.sql.*;

/**
 * This class consists of the Billing relation. It contains all functionalities that are
 * related to the billing relation
 * @author Team-D for CSC540 - Spring2018
 *
 */
public class Billing {
	/**
	 * To create the Billing table.
	 * @param conn
	 */
	public static void createBilling(Connection conn) {    
		try {
			Statement s = conn.createStatement();
			String query = "CREATE TABLE Billing " +
				     "(billing_id INT(20) NOT NULL AUTO_INCREMENT," +
					 "checkin_id_fk INT(30) NOT NULL," +
					 "cust_id_fk INT(20) NOT NULL," +
					 "ssn_payee VARCHAR(15) NOT NULL," +
					 "billing_addr VARCHAR(90) NOT NULL," +
					 "payment_method VARCHAR(20) NOT NULL," +
					 "card_num VARCHAR(20)," +
					 "service_price FLOAT(20,2) NOT NULL," +
					 "room_price FLOAT(20,2) NOT NULL," + 
					 "amount FLOAT(20,2) NOT NULL," +
					 "PRIMARY KEY (billing_id)," +
					 "FOREIGN KEY(checkin_id_fk) REFERENCES CheckinInfo(checkin_id)," +
					 "FOREIGN KEY(cust_id_fk) REFERENCES Customer(cust_id)" +
					 ");";
			s.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * Generate final bill for a particular customer
	 * @param conn
	 * @param checkinID
	 * @param category
	 * @throws SQLException
	 */

	/* A transaction has been added below for generating the bill. The following transaction has been added due to 2 sepearte updates.
	   These updates are supposed to be atomic, ie, both update are to happen together, or not happen at all. The following 2 updates are of interest:
	   a) Update the service price, room price and total amount owed by the customer in the billing table
	   b) Update the total amount if customer is using hotel card, by adding 5 % discount
	   The situation of interest is in the following scenario:
	   Assume that the we have generated the bill for a customer who has a hotel card. Before we can update his total amount by applying his discount
	   the system crashes. In such a scenario, our customer would not be benefited by the 5% discount and would have to pay the full price, which is not 
	   the right amount. Hence we need to ensure that both updates happen together atomically and hence we use a transaction.
	 */
	public void generateBill(Connection conn, int checkinID, String category) throws SQLException{ 
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String query=  "UPDATE Billing SET service_price = " +
						   "(SELECT sum(price) as price FROM Services " +
						   "WHERE service_id IN (SELECT service_id_fk FROM Buys " +
						   "WHERE checkin_id_fk = '" +  checkinID + "')), room_price = (SELECT nightly_rate * date.Stay " + 
						   "FROM (SELECT distinct(nightly_rate) FROM Room WHERE category = '" + category + "') as rate, " +    //HAVE TO MAKE CHANGE IN CATEGORY
						   "(SELECT DATEDIFF(end_date, start_date) AS Stay FROM CheckinInfo WHERE checkin_id = '" + checkinID +"') as date), amount = service_price + room_price where checkin_id_fk = '" + checkinID + "'";
			s.executeUpdate(query);
			query = "UPDATE Billing SET amount = amount - 0.05*amount WHERE checkin_id_fk = '" + checkinID + "' AND payment_method = 'hotel credit'";
			s.executeUpdate(query);
			conn.commit();
		} catch(Exception e) {
			conn.rollback();
			System.out.print(e);
		}
	}
	
	/**
	 * Update or maintain the bill for a particular checkin
	 * @param conn
	 * @param checkinID
	 * @param custId
	 * @param ssn_payee
	 * @param billing_addr
	 * @param card_num
	 * @param paymentMethod
	 */ 
	public void maintainBill(Connection conn, int checkinID, int custId, String ssn_payee, String billing_addr, String card_num, String paymentMethod) {
		try {
			Statement s = conn.createStatement();
			String query= "INSERT INTO Billing (checkin_id_fk, cust_id_fk, ssn_payee, billing_addr, payment_method, card_num, service_price, room_price, amount) VALUES ('" + checkinID + "','" + custId + "','" + ssn_payee + "','" + billing_addr + "','" + paymentMethod + "','" + card_num + "',0,0,0)" ;
			System.out.println("IN");
			s.executeUpdate(query);
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	
	/**
	 * Enter total amount owed by the customer (for that particular checkin)
	 * @param conn
	 * @param checkinID
	 */
	public void totalAmountOwed(Connection conn, int checkinID) {
		try {
			Statement s = conn.createStatement();
			String query= "select billing_id,amount from Billing where checkin_id_fk = '" + checkinID + "'";
			ResultSet rs = s.executeQuery(query);
			System.out.println("Total Amount Owed: ");
			while(rs.next()) {
				int billing = rs.getInt("billing_id");
				int amount = rs.getInt("amount");
				System.out.println(billing + " \t " + amount);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
	}

	/**
	 * Return the itemized Receipt of the particular customer
	 * @param conn
	 * @param checkinID
	 */
	public void itemizedReceipt(Connection conn, int checkinID) {
		try {
			Statement s = conn.createStatement();
			String query= "select billing_id, service_price, room_price,amount from Billing where checkin_id_fk = '" + checkinID + "'";
			ResultSet rs = s.executeQuery(query);
			System.out.println("Itemized Receipt: ");
			while(rs.next()) {
				int billing = rs.getInt("billing_id");
				int s_price = rs.getInt("service_price");
				int r_price = rs.getInt("room_price");
				int amount = rs.getInt("amount");
				System.out.println("Billing ID: " + billing);
				System.out.println("Service Price: " + s_price);
				System.out.println("Room Price: " + r_price);
				System.out.println("Total Amount: " + amount);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	
	/**
	 * Returns the Revenue as requested by the manager for a start and end Date
	 * @param conn
	 * @param startDate
	 * @param endDate
	 */
	public void generateRevenue(Connection conn, String startDate, String endDate) {
		try {
			Statement s = conn.createStatement();
			String query= "SELECT Hotel.hotel_id, temp1.start_date, temp1.end_date, coalesce(temp1.amount,0) AS amount" +
						   " FROM (select start_date, end_date,CheckinInfo.hotel_id, sum(amount) as amount " +
						  " FROM Billing JOIN CheckinInfo  ON CheckinInfo.checkin_id = Billing.checkin_id_fk " +
						  " WHERE CheckinInfo.start_date > '" + startDate + "'AND CheckinInfo.end_date < '" + endDate +
						   "'GROUP BY CheckinInfo.hotel_id) as temp1 RIGHT JOIN Hotel " +
						   "ON temp1.hotel_id = Hotel.hotel_id";
			ResultSet rs = s.executeQuery(query);
			System.out.println("Total Revenue: ");
			while(rs.next()) {
				int hid = rs.getInt("hotel_id");
				String start_date = rs.getString("start_date");
				String end_date = rs.getString("end_date");
				int amount = rs.getInt("amount");
				System.out.println("Hotel ID: " + hid);
				System.out.println("Start Date: " + start_date);
				System.out.println("End Date: " + end_date);
				System.out.println("Amount: " + amount);
			}
		} catch(Exception e) {
			System.out.print(e);
		}
	}
}
