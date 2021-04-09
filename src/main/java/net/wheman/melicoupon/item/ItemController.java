package net.wheman.melicoupon.item;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.wheman.melicoupon.meli.MeliService;

@RestController
public class ItemController {
    
    @GetMapping("/items")
    public List<Item> GetItemById(@RequestParam(value = "amount", defaultValue = "0") int amount, @RequestParam(value = "ids", defaultValue = "") String ids) throws IOException, InterruptedException {
        if(ids.isEmpty())
        {
            ids = "MLA740141672";
        }

        var items = new MeliService().GetItemsPriceById(ids);

        return items.stream().filter(i -> i.getPrice() != null && i.getPrice() >= amount).collect(Collectors.toList());
    }
}
