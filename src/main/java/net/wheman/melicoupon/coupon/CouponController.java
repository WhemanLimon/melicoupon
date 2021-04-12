package net.wheman.melicoupon.coupon;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.wheman.melicoupon.meli.MeliService;
import net.wheman.melicoupon.helper.KnapSackHelper;

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
        for (String item : favItems) {
            var price = meliService.GetItemPriceById(item);    
            if(price != null){
                itemsWithPrice.put(item, price);
            }
        }
        
        var selectedItems = KnapSackHelper.BackTrackDp(itemsWithPrice, maxAmount);

        return selectedItems;
    }
}
