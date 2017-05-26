package com.saofenbao.jifenshangcheng.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saofenbao.jifenshangcheng.dao.OrderDao;
import com.saofenbao.jifenshangcheng.dao.VendorGiftDao;
import com.saofenbao.jifenshangcheng.domain.ConsigneeAddress;
import com.saofenbao.jifenshangcheng.domain.Order;
import com.saofenbao.jifenshangcheng.domain.OrderItem;
import com.saofenbao.jifenshangcheng.domain.VendorGift;

@Service
public class OrderService {
	private static final Log log = LogFactory.getLog(OrderService.class);

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private VendorGiftDao vendorGiftDao;
	
	public Order create(Order order){
		order.setOrderDate(Calendar.getInstance().getTime());
		//从查询积分和供货价，并计入订单
		for(OrderItem item: order.getOrderItems()){
			VendorGift gift = vendorGiftDao.getVendorGift(order.getVendorId(), item.getGiftId());
			if(gift == null){
				throw new RuntimeException("客户(" + order.getVendorId() + ")未上架礼品" + item.getGiftId() + ",无法兑换");
			}else{
				if(log.isTraceEnabled()){
					log.trace("设置" + item.getGiftId() + "积分：" + gift.getJifen() + ";供货价:" + gift.getSupplyPrice());
				}
				item.setJifen(gift.getJifen());
				item.setSupplyPrice(gift.getSupplyPrice());
			}
		}
		//保存订单
		int result = orderDao.insertOrder(order);
		if(log.isTraceEnabled()){
			log.trace("插入的新记录" + order.getId() + "; sql return " + result);
		}
		return order;
	}
	
	@Transactional
	public Order createARandomOrder(){

		Order newOrder = new Order();
		//newOrder.setConsignee("郝尤乾");
		newOrder.setOrderDate(new Date());
		newOrder.setConsigneeAddress(new ConsigneeAddress("张三", "13612348888", "江苏省", "南京市", "xx区", "xyz路20号"));
		List<OrderItem> list = new ArrayList<OrderItem>();
		Random rnd = new Random();
		int max = 1 + rnd.nextInt(5);
		for(int i=0; i<max; i++){
			OrderItem item = new OrderItem();
			item.setGiftId("EL" +  (rnd.nextInt() + 1000) % 1000);
			item.setGiftName("苹果" + (rnd.nextInt(20) + 1) + "代");
			item.setNum(rnd.nextInt(10) + 1);
			item.setJifen(rnd.nextInt(1000));
			item.setSupplyPrice(rnd.nextDouble() * 100);
			list.add(item);
		}
		newOrder.setOrderItems(list);
		
		int result = orderDao.insertOrder(newOrder);
		if(log.isTraceEnabled()){
			log.trace("插入的新记录" + newOrder.getId() + "; sql return " + result);
		}
		return newOrder;
	}
	
	public Order retrieveOrder(int id){
		return orderDao.getOrderById(id);
	}
}
