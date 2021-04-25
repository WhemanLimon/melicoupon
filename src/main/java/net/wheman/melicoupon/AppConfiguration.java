package net.wheman.melicoupon;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
/**
 * This class represents a component used for mapping key values from the {@code application.properties} file that will be injected later for usage.
 */

@Component
@Getter
public class AppConfiguration implements InitializingBean {

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

    @Value("${cache.item.price.durationhours}")
    private int CacheItemPriceDurationHours;

    @Value("${cache.items.top.count}")
    private int CacheItemsTopCount;

    public AppConfiguration() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if(System.getenv("AWS_REGION") != null){
            this.KnapSackTargetFrom = Integer.parseInt(System.getenv("strategy_knapsack_target_from"));
            this.KnapSackTargetTo = Integer.parseInt(System.getenv("strategy_knapsack_target_to"));
            this.KnapSackItemsMax = Integer.parseInt(System.getenv("strategy_knapsack_items_max"));
            this.MeliItemsApiUrl = System.getenv("meli_items_api_url");
            this.MeliItemsApiMaxRequest = Integer.parseInt(System.getenv("meli_items_api_maxrequest"));
            this.CacheItemPriceDurationHours = Integer.parseInt(System.getenv("cache_item_price_durationhours"));
            this.CacheItemsTopCount = Integer.parseInt(System.getenv("cache_items_top_count"));
        }
        
    }
    
}
