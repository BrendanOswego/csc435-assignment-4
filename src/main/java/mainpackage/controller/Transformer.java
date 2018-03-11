package mainpackage.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import spark.ResponseTransformer;

public class Transformer implements ResponseTransformer {

  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public String render(Object model) throws Exception {
    return gson.toJson(model);
  }

}