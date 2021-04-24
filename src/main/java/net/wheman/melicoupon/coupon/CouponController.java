package net.wheman.melicoupon.coupon;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 
 * This class is a controller for handling <b>Coupon API requests</b>. 
 * The purpose of this API is to calculate a coupon selecting one or more items from the user's favorite list and maximize the coupon value.
 * <p>
 * In order to get the items prices the Coupon API will invoke MELI's Items API and store the values retrieved in memory to speed up future coupon
 * requests for the same items.
 * <p>
 * The items of each coupon are selected by aplying a Strategy based on the size of the request in order to get a subset of items whose added values maximizes 
 * the value or the coupon for a given limit. 
*/

@RestController
public class CouponController {

    @Autowired
    private CouponService couponService;

    /**
     * Returns a coupon containing a subset of items whose sum of their price is equals or nearest to a limit.
     * <p>
     * A request body example is: {{@code "item_ids": ["MLA1", "MLA2", "MLA3"], "amount: 1200"}}.
     * @param request Body composed by an array of strings representing the items IDs and the limit amout.
     * @return {@link CouponResponse}
     */
    @PostMapping("/coupon")
    public CouponResponse GetCoupon(@RequestBody CouponRequest request) {

        HashMap<String, Double> itemsForCoupon = couponService.getItemsForCoupon(request.getItem_ids(), request.getAmount());
        String[] items = itemsForCoupon.keySet().toArray(new String[itemsForCoupon.keySet().size()]);
        Double total = (double) 0;
        for (Double item : itemsForCoupon.values()) {
            total += item;
        }
        return new CouponResponse(items, total);
    }

    /**
     * Returns a top five list of statistics for the most used items among all coupons generated.
     * @return A list of {@link CouponStatisticsResponse}
     */
    @GetMapping("/coupon/stats")
    public List<CouponStatisticsResponse> GetCouponStats(){
        List<CouponStatisticsResponse> topFiveItems = new ArrayList<CouponStatisticsResponse>();
        couponService.getTopFiveItems().entrySet().stream().forEach(i -> {
            topFiveItems.add(new CouponStatisticsResponse(i.getKey(), i.getValue()));
        });
        return topFiveItems;
    }

}
