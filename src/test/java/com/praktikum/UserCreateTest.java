package com.praktikum;

import api.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.User;
import model.UserAuthorization;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class UserCreateTest {
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
    @DisplayName("User create successfully /api/auth/register")
    @Description("New user can be created")
    public void testUserNewCreatedSuccessfully() {
        Boolean success = userCreateResponse.then().extract().path("success");
        Assert.assertEquals("User_creation_result doesn't equal to success",true, success);
    }

    @Test
    @DisplayName("The second call with the same credentials to /api/auth/register isn't successfull")
    @Description("Impossible create user with the same credentials")
    public void testSameUserCreateFailed() {
        Response userSameCreateResponse = UserClient.sendPostRegisterUser(user);
        Boolean success = userSameCreateResponse.then().extract().path("success");
        Assert.assertEquals("The same user creation result  should be false",false, success);
    }

    @Test
    @DisplayName("User can't be created with email = \' \' to /api/auth/register ")
    @Description("Impossible create user if email is an empty space")
    public void testUserCreateFailedEmailIsEmpty() {
        user = new User(" ", userData.get(1), userData.get(2));
        Response userCreateResponse = UserClient.sendPostRegisterUser(user);
        Boolean success = userCreateResponse.then().extract().path("success");
        Assert.assertEquals("Email is empty. User creation result should be false",false, success);
    }

    @Test
    @DisplayName("User can't be created with email = null to /api/auth/register ")
    @Description("Impossible create user if email is a null")
    public void testUserCreateFailedEmailIsNull() {
        user = new User(null, userData.get(1), userData.get(2));
        Response userSameCreateResponse = UserClient.sendPostRegisterUser(user);
        Boolean success = userSameCreateResponse.then().extract().path("success");
        Assert.assertEquals("Email is null. User creation result should be false",false, success);
    }

    @Test
    @DisplayName("User can't be created with Password = \' \' to /api/auth/register ")
    @Description("Impossible create user if Password is an empty space")
    public void testUserCreateFailedPasswordIsEmpty() {
        user = new User(userData.get(0), " ", userData.get(2));
        Response userSameCreateResponse = UserClient.sendPostRegisterUser(user);
        Boolean success = userSameCreateResponse.then().extract().path("success");
        Assert.assertEquals("Password is empty. User creation result should be false",false, success);
    }

    @Test
    @DisplayName("User can't be created with Password = null to /api/auth/register ")
    @Description("Impossible create user if Password is a null")
    public void testUserCreateFailedPasswordIsNull() {
        user = new User(userData.get(0), null, userData.get(2));
        Response userSameCreateResponse = UserClient.sendPostRegisterUser(user);
        Boolean success = userSameCreateResponse.then().extract().path("success");
        Assert.assertEquals("Password is null. User creation result should be false",false, success);
    }


    @After
    public void clearUsers(){
    UserClient.sendDeleteUser(token);
    }

}
