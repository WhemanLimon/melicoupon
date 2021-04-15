package net.wheman.melicoupon.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a POJO for handling the Coupon API request. 
 * It holds a string containing the items IDs that a user added as favorites.
 * It also holds a number that is the maximum value for the coupon.
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CouponRequest {
    private String[] item_ids;
    private Double amount;
}