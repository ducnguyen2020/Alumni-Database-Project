package edu.depauw.csc480;

import java.util.Collection;

import edu.depauw.csc480.dao.DatabaseManager;
import edu.depauw.csc480.model.Address;
import edu.depauw.csc480.model.Alumni;
import edu.depauw.csc480.model.Specialties;
import edu.depauw.csc480.model.Company;

/**
 * 
 * 
 * @author ducnguyen
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
		
		dbm.clearTables();
		
		
		
		//SPECIALTIES:
		Specialties compsci = dbm.insertSpecialties(1, "Computer Science" , "Software Development");
		Specialties econ = dbm.insertSpecialties(2, "Economics" , "Worlds Macroeconomics");
		Specialties japn = dbm.insertSpecialties(3, "Japanese" , "Learn Japanese with Kanji");
		Specialties chin = dbm.insertSpecialties(4, "Chinese" , "Learn Chinese at Depauw");
		Specialties psy = dbm.insertSpecialties(5, "Psychology" , "Learn about neuroscience and behaviors");
		
		//Address:
		Address add1 = dbm.insertAddress(10, "408 S Locust St", 46135, "U.S.");
		Address add2 = dbm.insertAddress(20, "15 Ngoc Khanh", 100000, "Vietnam");
		Address add3 = dbm.insertAddress(30, "24 Chongquing", 23232, "China");
		Address add4 = dbm.insertAddress(40, "Shanghai", 45454, "China");
		Address add5 = dbm.insertAddress(50, "NewYork St", 32494, "U.S.");

		
		//Company 
		Company cmc = dbm.insertCompany(100, "CMC Corporation" , "cmc.com.vn" , "123123123", 10000 , compsci , add2);
		Company viettel = dbm.insertCompany(200, "Viettel Corporation" , "viettel.com.vn" , "92929", 20000 , compsci , add3);
		Company fpt = dbm.insertCompany(300, "FPT Psy" , "fpt.com.us" , "23834", 30000 , psy , add1);
		Company trungquoc = dbm.insertCompany(100, "Chinese company" , "Taipeicompany" , "0000", 40000 , chin , add4);
		Company nhat = dbm.insertCompany(500, "Cong ty nhat" , "Japan.jpn" , "9999", 50000 , japn , add5);
				
		dbm.insertAlumni(1111, "Kyle" , "kyle@edu" , 2020 , "0909009" , compsci, cmc, add1);
		dbm.insertAlumni(2222, "Loc" , "Loc@edu" , 1998 , "2323" , psy, cmc, add2);
		dbm.insertAlumni(3333, "Juan" , "Juan@edu" , 2000 , "343434" , japn, cmc, add3);
		dbm.insertAlumni(4444, "Shuto" , "Shut@edu" , 900 , "5555" , chin, cmc, add4);
		dbm.insertAlumni(5555, "Tam" , "Tam@edu" , 000 , "666" , econ, cmc, add5);
				
		
		dbm.commit();
		
		
		Collection<Company> comps = compsci.getCompany();
		for (Company comp : comps) {
			System.out.println(comp);
		}
		
		
		dbm.commit();
		
		dbm.close();
		
		System.out.println("Done");
	}

}
