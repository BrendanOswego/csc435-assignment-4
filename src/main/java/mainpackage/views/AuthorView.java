package mainpackage.views;

import java.util.List;
import mainpackage.controller.API;
import mainpackage.controller.Transformer;
import mainpackage.models.AuthorModel;
import mainpackage.models.BookModel;
import mainpackage.models.DataResponseModel;
import mainpackage.models.ResponseModel;
import spark.Service;

public class AuthorView extends RestEndpoint {

  public AuthorView(Service service) {
    super(service);
  }

  public void get() {

    getService().get("/authors", (req, res) -> {
      res.type(getType());
      DataResponseModel<List<AuthorModel>> apiModel = API.instance().getAllAuthors();
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

    getService().get("/authors/:authorID", (req, res) -> {
      res.type(getType());
      String authorID = req.params(":authorID");
      DataResponseModel<AuthorModel> apiModel = API.instance().getAuthorById(authorID);
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

    getService().get("/authors/:authorID/books", (req, res) -> {
      res.type(getType());
      String authorID = req.params(":authorID");
      DataResponseModel<List<BookModel>> apiModel = API.instance().getBooksByAuthor(authorID);
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

  }

  public void post() {

    getService().post("/authors", (req, res) -> {
      res.type(getType());
      ResponseModel apiModel = API.instance().addAuthor(req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

    getService().post("/authors/:authorID/books", (req, res) -> {
      res.type(getType());
      String authorID = req.params(":authorID");
      ResponseModel apiModel = API.instance().addBookToAuthor(authorID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

  }

  public void put() {

    getService().put("/authors/:authorID", (req, res) -> {
      res.type(getType());
      String authorID = req.params(":authorID");
      ResponseModel apiModel = API.instance().updateAuthor(authorID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

    getService().put("/authors/:authorID/books/:bookID", (req, res) -> {
      res.type(getType());
      String bookID = req.params(":bookID");
      ResponseModel apiModel = API.instance().updateBook(bookID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

  }

  public void delete() {

    getService().delete("/authors/:authorID", (req, res) -> {
      res.type(getType());
      String authorID = req.params(":authorID");
      ResponseModel apiModel = API.instance().removeAuthor(authorID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

    getService().delete("/authors/:authorID/books/:bookID", (req, res) -> {
      res.type(getType());
      String authorID = req.params(":authorID");
      String bookID = req.params(":bookID");
      ResponseModel apiModel = API.instance().removeBookByAuthor(authorID, bookID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

  }

}