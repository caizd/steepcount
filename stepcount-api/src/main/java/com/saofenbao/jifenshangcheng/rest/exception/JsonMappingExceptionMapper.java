package com.saofenbao.jifenshangcheng.rest.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.JsonMappingException;

@Provider 
public class JsonMappingExceptionMapper  implements ExceptionMapper<JsonMappingException> {
	private static final Log log = LogFactory.getLog(JsonMappingExceptionMapper.class);

	@Override
	public Response toResponse(JsonMappingException jme) {
		Map<String, Object>messageMap = new HashMap<String, Object>();
		messageMap.put("message", "遇到服务器端错误。");
		messageMap.put("developerMessage", "JSON转换错误：" + jme.getMessage());
		if(log.isErrorEnabled()){
			log.error("JSON转换错误", jme);
		}
		return Response.status(500).entity(messageMap).type(MediaType.APPLICATION_JSON).build();
	}

}
