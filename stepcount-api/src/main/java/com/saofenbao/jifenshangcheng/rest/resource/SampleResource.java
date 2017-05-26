package com.saofenbao.jifenshangcheng.rest.resource;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.saofenbao.jifenshangcheng.domain.Order;
import com.saofenbao.jifenshangcheng.domain.annotation.ValidOrder;
import com.saofenbao.jifenshangcheng.rest.exception.GenericException;
import com.saofenbao.jifenshangcheng.service.OrderService;

@Controller
@Path("/samples/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SampleResource {
	private static final Log log = LogFactory.getLog(SampleResource.class);

	private @Value("${fileFolder}") String fileFolder;

	@Autowired
	private OrderService orderService;


	@Path("/roles/user")
	@RolesAllowed({"admin","user"})
	@GET
	public Map<String, Object> userRoleRequired(){
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("message", "if you are a user, you can see this");
		return result;
	}
	
	@Path("/admin")
	@RolesAllowed({"admin","superman"})
	@DenyAll
	@GET
	public Map<String, Object> adminOnly(){
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("message", "you cannot see this if you are not admin");
		return result;
	}
	
	@Path("/deny")
	@DenyAll
	@GET
	public Map<String, Object> denyAll(){
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("message", "you cannot see this ");
		return result;
	}
	/**
	 * 如果方法需要获取当前用户，从SecurityContext中获取
	 * @param req
	 * @param sc
	 * @return
	 */
	@PermitAll
	@GET
	@Path("/hello")
	public Map<String, Object> sayHello(@Context HttpServletRequest req, @Context SecurityContext sc) {
		if (log.isTraceEnabled()) {
			log.trace("sayHello() " + this);

			if(req != null){
				log.trace("reqest " + (req.getUserPrincipal() == null ? " user from req is null " : req.getUserPrincipal().getName()));
			}
			if(sc != null){
				log.trace("SC " + sc.getAuthenticationScheme() + ", " + (sc.getUserPrincipal() == null ? "user is null" : sc.getUserPrincipal().getName() ));
			}
		}
		Order order = orderService.createARandomOrder();
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("order", order);
		result.put("hello", "Hello, world.");
		return result;
	}

	@PermitAll
	@Path("/order/{id:\\d+}/")
	@GET
	public Map<String, Object> getOrder(@PathParam("id") int id, @Context HttpServletRequest req, @Context SecurityContext sc)
			throws GenericException {
		if (log.isTraceEnabled()) {
			log.trace("readArray()" + id + "; SecurityContext is "
					+ sc.getClass().getName() + "; user="
					+ sc.getUserPrincipal() + "  "
					+ sc.getUserPrincipal().getClass().getName());
		}
		Order order = orderService.retrieveOrder(id);
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("order", order);
		result.put("otherthings", "no");
		// result.put("user", requestContext.getUser().getName() + "," +
		// requestContext.getUser().getId());
		result.put("user", sc.getUserPrincipal());
		return result;
	}

	/**
	 * 这里使用了两种验证方式@Valid使用的是Order类内部的注解进行验证。
	 * 注解@ValidOrder是自定义的验证器，使用OrderValidator类进行验证。
	 * 可根据需要用一个或多个
	 * 
	 * @param order
	 * @return
	 */
	@PermitAll
	@POST
	public Map<String, Object> createOrder(@Valid @ValidOrder Order order) {
		if (log.isTraceEnabled()) {
			log.trace("createSomething......" + order.toString() + "; ");
		}
		
		//TODO:设置一些订单信息，例如订单用户，
		order.setStatus(0);
		//TODO:做检查，例如检查用户是否可以购买订购的商品，收件地址是否合法等
		Order savedOrder = orderService.create(order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("jifenLeft", 888);
		result.put("name", "");
		result.put("order", savedOrder);
		return result;
	}

	@Path("/array/{id:\\d+}/")
	@GET
	public Map<String, Object> readArray(@Context HttpHeaders hh,
			@QueryParam("q") String q,
			@HeaderParam("user-agent") String userAgent,
			@Context HttpServletRequest request, @PathParam("id") int id)
			throws GenericException {
		if (log.isTraceEnabled()) {
			log.trace("readArray()" + id);
		}
		if (id == 0) {
			throw new WebApplicationException(401);
		} else if (id == 1) {
			throw new RuntimeException("iD=1 Exception");
		} else if (id == 2) {
			throw new GenericException(401, "用户未登录。");
		} else if (id == 3) {
			GenericException ge = new GenericException(401, "用户未登录。",
					"请先调用登录接口，然后在http header中传递access_token参数",
					"http://192.168.1.9/wiki/login/accesstoken");
			throw ge;
		} else if (id == 4) {
			GenericException ge = new GenericException(411, "用户未登录。",
					"请先调用登录接口，然后在http header中传递access_token参数",
					"http://192.168.1.9/wiki/login/accesstoken");
			String[] errInfo = new String[] { "第一条信息长度不足20字。", "第2张图片尺寸太小。",
					"第三章图片文件过大。" };
			ge.putAdditionMessage("detail", errInfo);
			throw ge;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		String[] ary = new String[5];
		for (int i = 0; i < ary.length; i++) {
			ary[i] = "ary-" + i + "/" + id;
		}
		result.put("data", ary);
		result.put("message", "OK");
		result.put("count", 5);
		result.put("q", q);
		result.put("sessionId", request.getSession().getId());
		// result.put("user-agent", hh.getRequestHeader("user-agent").get(0));
		result.put("user-agent", userAgent);
		return result;
	}

}
