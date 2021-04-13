package net.wheman.melicoupon.coupon;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Iterator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.wheman.melicoupon.meli.MeliService;
import net.wheman.melicoupon.datakeeper.MapMemory;
import net.wheman.melicoupon.helper.KnapSackHelper;

@RestController
public class CouponController {
    
    @PostMapping("/coupon")
    public CouponResponse GetCoupon(@RequestBody CouponRequest request) {
        var itemsForCoupon = getItemsForCoupon(request.getItem_ids(), request.getAmount());
        String[] items = itemsForCoupon.keySet().toArray(new String[itemsForCoupon.keySet().size()]);
        Double total = (double) 0;
        for (Double item : itemsForCoupon.values()) {
            total += item;
        }
        return new CouponResponse(items, total);
    }

    @GetMapping("/coupon/stats")
    public CouponStatistics[] GetCouponStats(){
        var top5items = MapMemory.GetTopFive();
        Iterator<Entry<String, Long>> iterator = top5items.entrySet().iterator();
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
        var meliService = new MeliService();
        HashMap<String, Double> itemsWithPrice = new HashMap<String, Double>();
        for (String item : favItems) {
            var price = meliService.GetItemPriceById(item);    
            if(price != null){
                itemsWithPrice.put(item, price);
            }
        }
        
        var selectedItems = KnapSackHelper.BackTrackDp(itemsWithPrice, maxAmount);
        selectedItems.keySet().stream().forEach(i -> MapMemory.AddItemCount(i));

        return selectedItems;
    }
}
