package net.wheman.melicoupon.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class represents a POJO for handling the Coupon API response.
 * It contains the favorite items IDs that were selected for the coupon.
 * It also contains the total amount of the coupon by adding all selected items's price.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CouponResponse {
    private String[] item_ids;
    private Double total;
}
