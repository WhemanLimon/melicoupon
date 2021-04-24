package net.wheman.melicoupon.datakeeper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.wheman.melicoupon.AppConfiguration;

/**
 * This class contains a set of static methods to help manage an in-memory List
 * of {@linkplain Item}. 
 * <p>
 * It acts as a cache for speeding up "MELI's API Items"
 * invoke by reducing round-trips for items already retrieved. 
 * <p>
 * It also keep keep
 * a record of how many times an Item has been used for coupons and offers a
 * function to retrieve top five of most used items.
 */
@Repository
public class ItemMemory {

    @Autowired
    private AppConfiguration appConfiguration;

    private static List<Item> cachedItems = new ArrayList<Item>();

    /**
     * Retrieves items from the cache searching by the item ID.
     * 
     * @param item_ids The IDs of the items to retrieve.
     * @return a map of the items
     */
    public HashMap<String, Double> GetItemsByIdsFromCache(String[] item_ids){
        HashMap<String, Double> cacheItems = new HashMap<String, Double>();

        cachedItems.stream()
                    .filter(i -> Arrays.stream(item_ids).anyMatch(f -> !i.IsCacheExpired() && i.getId_item().equals(f)))
                    .forEach(i -> cacheItems.put(i.getId_item(), i.getPrice()));

        return cacheItems;
    }

    /**
     * Creates a new {@linkplain Item} with the given ID and price and stores it in the cache.
     * The value of LastPriceCheck attribute is initialized with {@link Instant} 'now' method.
     * <p>
     * If the item already exists it updates the price. This means that
     * 
     * @param itemId The ID of the Item to store
     * @param price   The price of the Item to store
     */
    public void AddItemToCache(String itemId, Double price) {
        Optional<Item> existingItem = cachedItems.stream().filter(i -> i.getId_item().equals(itemId)).findFirst();
        if(existingItem.isPresent()){
            existingItem.get().setPrice(price);
            existingItem.get().setLastPriceCheck(Instant.now());
        }else{
            cachedItems.add(new Item(itemId, price, Instant.now(), 0, appConfiguration.getCacheItemPriceDurationHours()));
        }
    }

    /**
     * Increases by 1 the amount of times an {@linkplain Item} has been used in a coupon.
     * 
     * @param item_ids The IDs of the items to increase count
     */
    public void IncreaseItemsCount(String[] item_ids){
        cachedItems.stream()
        .filter(i -> Arrays.stream(item_ids).anyMatch(f -> !i.IsCacheExpired() && i.getId_item().equals(f)))
        .forEach(i -> i.setFavCount(i.getFavCount() + 1));
    }

    /**
     * Returns the top five most used items by coupons.
     * @return A map of five entries with each item and its used count 
     */
    public HashMap<String, Integer> GetTopFiveItems() {
        HashMap<String, Integer> items = cachedItems.stream().filter(i -> i.getFavCount() > 0)
                .sorted(Comparator.comparingInt(e -> -e.getFavCount())).limit(5)
                .collect(Collectors.toMap(Item::getId_item, Item::getFavCount, (a, b) -> {
                    throw new AssertionError();
                }, LinkedHashMap::new));

        return items;
    }
}
