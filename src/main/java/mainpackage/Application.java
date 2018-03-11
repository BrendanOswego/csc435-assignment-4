package mainpackage;

import mainpackage.views.AuthorView;
import mainpackage.views.BookView;
import spark.Service;

public class Application {

    private static Service spark;

    public static void main(String[] args) {

        spark = Service.ignite().port(8080);

        spark.get("/", (req, res) -> "API Running");

        AuthorView authors = new AuthorView();
        authors.configure(spark);

        BookView books = new BookView();
        books.configure(spark);

    }

}
