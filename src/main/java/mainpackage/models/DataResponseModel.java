package mainpackage.models;

public class DataResponseModel<T> extends ResponseModel {

  private T data;

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

}