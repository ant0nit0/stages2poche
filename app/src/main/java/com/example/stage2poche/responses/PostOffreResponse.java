package com.example.stage2poche.responses;

import com.google.gson.annotations.SerializedName;

public class PostOffreResponse {


    @SerializedName("@context")
    public String _context;
    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;

    @SerializedName("id")
    public Integer id;
    @SerializedName("compteEtudiant")
    public String compteEtudiant;
    @SerializedName("offre")
    public String offre;


}
