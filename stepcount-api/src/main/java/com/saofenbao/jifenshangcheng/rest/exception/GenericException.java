package com.saofenbao.jifenshangcheng.rest.exception;

import java.util.HashMap;
import java.util.Map;


public class GenericException extends RuntimeException {
	private static final long serialVersionUID = 6319003249837089260L;
	
	private String message;
	private int responseCode;
	private String developerMessage;
	private String link;
	private Map<String, Object> additionMessageMap = null;

	public GenericException(int responseCode, String message){
		this(responseCode, message, null, null);
	}
	public GenericException(int responseCode, String message, String developerMessage){
		this(responseCode, message, developerMessage, null);
	}
	public GenericException(int responseCode, String message, String developerMessage, String link){
		this.responseCode = responseCode;
		this.message = message;
		this.developerMessage = developerMessage;
		this.link = link;
	}

	public void putAdditionMessage(String key, Object value){
		if(additionMessageMap == null){
			additionMessageMap = new HashMap<String, Object>();
		}
		additionMessageMap.put(key,  value);
	}
	public String getMessage() {
		return message;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public String getDeveloperMessage() {
		return developerMessage;
	}
	public String getLink() {
		return link;
	}
	
	public Map<String, Object> getMessages(){
		Map<String, Object> result = new HashMap<String, Object>();
		if(additionMessageMap != null){
			result.putAll(additionMessageMap);
		}
		result.put("message", this.message);
		if(this.developerMessage != null){
			result.put("developerMessage", developerMessage);
		}
		if(this.link  != null){
			result.put("link", link);
		}
		return result;
	}
}
