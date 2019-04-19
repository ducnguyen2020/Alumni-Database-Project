package edu.depauw.csc480.model;

import edu.depauw.csc480.dao.CompanyDAO;

public class Company {
	private CompanyDAO dao;
	private int cid;
	private String name;
	private String email;
	private String phone;
	private int salary;
	private Specialties industry;
	private Address address;
	
	
	public Company(CompanyDAO dao, int cid, String name, String email, String phone, int salary, Specialties industry, Address address){
		this.dao = dao;
		this.cid = cid;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.salary = salary;
		this.industry = industry;
		this.address = address;
	}
	
	public String toString() {
		return name + "," + email + "," + phone + "," + salary + "," + industry + "," + address;
	}
	
	public int getCid() {
		return cid;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhone() {
		return phone; 
	}
	
	public int getSalary() {
		return salary;
	}
	
	public Specialties getIndustry() {
		return industry;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void setSalary(int newsal) {
		this.salary = newsal;
		dao.changeSalary(cid, salary);
	}
	
	public void setAddress(Address add) {
		this.address = add;
		dao.changeAddress(cid,address);
	}
}
