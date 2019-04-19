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
public class SpecialtiesDAO {
	private Connection conn;
	private DatabaseManager dbm;

	public SpecialtiesDAO(Connection conn, DatabaseManager dbm) {
		this.conn = conn;
		this.dbm = dbm;
	}

	/**
	 * Create the Specialties table via SQL
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void create(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "create table Specialties("
				+ "Sid int not null, "
				+ "Name varchar(100) not null, "
				+ "Definition varchar(255) not null, "
				+ "primary key(Sid))";
		stmt.executeUpdate(s);
	}

	/**
	 * Modify the Specialties table to add foreign key constraints (needs to
	 * happen after the other tables have been created)
	 */
	static void addConstraints(Connection conn) throws SQLException {
		
	}

	/**
	 * Retrieve a Specialties object given its key.
	 * 
	 */
	public Specialties find(int Sid) {
		try {
			String qry = "select Name, Definition from Specialties where Sid = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, Sid);
			ResultSet rs = pstmt.executeQuery();

			// return null if department doesn't exist
			if (!rs.next())
				return null;

			String name = rs.getString("Name");
			String definition = rs.getString("Definition");
			rs.close();

			Specialties spec = new Specialties(this, Sid, name, definition);

			return spec;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding specialties", e);
		}
	}

	/**
	 * Retrieve a Specialties object by name. 
	 * 
	 */
	public Specialties findByName(String name) {
		try {
			String qry = "select Sid, Definition from Specialties where Name = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();

			// return null if department doesn't exist
			if (!rs.next())
				return null;

			int sid = rs.getInt("Sid");
			String definition = rs.getString("Definition");
			rs.close();

			Specialties spec = new Specialties(this, sid, name, definition);

			return spec;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding specialties by name", e);
		}
	}

	/**
	 * Add a new Specialties with the given attributes.
	 */
	public Specialties insert(int sid, String name, String definition) {
		try {
			// make sure that the deptid is currently unused
			if (find(sid) != null)
				return null;

			String cmd = "insert into Specialties(Sid, Name, Definition) "
					+ "values(?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, sid);
			pstmt.setString(2, name);
			pstmt.setString(3, definition);
			pstmt.executeUpdate();

			Specialties spec = new Specialties(this, sid, name, definition);

			return spec;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new specialties", e);
		}
	}


	public void changeDefinition(int sid, String newDef) {
		try {
			String cmd = "update Specialties set Definition = ? where Sid = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			
			pstmt.setString(1, newDef);
			pstmt.setInt(2, sid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing definition", e);
		}
	}


	public Collection<Alumni> getAlumni(int sid) {
		try {
			Collection<Alumni> alumni = new ArrayList<Alumni>();
			String qry = "select SSN from ALUMNI where Sid = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, sid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int ssn = rs.getInt("SSN");
				alumni.add(dbm.findAlumni(ssn));
			}
			rs.close();
			return alumni;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error getting Alumni", e);
		}
	}

	/**
	 * Retrieve a Collection of all Company in the specialty.
	 */
	public Collection<Company> getCompany(int sid) {
		try {
			Collection<Company> companies = new ArrayList<Company>();
			String qry = "select Cid from Company where Industry = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, sid);
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
		String s = "delete from Specialties";
		stmt.executeUpdate(s);
	}
}
