package mainpackage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mainpackage.models.AuthorModel;
import mainpackage.models.BookAuthorModel;
import mainpackage.models.BookModel;
import mainpackage.models.DataResponseModel;
import mainpackage.models.ResponseModel;
import mainpackage.sql.SQLConnection;
import spark.Request;

public class API {

  private static API instance = null;

  private API() {
  }

  public static API instance() {
    if (instance == null)
      instance = new API();
    return instance;
  }

  public ResponseModel addBookToAuthor(String author_id, Request req) {
    ObjectMapper mapper = new ObjectMapper();
    ResponseModel response = new ResponseModel();
    response.setHref(req.pathInfo());
    String query1 = "SELECT * FROM book WHERE title=? AND genre=? AND year_published=? AND pages=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query1)) {
      BookModel book = mapper.readValue(req.body(), BookModel.class);
      BookAuthorModel ba = new BookAuthorModel(book.getID(), UUID.fromString(author_id));
      statement.setString(1, book.getTitle());
      statement.setString(2, book.getGenre());
      statement.setInt(3, book.getYearPublished());
      statement.setInt(4, book.getPages());
      ResultSet result1 = statement.executeQuery();
      if (result1.next()) { //IF the book is already in the database, but the author doesn't have the book
        if (authorHasBook(result1.getString("book_id"), author_id)) {
          response.setStatus(200);
          response.setMessage("Book already added to Author");
          return response;
        }
        book.setID(result1.getString("book_id"));
        ba.setBookID(book.getID().toString());
        String query2 = "INSERT INTO book_author " + "SELECT * FROM (SELECT ?, ?) AS tmp WHERE NOT EXISTS ( "
            + "SELECT book_id, author_id FROM book_author WHERE book_id=? AND author_id=? ) LIMIT 1;";
        PreparedStatement statement2 = conn.prepareStatement(query2);
        statement2.setString(1, ba.getBookID().toString());
        statement2.setString(2, ba.getAuthorID().toString());
        statement2.setString(3, ba.getBookID().toString());
        statement2.setString(4, ba.getAuthorID().toString());
        statement2.execute();
        if (statement2.getUpdateCount() > 0) {
          response.setMessage("Book was added to Author");
          response.setStatus(201);
          return response;
        }

        response.setMessage("Book already added to Author");
        response.setStatus(200);
        return response;
      }
      String query3 = "INSERT INTO book (book_id, title, genre, year_published, pages) "
          + "SELECT * FROM (SELECT ?, ?, ?, ?, ?) AS tmp WHERE NOT EXISTS ( "
          + "SELECT title, genre, year_published, pages FROM book WHERE title=? AND genre=? AND year_published=? AND pages=? ) LIMIT 1;";
      PreparedStatement statement3 = conn.prepareStatement(query3);
      statement3.setString(1, book.getID().toString());
      statement3.setString(2, book.getTitle());
      statement3.setString(3, book.getGenre());
      statement3.setInt(4, book.getYearPublished());
      statement3.setInt(5, book.getPages());
      statement3.setString(6, book.getTitle());
      statement3.setString(7, book.getGenre());
      statement3.setInt(8, book.getYearPublished());
      statement3.setInt(9, book.getPages());
      statement3.execute();
      if (statement3.getUpdateCount() > 0) {
        String query4 = "INSERT INTO book_author " + "SELECT * FROM (SELECT ?, ?) AS tmp WHERE NOT EXISTS ( "
            + "SELECT book_id, author_id FROM book_author WHERE book_id=? AND author_id=? ) LIMIT 1;";
        PreparedStatement statement4 = conn.prepareStatement(query4);
        statement4.setString(1, ba.getBookID().toString());
        statement4.setString(2, ba.getAuthorID().toString());
        statement4.setString(3, ba.getBookID().toString());
        statement4.setString(4, ba.getAuthorID().toString());
        statement4.execute();
        if (statement4.getUpdateCount() > 0) {
          response.setMessage("Book added to Author");
          response.setStatus(201);
          return response;
        }

        response.setStatus(200);
        response.setMessage("Book already added to Author");
        return response;
      }
      response.setMessage("Book already added to Author");
      response.setStatus(200);
      return response;
    } catch (SQLException | IOException e) {
      e.printStackTrace();
      response.setStatus(500);
      response.setMessage("There was an internal SQL exception");
      return response;
    }
  }

  public ResponseModel addAuthor(Request req) {
    ObjectMapper mapper = new ObjectMapper();
    ResponseModel response = new ResponseModel();
    response.setHref(req.pathInfo());
    try {
      AuthorModel author = mapper.readValue(req.body(), AuthorModel.class);
      String query = "INSERT INTO author (author_id, first_name, last_name) SELECT * FROM (SELECT ?, ?, ?) as tmp WHERE NOT EXISTS (SELECT first_name, last_name FROM author WHERE first_name=? AND last_name=?)";
      try (Connection conn = SQLConnection.instance().createConnection();
          PreparedStatement statement = conn.prepareStatement(query)) {
        statement.setString(1, author.getID().toString());
        statement.setString(2, author.getFirstName());
        statement.setString(3, author.getLastName());
        statement.setString(4, author.getFirstName());
        statement.setString(5, author.getLastName());
        statement.execute();
        if (statement.getUpdateCount() > 0) {
          response.setMessage("Author added");
          response.setStatus(201);
          return response;
        }

        response.setMessage("Author already added");
        response.setStatus(200);
        return response;
      } catch (SQLException e) {
        e.printStackTrace();
        response.setSQLException();
        return response;
      }
    } catch (IOException e) {
      e.printStackTrace();
      response.setSQLException();
      return response;
    }
  }

  public DataResponseModel<List<AuthorModel>> getAllAuthors() {
    DataResponseModel<List<AuthorModel>> response = new DataResponseModel<List<AuthorModel>>();
    response.setHref("/authors");
    response.setData(new ArrayList<AuthorModel>());
    String query = "SELECT * FROM author";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query);) {
      ResultSet result = statement.executeQuery();
      while (result.next()) {
        AuthorModel model = new AuthorModel();
        model.setID(result.getString("author_id"));
        model.setFirstName(result.getString("first_name"));
        model.setLastName(result.getString("last_name"));
        response.getData().add(model);
      }
      response.setMessage("List of all Authors");
      response.setStatus(200);
      return response;
    } catch (SQLException e) {
      e.printStackTrace();
      response.setSQLException();
      return response;
    }
  }

  public DataResponseModel<AuthorModel> getAuthorById(String author_id) {
    DataResponseModel<AuthorModel> response = new DataResponseModel<>();
    response.setHref("/authors/" + author_id);
    String query = "SELECT * FROM author WHERE author_id=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query);) {
      statement.setString(1, author_id);
      ResultSet result = statement.executeQuery();
      if (result.next()) {
        AuthorModel model = new AuthorModel();
        model.setID(result.getString("author_id"));
        model.setFirstName(result.getString("first_name"));
        model.setLastName(result.getString("last_name"));
        response.setData(model);
        response.setMessage("Author by ID");
        response.setStatus(200);
        return response;
      }
      response.setMessage("Author by ID was not found");
      response.setStatus(404);
      return response;
    } catch (SQLException e) {
      e.printStackTrace();
      response.setSQLException();
      return response;
    }
  }

  public DataResponseModel<List<BookModel>> getAllBooks() {
    DataResponseModel<List<BookModel>> response = new DataResponseModel<List<BookModel>>();
    response.setHref("/books");
    response.setData(new ArrayList<BookModel>());
    String query = "SELECT * FROM book";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query)) {
      ResultSet result = statement.executeQuery();
      while (result.next()) {
        BookModel model = new BookModel();
        model.setID(result.getString("book_id"));
        model.setTitle(result.getString("title"));
        model.setGenre(result.getString("genre"));
        model.setYearPublished(result.getInt("year_published"));
        model.setPages(result.getInt("pages"));
        response.getData().add(model);
      }
      response.setStatus(200);
      response.setMessage("List of all Books");
      return response;
    } catch (SQLException e) {
      e.printStackTrace();
      response.setSQLException();
      return response;
    }
  }

  public DataResponseModel<List<BookModel>> getBooksByAuthor(String author_id) {
    DataResponseModel<List<BookModel>> response = new DataResponseModel<List<BookModel>>();
    response.setHref("/authors/" + author_id + "/books");
    response.setData(new ArrayList<BookModel>());
    String query = "SELECT * FROM book b JOIN book_author ba ON b.book_id=ba.book_id JOIN author a ON a.author_id=ba.author_id WHERE a.author_id=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setString(1, author_id);
      ResultSet result = statement.executeQuery();
      while (result.next()) {
        BookModel model = new BookModel();
        model.setID(result.getString("book_id"));
        model.setTitle(result.getString("title"));
        model.setGenre(result.getString("genre"));
        model.setYearPublished(result.getInt("year_published"));
        model.setPages(result.getInt("pages"));
        response.getData().add(model);
      }
      response.setMessage("List of Books by Author");
      response.setStatus(200);
      return response;
    } catch (SQLException e) {
      e.printStackTrace();
      response.setSQLException();
      return response;
    }
  }

  public ResponseModel updateAuthor(String author_id, Request req) {
    ObjectMapper mapper = new ObjectMapper();
    ResponseModel response = new ResponseModel();
    response.setHref(req.pathInfo());
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM author WHERE author_id=?",
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
      AuthorModel newModel = (AuthorModel) mapper.readValue(req.body(), AuthorModel.class);
      DataResponseModel<AuthorModel> oldModel = API.instance().getAuthorById(author_id);
      statement.setString(1, author_id);
      ResultSet result = statement.executeQuery();
      if (result.next()) {
        if (newModel.getFirstName() != null && !newModel.getFirstName().equals(oldModel.getData().getFirstName()))
          result.updateString("first_name", newModel.getFirstName());
        if (newModel.getLastName() != null && !newModel.getLastName().equals(oldModel.getData().getLastName()))
          result.updateString("last_name", newModel.getLastName());

        result.updateRow();
        response.setMessage("Author updated successfully");
        response.setStatus(200);
        return response;
      }
      response.setMessage("Author specified does not exist");
      response.setStatus(404);
      return response;
    } catch (SQLException | IOException e) {
      e.printStackTrace();
      response.setSQLException();
      return response;
    }
  }

  public ResponseModel updateBook(String book_id, Request req) {
    ObjectMapper mapper = new ObjectMapper();
    ResponseModel response = new ResponseModel();
    response.setHref(req.pathInfo());
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM book where book_id=?",
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
      BookModel oldBook = getBook(book_id).getData();
      BookModel newBook = (BookModel) mapper.readValue(req.body(), BookModel.class);
      statement.setString(1, book_id);
      ResultSet result = statement.executeQuery();
      if (result.next()) {
        if (newBook.getGenre() != null && !newBook.getGenre().equals(oldBook.getGenre()))
          result.updateString("genre", newBook.getGenre());
        if (newBook.getTitle() != null && !newBook.getTitle().equals(oldBook.getTitle()))
          result.updateString("title", newBook.getTitle());
        if (newBook.getPages() != 0 && (newBook.getPages() != oldBook.getPages()))
          result.updateInt("pages", newBook.getPages());
        if (newBook.getYearPublished() != 0 && (newBook.getYearPublished() != oldBook.getYearPublished()))
          result.updateInt("year_published", newBook.getYearPublished());

        result.updateRow();
        response.setMessage("Book updated successfully");
        response.setStatus(200);
        return response;
      }
      response.setMessage("Book does not exist");
      response.setStatus(404);
      return response;
    } catch (SQLException | IOException e) {
      e.printStackTrace();
      response.setSQLException();
      return response;
    }
  }

  public ResponseModel removeAuthor(String author_id, Request req) {
    ResponseModel response = new ResponseModel();
    response.setHref(req.pathInfo());
    String baQuery = "DELETE FROM book_author WHERE author_id=?";
    String authorQuery = "DELETE FROM author WHERE author_id=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement ba_statement = conn.prepareStatement(baQuery);
        PreparedStatement author_statement = conn.prepareStatement(authorQuery)) {
      ba_statement.setString(1, author_id);
      ba_statement.execute();
      author_statement.setString(1, author_id);
      author_statement.execute();
      if (author_statement.getUpdateCount() > 0) {
        response.setMessage("Author removed successfully");
        response.setStatus(200);
        return response;
      }
      response.setMessage("Author does not exist");
      response.setStatus(404);
      return response;
    } catch (SQLException e) {
      e.printStackTrace();
      response.setSQLException();
      return response;
    }
  }

  public ResponseModel removeBookByAuthor(String author_id, String book_id, Request req) {
    ResponseModel response = new ResponseModel();
    response.setHref(req.pathInfo());
    String query = "DELETE FROM book_author WHERE book_id=? AND author_id=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setString(1, book_id);
      statement.setString(2, author_id);
      statement.execute();
      if (statement.getUpdateCount() > 0) {
        response.setMessage("Book removed from Author successfully");
        response.setStatus(200);
        return response;
      }
      response.setMessage("Book by Author does not exist");
      response.setStatus(404);
      return response;
    } catch (SQLException e) {
      e.printStackTrace();
      response.setSQLException();
      return response;
    }
  }

  public ResponseModel removeBook(String book_id, Request req) {
    ResponseModel response = new ResponseModel();
    response.setHref(req.pathInfo());
    String baQuery = "DELETE FROM book_author WHERE book_id=?";
    String bookQuery = "DELETE FROM book WHERE book_id=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement ba_statement = conn.prepareStatement(baQuery);
        PreparedStatement book_statement = conn.prepareStatement(bookQuery)) {
      ba_statement.setString(1, book_id);
      ba_statement.execute();
      book_statement.setString(1, book_id);
      book_statement.execute();
      if (book_statement.getUpdateCount() > 0) {
        response.setMessage("Book removed successfully");
        response.setStatus(200);
        return response;
      }
      response.setMessage("Book does not exist");
      response.setStatus(404);
      return response;
    } catch (SQLException e) {
      e.printStackTrace();
      response.setSQLException();
      return response;
    }
  }

  public DataResponseModel<BookModel> getBook(String book_id) {
    DataResponseModel<BookModel> response = new DataResponseModel<>();
    response.setHref("/books/" + book_id);
    String query = "SELECT * FROM book where book_id=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setString(1, book_id);
      ResultSet result = statement.executeQuery();
      if (result.next()) {
        BookModel temp = new BookModel();
        temp.setID(result.getString("book_id"));
        temp.setTitle(result.getString("title"));
        temp.setYearPublished(result.getInt("year_published"));
        temp.setPages(result.getInt("pages"));
        temp.setGenre(result.getString("genre"));
        response.setData(temp);
        response.setMessage("Book with ID");
        response.setStatus(200);
        return response;
      }
      response.setMessage("Book with ID does not exist");
      response.setStatus(404);
      return response;
    } catch (SQLException e) {
      e.printStackTrace();
      response.setSQLException();
      return response;
    }
  }

  private boolean authorHasBook(String book_id, String author_id) {
    String query = "SELECT * FROM book_author WHERE book_id=? AND author_id=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setString(1, book_id);
      statement.setString(2, author_id);
      ResultSet result = statement.executeQuery();
      return result.next();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

}