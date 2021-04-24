package net.wheman.melicoupon.coupon;

import lombok.AllArgsConstructor;

/**
 * This class represents a response POJO for the Coupon Stats API.
 * It holds an item ID and a counter of how many times the item has been used in coupons.
 */
@AllArgsConstructor
public class CouponStatisticsResponse{
    public String id;
    public long quantity;
}
