package net.wheman.melicoupon;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.wheman.melicoupon.datakeeper.ItemMemory;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class DataKeeperTests {

    @Autowired
    private ItemMemory itemMemory;

    private static HashMap<String, Double> request1 = new HashMap<String, Double>();

    @BeforeAll
    public static void setup(){
        request1.put("MLA1", (double) 10);
        request1.put("MLA2", (double) 20);
        request1.put("MLA3", (double) 30);
        request1.put("MLA4", (double) 40);
        request1.put("MLA5", (double) 50);
        request1.put("MLA6", (double) 55.5);
    }
         
    @Test
    @Order(1)
    void addMultipleItemsToCache_thenRetrieveItemFromCache(){
        for (Entry<String, Double> entry : request1.entrySet()) {
            itemMemory.AddItemToCache(entry.getKey(), entry.getValue());            
        }
        assertTrue(itemMemory.GetItemsByIdsFromCache(request1.keySet().toArray(new String[request1.size()])).size() == request1.size());
    }

    @Test
    @Order(2)
    void increaseItemCount_thenRetrieveTop(){
        itemMemory.IncreaseItemsCount(request1.keySet().toArray(new String[request1.size()]));
        assertTrue(itemMemory.GetTopItems().size() > 0);
    }
    
    @Test
    @Order(3)
    void retrieveTop(){
        assertTrue(itemMemory.GetTopItems().size() > 0);
    }
}
