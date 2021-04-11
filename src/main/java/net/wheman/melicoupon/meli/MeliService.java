package net.wheman.melicoupon.meli;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.springframework.http.HttpStatus;


public class MeliService {

    public Double GetItemPriceById(String itemId){
        
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

        return itemResponse.getPrice();
    }
}

