package net.wheman.melicoupon.coupon;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;

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
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        Arrays.stream(favItems).forEach(fi -> {
            if(ItemMemory.getCachedItems().stream().anyMatch(ci -> fi.equals(ci.getId_item()) && !ci.ItemCacheIsExpired())){
                executor.submit(() -> { 
                    var itemFromCache = ItemMemory.GetItemByIdFromCache(fi);
                        itemsWithPrice.put(itemFromCache.getId_item(), itemFromCache.getPrice());
                });
            }else{
                executor.submit(() -> {
                    var price = meliService.GetItemPriceById(fi);    
                    if(price != null) {
                        itemsWithPrice.put(fi, price);
                        ItemMemory.AddItemToCache(fi, price);
                    }
                });
            }
        });

        try {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (Exception e) {
        }

        var selectedItems = KnapSackHelper.BackTrackDp(itemsWithPrice, maxAmount);
        System.out.println(Instant.now().toString() + ";" + "ALL ITEMS" + ";" + "Start IncreaseItemCount");
        selectedItems.entrySet().stream().forEach(i -> ItemMemory.IncreaseItemCount(i.getKey()));
        System.out.println(Instant.now().toString() + ";" + "ALL ITEMS" + ";" + "End IncreaseItemCount");
        System.out.println(Instant.now().toString() + ";" + "ALL ITEMS" + ";" + "End getItemsForCoupon()");
        return selectedItems;
    }
}
