package net.wheman.melicoupon.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * This class represents a response POJO for the Coupon Stats API.
 * It holds an item ID and a counter of how many times the item has been used in coupons.
 */
@Data
@AllArgsConstructor
public class CouponStatistics{
    public String id;
    public long quantity;
}
