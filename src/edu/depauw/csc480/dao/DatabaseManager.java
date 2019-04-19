package edu.depauw.csc480.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.derby.jdbc.EmbeddedDriver;

import edu.depauw.csc480.model.Address;
import edu.depauw.csc480.model.Alumni;
import edu.depauw.csc480.model.Specialties;
import edu.depauw.csc480.model.Company;

/**
 * 
 * Based on Sciore, Section 9.1.
 * 
 * @author ducnguyen
 */
public class DatabaseManager {
	private Driver driver;
	private Connection conn;
	private AddressDAO addressDAO;
	private AlumniDAO alumniDAO;
	private CompanyDAO companyDAO;
	private SpecialtiesDAO specialtiesDAO;
	

	private final String url = "jdbc:derby:AlumniDB";

	public DatabaseManager() {
		driver = new EmbeddedDriver();
		
		Properties prop = new Properties();
		prop.put("create", "false");
		
		// try to connect to an existing database
		try {
			conn = driver.connect(url, prop);
			conn.setAutoCommit(false);
		}
		catch(SQLException e) {
			// database doesn't exist, so try creating it
			try {
				prop.put("create", "true");
				conn = driver.connect(url, prop);
				conn.setAutoCommit(false);
				create(conn);
			}
			catch (SQLException e2) {
				throw new RuntimeException("cannot connect to database", e2);
			}
		}
		
		addressDAO = new AddressDAO(conn, this);
		alumniDAO = new AlumniDAO(conn, this);
		companyDAO = new CompanyDAO(conn, this);
		specialtiesDAO = new SpecialtiesDAO(conn, this);
	}

	/**
	 * Initialize the tables and their constraints in a newly created database
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	private void create(Connection conn) throws SQLException {
		SpecialtiesDAO.create(conn);
		CompanyDAO.create(conn);
		AddressDAO.create(conn);
		AlumniDAO.create(conn);
		SpecialtiesDAO.addConstraints(conn);
		CompanyDAO.addConstraints(conn);
		AddressDAO.addConstraints(conn);
		AlumniDAO.addConstraints(conn);
		conn.commit();
	}

	//***************************************************************
	// Data retrieval functions -- find a model object given its key
	
	public Address findByZipcode(int zipcode) {
		return addressDAO.findByZipcode(zipcode);
	}
	
	public Specialties findSpecsByName(String name) {
		return specialtiesDAO.findByName(name);
	}
	
	public Alumni findAlumni(int ssn) {
		return alumniDAO.find(ssn);
	}

	public Company findCompany(int cid) {
		return companyDAO.find(cid);
	}
	
	public Specialties findSpecialties(int sid) {
		return specialtiesDAO.find(sid);
	}
	
	public Address findAddress(int aid) {
		return addressDAO.find(aid);
	}

	//***************************************************************
	// Data insertion functions -- create new model object from attributes

	public Alumni insertAlumni(int ssn, String fname, String email, int gradyear, String phone, Specialties major, Company company, Address address) {
		return alumniDAO.insert(ssn, fname, email, gradyear, phone, major, company, address);
	}
	
	public Company insertCompany(int cid, String name, String email, String phone, int salary, Specialties industry, Address address) {
		return companyDAO.insert(cid, name, email, phone, salary, industry, address);
	}

	public Address insertAddress(int aid, String addtext, int zipcode, String country) {
		return addressDAO.insert(aid, addtext, zipcode, country);
	}
	
	public Specialties insertSpecialties(int sid, String name, String definition) {
		return specialtiesDAO.insert(sid, name, definition);
	}
	//***************************************************************
	// Utility functions
	
	/**
	 * Commit changes since last call to commit
	 */
	public void commit() {
		try {
			conn.commit();
		}
		catch(SQLException e) {
			throw new RuntimeException("cannot commit database", e);
		}
	}

	/**
	 * Abort changes since last call to commit, then close connection
	 */
	public void cleanup() {
		try {
			conn.rollback();
			conn.close();
		}
		catch(SQLException e) {
			System.out.println("fatal error: cannot cleanup connection");
		}
	}

	/**
	 * Close connection and shutdown database
	 */
	public void close() {
		try {
			conn.close();
		}
		catch(SQLException e) {
			throw new RuntimeException("cannot close database connection", e);
		}
		
		// Now shutdown the embedded database system -- this is Derby-specific
		try {
			Properties prop = new Properties();
			prop.put("shutdown", "true");
			conn = driver.connect(url, prop);
		} catch (SQLException e) {
			// This is supposed to throw an exception...
			System.out.println("Derby has shut down successfully");
		}
	}

	/**
	 * Clear out all data from database (but leave empty tables)
	 */
	public void clearTables() {
		try {
			// This is not as straightforward as it may seem, because
			// of the cyclic foreign keys -- I had to play with
			// "on delete set null" and "on delete cascade" for a bit
			addressDAO.clear();
			specialtiesDAO.clear();
			companyDAO.clear();
			alumniDAO.clear();
		} catch (SQLException e) {
			throw new RuntimeException("cannot clear tables", e);
		}
	}
}
