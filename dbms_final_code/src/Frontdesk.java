import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * This class consists of the Front desk Staff functionalities. It allows us to choose all the functions
 * that are under the control of the Front desk Staff
 * @author Team-D for CSC540 - Spring2018
 *
 */
public class Frontdesk {
	/**
	 * All FrontDesk function calls are made here
	 * @param conn
	 * @param choice
	 * @throws SQLException
	 */
	public void runFrontdesk(Connection conn, int choice) throws SQLException {
		Customer customer = new Customer();
		CheckinInfo checkininfo = new CheckinInfo();
		Room room = new Room();
		Billing billing = new Billing();
		Staff staff = new Staff();
		DedicatedTo dedicated = new DedicatedTo();
		Services services = new Services();
		Serves serves = new Serves();
		Buys buys = new Buys();
		Scanner s = new Scanner(System.in);
		switch(choice) {
			case 1: System.out.println("Enter Customer ID: ");     //Create New Customer
					int cID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Customer Name: ");
					String cName = s.nextLine();
					System.out.println("Enter Customer Date of birth: ");
					String cDOB = s.nextLine();
					System.out.println("Enter Customer Phone Number: ");
					String cPhoneNum = s.nextLine();
					System.out.println("Enter Customer Email-ID: ");
					String cemailID = s.nextLine();
					customer.insertCustomer(conn, cID, cName, cDOB, cPhoneNum, cemailID);
					break;
				
			case 2: System.out.println("Enter Customer ID: ");		// Create new Checkin
					cID = Integer.parseInt(s.nextLine());
					System.out.println("Enter In Time: ");   
					String in_time = s.nextLine();
					System.out.println("Enter Out Time: ");   
					String out_time = s.nextLine();
					System.out.println("Enter Number of Guests: ");
					int num_guest = Integer.parseInt(s.nextLine());
					System.out.println("Enter Start Date: ");
					String startDate = s.nextLine();
					System.out.println("Enter End Date: ");
					String endDate = s.nextLine();
					System.out.println("Is it a presenditial suite checkin? Yes - Enter 1 or No - Enter 0: ");
					int free_serv = Integer.parseInt(s.nextLine());
					System.out.println("Do you want any services from hotel? Yes - Enter 1 or No - Enter 0:");
					int service_question = Integer.parseInt(s.nextLine()); 
					System.out.println("Enter Services offered: ");
					String serv_offered = s.nextLine();
					String[] services_offered = serv_offered.split(",");
					// int len = services_offered.length();
					System.out.println("Enter Hotel ID: ");     
					int hID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Room Category: ");
					String room_category = s.nextLine();
					int flag = 0;
					/* The below try catch block contains a transaction. The transaction has been used here due to the fact that the following 4 operations should be atomic:
					   a) Assigning a room
					   b) Entering the checkin information into the checkin table
					   c) If presidential customer: i) Then assign catering staff to customer
					   								ii) Assign room service staff to customer
					   By setting the autocommit to false before executing the 4 operations, we ensure that we commit all the four transaction or none of them. The reason we
					   need to used a transaction for this is to avoid scenarios like the following :
					   Once a room has been assigned, we need to ensure that the checkin information must be entered into the checkinfo table. If this is not done then there would
					   be inconsistency as the room would be assigned and unavailable, but would not be mentioned in the checkinInfo table.	 Furthermore, if we have completed both the 
					   room update as well as the checkinfo update, but not the staff updates (where in the dedicated staff are being assigned to the presedential customer), that would be a
					   problem as well, since when a manager wants to query the list of staff who has served that particular checkin, he would get the wrong data as the dedicated staff have not 
					   been updated in the table due to a system crash. Hence we need to ensure that the four operations or updates need to happen atomically, or not happen at all. This is solved	
					   by using transactions				
					*/
					try {
						boolean available = room.checkRoomAvailability(conn, hID, room_category, startDate, endDate); 
						if(available) {
							System.out.println("Choose from above list of room numbers: ");
							int room_num = Integer.parseInt(s.nextLine());
							if(num_guest > room.getMaxOccupancy(conn, room_num, hID)) {
								System.out.println("Sorry max occupancy of room is lesser than what you requested");
								break;
							}
							conn.setAutoCommit(false);
							room.assignRoom(conn, room_num, hID);
							int checkin_id = checkininfo.enterCheckinInfo(conn,in_time,out_time,num_guest,startDate,endDate,free_serv, serv_offered, room_num, hID, cID );
							if(service_question == 1 && checkin_id != 0) {
								int j = 0;
								for(String se : services_offered) {
									int service_id = services.findServiceID(conn, se);
									if(service_id == 1) {
										if(staff.chooseFreeStaff(conn, "room service")) {
											System.out.println("Choose from above list of room service staff: ");
											int staff_id = Integer.parseInt(s.nextLine());
											String staff_name = staff.getStaffName(conn, staff_id);
											staff.assignStaff(conn, staff_id);
											serves.insertServes(conn, staff_id, staff_name, checkin_id);		
										}
										else {
											System.out.println("Sorry no free room service staff");
											continue;
										}
									}
									if(service_id == 2) {
										if(staff.chooseFreeStaff(conn, "catering staff")) {
											System.out.println("Choose from above list of Catering staff: ");
											int staff_id = Integer.parseInt(s.nextLine());
											String staff_name = staff.getStaffName(conn, staff_id);
											staff.assignStaff(conn, staff_id);
											serves.insertServes(conn, staff_id, staff_name, checkin_id);		
										}
										else {
											System.out.println("Sorry no free catering staff");
											continue;
										}
									}
									buys.enterServiceRecords(conn, checkin_id, service_id);
								}
							}
							if(free_serv == 1 && checkin_id != 0) {
								System.out.println("Assign complimentary staff to the presenditial people: \n");
								if(staff.chooseFreeStaff(conn, "room service")) {
									System.out.println("Choose from above list of room service staff: ");
									int staff_id = Integer.parseInt(s.nextLine());
									String staff_name = staff.getStaffName(conn, staff_id);
									staff.assignStaff(conn, staff_id);
									dedicated.insertDedicatedTo(conn, staff_id, staff_name, checkin_id);		
								}
								else {
									System.out.println("Sorry dude No Room service staff free");
								}
								if(staff.chooseFreeStaff(conn, "Catering Staff")) {
									System.out.println("Choose from above list of catering staff: ");
									int staff_id = Integer.parseInt(s.nextLine());
									String staff_name = staff.getStaffName(conn, staff_id);
									staff.assignStaff(conn, staff_id);
									dedicated.insertDedicatedTo(conn, staff_id, staff_name, checkin_id);	
								}
								else {
									System.out.println("Sorry dude No Catering staff free");	
								}
							}
						}
						else {
							System.out.println("Sorry dude no room for you!");
						}
						flag = 1;
					}
					catch (Exception e) {
						System.out.println(e);
					}
					if(flag == 1){
						conn.commit();
					}
					else {
						conn.rollback();
						System.out.println("FAILURE");
					}  
					break;
					
			case 3: System.out.println("Enter Customer ID: ");     //Update Customer
					cID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Customer Name: ");
					cName = s.nextLine();
					System.out.println("Enter Customer Date of birth: ");
					cDOB = s.nextLine();
					System.out.println("Enter Customer Phone Number: ");
					cPhoneNum = s.nextLine();
					System.out.println("Enter Customer Email-ID: ");
					cemailID = s.nextLine();
					customer.updateCustomer(conn, cID, cName, cDOB, cPhoneNum, cemailID);
					break;
					
			case 4: System.out.println("Enter Customer ID: "); //Delete Customer Info
					cID = Integer.parseInt(s.nextLine());
					customer.deleteCustomer(conn, cID);
					break;
					
			case 5: System.out.println("Enter Hotel ID: ");     //Releasing the room at checkout time
					hID = Integer.parseInt(s.nextLine());
					System.out.println("Enter Room Number: ");
					int room_num = Integer.parseInt(s.nextLine());
					room.releaseRoom(conn, room_num, hID);
					break;
			case 6: System.out.println("Enter Checkin ID: ");     //Total amount owed after discount by a customer
					int checkinID = Integer.parseInt(s.nextLine());
					billing.totalAmountOwed(conn, checkinID);
					break;
			case 7: System.out.println("Enter Checkin ID: ");     //Itemized Receipt of the customer
					checkinID = Integer.parseInt(s.nextLine());
					billing.itemizedReceipt(conn, checkinID);
					break; 					
			case 8: System.out.println("Enter the Checkin ID: ");  
					checkinID = Integer.parseInt(s.nextLine());
					ArrayList<Integer> ids = new ArrayList<Integer>();
					ids = staff.getStaffServed(conn, checkinID);
					for(int i = 0; i < ids.size(); i++) {
						staff.releaseStaff(conn, ids.get(i));
					}
					break;
			case 9: System.out.println("Enter the Checkin ID: ");
					checkinID = Integer.parseInt(s.nextLine());
					ArrayList<Integer> room_info = checkininfo.findRoomNumber(conn, checkinID);
					System.out.println("Now room is being released ....");
					room.releaseRoom(conn, room_info.get(0), room_info.get(1));  //To release rooms
					ArrayList<Integer> ids1 = new ArrayList<Integer>();
					System.out.println("Now staff is being released .... ");
					ids1 = staff.getStaffServed(conn, checkinID);
					for(int i = 0; i < ids1.size(); i++) {
						staff.releaseStaff(conn, ids1.get(i));
					}
					System.out.println("Now bill is being generated .... ");
					String category = room.findRoomCategory(conn, room_info.get(0), room_info.get(1));   // To find the room category for bill generation purpose
					billing.generateBill(conn, checkinID, category);
					break;
		}		
	}
}

