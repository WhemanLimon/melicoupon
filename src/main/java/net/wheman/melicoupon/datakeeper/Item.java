package net.wheman.melicoupon.datakeeper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String id_item;
    private Double price;
    private Instant lastPriceCheck;
    private int favCount;

    public Boolean ItemCacheIsExpired(){
        return ChronoUnit.HOURS.between(lastPriceCheck, Instant.now()) > 1;
    }
}
