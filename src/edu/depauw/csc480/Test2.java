package edu.depauw.csc480;

import java.util.Collection;

import edu.depauw.csc480.dao.DatabaseManager;
import edu.depauw.csc480.model.Address;
import edu.depauw.csc480.model.Alumni;
import edu.depauw.csc480.model.Specialties;
import edu.depauw.csc480.model.Company;

/**
 * @author ducnguyen
 */
public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
		
		Specialties compsci = dbm.findSpecsByName("Computer Science");
		
		// Now retrieve a table of MathCS faculty and their courses;
		// each course also lists the head of the department offering the course
		Collection<Company> comps = compsci.getCompany();
		for (Company comp : comps) {
			System.out.println(comp);
		}
		
		dbm.commit();
		
		dbm.close();
		
		System.out.println("Done");
	}

}
