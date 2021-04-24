package net.wheman.melicoupon.meli;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * This class represents a POJO for the MELI's Items API response. It is used for deserializing {@linkplain ItemsResponse} response body into Item objects.
 */
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemResponse {
        private String id;
        private String title;
        private Double price;
}
