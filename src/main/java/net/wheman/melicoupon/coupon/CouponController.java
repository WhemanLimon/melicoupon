package net.wheman.melicoupon.coupon;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.wheman.melicoupon.meli.MeliService;
import net.wheman.melicoupon.datakeeper.ItemMemory;
import net.wheman.melicoupon.helper.KnapSackHelper;

@RestController
public class CouponController {
    
    @PostMapping("/coupon")
    public CouponResponse GetCoupon(@RequestBody CouponRequest request) {
        System.out.println(Instant.now().toString() + ";" + "ALL ITEMS" + ";" + "Got API request()");
        var itemsForCoupon = getItemsForCoupon(request.getItem_ids(), request.getAmount());
        String[] items = itemsForCoupon.keySet().toArray(new String[itemsForCoupon.keySet().size()]);
        Double total = (double) 0;
        for (Double item : itemsForCoupon.values()) {
            total += item;
        }
        System.out.println(Instant.now().toString() + ";" + "ALL ITEMS" + ";" + "Return API response()");
        return new CouponResponse(items, total);
    }

    @GetMapping("/coupon/stats")
    public CouponStatistics[] GetCouponStats(){
        var top5items = ItemMemory.GetTopFive();
        Iterator<Entry<String, Integer>> iterator = top5items.entrySet().iterator();
        CouponStatistics[] top5 = new CouponStatistics[top5items.size()];
        int i = 0;
        while (iterator.hasNext()) {
            var item = iterator.next();
            top5[i] = new CouponStatistics(item.getKey(), item.getValue());
            i++;
        }
        return top5;
    }
 
    private HashMap<String, Double> getItemsForCoupon(String[]favItems, Double maxAmount){
        System.out.println(Instant.now().toString() + ";" + "ALL ITEMS" + ";" + "Start getItemsForCoupon()");
        ItemMemory.SortItemsMemory();
        var meliService = new MeliService();
        HashMap<String, Double> itemsWithPrice = new HashMap<String, Double>();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<String> meliItems = new ArrayList<String>();

        Arrays.stream(favItems).forEach(fi -> {
            if(ItemMemory.getCachedItems().stream().anyMatch(ci -> fi.equals(ci.getId_item()) && !ci.ItemCacheIsExpired())){
                executor.submit(() -> { 
                    var itemFromCache = ItemMemory.GetItemByIdFromCache(fi);
                        itemsWithPrice.put(itemFromCache.getId_item(), itemFromCache.getPrice());
                });
            }else{
                meliItems.add(fi);
            }
        });

        List<List<String>> subsets = Lists.partition(meliItems, 20);
        for (List<String> list : subsets) {
            executor.submit(() -> {
                HashMap<String, Double> price = meliService.GetItemPricesByIds(String.join(",", list));    
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

        System.out.println(Instant.now().toString() + ";" + "ALL ITEMS" + ";" + "Start KnapSackHelper.BackTrackDp");
        var selectedItems = KnapSackHelper.BackTrackDp(itemsWithPrice, maxAmount);
        System.out.println(Instant.now().toString() + ";" + "ALL ITEMS" + ";" + "End KnapSackHelper.BackTrackDp");
        selectedItems.entrySet().stream().forEach(i -> ItemMemory.IncreaseItemCount(i.getKey()));
        System.out.println(Instant.now().toString() + ";" + "ALL ITEMS" + ";" + "End getItemsForCoupon()");
        return selectedItems;
    }
}
