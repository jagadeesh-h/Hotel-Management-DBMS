import java.sql.*;
import java.util.Scanner;

/**
 * This class consists of the Managers functionalities. It allows us to choose all the functions
 * that are under the control of the manager
 * @author Team-D for CSC540 - Spring2018
 *
 */

public class Manager{
	/**
	 * Function calls for Manager operations
	 * @param conn
	 * @param choice
	 */
	public void runManager(Connection conn, int choice) {
		Hotel hotel = new Hotel();
		Staff staff = new Staff();
		Room room = new Room();
		Billing billing = new Billing();
		Serves serves = new Serves();
		Scanner s = new Scanner(System.in);	
		switch(choice) {
			case 1: System.out.println("Enter Hotel ID: ");     //Create New hotel 
					int hID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Hotel Name: ");
					String hName = s.nextLine();
					System.out.println("Enter Hotel Address: ");
					String hAddress = s.nextLine();
					System.out.println("Enter Hotel Phone Number: ");
					String hPhoneNum = s.nextLine();
					System.out.println("Enter Hotel Manager ID: ");
					int hManagerID = Integer.parseInt(s.nextLine());
					hotel.enterHotel(conn, hID, hName, hAddress, hPhoneNum, hManagerID);
					break;
				
			case 2: System.out.println("Enter Staff ID: ");   // Create new staff 
					int sID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Staff Department: ");
					String sDept = s.nextLine();
					System.out.println("Enter Staff Title: ");
					String sTitle = s.nextLine();
					System.out.println("Enter Staff Age: ");
					int sAge = Integer.parseInt(s.nextLine());
					System.out.println("Enter Staff Name: ");
					String sName = s.nextLine();
					System.out.println("Enter Hotel ID: ");
					int shID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Staff Phone Number: ");
					String sPhNum = s.nextLine();
					System.out.println("Enter Staff Address: ");
					String sAddr = s.nextLine();
					staff.enterStaff(conn, sID, sDept, sTitle, sAge, sName, shID, sPhNum, sAddr, 1);
					break;
			case 3: System.out.println("Enter Hotel ID: ");   // Update hotel   
					hID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Hotel Name: ");
					hName = s.nextLine();
					System.out.println("Enter Hotel Address: ");
					String haddr = s.nextLine();
					System.out.println("Enter Hotel Phone: ");
					String hPhone = s.nextLine();
					System.out.println("Enter Hotel Manager ID: ");
					int hmID = Integer.parseInt(s.nextLine());
					hotel.updateHotel(conn, hID, hName, haddr, hPhone, hmID);
					break;
			case 4: System.out.println("Enter Staff ID: ");   // Update staff   
					sID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Staff Department: ");
					sDept = s.nextLine();
					System.out.println("Enter Staff Title: ");
					sTitle = s.nextLine();
					System.out.println("Enter Staff Age: ");
					sAge = Integer.parseInt(s.nextLine());
					System.out.println("Enter Staff Name: ");
					sName = s.nextLine();
					System.out.println("Enter Hotel ID: ");
					shID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Staff Phone Number: ");
					sPhNum = s.nextLine();
					System.out.println("Enter Staff Address: ");
					sAddr = s.nextLine();
					staff.updateStaff(conn, sID, sDept, sTitle, sAge, sName, shID, sPhNum, sAddr, 1);
					break;
			case 5: System.out.println("Enter Room Number: ");   // Update Roominformation  
					int roomNum = Integer.parseInt(s.nextLine());
					System.out.println("Enter Hotel ID: ");
					hID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Room Category: ");
					String roomCategory = s.nextLine();
					System.out.println("Enter Max Occupancy: ");
					int max_occu = Integer.parseInt(s.nextLine());
					System.out.println("Enter Nightly rate: ");
					int rate = Integer.parseInt(s.nextLine());
					room.updateRoom(conn, roomNum, hID, roomCategory, max_occu, rate, 1);
					break; 
			case 6: System.out.println("Enter Hotel ID: ");  //Delete Hotel    
					hID = Integer.parseInt(s.nextLine());
					hotel.deleteHotel(conn, hID);
					break;
			
			case 7: System.out.println("Enter Staff ID: "); //Delete Staff     
					sID = Integer.parseInt(s.nextLine());
					staff.deleteStaff(conn, sID);
					break;
			
			case 8: System.out.println("Enter Room Number: "); //Delete Room Info  
					roomNum = Integer.parseInt(s.nextLine());
					System.out.println("Enter hotel ID: ");
					hID = Integer.parseInt(s.nextLine());
					room.deleteRoom(conn, roomNum, hID);
					break;
			case 9: System.out.println("Enter Start Date in YYYY/MM/DD: ");//Generate Revenue
					String startDate = s.nextLine();
					System.out.println("Enter End Date in YYYY/MM/DD: ");
					String endDate = s.nextLine();
					billing.generateRevenue(conn, startDate, endDate);
					break;
			case 10: System.out.println("Enter Choice: ");                             
					 System.out.println("1. By Hotel ID \n 2. By Room Category \n 3. By Date Range \n 4. By City");
					 int c = Integer.parseInt(s.nextLine());
					 switch(c) {
					 	case 1: room.reportOccupancyByHotel(conn);
					 			break;
					 	case 2: room.reportOccupancyByRoomCategory(conn);
					 			break;
					 	case 3: System.out.println("Enter Start Date in YYYY/MM/DD: ");
					 			startDate = s.nextLine();
					 			System.out.println("Enter End Date in YYYY/MM/DD: ");
					 			endDate = s.nextLine();
					 			room.reportOccupancyByDate(conn, startDate, endDate);
					 			break;
					 	case 4: room.reportOccupancyByCity(conn);
					 			break;
					 }
					 break;
					 
			case 11: serves.staffServed(conn);  //Staff Served 
					 break;
			case 12: staff.reportStaffByGroup(conn);	 //Report group of Staff By title
					 break;
			case 13: System.out.println("Enter Start Date: ");
				 	 startDate = s.nextLine();
					 System.out.println("Enter End Date: ");
					 endDate = s.nextLine();
			         hotel.reportTotalOccupancy(conn, startDate, endDate);    //Total occupancy and percentage occupied
			         break;
			case 14: System.out.println("Enter Room Number: ");  //Create a room 
					 roomNum = Integer.parseInt(s.nextLine());
					 System.out.println("Enter Hotel ID: ");
				 	 hID = Integer.parseInt(s.nextLine());
					 System.out.println("Enter Room Category: ");
					 roomCategory = s.nextLine();
					 System.out.println("Enter Max Occupancy: ");
					 max_occu = Integer.parseInt(s.nextLine());
					 System.out.println("Enter Nightly rate: ");
					 rate = Integer.parseInt(s.nextLine());
					 room.enterRoom(conn, roomNum, hID, roomCategory, max_occu, rate, 1);
					 break;
			case 15: hotel.reportPercentageOfRooms(conn);
					 break;
		}		
	}
}

