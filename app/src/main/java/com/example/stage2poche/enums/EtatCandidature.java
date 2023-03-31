package com.example.stage2poche.enums;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.example.stage2poche.R;

public enum EtatCandidature {

    UNKNOWN(0),
    A_ENVOYER(1),
    ATTENTE_REPONSE(2),
    REFUSEE(3),
    ATTENTE_RDV(4),
    ATTENTE_ENTRETIEN(5),
    ACCEPTEE(6);

    private static final String BASE_URL = "/api/etat_candidatures/";
    int _id;
    EtatCandidature(int _id) {
        this._id = _id;
    }

    public String getURL() {
        return BASE_URL + this._id;
    }

    public int getId() {
        return this._id;
    }

    public static EtatCandidature fromId(int id) {
        for (EtatCandidature etatCandidature : EtatCandidature.values()) {
            if (etatCandidature._id == id) {
                return etatCandidature;
            }
        }
        return EtatCandidature.UNKNOWN;
    }

    public static String getString(EtatCandidature etatCandidature) {
        switch (etatCandidature) {
            case A_ENVOYER:
                return "A envoyer";
            case ATTENTE_REPONSE:
                return "En attente de réponse";
            case REFUSEE:
                return "Refusée";
            case ATTENTE_RDV:
                return "En attente de rendez-vous";
            case ATTENTE_ENTRETIEN:
                return "En attente d'entretien";
            case ACCEPTEE:
                return "Acceptée";
            default:
                return "Inconnu";
        }
    }


    public static @ColorInt int getColor(EtatCandidature etatCandidature, Context context) {
        switch (etatCandidature) {
            case A_ENVOYER:
                return ContextCompat.getColor(context, R.color.card_a_envoyer);
            case ATTENTE_REPONSE:
                return ContextCompat.getColor(context, R.color.card_attente_reponse);
            case REFUSEE:
                return ContextCompat.getColor(context, R.color.card_refusee);
            case ATTENTE_RDV:
            case ATTENTE_ENTRETIEN:
                return ContextCompat.getColor(context, R.color.card_attente_entretien);
            case ACCEPTEE:
                return ContextCompat.getColor(context, R.color.card_acceptee);
            default:
                return 0xFFeab676;
        }
    }

    public static int getIcon(EtatCandidature etat){
        switch (etat) {
            case A_ENVOYER:
                return R.drawable.card_a_envoyer_icon;
            case ATTENTE_REPONSE:
                return R.drawable.card_attente_reponse_icon;
            case REFUSEE:
                return R.drawable.card_refusee_icon;
            case ATTENTE_RDV:
                return R.drawable.card_attente_rdv_icon;
            case ATTENTE_ENTRETIEN:
                return R.drawable.card_attente_rdv_icon;
            case ACCEPTEE:
                return R.drawable.card_acceptee_icon;
            default:
                return R.drawable.error_icon;
        }
    }

    public void setId(int _id) {
        this._id = _id;
    }

}
