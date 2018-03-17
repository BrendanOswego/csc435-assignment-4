package mainpackage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import spark.ResponseTransformer;

public class Transformer implements ResponseTransformer {

  private static final ObjectMapper mapper = new ObjectMapper();

  public String render(Object model) throws Exception {
    return mapper.writeValueAsString(model);
  }

}