package net.wheman.melicoupon.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
final class CouponStatistics{
    public String id;
    public long quantity;
}
