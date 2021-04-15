package net.wheman.melicoupon.meli;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This class represents a POJO for the MELI's Items API response. It is used for deserializing {@linkplain ItemsResponse} response body into Item objects.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemResponse {
        private String id;
        private String title;
        private Double price;
}
