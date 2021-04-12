package net.wheman.melicoupon.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CouponResponse {
    private String[] item_ids;
    private Double total;
}