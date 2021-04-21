package net.wheman.melicoupon;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.wheman.melicoupon.meli.MeliService;

@SpringBootTest
public class MeliServiceTests {
    
    @Test
    void callMeliService_thenCheckItemFound(){
        HashMap<String, Double> response = MeliService.GetItemPricesByIds("MLA754490044");
        assertNotNull(response.entrySet().stream().findFirst());
    }

        
    @Test
    void callMeliService_thenCheckItemNotFound(){
        HashMap<String, Double> response = MeliService.GetItemPricesByIds("MLA779055077");
        assertNull(response.entrySet().stream().findFirst().get().getValue());
    }

    @Test
    void callEmptyMeliService(){
        HashMap<String, Double> response = MeliService.GetItemPricesByIds("");
        assertNull(response);  
    }
}
