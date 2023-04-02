package com.example.stage2poche.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class is used to represent a student account
 */
public class CompteEtudiant implements Parcelable {

    @SerializedName("id")
    public String id;
    @SerializedName("login")
    public String login;
    @SerializedName("roles")
    public List<String> roles;
    @SerializedName("password")
    public String password;
    @SerializedName("derniereConnexion")
    @Nullable
    public String derniereConnexion;

    @SerializedName("etatRecherche")
    public String etatRecherche;

    // Constructor
    public CompteEtudiant(Parcel in) {
        id = in.readString();
        login = in.readString();
        roles = in.createStringArrayList();
        password = in.readString();
        derniereConnexion = in.readString();
        etatRecherche = in.readString();
    }

    // Copy constructor
    public CompteEtudiant(CompteEtudiant compteEtudiant) {
        this.id = compteEtudiant.id;
        this.login = compteEtudiant.login;
        this.roles = compteEtudiant.roles;
        this.password = compteEtudiant.password;
        this.derniereConnexion = compteEtudiant.derniereConnexion;
        this.etatRecherche = compteEtudiant.etatRecherche;
    }

    public void setEtatRecherche(String etatRecherche) {
        this.etatRecherche = etatRecherche;
    }

    // Parcelable implementation

    public static final Creator<CompteEtudiant> CREATOR = new Creator<CompteEtudiant>() {
        @Override
        public CompteEtudiant createFromParcel(Parcel in) {
            return new CompteEtudiant(in);
        }

        @Override
        public CompteEtudiant[] newArray(int size) {
            return new CompteEtudiant[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(login);
        parcel.writeStringList(roles);
        parcel.writeString(password);
        parcel.writeString(derniereConnexion);
        parcel.writeString(etatRecherche);
    }
}
