package com.example.alexis.serveur.Serveur;

import android.app.Activity;

import fi.iki.elonen.NanoHTTPD;

public class Serveur extends NanoHTTPD {

    private Activity mainActivity;
    //extrait de JSON trouver sur https://www.w3schools.com/js/js_json_intro.asp
    private String JSONSample = "{ \"name\":\"John\", \"age\":31, \"city\":\"New York\" }";

    public Serveur(String hostname, int port) {
        super(hostname, port);
        init(mainActivity);
    }

    private void init(Activity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public Response serve(IHTTPSession session){
        if(session.getUri().contains("/files/")){
            return new Response(Response.Status.OK, MIME_PLAINTEXT, JSONSample);
        }
        return new Response(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "NOT FOUND");
    }
}

