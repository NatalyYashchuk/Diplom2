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

public class UserLoginTest {
    private ArrayList<String> userData;
    private User user;
    private UserAuthorization userAuthorization;
    private UserAuthorization userAuthorization2;
    private Response userCreateResponse;

    int userDataSetQuantity;
    int signsQuantity;
    String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userDataSetQuantity = 2;
        signsQuantity = 7;
        userData = Utils.getUserData(userDataSetQuantity, signsQuantity);
        user = new User(userData.get(0), userData.get(1), userData.get(2));
        userAuthorization = new UserAuthorization(userData.get(0), userData.get(1));
        userCreateResponse = UserClient.sendPostRegisterUser(user);
        token = userCreateResponse.then().extract().path("accessToken");
    }

    @Test
    @DisplayName("User login successfully /api/auth/login ")
    @Description("New User login succcessfully")
    public void testUserLoginSuccessfully() {
        Response userLoginResponse = UserClient.sendPostLoginUser(userAuthorization);
        Boolean success = userLoginResponse.then().extract().path("success");
        Assert.assertEquals("Login should be successfull, key 'success' should be true",true, success);
    }

    @Test
    @DisplayName("Can't login with incorrect email /api/auth/login ")
    @Description("Login impossible with incorrect User email.")
    public void testUserLoginFaildEmailIncorrect() {
        userAuthorization.setEmail(userData.get(2));
        Response userLoginResponse = UserClient.sendPostLoginUser(userAuthorization);
        Boolean success = userLoginResponse.then().extract().path("success");
        Assert.assertEquals("Login  with incorrect email should be failed, ",false, success);
    }

    @Test
    @DisplayName("Can't login with incorrect password /api/auth/login ")
    @Description(" Login impossible with incorrect User password.")
    public void testUserLoginFaildPasswordIncorrect() {
        userAuthorization.setPassword(userData.get(3));
        Response userLoginResponse = UserClient.sendPostLoginUser(userAuthorization);
        Boolean success = userLoginResponse.then().extract().path("success");
        Assert.assertEquals("Login  with incorrect password should be failed, ",false, success);
    }

    @After
    public void clearUsers(){
    UserClient.sendDeleteUser(token);
    }

}
