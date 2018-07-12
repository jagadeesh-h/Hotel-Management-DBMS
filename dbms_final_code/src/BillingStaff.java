import java.sql.*;
import java.util.*;

/**
 * This class consists of the Billing Staff functionalities. It allows us to choose all the functions
 * that are under the control of the Billing Staff
 * @author Team-D for CSC540 - Spring2018
 *
 */

public class BillingStaff{
	/**
	 * This function is called to give the billing staff options of what they can control
	 * @param conn
	 * @param choice
	 * @throws SQLException
	 */
	public void runBilling(Connection conn, int choice) throws SQLException {
		Scanner s = new Scanner(System.in);
		Billing billing = new Billing();
		CheckinInfo checkinInfo = new CheckinInfo();
		Room room = new Room(); 
		
		switch(choice) {
			case 1: System.out.println("Enter Checkin ID: ");     //DONE
					int checkinID = Integer.parseInt(s.nextLine());
					ArrayList<Integer> room_num = checkinInfo.findRoomNumber(conn, checkinID);
					String category = room.findRoomCategory(conn, room_num.get(0), room_num.get(1));
					System.out.println(category);
					billing.generateBill(conn, checkinID, category);
					break;
		
			case 2: System.out.println("Enter Checkin ID: ");   // DONE
					checkinID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Customer ID: ");
					int custID = Integer.parseInt(s.nextLine());
					System.out.println("Enter ssn of Payee: ");
					String ssn_payee = s.nextLine();
					System.out.println("Enter Billing Address: ");
					String billing_addr = s.nextLine();
					System.out.println("Enter Card Number: ");
					String card_num = s.nextLine();
					System.out.println("Enter Payment Method: ");
					String paymentMethod = s.nextLine();
					billing.maintainBill(conn, checkinID, custID, ssn_payee, billing_addr, card_num, paymentMethod);
					break; 
		}
	}
}
