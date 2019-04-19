package edu.depauw.csc480.model;

import edu.depauw.csc480.dao.AlumniDAO;

public class Alumni {
	private AlumniDAO dao;
	private String fname;
	private String email;
	private int gradyear;
	private String phone;
	private int ssn;
	private Specialties major;
	private Company company;
	private Address address;
	
	
	public Alumni(AlumniDAO dao, int ssn, String fname, String email, int gradyear, String phone, Specialties major, Company company, Address address){
		this.dao = dao;
		this.ssn = ssn;
		this.fname = fname;
		this.email = email;
		this.gradyear = gradyear;
		this.phone = phone;
		this.major = major;
		this.company = company;
		this.address = address;
	}
	
	public String toString() {
		return fname + "," + major + "," + gradyear + "," + email + "," + phone + "," + company + "," + address;
	}
	
	public int getSSN() {
		return ssn;
	}
	
	public Specialties getMajor() {
		return major;
	}
	
	public Company getCompany() {
		return company;
	}
	
	public Address getAdd() {
		return address; 
	}
	
	public String getName() {
		return fname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String newEmail) {
		this.email = newEmail;
		dao.changeEmail(ssn, email);
	}
	
	public int getGradYear() {
		return gradyear;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String newphone) {
		this.phone = newphone;
		dao.changePhone(ssn,phone);
	}
	
	public void setCompany(Company company) {
		this.company = company;
		dao.changeCompany(ssn,company);
	}
	
	public void setAddress(Address add) {
		this.address = add;
		dao.changeAddress(ssn,add);
	}
}
