package com.saofenbao.jifenshangcheng.dao;

import org.apache.ibatis.annotations.Param;

import com.saofenbao.jifenshangcheng.domain.Order;

public interface OrderDao {

	
	public int insertOrder(@Param(value = "order") Order order);
	
	
	public Order getOrderById(int id);
}
