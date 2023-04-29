package main.java.service;

public interface Result {
    String ResultText(String response); // response에서 text만 뽑기
    String RecipeName(String ResultText);
    String Ingredients(String ResultText);
    String CookingOrder(String ResultText);
}
