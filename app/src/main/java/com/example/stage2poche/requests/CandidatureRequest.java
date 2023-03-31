package com.example.stage2poche.requests;

import com.google.gson.annotations.SerializedName;

public class CandidatureRequest {

    @SerializedName("compteEtudiant")
    public String compteEtudiant;
    @SerializedName("offre")
    public String offre;
    @SerializedName("typeAction")
    public String typeAction;
    @SerializedName("dateAction")
    public String dateAction;
    @SerializedName("etatCandidature")
    public String etatCandidature;

    public CandidatureRequest(String compteEtudiant, String offre, String typeAction, String dateAction, String etatCandidature) {
        this.compteEtudiant = compteEtudiant;
        this.offre = offre;
        this.typeAction = typeAction;
        this.dateAction = dateAction;
        this.etatCandidature = etatCandidature;
    }


}
