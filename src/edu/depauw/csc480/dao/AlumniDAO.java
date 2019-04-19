package edu.depauw.csc480.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import edu.depauw.csc480.model.Alumni;
import edu.depauw.csc480.model.Specialties;
import edu.depauw.csc480.model.Company;
import edu.depauw.csc480.model.Address;

public class AlumniDAO {
	private Connection conn;
	private DatabaseManager dbm;

	public AlumniDAO(Connection conn, DatabaseManager dbm) {
		this.conn = conn;
		this.dbm = dbm;
	}

	/**
	 * Create the Alumni table via SQL
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void create(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "create table Alumni("
				+ "SSN int not null, "
				+ "FName varchar(50) not null, "
				+ "Email varchar(255), "
				+ "Gradyear int not null, "
				+ "Phone varchar (15), "
				+ "Major int, "
				+ "Company int, "
				+ "Address int, "
				+ "primary key(SSN))";
		stmt.executeUpdate(s);
	}

	/**
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void addConstraints(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "alter table Alumni add constraint fk_alumspecs "
				+ "foreign key(Major) references SPECIALTIES on delete set null";
		stmt.executeUpdate(s);
		s = "alter table ALUMNI add constraint fk_alumadd "
				+ "foreign key(Address) references ADDRESS on delete set null";
		stmt.executeUpdate(s);
		s = "alter table ALUMNI add constraint fk_alumcomp "
				+ "foreign key(Company) references COMPANY on delete set null";
		stmt.executeUpdate(s);
	}

	/**
	 * Retrieve a ALUMNI object given its key.
	 * 
	 * @param ssn
	 * @return the ALUMNI object, or null if not found
	 */
	public Alumni find(int ssn) {
		try {
			String qry = "select FName, Email, Gradyear, Phone, Major, Company, Address from Alumni where SSN = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, ssn);
			ResultSet rs = pstmt.executeQuery();

			// return null if alumni doesn't exist
			if (!rs.next())
				return null;

			String fname = rs.getString("FName");
			String email = rs.getString("Email");
			int gradyear = rs.getInt("Gradyear");
			String phone = rs.getString("Phone");
			int majorid = rs.getInt("Major");
			int companyid = rs.getInt("Company");
			int addressid = rs.getInt("Address");
			
			rs.close();

			Specialties major = dbm.findSpecialties(majorid);
			Company company = dbm.findCompany(companyid);
			Address address = dbm.findAddress(addressid);

			Alumni alum = new Alumni(this, ssn, fname, email, gradyear, phone, major, company, address);
			return alum;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding Alumni", e);
		}
	}

	/**
	 * Retrieve a Alumni object by name. Similar to find(ssn), except it
	 * searches by name.
	 * 
	 */
	public Alumni findByName(String fname) {
		try {
			String qry = "select SSN, Email, Gradyear, Phone, Major, Company, Address from Alumni where FName = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setString(1, fname);
			ResultSet rs = pstmt.executeQuery();

			// return null if faculty member doesn't exist
			if (!rs.next())
				return null;

			int ssn = rs.getInt("SSN");
			String email = rs.getString("Email");
			int gradyear = rs.getInt("Gradyear");
			String phone = rs.getString("Phone");
			int majorid = rs.getInt("Major");
			int companyid = rs.getInt("Company");
			int addressid = rs.getInt("Address");
			rs.close();

			Specialties major = dbm.findSpecialties(majorid);
			Company company = dbm.findCompany(companyid);
			Address address = dbm.findAddress(addressid);

			Alumni alum = new Alumni(this, ssn, fname, email, gradyear, phone, major, company, address);
			return alum;
			
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding department by name", e);
		}
	}

	/**
	 * Add a new Alumni member with the given attributes.
	 */
	public Alumni insert(int ssn, String fname, String email, int gradyear, String phone, Specialties major, Company company, Address address) {
		try {
			// make sure that the ssn is currently unused
			if (find(ssn) != null)
				return null;

			String cmd = "insert into Alumni(SSN, FName, Email, Gradyear, Phone, Major, Company, Address) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, ssn);
			pstmt.setString(2, fname);
			pstmt.setString(3, email);
			pstmt.setInt(4, gradyear);
			pstmt.setString(5, phone);
			pstmt.setInt(6, major.getSid());
			pstmt.setInt(7, company.getCid());
			pstmt.setInt(8, address.getAid());
			pstmt.executeUpdate();

			Alumni alum = new Alumni(this, ssn, fname, email, gradyear, phone, major, company, address);

			return alum;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new Alumni", e);
		}
	}

	
	public void changeEmail(int ssn, String email) {
		try {
			String cmd = "update Alumni set Email = ? where SSN = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, email);
			pstmt.setInt(2, ssn);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing email", e);
		}
	}


	public void changePhone(int ssn, String phone) {
		try {
			String cmd = "update Alumni set Phone = ? where SSN = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, phone);
			pstmt.setInt(2, ssn);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing phone", e);
		}
	}

	public void changeCompany(int ssn, Company company) {
		try {
			String cmd = "update Alumni set Company = ? where SSN = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, company.getCid());
			pstmt.setInt(2, ssn);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing phone", e);
		}
	}
	
	public void changeAddress(int ssn, Address address) {
		try {
			String cmd = "update Alumni set Address = ? where SSN = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, address.getAid());
			pstmt.setInt(2, ssn);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing phone", e);
		}
	}
	/**
	 * Clear all data from the Faculty table.
	 * 
	 * @throws SQLException
	 */
	void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from Alumni";
		stmt.executeUpdate(s);
	}
}
