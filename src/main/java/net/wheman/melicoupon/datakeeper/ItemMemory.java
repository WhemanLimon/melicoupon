package net.wheman.melicoupon.datakeeper;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

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
public class ItemMemory {

    private static List<Item> cachedItems = new ArrayList<Item>();

    public static List<Item> getCachedItems() {
        return cachedItems;
    };

    /**
     * Retrieves an item from the cache searching by the item ID.
     * 
     * @param itemId The ID of the Item to retrieve.
     * @return A single {@linkplain Item}
     */
    public static Item GetItemByIdFromCache(String itemId) {
        Optional<Item> item = cachedItems.stream().filter(i -> itemId.equals(i.getId_item())).findFirst();
        return item.isPresent() ? item.get() : null;
    }

    /**
     * Creates a new {@linkplain Item} with the given ID and price and stores it in the cache.
     * The value of LastPriceCheck attribute is initialized with {@link Instant} 'now' method.
     * 
     * @param itemId The ID of the Item to store
     * @param price   The price of the Item to store
     */
    public static void AddItemToCache(String itemId, Double price) {
        cachedItems.add(new Item(itemId, price, Instant.now(), 0));
    }

    /**
     * Increases by 1 the amount of times an {@linkplain Item} has been used in a coupon.
     * 
     * @param itemId The ID of the item to increase count
     */
    public static void IncreaseItemCount(String itemId) {
        Item cacheItem = GetItemByIdFromCache(itemId);
        cacheItem.setFavCount(cacheItem.getFavCount() + 1);
    }

    /**
     * Returns the top five most used items by coupons.
     * @return A map of five entries with each item and its used count 
     */
    public static HashMap<String, Integer> GetTopFiveItems() {
        HashMap<String, Integer> items = cachedItems.stream().filter(i -> i.getFavCount() > 0)
                .sorted(Comparator.comparingInt(e -> -e.getFavCount())).limit(5)
                .collect(Collectors.toMap(Item::getId_item, Item::getFavCount, (a, b) -> {
                    throw new AssertionError();
                }, LinkedHashMap::new));

        return items;
    }
}
