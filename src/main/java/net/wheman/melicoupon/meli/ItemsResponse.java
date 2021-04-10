package net.wheman.melicoupon.meli;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemsResponse {
    private int code;
    private Item body;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        public String id;
        public String title;
        public Float price;
        public int quantity;
        public Instant lastPriceCheck;
    }
}

