package net.wheman.melicoupon.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class represents a POJO for handling the Coupon API request. 
 * It holds a string containing the items IDs that a user added as favorites.
 * It also holds a number that is the maximum value for the coupon.
 * 
 */
@AllArgsConstructor
@Getter
public class CouponRequest {
    private String[] item_ids;
    private Double amount;
}