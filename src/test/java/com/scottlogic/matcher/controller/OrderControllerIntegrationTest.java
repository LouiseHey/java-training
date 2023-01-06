package com.scottlogic.matcher.controller;

import com.scottlogic.matcher.controller.dto.OrderDto;
import com.scottlogic.matcher.controller.dto.TradeDto;
import com.scottlogic.matcher.models.Action;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void whenGetBuyOrders_ThenBuyOrdersReturned() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("userId", "userId");
        requestParams.put("price", 10);
        requestParams.put("quantity", 10);
        requestParams.put("action", "BUY");

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).
        when().
                post("/order").
        then().
                statusCode(200);

        List<OrderDto> orders =
                when().
                        get("/order/buy").
                then().
                        statusCode(200).
                extract().
                        as(new TypeRef<>() {});

        assertThat(orders, hasSize(1));
        assertThat(orders.get(0).getUserId(), equalTo("userId"));
        assertThat(orders.get(0).getPrice(), equalTo(10));
        assertThat(orders.get(0).getQuantity(), equalTo(10));
        assertThat(orders.get(0).getAction(), equalTo(Action.BUY));
    }

    @Test
    public void whenGetSellOrders_ThenSellOrdersReturned() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("userId", "userId");
        requestParams.put("price", 10);
        requestParams.put("quantity", 10);
        requestParams.put("action", "SELL");

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).
        when().
                post("/order").
        then().
                statusCode(200);

        List<OrderDto> orders =
                when().
                        get("/order/sell").
                then().
                        statusCode(200).
                extract().
                        as(new TypeRef<>() {});

        assertThat(orders, hasSize(1));
        assertThat(orders.get(0).getUserId(), equalTo("userId"));
        assertThat(orders.get(0).getPrice(), equalTo(10));
        assertThat(orders.get(0).getQuantity(), equalTo(10));
        assertThat(orders.get(0).getAction(), equalTo(Action.SELL));
    }

    @Test
    public void givenBuyOrderInList_whenPlaceSellOrder_ThenTradeReturned() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("userId", "userId");
        requestParams.put("price", 10);
        requestParams.put("quantity", 10);
        requestParams.put("action", "BUY");

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).
        when().
                post("/order").
        then().
                statusCode(200);

        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("userId", "userId");
        requestParams2.put("price", 10);
        requestParams2.put("quantity", 10);
        requestParams2.put("action", "SELL");

        List<TradeDto> trades =
                given().
                        header("Content-Type", "application/json").
                        body(requestParams2.toJSONString()).
                when().
                        post("/order").
                then().
                        statusCode(200).
                extract().
                        as(new TypeRef<>() {});

        assertThat(trades, hasSize(1));
        assertThat(trades.get(0).getPrice(), equalTo(10));
        assertThat(trades.get(0).getQuantity(), equalTo(10));
    }

    @Test
    public void givenNegativeQuantity_whenPlaceOrder_ThenBadRequest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("userId", "userId");
        requestParams.put("price", 10);
        requestParams.put("quantity", -10);
        requestParams.put("action", "BUY");

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).
                when().
                post("/order").
                then().
                statusCode(400);

        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("userId", "userId");
        requestParams2.put("price", 10);
        requestParams2.put("quantity", -10);
        requestParams2.put("action", "SELL");

        given().
                header("Content-Type", "application/json").
                body(requestParams2.toJSONString()).
                when().
                post("/order").
                then().
                statusCode(400);
    }

    @Test
    public void givenNegativePrice_whenPlaceOrder_ThenBadRequest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("userId", "userId");
        requestParams.put("price", -10);
        requestParams.put("quantity", -10);
        requestParams.put("action", "BUY");

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).
                when().
                post("/order").
                then().
                statusCode(400);

        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("userId", "userId");
        requestParams2.put("price", -10);
        requestParams2.put("quantity", 10);
        requestParams2.put("action", "SELL");

        given().
                header("Content-Type", "application/json").
                body(requestParams2.toJSONString()).
                when().
                post("/order").
                then().
                statusCode(400);
    }
}