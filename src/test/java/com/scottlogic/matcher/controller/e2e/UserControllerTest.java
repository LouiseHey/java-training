package com.scottlogic.matcher.controller.e2e;

import com.scottlogic.matcher.controller.e2e.util.DatabaseUtil;
import com.scottlogic.matcher.controller.e2e.util.TestConstants;
import com.scottlogic.matcher.models.User;
import io.restassured.RestAssured;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        cleanUp();
        User user = new User(TestConstants.USERNAME, TestConstants.PASSWORD);
        DatabaseUtil.insertUser(user);
    }

    @AfterEach
    public void cleanUp() {
        DatabaseUtil.deleteUser(TestConstants.USERNAME);
        DatabaseUtil.deleteUser(TestConstants.USERNAME2);
    }

    @Test
    public void givenUserExistsInDb_whenLogin_Then200Returned() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", TestConstants.USERNAME);
        requestParams.put("password", TestConstants.PASSWORD);

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).
                when().
                post("/auth/signin").
                then().
                statusCode(200);
    }

    @Test
    public void givenUserDoesNotExistInDb_whenLogin_Then401Returned() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", TestConstants.USERNAME_NONEXISTENT);
        requestParams.put("password", TestConstants.PASSWORD_NONEXISTENT);

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).
                when().
                post("/auth/signin").
                then().
                statusCode(401);
    }

    @Test
    public void givenUserDoesNotExistInDb_whenSignUp_Then200Returned() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", TestConstants.USERNAME2);
        requestParams.put("password", TestConstants.PASSWORD2);

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).
                when().
                post("/auth/signup").
                then().
                statusCode(200);
    }

    @Test
    public void givenUserAlreadyExistsInDb_whenSignUp_Then409Returned() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", TestConstants.USERNAME);
        requestParams.put("password", TestConstants.PASSWORD);

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).
                when().
                post("/auth/signup").
                then().
                statusCode(409);
    }
}
