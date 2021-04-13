package net.wheman.melicoupon.datakeeper;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MapMemory {

    private static HashMap<String, Long> itemsCount = new HashMap<String, Long>();
    
    public static void AddItemCount(String id_item){
        if(itemsCount.containsKey(id_item)){
            itemsCount.replace(id_item, itemsCount.get(id_item) + 1);
        }else{
            itemsCount.put(id_item, (long) 1);
        }
    }

    public static HashMap<String, Long> GetTopFive(){
        int i = 0;
        LinkedHashMap<String, Long> items = itemsCount.entrySet().stream()
                .sorted(Comparator.comparingLong(e -> -e.getValue()))
                .collect(Collectors.toMap(
                    Map.Entry::getKey, 
                    Map.Entry::getValue,
                    (a, b) -> { throw new AssertionError(); },
                    LinkedHashMap::new)
        );
        
        HashMap<String, Long> topFiveItems = new HashMap<String, Long>();
        Iterator<Entry<String, Long>> iterator = items.entrySet().iterator();
        while (i < 5 && iterator.hasNext()) {
            Map.Entry<String, Long> item = (Entry<String, Long>) iterator.next();
            topFiveItems.put(item.getKey(), item.getValue());
            i++;
        }
        return topFiveItems;
    }
}
