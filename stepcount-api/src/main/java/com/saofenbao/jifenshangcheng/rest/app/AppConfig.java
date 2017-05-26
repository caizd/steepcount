package com.saofenbao.jifenshangcheng.rest.app;


import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.saofenbao.jifenshangcheng.rest.exception.GenericExceptionMapper;
import com.saofenbao.jifenshangcheng.rest.exception.JsonMappingExceptionMapper;
import com.saofenbao.jifenshangcheng.rest.exception.NullPointerExceptionMapper;
import com.saofenbao.jifenshangcheng.rest.exception.ValidationExceptionMapper;
import com.saofenbao.jifenshangcheng.rest.filter.AuthenticationFilter;


public class AppConfig extends ResourceConfig {

	public AppConfig() {
		//设置哪些包下面的类会用于REST API
		packages("com.saofenbao.jifenshangcheng.rest.resource");
//		property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
//		property(ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
//		property(ServerProperties.BV_FEATURE_DISABLE, true);
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
		
		
		//登记身份验证过滤器，类上增加Provider注解，这里不需要登记(使用spring+jersey后，Provider注解不起作用了，需要手工登记)
		//Provider不起作用是jersey < v2.5 bug; https://java.net/jira/browse/JERSEY-2175
		//this should be helpful  http://stackoverflow.com/questions/25905941/jersey-global-exceptionhandler-doesnt-work
		register(AuthenticationFilter.class);
		
		register(RolesAllowedDynamicFeature.class); //这个会使得PermitAll, RolesRequired(""), DenyAll等注解起作用
		register(JacksonJsonProvider.class);
		register(MultiPartFeature.class);

		//登记异常转换用类
		register(NullPointerExceptionMapper.class);
		register(JsonMappingExceptionMapper.class);
		register(GenericExceptionMapper.class);
		register(ValidationExceptionMapper.class);
		//swagger
		Set<Class<?>> resources = new HashSet<>();
		resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
		resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		register(resources);
//		register(ValidationConfigurationContextResolver.class);
	}
}
