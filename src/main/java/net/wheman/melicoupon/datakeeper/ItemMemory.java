package net.wheman.melicoupon.datakeeper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemMemory {

    private static Collection<Item> itemsCount = new ArrayList<Item>();

    public static Item GetItemByIdFromCache(String id_item){
        Optional<Item> item = getItemById(id_item);
        return item.isPresent() ? item.get() : null;
    }
    
    public static Collection<Item> GetItemsByIdsFromCache(String[] ids_item){
        Collection<Item> items = new ArrayList<Item>();
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
            itemsCount.add(new Item(item_id, price, Instant.now(), 0));
        }
    }

    public static void IncreaseItemCount(String item_id){
        var existingItem = GetItemByIdFromCache(item_id);
        if(existingItem != null){
            existingItem.setFavCount(existingItem.getFavCount() + 1);
        }
    }

    private static Optional<Item> getItemById(String id_item){
        return itemsCount.stream().filter(i -> id_item.equals(i.getId_item())).findFirst();
    }

    public static HashMap<String, Integer> GetTopFive(){
        Map<String, Integer> items = itemsCount.stream()
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
}
