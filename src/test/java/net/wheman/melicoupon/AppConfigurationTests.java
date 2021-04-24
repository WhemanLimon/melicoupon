package net.wheman.melicoupon;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppConfigurationTests {

    @Autowired
    private AppConfiguration appConfiguration;

    @Test
    void getAppConfigurationKey_KnapSackTargetFrom(){
        assertNotNull(appConfiguration.getKnapSackTargetFrom());
    }

    @Test
    void getAppConfigurationKey_KnapSackTargetTo(){
        assertNotNull(appConfiguration.getKnapSackTargetTo());
    }

    @Test
    void getAppConfigurationKey_KnapSackItemsMax(){
        assertNotNull(appConfiguration.getKnapSackItemsMax());
    }

    @Test
    void getAppConfigurationKey_MeliItemsApiMaxRequest(){
        assertNotNull(appConfiguration.getMeliItemsApiMaxRequest());
    }
    
}
