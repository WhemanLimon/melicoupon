package net.wheman.melicoupon.coupon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.wheman.melicoupon.AppConfiguration;
import net.wheman.melicoupon.datakeeper.ItemMemory;
import net.wheman.melicoupon.meli.MeliService;
import net.wheman.melicoupon.strategy.StrategyFactory;

/**
 * This class represents the service which holds the business logic for getting the list of items of a coupon
 */
@Service
public class CouponService {

    @Autowired
    private StrategyFactory strategyFactory;

    @Autowired
    private MeliService meliService;

    @Autowired
    private AppConfiguration appConfiguration;

    @Autowired
    private ItemMemory itemMemory;

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
    public HashMap<String, Double> getItemsForCoupon(String[]favItems, Double maxAmount){
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        HashMap<String, Double> itemsWithPrice = itemMemory.GetItemsByIdsFromCache(favItems);
        List<String> meliItems = Arrays.stream(favItems).filter(i -> !itemsWithPrice.keySet().stream().anyMatch(f -> f.equals(i))).collect(Collectors.toList());

        List<List<String>> subsets = Lists.partition(meliItems, appConfiguration.getMeliItemsApiMaxRequest());
        for (List<String> list : subsets) {
            executor.submit(() -> {
                HashMap<String, Double> price = meliService.GetItemPricesByIds(String.join(",", list));    
                price.entrySet().stream().forEach(i -> {
                    itemsWithPrice.put(i.getKey(), i.getValue());
                    itemMemory.AddItemToCache(i.getKey(), i.getValue());
                });
            });
        }

        try {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (Exception e) {
        }

        HashMap<String, Double> selectedItems = strategyFactory.getStrategyResult(itemsWithPrice, maxAmount);
        itemMemory.IncreaseItemsCount(selectedItems.keySet().toArray(new String[selectedItems.keySet().size()]));
        return selectedItems;
    }

    /**
     * Returns the top five most used items stored in the memory cache.
     * @return a map containing the items IDs and the count of times used.
     */
    public HashMap<String, Integer> getTopFiveItems(){
        return itemMemory.GetTopFiveItems();
    }
    
}
