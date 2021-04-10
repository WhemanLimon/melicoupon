package net.wheman.melicoupon.coupon;

import java.util.HashMap;

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
        Float total = (float) 0;
        for (Float item : itemsForCoupon.values()) {
            total += item;
        }
        return new CouponResponse(items, total);
    }
    
    private HashMap<String, Float> getItemsForCoupon(String[]favItems, Float maxAmount){
        var meliService = new MeliService();
        HashMap<String, Float> itemsWithPrice = new HashMap<String, Float>();
        Float total = (float) 0;
        for (String item : favItems) {
            var price = meliService.GetItemPriceById(item);    
            if(price != null && (total + price < maxAmount )){
                itemsWithPrice.put(item, price);
                total += price;
            }
        }
        
        return itemsWithPrice;
    }
}
