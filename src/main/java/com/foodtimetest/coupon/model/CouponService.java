package com.foodtimetest.coupon.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CouponService {

	private CouponDAO_interface dao;

	public CouponService() {
		dao = new CouponDAO();
	}

	public CouponVO addCoupon(Integer storId, String couDes, String couType, Integer couMinOrd, Timestamp couDate) {

		CouponVO couponVO = new CouponVO();

		couponVO.setStorId(storId);
		couponVO.setCouDes(couDes);
		couponVO.setCouType(couType);
		couponVO.setCouMinOrd(couMinOrd);
		couponVO.setCouDate(couDate);
		dao.insert(couponVO);

		return couponVO;
	}

	public CouponVO updateCoupon(Integer couId, Integer storId, String couDes, String couType, Integer couMinOrd,
			Timestamp couDate) {

		CouponVO couponVO = new CouponVO();

		couponVO.setCouId(couId);
		couponVO.setStorId(storId);
		couponVO.setCouDes(couDes);
		couponVO.setCouType(couType);
		couponVO.setCouMinOrd(couMinOrd);
		couponVO.setCouDate(couDate);
		dao.update(couponVO);
		
		return couponVO;
	}

	public void deleteCoupon(Integer couId) {
		dao.delete(couId);
	}

	public CouponVO getOneCoupon(Integer couId) {
		return dao.findByPrimaryKey(couId);
	}

	public List<CouponVO> getAll() {
		return dao.getAll();
	}
	
	public List<CouponVO> getStorCoupon(Integer storId) {
		return dao.findByStorId(storId);
	}
	
	public List<CouponVO> getDistinctStorId(){
		return dao.findDistinctStorId();
	}
	
	public List<String> getDistinctCouType(){  //回傳不重複的優惠券類型
		
		List<CouponVO> allCoupons = dao.getAll();
		Set<String> coutypes = new HashSet<>();
		for(CouponVO coupon : allCoupons) {
			 coutypes.add(coupon.getCouType());
		}
		return new ArrayList<>(coutypes);
	}
	
	public List<CouponVO> getCouponsByCouType(String couType) {
	    List<CouponVO> allCoupons = dao.getAll();
	    List<CouponVO> filtered = new ArrayList<>();

	    for (CouponVO coupon : allCoupons) {
	        if (coupon.getCouType().equals(couType)) {
	            filtered.add(coupon);
	        }
	    }

	    return filtered;
	}
}
