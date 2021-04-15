package net.wheman.melicoupon.meli;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.springframework.http.HttpStatus;


public class MeliService {

    public Double GetItemPriceById(String itemId){
        System.out.println(Instant.now().toString() + ";" + itemId + ";" + "Start MeliService.GetItemPriceById()");
        HttpRequest request = HttpRequest.newBuilder(URI.create(String.format("https://api.mercadolibre.com/items/%s", itemId)))
                                            .header("Content-Type", "application/json")
                                            .GET()
                                            .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response;
        String responseBody = null;
        try {
            response = client.send(request, BodyHandlers.ofString());
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
        TypeFactory factory = mapper.getTypeFactory();
        JavaType itemType = factory.constructType(ItemResponse.class);
        ItemResponse itemResponse = null;
        try {
            itemResponse = mapper.readValue(responseBody, itemType);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(Instant.now().toString() + ";" + itemId + ";" + "End MeliService.GetItemPriceById()");
        return itemResponse.getPrice();
    }

    public HashMap<String, Double> GetItemPricesByIds(String itemIds){
        System.out.println(Instant.now().toString() + ";" + itemIds + ";" + "Start MeliService.GetItemPricesByIds()");
        HttpRequest request = HttpRequest.newBuilder(URI.create(String.format("https://api.mercadolibre.com/items?ids=%s", itemIds)))
                                            .header("Content-Type", "application/json")
                                            .GET()
                                            .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response;
        String responseBody = null;
        try {
            response = client.send(request, BodyHandlers.ofString());
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
        System.out.println(Instant.now().toString() + ";" + itemIds + ";" + "End MeliService.GetItemPricesByIds()");
        HashMap<String, Double> itemsPrice = new HashMap<String, Double>();
        for (ItemsResponse it : itemResponse) {
            if(it.getBody().getPrice() != null){
                itemsPrice.put(it.getBody().getId(), it.getBody().getPrice());
            }
        }
        return itemsPrice;
    }
}

