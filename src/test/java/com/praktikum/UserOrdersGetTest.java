package com.praktikum;

import api.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class UserOrdersGetTest {
    private ArrayList<String> userData;
    private User user;
    private Response userCreateResponse;

    int userDataSetQuantity;
    int signsQuantity;
    String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userDataSetQuantity = 1;
        signsQuantity = 7;
        userData = Utils.getUserData(userDataSetQuantity, signsQuantity);
        user = new User(userData.get(0), userData.get(1), userData.get(2));

        userCreateResponse = UserClient.sendPostRegisterUser(user);
        token = userCreateResponse.then().extract().path("accessToken");
        System.out.println(userData.get(0) + " " + userData.get(1) + " " + userData.get(2) + "\n" + token);


    }
    @Test
    @DisplayName("Get Authorized User Orders successfull.")
    @Description("Get User orders wih authorization.")
    public void testGetAuthorizedUserOrdersListSuccessfull() throws Exception {
       Integer id1 = Utils.createAnOrder(token, 2);
        Integer id2 = Utils.createAnOrder(token, 3);
        Response getUserOrders = UserClient.sendGetUserOrderList(token);
        int userTotalOrdersToday = getUserOrders.then().extract().path("totalToday");
        Assert.assertEquals("Total orders of the new user today doesn't equals to", 2, userTotalOrdersToday);
    }

    @Test
    @DisplayName("GetUser Orders failed without authorization.")
    @Description("GetUser Orders failed because authorization is absent.")
    public void testGetNonAuthorizedUserOrdersListFailed() throws Exception {
        Integer id1 = Utils.createAnOrder(token, 2);
        Integer id2 = Utils.createAnOrder(token, 3);

        Response getUserOrders = given()
                .header("Content-type","application/json")
                .and()
                .when()
                .get("/api/orders");

        int statusCodeResponce = getUserOrders.then().extract().statusCode();
        Assert.assertEquals("Total orders of the new user today doesn't equals to", 401, statusCodeResponce);
    }

    @After
    public void clearUsers(){
        UserClient.sendDeleteUser(token);
    }
}
