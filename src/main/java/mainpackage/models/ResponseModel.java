package mainpackage.models;

public class ResponseModel {
  
  private int status;
  private String message;
  private String href;

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public String getHref() {
    return href;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public void setSQLException() {
    message = "There was an internal SQL exception";
    status = 500;
  }

}