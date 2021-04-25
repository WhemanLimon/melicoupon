package net.wheman.melicoupon.coupon;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class CouponControllerTests {

    @Autowired
    CouponController couponController;

    @Test
    public void givenEmptyItemId_whenItemIsRetrieved_then404IsReceived() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(String.format("https://api.mercadolibre.com/items?ids=%s", "")))
        .header("Content-Type", "application/json")
        .GET()
        .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.statusCode() == HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenMoreThan20ItemsId_whenItemIsRetrieved_then400IsReceived() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.mercadolibre.com/items?ids=MLA792238696,MLA768430699,MLA750188014,MLA774448514,MLA791881020,MLA779964373,MLA759942879,MLA766832798,MLA791074957,MLA792643250,MLA753491597,MLA756385586,MLA767418898,MLA764746727,MLA760268876,MLA780917124,MLA784241563,MLA749083853,MLA773561899,MLA767858883,MLA770556569,MLA772802098,MLA750064467"))
        .header("Content-Type", "application/json")
        .GET()
        .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.statusCode() == HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void givenUpTo20ItemsId_whenItemIsRetrieved_then200IsReceived() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.mercadolibre.com/items?ids=MLA792238696,MLA768430699,MLA750188014,MLA774448514,MLA791881020,MLA779964373,MLA759942879,MLA766832798,MLA791074957,MLA792643250,MLA753491597,MLA756385586,MLA767418898,MLA764746727,MLA760268876,MLA780917124,MLA784241563,MLA749083853,MLA773561899,MLA767858883"))
        .header("Content-Type", "application/json")
        .GET()
        .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.statusCode() == HttpStatus.OK.value());
    }

    @Test
    void fromControllerGetCoupon(){
        String[] ids = {"MLA748119382","MLA749371233","MLA755290360","MLA784242531"};
        CouponRequest request = new CouponRequest(ids, 10000.0);
        assertTrue(couponController.GetCoupon(request).getTotal() > 0);
    }
    
}
