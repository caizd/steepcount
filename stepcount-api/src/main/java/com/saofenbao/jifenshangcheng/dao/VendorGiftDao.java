package com.saofenbao.jifenshangcheng.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.saofenbao.jifenshangcheng.domain.VendorGift;

public interface VendorGiftDao {

	public List<VendorGift> getGiftsByCategory(@Param(value = "vendorId") int vendorId, @Param(value = "categoryId")  int categoryId);
	
	public List<VendorGift> getAllGifts(int vendorId);
	
	public VendorGift getVendorGift(@Param(value = "vendorId") int vendorId, @Param(value = "giftId") String giftId);
}
