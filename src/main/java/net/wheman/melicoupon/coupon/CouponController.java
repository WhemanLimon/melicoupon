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
        //var selectedItems = KnapSackHelper.getSelectedItems((int)Math.round(maxAmount), itemsWithPrice);
        var selectedItems = KnapSackHelper.getSelectedItems2(maxAmount, itemsWithPrice);

        return selectedItems;
    }
}

  
final class KnapSackHelper {

    static HashMap<String, Double> getSelectedItems2(Double maxAmount, HashMap<String, Double> items){
        //Double[] prices = items.values().toArray(new Double[items.size()]);
        //var max = backTrack(prices, prices.length, maxAmount, (double) 0, 0);
        var max = backTrackDp(items, maxAmount);
        
        return max;
    }

    static Double backTrack(Double[] prices, int pricesLength, Double target, Double sum, int lastIndex){
        if(sum > target){
            return (double) 0;
        }
        if(lastIndex == pricesLength){
            return sum;
        }
        try {
            Double pick = backTrack(prices, pricesLength, target, sum + prices[lastIndex], ++lastIndex);
            Double leave = backTrack(prices, pricesLength, target, sum, ++lastIndex);
            return Math.max(pick, leave);            
        } catch (ArrayIndexOutOfBoundsException e) {
            return sum;
        }

    }

    static HashMap<String, Double> backTrackDp(HashMap<String, Double> items, Double target){
        Double[] prices = items.values().toArray(new Double[items.size()]);
        int pricesLength = prices.length;
        HashMap<String, Double> selectedItems = new HashMap<String, Double>();

        int sum, i, k;
        Double dp[][] = new Double[pricesLength + 1][(int) (target + 0)];
        for (sum = 0; sum < target; sum++){
            dp[pricesLength][sum] = (double) sum;
        }

        Double pick, leave;

        for (i = pricesLength - 1;i >= 0;i--){
            for(k = (int) (target + 0); k > 0; k--){
                pick = 0.0;
                if(k + prices[i] <= target){
                    pick = dp[i + 1][(int) (k + prices[i] - 1)];
                }
                leave = dp[i + 1][k - 1];
                var value = Math.max(pick, leave);
                dp[i][k - 1] = value;
                Optional<String> key = items.entrySet().stream().filter(e -> e.getValue() == value).map(Map.Entry::getKey).findFirst();
                if(!key.isEmpty() && !selectedItems.containsKey(key.get())){
                    selectedItems.put(key.get(), value);
                }
            }
        }

        System.out.println(dp[0][0]);
        return selectedItems;
    }    
      
    // Prints the items which are put 
    // in a knapsack of capacity W
    static HashMap<String, Double> getSelectedItems(int maxAmount, HashMap<String, Double> items)
    {
        HashMap<String, Double> selectedItems = new HashMap<String, Double>();
        Double[] prices = items.values().toArray(new Double[items.size()]);
        Double[] prices2 = items.values().toArray(new Double[items.size()]);
        int i, w, n;
        n = prices.length;

        Double K[][] = new Double[n + 1][(int) (maxAmount + 1)];
  
        // Build table K[][] in bottom up manner
        for (i = 0; i <= n; i++) {
            for (w = 0; w <= maxAmount; w++) {
                if (i == 0 || w == 0)
                    K[i][w] = (double) 0;
                else if (prices[i - 1] <= w){
                    var value = Math.max(prices2[i - 1] + K[i - 1][(int) (w - prices[i - 1])], K[i - 1][w]);
                    K[i][w] = value;
                    Optional<String> key = items.entrySet().stream().filter(e -> e.getValue() == value).map(Map.Entry::getKey).findFirst();
                    if(!key.isEmpty() && !selectedItems.containsKey(key.get())){
                        selectedItems.put(key.get(), value);
                    }
                }
                else
                    K[i][w] = K[i - 1][w];
            }
        }

        Double res = K[n][maxAmount];
        System.out.println(res);

        w = maxAmount;
        for (i = n; i > 0 && res > 0; i--) {
  
            // either the result comes from the top
            // (K[i-1][w]) or from (val[i-1] + K[i-1]
            // [w-wt[i-1]]) as in Knapsack table. If
            // it comes from the latter one/ it means
            // the item is included.
            if (res == K[i - 1][w])
                continue;
            else {
  
                // This item is included.
                System.out.print(prices[i - 1] + " ");
  
                // Since this weight is included its
                // value is deducted
                res = res - prices2[i - 1];
                w = (int) (w - prices[i - 1]);
            }
        }

        return selectedItems;
    }
}
