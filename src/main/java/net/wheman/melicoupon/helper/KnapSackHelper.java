package net.wheman.melicoupon.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class KnapSackHelper {

    public static HashMap<String, Double> BackTrackDp(HashMap<String, Double> items, Double target){
        Double[] prices = items.values().toArray(new Double[items.size()]);
        int pricesLength = prices.length;
        HashMap<String, Double> selectedItems = new HashMap<String, Double>();

        int sum, i, k;
        Double dp[][] = new Double[pricesLength + 1][(int) (target + 1)];
        for (sum = 0; sum <= target; sum++){
            dp[pricesLength][sum]= (double) sum;
        }

        Double pick, leave;

        for (i = pricesLength - 1;i >= 0;i--){
            for(k = (int) (target + 0); k >= 0; k--){
                pick = 0.0;
                if(k + prices[i] <= target){
                    pick = dp[i + 1][(int) (k + prices[i])];
                }
                leave = dp[i + 1][k];
                dp[i][k] = Math.max(pick, leave);

            }
        }

        reconstruct(0, 0, dp, items, selectedItems);
        return selectedItems;
    }

    static void reconstruct(int i,  int w, Double[][] dp, HashMap<String, Double> items, HashMap<String, Double> selectedItems){
        Double[] prices = items.values().toArray(new Double[items.size()]);
        var itemsLength = prices.length;
        
        if(i != itemsLength){
            if(dp[i][w] > dp[i+1][w]){
                Optional<String> key = items.entrySet().stream().filter(e -> e.getValue() == prices[i]).map(Map.Entry::getKey).findFirst();
                if(!key.isEmpty() && !selectedItems.containsKey(key.get())){
                    selectedItems.put(key.get(), prices[i]);
                }
                reconstruct(i + 1, (int) (w + prices[i]), dp, items, selectedItems);
            }else{
                reconstruct(i + 1, w, dp, items, selectedItems);
            }
        }
    }
}
