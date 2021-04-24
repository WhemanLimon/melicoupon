package net.wheman.melicoupon.meli;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import net.wheman.melicoupon.AppConfiguration;

/**
 * This class represents a service used to invoke MELI's Items API using an {@link HttpClient} to make the request and process the response.
 */
@Service
public class MeliService {

    private final AppConfiguration appConfiguration;

    @Autowired
    public MeliService(AppConfiguration appConfiguration){
        this.appConfiguration = appConfiguration;
    }

    /**
     * Invokes MELI's Items API with the given item IDs to retrieve each item's price.
     * 
     * @param itemIds A string of IDs separated with 'comma'. ie.: {@code MLA1,MLA2,MLA3...}
     * @return A key-value pair with the item ID and its price.
     */
    public HashMap<String, Double> GetItemPricesByIds(String itemIds){

        HttpRequest request = HttpRequest.newBuilder(URI.create(String.format(appConfiguration.getMeliItemsApiUrl(), itemIds)))
                                            .header("Content-Type", "application/json")
                                            .GET()
                                            .build();

        String responseBody = null;
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            responseBody = response.body();
            if(response.statusCode() != HttpStatus.OK.value()){
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        ObjectMapper mapper = new ObjectMapper();
        List<ItemsResponse> itemResponse = null;
        
        try {
            itemResponse = mapper.readValue(responseBody, new TypeReference<List<ItemsResponse>>(){});
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HashMap<String, Double> itemsPrice = new HashMap<String, Double>();
        for (ItemsResponse it : itemResponse) {
            itemsPrice.put(it.getBody().getId(), it.getBody().getPrice());
        }

        return itemsPrice;
    }
}

