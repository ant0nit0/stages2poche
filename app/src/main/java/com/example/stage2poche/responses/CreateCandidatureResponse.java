package com.example.stage2poche.responses;

import com.google.gson.annotations.SerializedName;

public class CreateCandidatureResponse {
    @SerializedName("@context")
    private String _context;

    @SerializedName("@id")
    private String _id;

    @SerializedName("@type")
    private String _type;
    @SerializedName("compteEtudiant")
    private String compteEtudiant;

    @SerializedName("offre")
    private String offre;

    @SerializedName("id")
    public Integer id;

    @SerializedName("typeAction")
    private String typeAction;

    @SerializedName("dateAction")
    private String dateAction;

    @SerializedName("etatCandidature")
    private String etatCandidature;
}
