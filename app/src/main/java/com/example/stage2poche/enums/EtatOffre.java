package com.example.stage2poche.enums;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.example.stage2poche.R;

public enum EtatOffre {

    UNKNOWN(0),
    DISPONIBLE(1),
    INDISPONIBLE(2),
    EXPIREE(3);

    private static final String BASE_URL = "/api/etat_offres/";
    int _id;
    EtatOffre(int _id) {
        this._id = _id;
    }

    public String getURL() {
        return BASE_URL + this._id;
    }

    public int getId() {
        return this._id;
    }

    public static EtatOffre fromId(int id) {
        for (EtatOffre etatOffre : EtatOffre.values()) {
            if (etatOffre._id == id) {
                return etatOffre;
            }
        }
        return EtatOffre.UNKNOWN;
    }

    public static String getString(EtatOffre etatOffre) {
        switch (etatOffre) {
            case DISPONIBLE:
                return "Disponible";
            case INDISPONIBLE:
                return "L'offre n'est malheuresement plus disponible";
            case EXPIREE:
                return "L'offre a malheuresement expiré";
            default:
                return "Le status de l'offre est inconnu";
        }
    }


    public static @ColorInt int getColor(EtatOffre etatOffre, Context context) {
        switch (etatOffre) {
            case DISPONIBLE:
                return ContextCompat.getColor(context, R.color.etat_offre_disponible);
            case INDISPONIBLE:
                return ContextCompat.getColor(context, R.color.etat_offre_indisponible);
            case EXPIREE:
                return ContextCompat.getColor(context, R.color.etat_offre_expiree);
            default:
                return 0xFFeab676;
        }
    }

    public void setId(int _id) {
        this._id = _id;
    }


}
