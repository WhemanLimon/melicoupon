package net.wheman.melicoupon.meli;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.IntNode;

import net.wheman.melicoupon.item.Item;

public class MeliService {
    public List<Item> GetItemsPriceById(String items) throws IOException, InterruptedException{
        //String itemsParam = String.join(",", items.stream().map(Item::getId).collect(Collectors.toList()));

        HttpRequest request = HttpRequest.newBuilder(URI.create(String.format("https://api.mercadolibre.com/items?ids=%s", items)))
                                            .header("Content-Type", "application/json")
                                            .GET()
                                            .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String out = response.body();
        var mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Item.class, new ItemDeserializer());
        mapper.registerModule(module);
        Item it = mapper.readValue(out, Item.class);
        var its = new ArrayList<Item>();
        its.add(it);
        return its;
    }
}

final class ItemDeserializer extends StdDeserializer<Item> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ItemDeserializer() {
        this(null);
    }

    public ItemDeserializer(Class<?> vc) {
        super(vc);
    }
    
    @Override
    public Item deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode bodyNode = ((IntNode) node.get("body"));
        String id = bodyNode.get("id").asText();
        String itemName = bodyNode.get("title").asText();
        Float price = (Float) bodyNode.get("price").numberValue();

        return new Item(id, itemName, price, 1, Instant.now());
    }
    
}

