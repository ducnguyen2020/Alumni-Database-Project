package edu.depauw.csc480.model;

import java.util.Collection;

import edu.depauw.csc480.dao.SpecialtiesDAO;

public class Specialties {
	private SpecialtiesDAO dao;
	private int Sid;
	private String name;
	private String definition;
	private Collection<Alumni> alumni;
	private Collection<Company> company;
	
	public Specialties(SpecialtiesDAO dao, int Sid, String name, String definition){
		this.dao = dao;
		this.Sid = Sid;
		this.name = name;
		this.definition = definition;
	}
	
	public String toString() {
		return name + "," + definition;
	}
	
	public int getSid() {
		return Sid;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDefinition() {
		return definition;
	}
	
	public void setDefinition(String newDef) {
		this.definition= newDef;
		dao.changeDefinition(Sid,definition);
	}
	
	public Collection<Alumni> getAlumni(){
		if (alumni == null) alumni = dao.getAlumni(Sid);
		return alumni;
	}
	
	public Collection<Company> getCompany(){
		if (company == null) company = dao.getCompany(Sid);
		return company;
	}
}
