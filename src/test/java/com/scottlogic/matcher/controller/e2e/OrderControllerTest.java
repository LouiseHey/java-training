package com.scottlogic.matcher.controller.e2e;

import com.scottlogic.matcher.controller.dto.OrderDto;
import com.scottlogic.matcher.controller.dto.TradeDto;
import com.scottlogic.matcher.controller.e2e.util.DbTestUtil;
import com.scottlogic.matcher.controller.e2e.util.TestConstants;
import com.scottlogic.matcher.models.Action;
import com.scottlogic.matcher.models.User;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
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
class OrderControllerTest {

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        cleanUp();

        User user = new User(TestConstants.USERNAME, TestConstants.PASSWORD);
        DbTestUtil.insertUser(user);

        this.token = DbTestUtil.retrieveToken(user);
    }

    @AfterAll
    static void cleanUp() {
        DbTestUtil.deleteUser(TestConstants.USERNAME);
        DbTestUtil.cleanOrders();
        DbTestUtil.cleanTrades();
    }

    @Test
    public void whenGetBuyOrders_ThenBuyOrdersReturned() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("price", 10);
        requestParams.put("quantity", 10);
        requestParams.put("action", "BUY");

        given().
                header("Content-Type", "application/json").
                header(TestConstants.AUTH_HEADER, token).
                body(requestParams.toJSONString()).
        when().
                post("/order").
        then().
                statusCode(200);

        List<OrderDto> orders =
                given().
                        header(TestConstants.AUTH_HEADER, token).
                when().
                        get("/order/buy").
                then().
                        statusCode(200).
                extract().
                        as(new TypeRef<>() {});

        assertThat(orders, hasSize(1));
        assertThat(orders.get(0).getUsername(), equalTo(TestConstants.USERNAME));
        assertThat(orders.get(0).getPrice(), equalTo(10));
        assertThat(orders.get(0).getQuantity(), equalTo(10));
        assertThat(orders.get(0).getAction(), equalTo(Action.BUY));
    }

    @Test
    public void whenGetSellOrders_ThenSellOrdersReturned() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("price", 10);
        requestParams.put("quantity", 10);
        requestParams.put("action", "SELL");

        given().
                header("Content-Type", "application/json").
                header(TestConstants.AUTH_HEADER, token).
                body(requestParams.toJSONString()).
        when().
                post("/order").
        then().
                statusCode(200);

        List<OrderDto> orders =
                given().
                        header(TestConstants.AUTH_HEADER, token).
                when().
                        get("/order/sell").
                then().
                        statusCode(200).
                extract().
                        as(new TypeRef<>() {});

        assertThat(orders, hasSize(1));
        assertThat(orders.get(0).getUsername(), equalTo(TestConstants.USERNAME));
        assertThat(orders.get(0).getPrice(), equalTo(10));
        assertThat(orders.get(0).getQuantity(), equalTo(10));
        assertThat(orders.get(0).getAction(), equalTo(Action.SELL));
    }

    @Test
    public void givenBuyOrderInList_whenPlaceSellOrder_ThenTradeReturned() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("price", 10);
        requestParams.put("quantity", 10);
        requestParams.put("action", "BUY");

        given().
                header("Content-Type", "application/json").
                header(TestConstants.AUTH_HEADER, token).
                body(requestParams.toJSONString()).
        when().
                post("/order").
        then().
                statusCode(200);

        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("price", 10);
        requestParams2.put("quantity", 10);
        requestParams2.put("action", "SELL");

        List<TradeDto> trades =
                given().
                        header("Content-Type", "application/json").
                        header(TestConstants.AUTH_HEADER, token).
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
        requestParams.put("price", 10);
        requestParams.put("quantity", -10);
        requestParams.put("action", "BUY");

        given().
                header("Content-Type", "application/json").
                header(TestConstants.AUTH_HEADER, token).
                body(requestParams.toJSONString()).
                when().
                post("/order").
                then().
                statusCode(400);

        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("price", 10);
        requestParams2.put("quantity", -10);
        requestParams2.put("action", "SELL");

        given().
                header("Content-Type", "application/json").
                header(TestConstants.AUTH_HEADER, token).
                body(requestParams2.toJSONString()).
                when().
                post("/order").
                then().
                statusCode(400);
    }

    @Test
    public void givenNegativePrice_whenPlaceOrder_ThenBadRequest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("price", -10);
        requestParams.put("quantity", -10);
        requestParams.put("action", "BUY");

        given().
                header("Content-Type", "application/json").
                header(TestConstants.AUTH_HEADER, token).
                body(requestParams.toJSONString()).
                when().
                post("/order").
                then().
                statusCode(400);

        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("price", -10);
        requestParams2.put("quantity", 10);
        requestParams2.put("action", "SELL");

        given().
                header("Content-Type", "application/json").
                header(TestConstants.AUTH_HEADER, token).
                body(requestParams2.toJSONString()).
                when().
                post("/order").
                then().
                statusCode(400);
    }
}