package net.wheman.melicoupon.coupon;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.wheman.melicoupon.meli.MeliService;

@RestController
public class CouponController {
    
    @PostMapping("/coupon")
    public CouponResponse GetCoupon(@RequestBody CouponRequest request) {
        var itemsForCoupon = getItemsForCoupon(request.getItem_ids(), request.getAmount());
        String[] items = itemsForCoupon.keySet().toArray(new String[ itemsForCoupon.keySet().size()]);
        Double total = (double) 0;
        for (Double item : itemsForCoupon.values()) {
            total += item;
        }
        return new CouponResponse(items, total);
    }
    
    private HashMap<String, Double> getItemsForCoupon(String[]favItems, Double maxAmount){
        var meliService = new MeliService();
        HashMap<String, Double> itemsWithPrice = new HashMap<String, Double>();
        Double total = (double) 0;
        for (String item : favItems) {
            var price = meliService.GetItemPriceById(item);    
            if(price != null){
                itemsWithPrice.put(item, price);
            }
        }

        var sortedItems = itemsWithPrice.entrySet()
                .stream()
                .sorted(Comparator.comparingDouble(i -> -i.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, 
                        Map.Entry::getValue, 
                        (a, b) -> { throw new AssertionError(); }, 
                        LinkedHashMap::new
                ));
        HashMap<String, Double> filteredItemsWithPrice = new HashMap<String, Double>();
        Iterator<Map.Entry<String, Double>> it = sortedItems.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Double> entry = (Map.Entry<String, Double>)it.next();
            String key = entry.getKey().toString();
            Double value = (double)entry.getValue();
            if(total + value < maxAmount){
                filteredItemsWithPrice.put(key, value);
                total += value;
            }
            it.remove();
        }
        return filteredItemsWithPrice;
    }
}
