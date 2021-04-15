package net.wheman.melicoupon.datakeeper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents an POJO item used in a coupon. 
 * It serves as an object for holding also the timestamp {@link Instant} of when the item got an item's price from MELI's Items API.
 * This will be used to establish an validity period in which the item's price is expired and must be retrieved again from MELI's Items API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private static final long MAX_ITEM_PRICE_VALIDITY = 1;

    private String id_item;
    private Double price;
    private Instant lastPriceCheck;
    private int favCount;

    /**
     * Indicates if the Item price timestamp has exceeded the validity window and must be retrieved again from MELI's Items API.
     * It calculates the amount ot time between lastPriceCheck attribute and {@link Instant} 'now' method.
     * @return True if the LastPriceCheck is older than {@value #MAX_ITEM_PRICE_VALIDITY} hour
     */
    public Boolean IsCacheExpired(){
        return ChronoUnit.HOURS.between(lastPriceCheck, Instant.now()) > MAX_ITEM_PRICE_VALIDITY;
    }
}
