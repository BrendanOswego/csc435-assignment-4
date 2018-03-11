package mainpackage.models;

public interface IResponse {

  public void setSQLException();

  public String getMessage();

  public int getStatus();

  public String getHref();

}