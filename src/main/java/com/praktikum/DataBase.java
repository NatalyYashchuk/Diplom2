package com.praktikum;

import java.util.HashMap;
import java.util.Map;

public class DataBase {
    public static Map<String,String> ingredientsMap () {
        Map<String,String> ingredients = new HashMap<>();
        ingredients.put("Флюоресцентная булка R2-D3","61c0c5a71d1f82001bdaaa6d");
        ingredients.put("Краторная булка N-200i","61c0c5a71d1f82001bdaaa6c");
        ingredients.put("Мясо бессмертных моллюсков Protostomia","61c0c5a71d1f82001bdaaa6f");
        ingredients.put("Филе Люминесцентного тетраодонтимформа","61c0c5a71d1f82001bdaaa6e");
        ingredients.put("Говяжий метеорит (отбивная)","61c0c5a71d1f82001bdaaa70");
        ingredients.put("Биокотлета из марсианской Магнолии","61c0c5a71d1f82001bdaaa71");
        ingredients.put("Хрустящие минеральные кольца","61c0c5a71d1f82001bdaaa76");
        ingredients.put("Плоды Фалленианского дерева","61c0c5a71d1f82001bdaaa77");
        ingredients.put("Кристаллы марсианских альфа-сахаридов","61c0c5a71d1f82001bdaaa78");
        ingredients.put("Мини-салат Экзо-Плантаго","61c0c5a71d1f82001bdaaa79");
        ingredients.put("Сыр с астероидной плесенью","61c0c5a71d1f82001bdaaa7a");
        return ingredients;
    }
}
