package net.wheman.melicoupon.strategy;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

/**
 * This class represents a Strategy based on <b>Top-Bottom sorting</b>.
 * <p>
 * This approach sort the input ascending and iterates adding each value until the target is reached.
 * Then it sorts again the input descending and iterates adding each value until the target is reached.
 * Finally it returns the subitems whose sum is closest to the target.
*/
public class TopBottom implements StrategyHandler {

    /**
     * Calculates the items that will componse the coupon by executing the <b>Top-Bottom sorting</b> logic.
     * <p>
     * If there is an item that matches exactly the target price it is returned and KP algorithm is skipped.
     * @param items favorite items being considered into coupon's limit. 
     * @param target the coupon's price limit.
     * @return {@link HashMap} with the subset of items selected for the coupon.
     */
    @Override
    public HashMap<String, Double> ApplyStrategy(HashMap<String, Double> items, Double target) {

        Double sumA = 0.0;
        Double sumD = 0.0;
        HashMap<String, Double> resultA = new HashMap<String, Double>();
        HashMap<String, Double> resultD = new HashMap<String, Double>();

        items = new HashMap<String, Double>(Maps.filterEntries(items, i -> i.getValue() != null && i.getValue() <= target));

        Optional<Entry<String, Double>> instantMatch = items.entrySet().stream().filter(i -> i.getValue().equals(target)).findFirst();
        if(instantMatch.isPresent())
        {
            resultA.put(instantMatch.get().getKey(), instantMatch.get().getValue());
            return (HashMap<String, Double>) resultA;
        }

      
        for (Entry<String, Double> entry : sortMap(items, "A").entrySet()) {
            if(sumA + entry.getValue() <= target){
                resultA.put(entry.getKey(), entry.getValue());
                sumA += entry.getValue();
            }
        }

        for (Entry<String, Double> entry : sortMap(items, "D").entrySet()) {
            if(sumD + entry.getValue() <= target){
                resultD.put(entry.getKey(), entry.getValue());
                sumD += entry.getValue();
            }
        }
        
        return sumA > sumD ? resultA : resultD;
    }    

    /**
     * Sorts the HashMap by its value ascending or descending
     * @param unsortedMap A hashmap
     * @param sortOrder
     * @return
     */
    private HashMap<String, Double> sortMap(HashMap<String, Double> unsortedMap, String sortOrder){
        if(sortOrder.equals("A")){
            HashMap<String, Double> sortedMap = unsortedMap.entrySet().stream()
            .sorted(Comparator.comparingDouble(e -> e.getValue()))
            .collect(Collectors.toMap(
                    HashMap.Entry::getKey,
                    HashMap.Entry::getValue,
                    (a, b) -> { throw new AssertionError(); },
                    LinkedHashMap::new
            ));
            return sortedMap;
        }else{
            HashMap<String, Double> sortedMap = unsortedMap.entrySet().stream()
            .sorted(Comparator.comparingDouble(e -> -e.getValue()))
            .collect(Collectors.toMap(
                    HashMap.Entry::getKey,
                    HashMap.Entry::getValue,
                    (a, b) -> { throw new AssertionError(); },
                    LinkedHashMap::new
            ));
            return sortedMap;
        }


        
    }
}
