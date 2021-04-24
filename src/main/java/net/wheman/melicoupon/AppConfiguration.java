package net.wheman.melicoupon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
/**
 * This class represents a component used for mapping key values from the {@code application.properties} file that will be injected later for usage.
 */

@Component
@Data
public class AppConfiguration {

    @Value("${strategy.knapsack.target.from}")
    private int KnapSackTargetFrom;

    @Value("${strategy.knapsack.target.to}")
    private int KnapSackTargetTo;

    @Value("${strategy.knapsack.items.max}")
    private int KnapSackItemsMax;

    @Value("${meli.items.api.url}")
    private String MeliItemsApiUrl;

    @Value("${meli.items.api.maxrequest}")
    private int MeliItemsApiMaxRequest;

    public AppConfiguration() {
        
    }
    
}
