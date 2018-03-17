package mainpackage.views;

import spark.Service;

public abstract class RestEndpoint {

    private Service service;

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

}