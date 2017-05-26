package com.saofenbao.jifenshangcheng.rest.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
	private static final Log log = LogFactory.getLog(ValidationExceptionMapper.class);
	
	@Override
	public Response toResponse(ConstraintViolationException ve) {
		Map<String, Object> messageMap = new HashMap<String, Object>();
		messageMap.put("message", ve.getMessage());
		messageMap.put("developerMessage", "传递参数未通过校验：" + ve.getMessage());
		List<Map<String, Object>> detailMessages = new ArrayList<Map<String, Object>>();
		for(Iterator<ConstraintViolation<?>> it = ve.getConstraintViolations().iterator(); it.hasNext();){
			ConstraintViolation<?> cv = it.next();
			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("constraintDescriptor", cv.getConstraintDescriptor());
			map.put("message", cv.getMessage());
			map.put("leaf", cv.getLeafBean().getClass().getName());
			map.put("root", cv.getRootBean().getClass().getName());
			map.put("invalidValue", cv.getInvalidValue().toString());
			detailMessages.add(map);
		}
		messageMap.put("details", detailMessages);
		if(log.isErrorEnabled()){
			log.error("传递参数未通过校验", ve);
		}
		return Response.status(400).entity(messageMap).type(MediaType.APPLICATION_JSON).build();
	}

}
