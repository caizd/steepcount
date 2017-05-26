package com.saofenbao.jifenshangcheng.rest.app;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.validation.ParameterNameProvider;
import javax.validation.Validation;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.server.validation.ValidationConfig;
import org.glassfish.jersey.server.validation.internal.InjectingConstraintValidatorFactory;

/**
 * 只是测试了下验证器；并未实际使用
 *
 */
public class ValidationConfigurationContextResolver implements
        ContextResolver<ValidationConfig> {
    private final static Log log = LogFactory.getLog(ValidationConfigurationContextResolver.class);
    
    @Context
    private ResourceContext resourceContext;
 
    @Override
    public ValidationConfig getContext(final Class<?> type) {
        if(log.isTraceEnabled()){
            log.trace("getContext(" + type.getName() + ")");
        }
        final ValidationConfig config = new ValidationConfig();
        config.constraintValidatorFactory(resourceContext.getResource(InjectingConstraintValidatorFactory.class));
        config.parameterNameProvider(new CustomParameterNameProvider());
        return config;
    }
 
    /**
     * See ContactCardTest#testAddInvalidContact.
     */
    private class CustomParameterNameProvider implements ParameterNameProvider {
 
        private final ParameterNameProvider nameProvider;
 
        public CustomParameterNameProvider() {
            nameProvider = Validation.byDefaultProvider().configure().getDefaultParameterNameProvider();
        }
 
        @Override
        public List<String> getParameterNames(final Constructor<?> constructor) {
        	if(log.isTraceEnabled()){
        		log.trace("getParameterNames(constructor:" + constructor.getName() + ", " + constructor.getDeclaringClass().getName() + ")");
        	}
            return nameProvider.getParameterNames(constructor);
        }
 
        @Override
        public List<String> getParameterNames(final Method method) {
        	if(log.isTraceEnabled()){
        		log.trace("getParameterNames(method:" + method.getName() + ", " + method.getDeclaringClass().getName()  + ")");
        	}
            // See ContactCardTest#testAddInvalidContact.
            if ("addContact".equals(method.getName())) {
                return Arrays.asList("contact");
            }
            return nameProvider.getParameterNames(method);
        }

    }

}

