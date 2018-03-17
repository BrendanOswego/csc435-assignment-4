package mainpackage.views;

import java.util.List;

import mainpackage.controller.API;
import mainpackage.models.BookModel;
import mainpackage.models.DataResponseModel;
import mainpackage.models.ResponseModel;
import spark.Service;

public class BookView extends RestEndpoint {

  public BookView(Service service) {
    super(service);
  }

  public void get() {

    getService().get("/books", (req, res) -> {
      res.type(getType());
      DataResponseModel<List<BookModel>> apiModel = API.instance().getAllBooks();
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

    getService().get("/books/:bookID", (req, res) -> {
      res.type(getType());
      String bookID = req.params(":bookID");
      DataResponseModel<BookModel> apiModel = API.instance().getBook(bookID);
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

  }

  public void post() {
    /**
     * Check with Alex if he wants this to POST, since needs
     * at least one author for book
     */
  }

  public void put() {

    getService().put("/books/:bookID", (req, res) -> {
      res.type(getType());
      String bookID = req.params(":bookID");
      ResponseModel apiModel = API.instance().updateBook(bookID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

  }

  public void delete() {

    getService().delete("/books/:bookID", (req, res) -> {
      res.type(getType());
      String bookID = req.params(":bookID");
      ResponseModel apiModel = API.instance().removeBook(bookID, req);
      res.status(apiModel.getStatus());
      return apiModel;
    }, getTransformer());

  }

}