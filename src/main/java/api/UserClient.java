package api;

import io.qameta.allure.Step;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import model.*;

import static io.restassured.RestAssured.given;

public class UserClient {
    @Step("Send Post request UserCreate to /api/auth/register")
    public static Response sendPostRegisterUser(User user) {
        Response userCreateResponce = given().header("Content-type", "application/json").and().body(user).when().post("/api/auth/register");
        return userCreateResponce;
    }

    @Step("Delete all users")
    public static Response sendDeleteUser(String token) {
        Response deleteUserResponse = given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .when()
                .delete("/api/auth/user");
        return deleteUserResponse;
    }

    @Step("Login")
    public static Response sendPostLoginUser(UserAuthorization userAuthorization) {
        Response loginUserResponse = given()
                .header("Content-type", "application/json")
                .and().body(userAuthorization)
                .when()
                .post("/api/auth/login");
    return  loginUserResponse;
    }


    @Step("Logout")
    public static Response sendPostLogoutUser(UserLogout userLogout) {
        Response logoutUserResponse = given()
                .header("Content-type", "application/json")
                .and().body(userLogout)
                .when()
                .post("/api/auth/logout");
        return logoutUserResponse;
    }


    @Step("User Data Update")
    public static Response sendPatchUpdateUserData(UpdateUser updateUser, String token, Boolean authorized) {
        Response updateUserResponse;
        if(authorized.equals(true)){
            updateUserResponse = given()
                    .header("Content-type", "application/json")
                    .header("Authorization", token)
                    .and().body(updateUser)
                    .when()
                    .patch("/api/auth/user");
        } else {
            updateUserResponse = given()
                    .header("Content-type", "application/json")
                    .and().body(updateUser)
                    .when()
                    .patch("/api/auth/user");
        }

        return updateUserResponse;
    }


    @Step("Order create")
    public static Response sendPostOrderCreate(Order order, String token){
        Response orderCreateResponse;
        orderCreateResponse = given()
                .header("Content-type","application/json")
                .header("Authorization", token)
                .and().body(order)
                .when()
                .post("/api/orders");
        return orderCreateResponse;

    }

    @Step("User order list get")
    public static Response sendGetUserOrderList(String token){
        Response orderCreateResponse;
        orderCreateResponse = given()
                .header("Content-type","application/json")
                .header("Authorization", token)
                .and()
                .when()
                .get("/api/orders");
        return orderCreateResponse;

    }
}
