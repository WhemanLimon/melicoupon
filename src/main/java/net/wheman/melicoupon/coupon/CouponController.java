package net.wheman.melicoupon.coupon;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        for (String item : favItems) {
            var price = meliService.GetItemPriceById(item);    
            if(price != null){
                itemsWithPrice.put(item, price);
            }
        }
        
        var selectedItems = KnapSackHelper.backTrackDp(itemsWithPrice, maxAmount);

        return selectedItems;
    }
}

  
final class KnapSackHelper {

    static HashMap<String, Double> backTrackDp(HashMap<String, Double> items, Double target){
        Double[] prices = items.values().toArray(new Double[items.size()]);
        int pricesLength = prices.length;
        HashMap<String, Double> selectedItems = new HashMap<String, Double>();

        int sum, i, k;
        Double dp[][] = new Double[pricesLength + 1][(int) (target + 0)];
        for (sum = 0; sum < target; sum++){
            dp[pricesLength][sum]= (double) sum;
        }

        Double pick, leave;

        for (i = pricesLength - 1;i >= 0;i--){
            for(k = (int) (target + 0); k > 0; k--){
                pick = 0.0;
                if(k + prices[i] <= target){
                    pick = dp[i + 1][(int) (k + prices[i] - 1)];
                }
                leave = dp[i + 1][k - 1];
                dp[i][k - 1] = Math.max(pick, leave);

            }
        }

        reconstruct(0, 0, dp, pricesLength, prices, items, selectedItems);
        return selectedItems;
    }

    static void reconstruct(int i,  int w, Double[][] dp, int itemsLength, Double[] prices, HashMap<String, Double> items, HashMap<String, Double> selectedItems){
        if(i != itemsLength){
            if(dp[i][w] > dp[i+1][w]){
                Optional<String> key = items.entrySet().stream().filter(e -> e.getValue() == prices[i]).map(Map.Entry::getKey).findFirst();
                if(!key.isEmpty() && !selectedItems.containsKey(key.get())){
                    selectedItems.put(key.get(), prices[i]);
                }
                reconstruct(i + 1, (int) (w + prices[i]), dp, itemsLength, prices, items, selectedItems);
            }else{
                reconstruct(i + 1, w, dp, itemsLength, prices, items, selectedItems);
            }
        }
    }
}
