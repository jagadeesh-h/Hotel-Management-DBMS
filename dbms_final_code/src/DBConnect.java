import java.util.Scanner;
import java.sql.*;

/**
 * This class is to help the user to choose all functionalities that he wants to select
 * It gives the user all the options of what are the allowed functionalities for that user
 * @author Team-D for CSC540 - Spring2018
 *
 */
public class DBConnect{
	

	static final String jdbcURL = "jdbc:mysql://localhost:3306/demo";  //connect to database
	static Connection conn = null;
	static void closeconnection(Connection conn) {
    	if(conn != null) {
        		try { conn.close(); } catch(Throwable whatever) {}
    	}
	}
	/**
	 * Main method where the project execution starts. Allows the user to select various views
	 * @param args
	 * @throws Exception
	 * @throws SQLException
	 */
	public static void main(String[] args) throws Exception, SQLException
	{
		try {
			
		        Class.forName("org.mariadb.jdbc.Driver");
		        String user = "root";
		        String passwd = "";
		        conn = DriverManager.getConnection(jdbcURL, user, passwd);
				Manager manager = new Manager();
				Frontdesk frontdesk = new Frontdesk();
				BillingStaff billingStaff = new BillingStaff();
				Maintenance maintenance = new Maintenance();
				char cont;

				Hotel hotel = new Hotel();
				// hotel.createHotel(conn);
				Staff staff = new Staff();
				// staff.createStaff(conn);
				Customer customer = new Customer();
				// customer.createCustomer(conn);
				Room room = new Room();
				// room.createRoom(conn);
				CheckinInfo checkin = new CheckinInfo();
				// checkin.createCheckinInfo(conn);
				Billing billing = new Billing();
				// billing.createBilling(conn);
				Services services = new Services();
				// services.createServices(conn);
				Serves serves = new Serves();
				// serves.createServes(conn);
				Buys buys = new Buys();
				// buys.createBuys(conn);
				DedicatedTo dedicated = new DedicatedTo();
				// dedicated.createDedicatedTo(conn);

			do //choices for the user
			{
				
				Scanner scanner = new Scanner(System.in);				
				System.out.println("Enter the choice of view: \n1. Manager\n2. Front Desk\n3. Billing\n4. Maintanence");
				int view_choice = scanner.nextInt();
			
				switch(view_choice) {
				case 1: //Manager View
					System.out.println("Enter your option: ");
					System.out.println("1. Create New Hotel \n2. Create New staff \n3. Update Hotel \n4. Update the Staff \n" +
							"5. Update the Room \n6. Delete a Hotel \n7. Delete a staff \n8. Delete Room information \n" +
							"9. Generate Revenue \n10. Report Occupancy \n11. Report Serving staff \n12. Report Staff By Group\n" +
							"13. Report total Occupancy \n14. Enter Room Information \n15: Report Percentage of rooms occupied");
					int choice = scanner.nextInt();
					manager.runManager(conn, choice);	
					break;
				case 2: //FrontDesk view
					System.out.println("Enter your option: ");
					System.out.println("1. Create New Customer \n2. Create New Checkin \n3. Update Customer \n4. Delete Customer \n" +
							"5. Release the Room \n6. Get total bill of customer \n7. Get Itemized Receipt of the customer \n 8.Release Staff from serving \n 9. Checkout the customer");
					choice = scanner.nextInt();
					frontdesk.runFrontdesk(conn, choice);	
					break;
				case 3: //Billing view
					System.out.println("1. Generate a Bill \n2. Enter Billing Information \n");
					System.out.println("Enter your option: ");
					choice = scanner.nextInt();
					billingStaff.runBilling(conn, choice);
					break;
				case 4: //Maintenance staff view
					System.out.println("1. Enter Service Record \n2. Update Service Record \n");
					System.out.println("Enter your option: ");
					choice = scanner.nextInt();
					maintenance.runMaintenance(conn, choice);
					break;
				default:
					System.out.println("Invalid Option entered. Please try again.");
				} 
				System.out.println("Do you want to continue? Yes or No");
				cont = scanner.next().charAt(0);
			}while(cont == 'y' || cont == 'Y');

		}
		catch (SQLException ex)
		{
			System.out.println("Invalid Input");
		}
		
		finally 
		{
            closeconnection(conn);
		}

	}
		
}

