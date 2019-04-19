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

/**
 * Data Access Object for the Department table.
 * Encapsulates all of the relevant SQL commands.
 * Based on Sciore, Section 9.1.
 * 
 * @author ducnguyen
 */
public class AddressDAO {
	private Connection conn;
	private DatabaseManager dbm;

	public AddressDAO(Connection conn, DatabaseManager dbm) {
		this.conn = conn;
		this.dbm = dbm;
	}

	/**
	 * Create the Address table via SQL
	 * 
	 */
	static void create(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "create table Address("
				+ "Aid int not null, "
				+ "Addtext varchar(255) not null, "
				+ "Zipcode int not null, "
				+ "Country varchar(50) not null, "
				+ "primary key(Aid))";
		stmt.executeUpdate(s);
	}

	
	static void addConstraints(Connection conn) throws SQLException {
		
	}

	/**
	 * Retrieve a Address object given its key.
	 * 
	 */
	public Address find(int Aid) {
		try {
			String qry = "select Addtext, Zipcode, Country from Address where Aid = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, Aid);
			ResultSet rs = pstmt.executeQuery();

			// return null if department doesn't exist
			if (!rs.next())
				return null;

			String addtext = rs.getString("Addtext");
			int zipcode = rs.getInt("Zipcode");
			String country = rs.getString("Country");
			rs.close();

			Address add = new Address(this, Aid, addtext, zipcode, country);

			return add;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding address", e);
		}
	}

	/**
	 * Retrieve a Address by Zipcode
	 */
	public Address findByZipcode(int zipcode) {
		try {
			String qry = "select AId, Addtext, Country from Address where Zipcode = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, zipcode);
			ResultSet rs = pstmt.executeQuery();

			// return null if department doesn't exist
			if (!rs.next())
				return null;

			int aid = rs.getInt("Aid");
			String addtext = rs.getString("Addtext");
			String country = rs.getString("Country");
			rs.close();

			Address add = new Address(this, aid, addtext, zipcode, country);

			return add;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding Address by zipcode", e);
		}
	}

	/**
	 * Add a new Address
	 */
	public Address insert(int aid, String addtext, int zipcode, String country) {
		try {
	
			if (find(aid) != null)
				return null;

			String cmd = "insert into Address(Aid, Addtext, Zipcode, Country) "
					+ "values(?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, aid);
			pstmt.setString(2, addtext);
			pstmt.setInt(3, zipcode);
			pstmt.setString(4, country);
			pstmt.executeUpdate();

			Address add = new Address(this, aid, addtext, zipcode, country);

			return add;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new Address", e);
		}
	}


	/**
	 * Retrieve a Collection of all Alumni in the given address. 
	 */
	public Collection<Alumni> getAlumni(int aid) {
		try {
			Collection<Alumni> alumni = new ArrayList<Alumni>();
			String qry = "select SSN from ALUMNI where Aid = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, aid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int ssn = rs.getInt("SSN");
				alumni.add(dbm.findAlumni(ssn));
			}
			rs.close();
			return alumni;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error getting alumni", e);
		}
	}

	/**
	 * Retrieve a Collection of all Company offered by the given address.
	 * 
	 */
	public Collection<Company> getCompany(int aid) {
		try {
			Collection<Company> companies = new ArrayList<Company>();
			String qry = "select Cid from Company where Aid = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, aid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int cid = rs.getInt("Cid");
				companies.add(dbm.findCompany(cid));
			}
			rs.close();
			return companies;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error getting Company", e);
		}
	}

	
	/**
	 * Clear all data from the Department table.
	 * 
	 * @throws SQLException
	 */
	void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from Address";
		stmt.executeUpdate(s);
	}
}
