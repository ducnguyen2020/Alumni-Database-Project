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

public class CompanyDAO {
	private Connection conn;
	private DatabaseManager dbm;

	public CompanyDAO(Connection conn, DatabaseManager dbm) {
		this.conn = conn;
		this.dbm = dbm;
	}

	/**
	 * Create the Company table via SQL
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void create(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "create table Company("
				+ "Cid int not null, "
				+ "Name varchar(20) not null, "
				+ "Email varchar(255), "
				+ "Phone varchar (15) not null, "
				+ "Salary int, "
				+ "Industry int, "
				+ "Address int, "
				+ "primary key(Cid))";
		stmt.executeUpdate(s);
	}

	/**
	 * Modify the Company table to add foreign key constraints (needs to happen
	 * after the other tables have been created)
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void addConstraints(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "alter table Company add constraint fk_compspecs "
				+ "foreign key(Industry) references Specialties on delete set null";
		stmt.executeUpdate(s);
		s = "alter table Company add constraint fk_compadd "
				+ "foreign key(Address) references Address on delete set null";
		stmt.executeUpdate(s);
	}

	/**
	 * Retrieve a Company object given its key.
	 * 
	 */
	public Company find(int cid) {
		try {
			String qry = "select Name, Email, Phone, Salary, Industry, Address from Company where Cid = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, cid);
			ResultSet rs = pstmt.executeQuery();

			// return null if alumni doesn't exist
			if (!rs.next())
				return null;

			String name = rs.getString("Name");
			String email = rs.getString("Email");
			int salary = rs.getInt("Salary");
			String phone = rs.getString("Phone");
			int industryID = rs.getInt("Industry");
			int addressid = rs.getInt("Address");
			
			rs.close();

			Specialties industry = dbm.findSpecialties(industryID);
			Address address = dbm.findAddress(addressid);

			Company comp = new Company(this, cid, name, email, phone, salary, industry, address);
			return comp;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding Company", e);
		}
	}

	/**
	 * Retrieve a Company object by name. 
	 */
	public Company findByName(String name) {
		try {
			String qry = "select Cid, Email, Phone, Salary, Industry, Address from Company where Name = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();

			// return null if faculty member doesn't exist
			if (!rs.next())
				return null;

			int cid = rs.getInt("Cid");
			String email = rs.getString("Email");
			int salary = rs.getInt("Salary");
			String phone = rs.getString("Phone");
			int industryID = rs.getInt("Industry");
			int addressid = rs.getInt("Address");
			rs.close();

			Specialties industry = dbm.findSpecialties(industryID);
			Address address = dbm.findAddress(addressid);

			Company comp = new Company(this, cid, name, email, phone, salary, industry, address);
			return comp;
			
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding company by name", e);
		}
	}

	/**
	 * Add a new Company member with the given attributes.
	 * 
	 */
	public Company insert(int cid, String name, String email, String phone, int salary, Specialties industry, Address address) {
		try {
			// make sure that the ssn is currently unused
			if (find(cid) != null)
				return null;

			String cmd = "insert into Company(Cid, Name, Email, Phone, Salary, Industry, Address) "
					+ "values(?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, cid);
			pstmt.setString(2, name);
			pstmt.setString(3, email);
			pstmt.setString(4, phone);
			pstmt.setInt(5, salary);
			pstmt.setInt(6, industry.getSid());
			pstmt.setInt(7, address.getAid());
			pstmt.executeUpdate();

			Company comp = new Company(this, cid, name, email, phone, salary, industry, address);
			return comp;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new Company", e);
		}
	}

	public void changeSalary(int cid, int salary) {
		try {
			String cmd = "update Company set Salary = ? where Cid = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, salary);
			pstmt.setInt(2, cid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing Salary", e);
		}
	}

	public void changeAddress(int cid, Address address) {
		try {
			String cmd = "update Company set Address = ? where Cid = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, address.getAid());
			pstmt.setInt(2, cid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing Address", e);
		}
	}
	/**
	 * Clear all data from the Faculty table.
	 * 
	 * @throws SQLException
	 */
	void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from Company";
		stmt.executeUpdate(s);
	}
}
