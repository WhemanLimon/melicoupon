package net.wheman.melicoupon.meli;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MeliServiceTests {

    @Autowired
    private MeliService meliService;
    
    @Test
    void callMeliService_thenCheckItemFound(){
        HashMap<String, Double> response = meliService.GetItemPricesByIds("MLA754490044");
        assertNotNull(response.entrySet().stream().findFirst());
    }

        
    @Test
    void callMeliService_thenCheckItemNotFound(){
        HashMap<String, Double> response = meliService.GetItemPricesByIds("MLA779055077");
        assertNull(response.entrySet().stream().findFirst().get().getValue());
    }

    @Test
    void callEmptyMeliService(){
        HashMap<String, Double> response = meliService.GetItemPricesByIds("");
        assertNull(response);  
    }
}
