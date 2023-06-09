package com.example.stage2poche.responses;

import com.example.stage2poche.entities.offres.Offre;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OffresResponse {

    @SerializedName("@context")
    public String _context;
    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;
    @SerializedName("hydra:totalItems")
    public Integer _totalItems;
    @SerializedName("hydra:member")
    public List<Offre> offres = new ArrayList();

}
