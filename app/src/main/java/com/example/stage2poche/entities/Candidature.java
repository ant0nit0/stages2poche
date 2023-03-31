package com.example.stage2poche.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;

import com.example.stage2poche.entities.offres.Offre;
import com.example.stage2poche.enums.EtatCandidature;
import com.google.gson.annotations.SerializedName;

public class Candidature implements Parcelable {

    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;
    @SerializedName("id")
    public long id;
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

    // private EtatCandidature etatCandidatureEnum;

    public Candidature() {
        // etatCandidatureEnum = EtatCandidature.fromId(0);
    }

    protected Candidature(Parcel in) {
        _id = in.readString();
        _type = in.readString();
        compteEtudiant = in.readString();
        offre = in.readString();
        typeAction = in.readString();
        dateAction = in.readString();
        etatCandidature = in.readString();
    }

    public static final Parcelable.Creator<Candidature> CREATOR = new Parcelable.Creator<Candidature>() {
        @Override
        public Candidature createFromParcel(Parcel in) {
            return new Candidature(in);
        }

        @Override
        public Candidature[] newArray(int size) {
            return new Candidature[size];
        }
    };

    public int getOffreId() {
        return Integer.valueOf(offre.replace("/api/offres/", ""));
    }

    public int getEtatCandidatureId() {
        return Integer.parseInt(etatCandidature.replace("/api/etat_candidatures/", ""));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(_type);
        parcel.writeString(compteEtudiant);
        parcel.writeString(offre);
        parcel.writeString(typeAction);
        parcel.writeString(dateAction);
        parcel.writeString(etatCandidature);
    }

    @NonNull
    @Override
    public String toString() {
        return "Candidature{" +
                "_id='" + _id + '\'' +
                ", _type='" + _type + '\'' +
                ", compteEtudiant='" + compteEtudiant + '\'' +
                ", offre='" + offre + '\'' +
                ", typeAction='" + typeAction + '\'' +
                ", dateAction='" + dateAction + '\'' +
                ", etatCandidature='" + etatCandidature + '\'' +
                '}';
    }
}
