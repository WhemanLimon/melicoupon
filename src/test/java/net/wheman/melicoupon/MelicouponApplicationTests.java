package net.wheman.melicoupon;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.wheman.melicoupon.coupon.CouponController;
import net.wheman.melicoupon.coupon.CouponStatisticsResponse;

@SpringBootTest
class MelicouponApplicationTests {

	@Autowired
	private CouponController couponController;

	@Test
	void contextLoads() {
	}



	@Test
	void checkApiIsStarted(){
		List<CouponStatisticsResponse> result = couponController.GetCouponStats();
		assertTrue(result != null);
	}

	@Test
	void executeMain(){
		assertDoesNotThrow(() -> MelicouponApplication.main(new String[0]));
	}

}
