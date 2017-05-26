package  com.saofenbao.jifenshangcheng.rest.filter;

import java.util.Random;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saofenbao.jifenshangcheng.domain.User;



/**
 * 身份验证  
 **/
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter{
	private static final Log log = LogFactory.getLog(AuthenticationFilter.class);

	@Override
	public void filter(ContainerRequestContext containerRequestContext) {

		User user = null;
		String accessToken = containerRequestContext.getHeaderString("access-token");
		if(accessToken == null){
			//尝试从cookie获取
			Cookie cookie = containerRequestContext.getCookies().get("accessToken");
            if(cookie!=null){
	            accessToken = cookie.getValue();
            }
		}
		if(log.isTraceEnabled()){
			log.trace("access token is " + accessToken);
		}
		
		if (accessToken != null && accessToken.trim().length() > 0) {
			// TODO:根据accessToken获取User
			//设置当前用户
			user = new User();
			user.setName("Dolores-" + new Random().nextDouble());
			user.setId(8888);
		}else{
			//TODO:设置当前用户为guest
			user = new User();
			user.setName("Wyatt-" + new Random().nextInt());
			user.setId(999);
		}
		containerRequestContext.setSecurityContext(new AppSecurityContext(user));
	}
	
}