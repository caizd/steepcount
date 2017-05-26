package com.saofenbao.jifenshangcheng.domain;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class OrderItem {
	@NotNull
	private String giftId;
	private String giftName;
	
    @DecimalMin(value = "2", message = "购买数量不可以少于2个")
    @DecimalMax(value = "5", message = "购买数量不可以多于5个")
	private int num;
    
	private int jifen;
	private int id;
	private double supplyPrice;
	public String getGiftId() {
		return giftId;
	}
	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}
	public String getGiftName() {
		return giftName;
	}
	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getJifen() {
		return jifen;
	}
	public void setJifen(int jifen) {
		this.jifen = jifen;
	}
	public double getSupplyPrice() {
		return supplyPrice;
	}
	public void setSupplyPrice(double supplyPrice) {
		this.supplyPrice = supplyPrice;
	}
	
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	
	@Override
	public String toString(){
		return "OrderItem{id=" + id + "; giftId=" + giftId + "; num=" + num + "; jifen=" + jifen + "; supplyPrice=" + supplyPrice + "}";
	}
}
