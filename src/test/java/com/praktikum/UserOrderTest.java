package com.praktikum;

import api.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserOrderTest {
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
    }


    @Test
    @DisplayName("Order successfull")
    @Description("Create one order wih authorization")
    public void testOrderCreate() throws Exception {
        List<String> ingredientsId;

        List<String> ingrenientsNames = new ArrayList<>();
        ingrenientsNames.add("Мясо бессмертных моллюсков Protostomia");
        ingrenientsNames.add("Краторная булка N-200i");

        ingredientsId = Utils.getIngredient(0,ingrenientsNames);

        Order order = new Order(ingredientsId);
        Response orderCreateResponce = UserClient.sendPostOrderCreate(order,token);
        Assert.assertEquals("Authorized user has not created  Order with two ingredients ",true,orderCreateResponce.then().extract().path("success"));
    }

    @Test
    @DisplayName("Order successfull. All ingredients")
    @Description("Create one order wih authorization with all ingredients")
    public void testOrderCreateAllIngredients() throws Exception {
        List<String> ingredientsId;
        ingredientsId = Utils.getIngredient(DataBase.ingredientsMap().size(),null);
        for(int i=0; i< ingredientsId.size(); i++) {
            System.out.println(ingredientsId.get(i));
        }
        Order order = new Order(ingredientsId);
        Response orderCreateResponce = UserClient.sendPostOrderCreate(order,token);
        Assert.assertEquals("Authorized user has not created  Order with all possible ingredients",true,orderCreateResponce.then().extract().path("success"));
    }


    @Test
    @DisplayName("Order failed. Ingredients = empty. ")
    @Description("Order  can't be created without ingredients wih authorization ")
    public void testOrderIngredientsNullFailed() {
        List<String> ingredientsId = new ArrayList<>();
        Order order = new Order(ingredientsId);
        Response orderCreateResponce = UserClient.sendPostOrderCreate(order,token);
        Assert.assertEquals("Order shouldn't be created if ingredients key in body request equals to null ",
                false,orderCreateResponce.then().extract().path("success"));
    }


    @Test
    @DisplayName("Order failed.Access token incorrect value")
    @Description("Order  creatation failed without authorization:")
    public void testOrderCreateAuthorizationIncorrectFailed() throws Exception {
        List<String> ingredientsId;

        List<String> ingrenientsNames = new ArrayList<>();
        ingrenientsNames.add("Мясо бессмертных моллюсков Protostomia");
        ingrenientsNames.add("Краторная булка N-200i");

        ingredientsId = Utils.getIngredient(0,ingrenientsNames);

        Order order = new Order(ingredientsId);


        String tokenIncorrect = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYyNmJjM2NkOTk5ZmIyMDAxYjZlNTU2YyIsImlhdCI6MTY1MTIyOTY0NSwiZXhwIjoxNjUxMjMwODQ1fQ.tzsULOfSVi2E2TD-qyp_-jVwlfsES3SQppl17pxMhAA";

        Response orderCreateResponce = UserClient.sendPostOrderCreate(order,tokenIncorrect);
        Assert.assertEquals("Order shouldn't be created",false,orderCreateResponce.then().extract().path("success"));
    }

    @Test
    @DisplayName("Order failed.Authorization absent in request")
    @Description("Order  creation failed because authorization absent in request")
    public void testOrderCreateAuthorizationAbsentFailed() throws Exception {
        List<String> ingredientsId;

        List<String> ingrenientsNames = new ArrayList<>();
        ingrenientsNames.add("Мясо бессмертных моллюсков Protostomia");
        ingrenientsNames.add("Краторная булка N-200i");

        ingredientsId = Utils.getIngredient(0,ingrenientsNames);

        Order order = new Order(ingredientsId);

        Response orderCreateResponse = given()
                .header("Content-type","application/json")
                .and().body(order)
                .when()
                .post("/api/orders");

        Assert.assertEquals("Unauthorized order shouldn't be created ",false,orderCreateResponse.then().extract().path("success"));
    }


    @Test
    @DisplayName("Order failed. incorrect value - not from ingredient list. ")
    @Description("Order  can't be created with ingredient incorrect value ingredients wih authorization ")
    public void testOrderIngredientIncorrectValueFailed() {
        List<String> ingredientsId = Arrays.asList("61c0c5a71d1f82001bdaaaad");
        Order order = new Order(ingredientsId);

        Response orderCreateResponce = UserClient.sendPostOrderCreate(order, token);
        Assert.assertEquals("Incorrect ingredient value should back StatusCode = 500",
                500,orderCreateResponce.then().extract().statusCode());

    }


    @After
    public void clearUsers(){
        if(token!= null) {
            UserClient.sendDeleteUser(token);
        }
    }
}
