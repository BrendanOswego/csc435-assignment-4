package mainpackage.views;

import spark.Service;
import mainpackage.controller.Transformer;

public abstract class RestEndpoint {

    private Service service;
    private static final Transformer transformer = new Transformer();
    private static final String type = "application/json";

    public RestEndpoint(Service service) {
        this.service = service;
        configure();
    }

    private void configure() {
        get();
        post();
        put();
        delete();
    }

    public abstract void get();

    public abstract void post();

    public abstract void put();

    public abstract void delete();

    public Service getService() {
        return service;
    }

    public Transformer getTransformer() {
        return transformer;
    }

    public String getType() {
        return type;
    }

}