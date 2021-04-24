package net.wheman.melicoupon.meli;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a wrapper POJO for the MELI's Items API response. It holds the HTTP status response code and the response body. 
 */

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsResponse {
    private int code;
    private ItemResponse body;
}

