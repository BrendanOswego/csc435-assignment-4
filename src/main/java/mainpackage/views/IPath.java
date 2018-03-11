package mainpackage.views;

import spark.Service;

public interface IPath {

  void configure(Service service);

  void get(Service service);

  void post(Service service);

  void put(Service service);

  void delete(Service service);

}