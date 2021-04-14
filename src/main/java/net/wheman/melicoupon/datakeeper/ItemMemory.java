package net.wheman.melicoupon.datakeeper;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemMemory {

    private static List<Item> cachedItems = new ArrayList<Item>();
    
    public static List<Item> getCachedItems(){
        return cachedItems;
    };

    public static Item GetItemByIdFromCache(String id_item){
        System.out.println(Instant.now().toString() + ";" + id_item + ";" + "Start ItemMemory.GetItemByIdFromCache()");
        Optional<Item> item = getItemById(id_item);
        System.out.println(Instant.now().toString() + ";" + id_item + ";" + "End ItemMemory.GetItemByIdFromCache()");
        return item.isPresent() ? item.get() : null;
    }
    
    public static List<Item> GetItemsByIdsFromCache(String[] ids_item){
        List<Item> items = new ArrayList<Item>();
        for (String itemStr : ids_item) {
            var item = GetItemByIdFromCache(itemStr);
            if (item != null){
                items.add(item);
            }
        }
        return items;
    }

    public static void AddItemToCache(String item_id, Double price) {
        var existingItem = GetItemByIdFromCache(item_id);
        if(existingItem == null){
            cachedItems.add(new Item(item_id, price, Instant.now(), 0));
        }
    }

    public static void IncreaseItemCount(String item_id){
        var existingItem = GetItemByIdFromCache(item_id);
        if(existingItem != null){
            existingItem.setFavCount(existingItem.getFavCount() + 1);
        }
    }

    private static Optional<Item> getItemById(String id_item){
        System.out.println(Instant.now().toString() + ";" + id_item + ";" + "Find ItemMemory getItemById()");
        var item = cachedItems.stream().filter(i -> id_item.equals(i.getId_item())).findFirst();
        System.out.println(Instant.now().toString() + ";" + id_item + ";" + "Done ItemMemory getItemById()");
        return item;
    }

    public static HashMap<String, Integer> GetTopFive(){
        Map<String, Integer> items = cachedItems.stream()
                .filter(i -> i.getFavCount() > 0)
                .sorted(Comparator.comparingInt(e -> -e.getFavCount()))
                .limit(5)
                .collect(Collectors.toMap(
                    Item::getId_item,
                    Item::getFavCount,
                    (a, b) -> { throw new AssertionError(); },
                    LinkedHashMap::new)
        );
        
        return (HashMap<String, Integer>) items;
    }

    public static void SortItemsMemory(){
        Collections.sort(cachedItems);
    }
}
