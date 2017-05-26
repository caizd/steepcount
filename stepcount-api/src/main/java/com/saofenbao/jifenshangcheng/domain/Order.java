package com.saofenbao.jifenshangcheng.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class Order {
	private int id;
	
	private int vendorId;

	@Null
	private Date orderDate;
	
	@NotNull
	@Valid
	private ConsigneeAddress consigneeAddress;
	
	@NotNull
	@Size(min=1, message="至少需要兑换一件礼品")
	@Valid
	private List<OrderItem> orderItems;
	
	private int status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(@Valid List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	public int getTotalJifen(){
		if(this.orderItems == null){
			throw new RuntimeException("no orderItems found in order #" + this.id);
		}
		int jf = 0;
		for(OrderItem item : this.orderItems){
			jf += item.getJifen() * item.getNum();
		}
		return jf;
	}
	public ConsigneeAddress getConsigneeAddress() {
		return consigneeAddress;
	}
	public void setConsigneeAddress(ConsigneeAddress consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString(){
		return "Order{id=" + id + "; consigeeAddress=" + (consigneeAddress) + "; orderItems=" + 
					(orderItems == null ? "null" : "size=" + orderItems.size() + ", " + Arrays.toString(orderItems.toArray())) + "}";
	}
	/**
	 * @return the vendorId
	 */
	public int getVendorId() {
		return vendorId;
	}
	/**
	 * @param vendorId the vendorId to set
	 */
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
}
