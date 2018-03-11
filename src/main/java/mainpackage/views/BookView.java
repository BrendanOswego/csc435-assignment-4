package mainpackage.views;

import java.util.List;

import mainpackage.controller.API;
import mainpackage.controller.Transformer;
import mainpackage.models.BookModel;
import mainpackage.models.DataResponseModel;
import mainpackage.models.ResponseModel;
import spark.Service;

public class BookView implements IPath {

  private static final Transformer transform = new Transformer();
  private static final String type = "application/json";

  public void configure(Service service) {
    get(service);
    post(service);
    put(service);
    delete(service);
  }

  public void get(Service service) {

    service.get("/books", (req, res) -> {
      res.type(type);
      DataResponseModel<List<BookModel>> apiModel = API.instance().getAllBooks();
      res.status(apiModel.getStatus());
      return apiModel;
    }, transform);

    service.get("/books/:bookID", (req, res) -> {
      res.type(type);
      String bookID = req.params(":bookID");
      DataResponseModel<BookModel> apiModel = API.instance().getBook(bookID);
      res.status(apiModel.getStatus());
      return apiModel;
    }, transform);

  }

  public void post(Service service) {
    /**
     * Check with Alex if he wants this to POST, since needs
     * at least one author for book
     */
  }

  public void put(Service service) {

    service.put("/books/:bookID", (req, res) -> {
      res.type(type);
      String bookID = req.params(":bookID");
      ResponseModel apiModel = API.instance().updateBook(bookID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, transform);

  }

  public void delete(Service service) {

    service.delete("/books/:bookID", (req, res) -> {
      res.type(type);
      String bookID = req.params(":bookID");
      ResponseModel apiModel = API.instance().removeBook(bookID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    });

  }

}