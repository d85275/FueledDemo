package com.e.fueleddemo.restaurant_model;

public class SearchResult {
    Response response;
    Meta meta;

    public SearchResult(Response response, Meta meta) {
        this.response = response;
        this.meta = meta;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
