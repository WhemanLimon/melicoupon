package net.wheman.melicoupon;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.wheman.melicoupon.coupon.CouponController;
import net.wheman.melicoupon.coupon.CouponStatisticsResponse;

@SpringBootTest
class MelicouponApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void checkApiIsStarted(){
		CouponController couponController = new CouponController();
		List<CouponStatisticsResponse> result = couponController.GetCouponStats();
		assertTrue(result != null);
	}

}
