package com.praktikum;

import api.UserClient;
import io.restassured.response.Response;
import model.Order;
import org.apache.commons.lang3.RandomStringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {

    private static Exception ex;

    public static ArrayList<String> getUserData(Integer quantity, Integer signs){
        ArrayList<String> userDataArray = new ArrayList<>();

        for(int i = 0; i< quantity; i++) {
            String userEmail = RandomStringUtils.randomAlphabetic(signs).toLowerCase() + "@yandex.ru";
            String userPassword = RandomStringUtils.randomAlphabetic(signs).toLowerCase();
            String userName = RandomStringUtils.randomAlphabetic(signs).toLowerCase();

            userDataArray.add(userEmail);
            userDataArray.add(userPassword);
            userDataArray.add(userName);
        }
        return userDataArray;
    }

    public static ArrayList<String> getIngredient(Integer quantity, List<String> byName) throws Exception {

            if(quantity > DataBase.ingredientsMap().size()){
                throw ex;
        }

        List<String> ingredientsList = new ArrayList<>();
        HashMap<String,String> ingredients = DataBase.ingredientsMap();

        int iterration = 0;
        if(quantity.equals(0)) {
            for (int i = 0; i < byName.size(); i++) {
                ingredientsList.add(ingredients.get(byName.get(i)));
            }
        }else {
            for (String value: ingredients.values()){
                ingredientsList.add(value);
                iterration = iterration+1;
                if(iterration ==quantity){
                    break;
                }
            }

        }
        return (ArrayList<String>) ingredientsList;
    }



    public static Integer createAnOrder(String token, Integer ingredientsQuantity) throws Exception {
        List<String> ingredientsId = Utils.getIngredient(ingredientsQuantity,null);
        for(int i=0; i< ingredientsId.size(); i++) {
            System.out.println(ingredientsId.get(i));
        }

        Order order = new Order(ingredientsId);
        Response orderCreateResponce = UserClient.sendPostOrderCreate(order,token);

       Integer orderId = orderCreateResponce.then().extract().path("order.number");
       return  orderId;
    }

}
