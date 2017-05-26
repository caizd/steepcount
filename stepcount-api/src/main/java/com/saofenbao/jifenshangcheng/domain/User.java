package com.saofenbao.jifenshangcheng.domain;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class User  implements Principal {
	private String name;
	private int id;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	

	public List<String> getRoles(){
		 List<String> roles = new ArrayList<String>();
		 roles.add("user");
		 return roles;
	}
}
