package net.wheman.melicoupon.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

/**
 * This class acts as a helper for the <b>KnapSack Problem algorithm</b>
 * <p>
 * The purpose of this algorithm is given a set of items with a price 
 * determine which items are included in a subset so that the total price 
 * for the subset is equal or nearest to the limit.
 * <p>
 * Considering KnapSack Problem purpose is to find the final total price of the subset without 
 * keeping track of the items that belongs to the subset itselfe, once the subset is defined 
 * it reverses the algorithm in order to retrieve the items IDs selected for the coupon.
*/
public class KnapSack implements StrategyHandler<KnapSack> {

    /**
     * Calculates the items that will componse the coupon by executing the <b>KnapSack Problem algorithm</b> logic.
     * <p>
     * If there is an item that matches exactly the target price it is returned and KP algorithm is skipped.
     * @param items favorite items being considered into coupon's limit. 
     * @param target the coupon's price limit.
     * @return {@link HashMap} with the subset of items selected for the coupon.
     */
    @Override
    public HashMap<String, Double> ApplyStrategy(HashMap<String, Double> items, Double target) {
        HashMap<String, Double> selectedItems = new HashMap<String, Double>();
        items = new HashMap<String, Double>(Maps.filterEntries(items, e -> e.getValue() <= target));

        Double[] prices = Collections2.filter(items.values(), item -> item <= target).toArray(Double[]::new);
        Optional<Entry<String, Double>> instantMatch = items.entrySet().stream().filter(i -> i.getValue().equals(target)).findFirst();
        if(instantMatch.isPresent())
        {
            selectedItems.put(instantMatch.get().getKey(), instantMatch.get().getValue());
            return (HashMap<String, Double>) selectedItems;
        }
        
        int pricesLength = prices.length;

        int sum, i, k;
        Double dp[][] = new Double[pricesLength + 1][(int) (target + 1)];
        for (sum = 0; sum <= target; sum++){
            dp[pricesLength][sum] = (double) sum;
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

        getItemsSubsetIds(0, 0, dp, items, selectedItems);
        return (HashMap<String, Double>) selectedItems;
    }

    /**
     * Reverses KnapSack Problem algorithm in order to determinate which items are part of the subset that maximizes the coupon.
     * The method is recursive and it runs until all subset items are passed through.
     * @param i index used for iterating each item.
     * @param w index used for iterating each item's price.
     * @param calculatedMatrix result matrix used for keeping calculated values and final result.
     * @param favoriteItems list of items with IDs and values to match for.
     * @param selectedItems list of items selected for the coupon's subset.
     */
    private static void getItemsSubsetIds(int i,  int w, Double[][] calculatedMatrix, HashMap<String, Double> favoriteItems, HashMap<String, Double> selectedItems){
        Double[] prices = favoriteItems.values().toArray(new Double[favoriteItems.size()]);
        int itemsLength = prices.length;
        
        if(i != itemsLength){
            if(calculatedMatrix[i][w] > calculatedMatrix[i+1][w]){
                Optional<String> key = favoriteItems.entrySet().stream().filter(e -> e.getValue() == prices[i]).map(Map.Entry::getKey).findFirst();
                if(!key.isEmpty() && !selectedItems.containsKey(key.get())){
                    selectedItems.put(key.get(), prices[i]);
                }
                getItemsSubsetIds(i + 1, (int) (w + prices[i]), calculatedMatrix, favoriteItems, selectedItems);
            }else{
                getItemsSubsetIds(i + 1, w, calculatedMatrix, favoriteItems, selectedItems);
            }
        }
    }
}
