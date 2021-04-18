package net.wheman.melicoupon.coupon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.wheman.melicoupon.meli.MeliService;
import net.wheman.melicoupon.strategy.StrategyFactory;

import net.wheman.melicoupon.datakeeper.ItemMemory;

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

    /**
     * Returns a coupon containing a subset of items whose sum of their price is equals or nearest to a limit.
     * <p>
     * A request body example is: {{@code "item_ids": ["MLA1", "MLA2", "MLA3"], "amount: 1200"}}.
     * @param request Body composed by an array of strings representing the items IDs and the limit amout.
     * @return {@link CouponResponse}
     */
    @PostMapping("/coupon")
    public CouponResponse GetCoupon(@RequestBody CouponRequest request) {
        HashMap<String, Double> itemsForCoupon = getItemsForCoupon(request.getItem_ids(), request.getAmount());
        String[] items = itemsForCoupon.keySet().toArray(new String[itemsForCoupon.keySet().size()]);
        Double total = (double) 0;
        for (Double item : itemsForCoupon.values()) {
            total += item;
        }
        return new CouponResponse(items, total);
    }

    /**
     * Returns a top five list of statistics for the most used items among all coupons generated.
     * @return A list of {@link CouponStatistics}
     */
    @GetMapping("/coupon/stats")
    public List<CouponStatistics> GetCouponStats(){
        List<CouponStatistics> topFiveItems = new ArrayList<CouponStatistics>();
        ItemMemory.GetTopFiveItems().entrySet().stream().forEach(i -> {
            topFiveItems.add(new CouponStatistics(i.getKey(), i.getValue()));
        });
        return topFiveItems;
    }
 
    /**
     * Retrieves the subset of items that fulfills a coupon limit giving a list of favorite items. 
     * <p>
     * Checks if each favorite item is available in cache, otherwise it will request it to MELI's Items API.
     * Once all favorite items have price it will get the best subset of items based on maximizing coupon's limit 
     * by selecting and executing a Strategy based on the size of the request.
     * 
     * @param favItems array of string containing all favorite items.
     * @param maxAmount coupon's price limit.
     * @return {@link HashMap} key-value pair with the items that are part of the coupon.
     */
    private HashMap<String, Double> getItemsForCoupon(String[]favItems, Double maxAmount){
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        HashMap<String, Double> itemsWithPrice = ItemMemory.GetItemsByIdsFromCache(favItems);
        List<String> meliItems = Arrays.stream(favItems).filter(i -> !itemsWithPrice.keySet().stream().anyMatch(f -> f.equals(i))).collect(Collectors.toList());

        List<List<String>> subsets = Lists.partition(meliItems, 20);
        for (List<String> list : subsets) {
            executor.submit(() -> {
                HashMap<String, Double> price = MeliService.GetItemPricesByIds(String.join(",", list));    
                price.entrySet().stream().forEach(i -> {
                    itemsWithPrice.put(i.getKey(), i.getValue());
                    ItemMemory.AddItemToCache(i.getKey(), i.getValue());
                });
            });
        }

        try {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (Exception e) {
        }

        HashMap<String, Double> selectedItems = StrategyFactory.getStrategResult(itemsWithPrice, maxAmount);
        ItemMemory.IncreaseItemsCount(selectedItems.keySet().toArray(new String[selectedItems.keySet().size()]));
        return selectedItems;
    }
}
