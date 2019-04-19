package edu.depauw.csc480.model;

import java.util.Collection;

import edu.depauw.csc480.dao.AddressDAO;

public class Address {
	private AddressDAO dao;
	private int aid;
	private String addtext;
	private int zipcode;
	private String country;
	private Collection<Alumni> alumni;
	private Collection<Company> company;
	
	public Address(AddressDAO dao, int aid, String addtext, int zipcode, String country){
		this.dao = dao;
		this.aid = aid;
		this.addtext = addtext;
		this.zipcode = zipcode;
		this.country = country;
	}
	
	public String toString() {
		return addtext + "," + zipcode + "," + country;
	}
	
	public int getAid() {
		return aid;
	}
	
	public String getAddtext() {
		return addtext;
	}
	
	
	public int getZipcode() {
		return zipcode;
	}

	
	public String country() {
		return country;
	}
	
	public Collection<Alumni> getAlumni(){
		if (alumni == null) alumni = dao.getAlumni(aid);
		return alumni;
	}
	
	public Collection<Company> getCompany(){
		if (company == null) company = dao.getCompany(aid);
		return company;
	}
	
}
