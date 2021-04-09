package net.wheman.melicoupon.meli;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import net.wheman.melicoupon.item.Item;

public class MeliService {
    public List<Item> GetItemsPriceById(String itemsStr) throws IOException, InterruptedException{
        //String itemsParam = String.join(",", items.stream().map(Item::getId).collect(Collectors.toList()));

        HttpRequest request = HttpRequest.newBuilder(URI.create(String.format("https://api.mercadolibre.com/items?ids=%s", itemsStr)))
                                            .header("Content-Type", "application/json")
                                            .GET()
                                            .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String responseBody = response.body();
        var items = deserializeItems(responseBody);
        return items;
    }

    private List<Item> deserializeItems(String jsonString) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory factory = mapper.getTypeFactory();
        JavaType list = factory.constructCollectionType(List.class, ItemResponse.class);
        List<ItemResponse> response = mapper.readValue(jsonString, list);
       
        return response.stream().map(obj -> new Item(obj.getBody())).collect(Collectors.toList());
    }
}

