package com.praktikum;

import api.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.UpdateUser;
import model.User;
import model.UserAuthorization;
import model.UserLogout;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class UserUpdateTest {
    private ArrayList<String> userData;
    private User user;
    private UserAuthorization userAuthorization;
    private Response userCreateResponse;
    private UserLogout userLogout;
    private UpdateUser updateDataModel;

    int userDataSetQuantity;
    int signsQuantity;
    String token;
    String authorizationToken;
    String refreshToken;



    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userDataSetQuantity = 2;
        signsQuantity = 7;
        userData = Utils.getUserData(userDataSetQuantity, signsQuantity);
        user = new User(userData.get(0), userData.get(1), userData.get(2));
        userAuthorization = new UserAuthorization(userData.get(0), userData.get(1));

        updateDataModel = new UpdateUser(userData.get(3),userData.get(4),userData.get(5));

        userCreateResponse = UserClient.sendPostRegisterUser(user);                  //create
        token = userCreateResponse.then().extract().path("accessToken");
        refreshToken = userCreateResponse.then().extract().path("refreshToken");
    }

    @Test
    @DisplayName("Authorized user updates credetials successfully /api/auth/register")
    @Description("User send patch request with authorization token")
    public void testAuthorizedUserDataUpdateSuccessfully() {
        Response userLoginResponse = UserClient.sendPostLoginUser(userAuthorization); //login
        token = userLoginResponse.then().extract().path("accessToken");
        refreshToken = userLoginResponse.then().extract().path("refreshToken");

        Response userUpdateResponse = UserClient.sendPatchUpdateUserData(updateDataModel, token, true);

        String updatedEmail = userUpdateResponse.then().extract().path("user.email");
        String updatedName = userUpdateResponse.then().extract().path("user.name");
        Assert.assertEquals("Email hasn't been updated", userData.get(3), updatedEmail);
        Assert.assertEquals("Name hasn't been updated", userData.get(5), updatedName);

        UserClient.sendPostLogoutUser(new UserLogout(refreshToken));

        userAuthorization = new UserAuthorization(userData.get(3),userData.get(4));
        Response userLoginResponse2 = UserClient.sendPostLoginUser(userAuthorization); //login
        Boolean successLogin = userLoginResponse2.then().extract().path("success");
        Assert.assertEquals("Updated password hasn't passed", true, successLogin);
    }

    @Test
    @DisplayName("Authorized user updates credetials successfully /api/auth/register")
    @Description("User send patch request without authorization token")
    public void testNonAuthorizedUserDataUpdateFailed() {
        Response userUpdateResponse = UserClient.sendPatchUpdateUserData(updateDataModel, token, false);
        Boolean updateResult = userUpdateResponse.then().extract().path("success");
        Assert.assertEquals("User Data shouldn't be updated because Authorization token has not provided", false, updateResult);
    }


    @After
    public void clearUsers(){
        UserClient.sendDeleteUser(token);
    }

}
