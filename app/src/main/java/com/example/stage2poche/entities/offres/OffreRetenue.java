package com.example.stage2poche.entities.offres;

import com.google.gson.annotations.SerializedName;

public class OffreRetenue {
    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;
    @SerializedName("compteEtudiant")
    public String compteEtudiant;
    @SerializedName("offre")
    public String offre;
}
