package net.wheman.melicoupon.strategy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StrategyTests {

    @Autowired
    StrategyFactory strategyFactory;

    @Test
    void executeKnapSack(){
        HashMap<String, Double> items = new HashMap<String, Double>();
        items.put("MLA748119382", 100.0);
        items.put("MLA749371233", 200.0);
        items.put("MLA755290360", 300.0);
        assertTrue(strategyFactory.getStrategyResult(items, 800.0).keySet().size() == 3);
    }

    
    @Test
    void executeTopBottom(){
        HashMap<String, Double> items = new HashMap<String, Double>();
        items.put("MLA748119382", 100.0);
        items.put("MLA749371233", 200.0);
        items.put("MLA755290360", 300.0);
        assertTrue(strategyFactory.getStrategyResult(items, 100000.0).keySet().size() == 3);
    }

    @Test
    void executeTopBottomInstantMatch(){
        HashMap<String, Double> items = new HashMap<String, Double>();
        items.put("MLA748119382", 100.0);
        items.put("MLA749371233", 200.0);
        items.put("MLA755290360", 100000.0);
        assertTrue(strategyFactory.getStrategyResult(items, 100000.0).containsKey("MLA755290360"));
    }
    
}
