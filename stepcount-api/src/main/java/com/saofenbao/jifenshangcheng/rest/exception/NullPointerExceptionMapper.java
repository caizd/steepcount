package com.saofenbao.jifenshangcheng.rest.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@Provider
public class NullPointerExceptionMapper implements ExceptionMapper<NullPointerException> {
	private static final Log log = LogFactory.getLog(NullPointerExceptionMapper.class);
	
	@Override
	public Response toResponse(NullPointerException npe) {
		Map<String, Object>messageMap = new HashMap<String, Object>();
		messageMap.put("message", "遇到服务器端错误。");
		messageMap.put("developerMessage", "空指针：" + npe.getMessage());
		if(log.isErrorEnabled()){
			log.error("遇到空指针错误", npe);
		}
		return Response.status(500).entity(messageMap).type(MediaType.APPLICATION_JSON).build();
	}

}
