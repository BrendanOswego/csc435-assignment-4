package mainpackage.models;

public class DataResponseModel<T> implements IResponse {

  private String message;
  private int status;
  private String href;

  private T data;

  public DataResponseModel() {
  }

  public void setSQLException() {
    message = "There was an internal SQL exception";
    status = 500;
  }

  public T getData() {
    return data;
  }

  public String getMessage() {
    return message;
  }

  public int getStatus() {
    return status;
  }

  public String getHref() {
    return href;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public void setData(T data) {
    this.data = data;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}