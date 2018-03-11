package mainpackage.views;

import java.util.List;
import mainpackage.controller.API;
import mainpackage.controller.Transformer;
import mainpackage.models.AuthorModel;
import mainpackage.models.BookModel;
import mainpackage.models.DataResponseModel;
import mainpackage.models.ResponseModel;
import spark.Service;

public class AuthorView implements IPath {

  private static final Transformer transformer = new Transformer();
  private static final String type = "application/json";

  public void configure(Service service) {
    get(service);
    post(service);
    put(service);
    delete(service);
  }

  public void get(Service service) {

    service.get("/authors", (req, res) -> {
      res.type(type);
      DataResponseModel<List<AuthorModel>> apiModel = API.instance().getAllAuthors();
      res.status(apiModel.getStatus());
      return apiModel;
    }, transformer);

    service.get("/authors/:authorID", (req, res) -> {
      res.type(type);
      String authorID = req.params(":authorID");
      DataResponseModel<AuthorModel> apiModel = API.instance().getAuthorById(authorID);
      res.status(apiModel.getStatus());
      return apiModel;
    }, transformer);

    service.get("/authors/:authorID/books", (req, res) -> {
      res.type(type);
      String authorID = req.params(":authorID");
      DataResponseModel<List<BookModel>> apiModel = API.instance().getBooksByAuthor(authorID);
      res.status(apiModel.getStatus());
      return apiModel;
    }, transformer);

  }

  public void post(Service service) {

    service.post("/authors", (req, res) -> {
      res.type(type);
      ResponseModel apiModel = API.instance().addAuthor(req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, transformer);

    service.post("/authors/:authorID/books", (req, res) -> {
      res.type(type);
      String authorID = req.params(":authorID");
      ResponseModel apiModel = API.instance().addBookToAuthor(authorID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, transformer);

  }

  public void put(Service service) {

    service.put("/authors/:authorID", (req, res) -> {
      res.type(type);
      String authorID = req.params(":authorID");
      ResponseModel apiModel = API.instance().updateAuthor(authorID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, transformer);

    service.put("/authors/:authorID/books/:bookID", (req, res) -> {
      res.type(type);
      String bookID = req.params(":bookID");
      ResponseModel apiModel = API.instance().updateBook(bookID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, transformer);

  }

  public void delete(Service service) {

    service.delete("/authors/:authorID", (req, res) -> {
      res.type(type);
      String authorID = req.params(":authorID");
      ResponseModel apiModel = API.instance().removeAuthor(authorID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, transformer);

    service.delete("/authors/:authorID/books/:bookID", (req, res) -> {
      res.type(type);
      String authorID = req.params(":authorID");
      String bookID = req.params(":bookID");
      ResponseModel apiModel = API.instance().removeBookByAuthor(authorID, bookID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, transformer);

  }

}