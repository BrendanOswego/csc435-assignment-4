package mainpackage;

import mainpackage.views.AuthorView;
import mainpackage.views.BookView;
import mainpackage.views.RestEndpoint;
import spark.Service;

public class Application {

    private static Service spark;

    public static void main(String[] args) {

        spark = Service.ignite().port(8080);

        spark.get("/", (req, res) -> "API Running");

        RestEndpoint authors = new AuthorView(spark);
        RestEndpoint books = new BookView(spark);
    }

}
