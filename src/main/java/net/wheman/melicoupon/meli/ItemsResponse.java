package net.wheman.melicoupon.meli;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a wrapper POJO for the MELI's Items API response. It holds the HTTP status response code and the response body. 
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsResponse {
    private int code;
    private ItemResponse body;
}

