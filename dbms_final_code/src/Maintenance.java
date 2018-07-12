import java.sql.*;
import java.util.*;

/**
 * This class consists of the Maintenance staff functionalities. It allows us to choose all the functions
 * that are under the control of the maintenance staff
 * @author Team-D for CSC540 - Spring2018
 *
 */
public class Maintenance {
	/**
	 * Method to choose the room service staff and catering staff
	 * @param conn
	 * @param choice
	 * @throws Exception
	 */
	public void runMaintenance(Connection conn, int choice) throws Exception {
		Scanner s = new Scanner(System.in);
		Buys buys = new Buys();
		Services services = new Services();
		Serves serves = new Serves();
		Staff staff = new Staff();
		switch(choice) {
			case 1: services.printList(conn);
					System.out.println("Enter Checkin ID: ");    //DONE 
					int checkinID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Service ID: ");
					int serviceID = Integer.parseInt(s.nextLine());
					if(serviceID == 1) {
						if(staff.chooseFreeStaff(conn, "room service")) {   // Selection of staff who are available
							System.out.println("Choose from above list of room service staff: ");
							int staff_id = Integer.parseInt(s.nextLine());
							String staff_name = staff.getStaffName(conn, staff_id);
							staff.assignStaff(conn, staff_id);
							serves.insertServes(conn, staff_id, staff_name, checkinID);		//To insert into serves table with the information of the staff name
						}
						else {
							System.out.println("Sorry no free room service staff");
							break;
						}
					}
					if(serviceID == 2) {
						if(staff.chooseFreeStaff(conn, "catering staff")) {
							System.out.println("Choose from above list of Catering staff: ");
							int staff_id = Integer.parseInt(s.nextLine());
							String staff_name = staff.getStaffName(conn, staff_id);
							staff.assignStaff(conn, staff_id);
							serves.insertServes(conn, staff_id, staff_name, checkinID);		//To insert into serves table with the information of the staff name
						}
						else {
							System.out.println("Sorry no free Catering staff");
							break;
						}
					}
					buys.enterServiceRecords(conn, checkinID, serviceID);
					break;
		
			case 2: System.out.println("Enter Checkin ID: ");     //DONE
					checkinID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Service ID: ");
					serviceID = Integer.parseInt(s.nextLine());
					buys.updateServiceRecords(conn, checkinID, serviceID);    // Update the service records with serviceId and checkinId
					break;
		}
	}
}
