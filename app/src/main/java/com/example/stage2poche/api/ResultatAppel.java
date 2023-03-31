package com.example.stage2poche.api;

public abstract class ResultatAppel<T> {

    public abstract void traiterResultat(T response);

    public abstract void traiterErreur();

}