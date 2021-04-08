package net.wheman.melicoupon.item;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private String id;
    private String name;
    private Float price;
    private int quantity;
    private Instant lastPriceCheck;
}
