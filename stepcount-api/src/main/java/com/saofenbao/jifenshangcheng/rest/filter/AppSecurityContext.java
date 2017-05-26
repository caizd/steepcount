package com.saofenbao.jifenshangcheng.rest.filter;

import java.security.Principal;
import java.util.Arrays;

import javax.ws.rs.core.SecurityContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saofenbao.jifenshangcheng.domain.User;

public class AppSecurityContext implements SecurityContext {
	private static final Log log = LogFactory.getLog(AppSecurityContext.class);
	private User user;
	
	public AppSecurityContext(User user){
		this.user = user;
	}
	
	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.FORM_AUTH;
	}

	@Override
	public Principal getUserPrincipal() {
		return user;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public boolean isUserInRole(String role) {
		if(log.isTraceEnabled()){
			log.trace("isUserInRole(" + role + "), user=" + (user == null ? "NULL" : Arrays.toString(user.getRoles().toArray())));
		}
		if(user == null || user.getRoles() == null){
			return false;
		}else{
			return user.getRoles().contains(role);
		}
	}

}
