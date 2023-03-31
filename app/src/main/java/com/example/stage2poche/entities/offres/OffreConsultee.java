package com.example.stage2poche.entities.offres;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OffreConsultee implements Parcelable {

    @SerializedName("@id")
    public String id;
    @SerializedName("@type")
    public String _type;
    @SerializedName("compteEtudiant")
    public String compteEtudiant;
    @SerializedName("offre")
    public String offre;

    protected OffreConsultee(Parcel in) {
        id = in.readString();
        _type = in.readString();
        compteEtudiant = in.readString();
        offre = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(_type);
        dest.writeString(compteEtudiant);
        dest.writeString(offre);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OffreConsultee> CREATOR = new Creator<OffreConsultee>() {
        @Override
        public OffreConsultee createFromParcel(Parcel in) {
            return new OffreConsultee(in);
        }

        @Override
        public OffreConsultee[] newArray(int size) {
            return new OffreConsultee[size];
        }
    };
}
