package com.example.fyp.domain;


import com.google.gson.Gson;

public class Response {
    private String message;
    private String data;

    public Response() {
    }

    public Response(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    //JSON to OBJ
    public static Response JSONToOBJ(String json)  {
        Gson gson = new Gson();

        Response response = gson.fromJson(json, Response.class);

        return response;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Response response) {
        Gson gson = new Gson();

        String JSON = gson.toJson(response);

        return JSON;
    }
}

