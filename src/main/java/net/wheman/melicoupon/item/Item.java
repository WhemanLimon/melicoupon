package net.wheman.melicoupon.item;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.wheman.melicoupon.meli.ItemResponse;

@Data
@AllArgsConstructor
public class Item {
    private String id;
    private String title;
    private Float price;
    private int quantity;
    private Instant lastPriceCheck;

    public Item(ItemResponse.Item itemResponse) {
        super();
        this.id = itemResponse.id;
        this.title = itemResponse.title;
        this.price = itemResponse.price;
        this.lastPriceCheck = Instant.now();
    }

}