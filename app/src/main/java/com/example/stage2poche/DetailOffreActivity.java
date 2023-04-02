package com.example.stage2poche;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stage2poche.api.APIClient;
import com.example.stage2poche.api.ResultatAppel;
import com.example.stage2poche.entities.Candidature;
import com.example.stage2poche.entities.Entreprise;
import com.example.stage2poche.entities.offres.Offre;
import com.example.stage2poche.entities.offres.OffreRetenue;
import com.example.stage2poche.enums.EtatCandidature;
import com.example.stage2poche.enums.EtatOffre;
import com.example.stage2poche.requests.CreateOffreRetenueRequest;
import com.example.stage2poche.responses.CandidaturesResponse;
import com.example.stage2poche.responses.OffresRetenuesResponse;
import com.example.stage2poche.responses.PostOffreResponse;

import java.util.List;

public class DetailOffreActivity extends StageAppActivity {

    private TextView appNameTv, accueilTV, lesOffresTV, titleTV, statusTV, entrepriseTV, villeTV, lienTV, etatCandidatureTV;
    private ImageView statusCircle;
    private Button modifierBtn;
    private Offre offre;

    private boolean candidatureAcceptee = false, comeFromCandidature = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_offre);

        // Init components
        appNameTv = findViewById(R.id.detail_appName);
        accueilTV = findViewById(R.id.detail_accueil);
        lesOffresTV = findViewById(R.id.detail_ariane_offres);
        titleTV = findViewById(R.id.detail_title);
        statusTV = findViewById(R.id.detail_statut_info);
        entrepriseTV = findViewById(R.id.detail_entreprise);
        villeTV = findViewById(R.id.detail_ville);
        lienTV = findViewById(R.id.detail_url);
        modifierBtn = findViewById(R.id.detail_candidater_btn);
        statusCircle = findViewById(R.id.detail_statut_circle);
        etatCandidatureTV = findViewById(R.id.detail_etat_candidature);

        // Get the offre
        try {
            offre = (Offre) getIntent().getParcelableExtra("offre", Offre.class);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Une erreur est survenue lors de la récupération des données.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // If the page is load from a candidature, dont show "lesOffres"
        comeFromCandidature = getIntent().getBooleanExtra("comeFromCandidature", false);
        if(comeFromCandidature) {
            lesOffresTV.setVisibility(View.GONE);
        }

        // Check if there is a candidature
        checkIfAlreadyCandidate();
        // Set the view
        initView();

        // Set the listener to go back
        accueilTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailOffreActivity.this, MainActivity.class);
                // Clear the stack
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        appNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailOffreActivity.this, MainActivity.class);
                // Clear the stack
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        lesOffresTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if there is a candidature
        checkIfAlreadyCandidate();
        // Set the view
        initView();
        setButton();
    }

    private void initView() {
        titleTV.setText(offre.intitule);
        statusTV.setText(offre.etatOffre);

        // Get the entreprise
        String entrepriseId = offre.entreprise.replace("/api/entreprises/", "");
        APIClient.getEntreprise(this, entrepriseId, new ResultatAppel<Entreprise>() {
            @Override
            public void traiterResultat(Entreprise entreprise) {
                if(entreprise != null) {
                    entrepriseTV.setText(entreprise.raisonSociale);
                    villeTV.setText(entreprise.ville);
                } else {
                    entrepriseTV.setText("Entreprise inconnue");
                    villeTV.setText("Ville inconnue");
                }

            }

            @Override
            public void traiterErreur() {
                entrepriseTV.setText("Entreprise inconnue");
                System.out.println("DEBUG : Error while fetching entreprise");
            }
        });

        // Open the link in browser on click
        String url = offre.urlPieceJointe;
        SpannableString spannableString = new SpannableString(url);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailOffreActivity.this);
                builder.setMessage("Voulez-vous ouvrir le lien dans le navigateur ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };

        spannableString.setSpan(clickableSpan, 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        lienTV.setText(spannableString);
        lienTV.setMovementMethod(LinkMovementMethod.getInstance());

        // Set the status
        try {
            int offreId = Integer.parseInt(offre.etatOffre.replace("/api/etat_offres/", ""));
            EtatOffre status = EtatOffre.fromId(offreId);
            statusTV.setText(EtatOffre.getString(status));
            // Circle
            statusCircle.setImageTintList(ColorStateList.valueOf(EtatOffre.getColor(status, DetailOffreActivity.this)));
        } catch (Exception e) {
            e.printStackTrace();
            statusTV.setText("Statut inconnu");
        }

    }


    private void checkIfAlreadyCandidate(){
        // Get the offre Id
        String offreId = offre._id;
        // Get all the candidatures
        APIClient.getCandidatures(DetailOffreActivity.this, new ResultatAppel<CandidaturesResponse>() {
            @Override
            public void traiterResultat(CandidaturesResponse response) {
                if(response == null) {
                    return;
                }
                if(response.candidatures == null) {
                    setButton();
                    return;
                }
                // Get the candidatures
                List<Candidature> candidatures = response.candidatures;
                // Check if the user is already candidate
                for (Candidature c : candidatures) {
                    if (c.offre.equals(offreId)) {
                        modifierBtn.setText("Modifier ma candidature");
                        try {
                            int id = Integer.parseInt(c.etatCandidature.replace("/api/etat_candidatures/", ""));
                            if(id == 6) {
                                candidatureAcceptee = true;
                            }
                            etatCandidatureTV.setText(EtatCandidature.getString(EtatCandidature.fromId(id)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                setButton();
            }

            @Override
            public void traiterErreur() {
                initView();
                setButton();
            }
        });
    }


    private void setButton() {
        try {
            int offreId = Integer.parseInt(offre.etatOffre.replace("/api/etat_offres/", ""));
            if(offreId != 1) {
                modifierBtn.setVisibility(View.GONE);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            modifierBtn.setVisibility(View.GONE);
            return;
        }
        // Get all offreRetenues
        APIClient.getOffresRetenues(DetailOffreActivity.this, new ResultatAppel<OffresRetenuesResponse>() {

            @Override
            public void traiterResultat(OffresRetenuesResponse response) {
                if(response == null) return;
                // Get the offreRetenues
                List<OffreRetenue> offreRetenues = response.offresRetenues;
                // Check if the user is already candidate
                for (OffreRetenue o : offreRetenues) {
                    if (o.offre.equals(offre._id) || o.compteEtudiant.replace("/api/compte_etudiants/", "").equals(getCompteId())) {
                        modifierBtn.setVisibility(View.GONE);
                        return;
                    }
                }
                modifierBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void traiterErreur() {
                modifierBtn.setVisibility(View.GONE);
            }
        });

        if(candidatureAcceptee) {
            modifierBtn.setText("Valider cette candidature");
            modifierBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Êtes vous sûr de vouloir valider cette candidature ? Vous ne pourrez plus postuler sur d'autres offres après avoir clické sur Oui.")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Perform the action for "Oui" button
                                validateCandidature();
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Cancel the action for "Non" button
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            });
        } else {
            modifierBtn.setOnClickListener(v -> {
                Intent intent = new Intent(DetailOffreActivity.this, EditCandidatureActivity.class);
                intent.putExtra("offre", offre);
                intent.putExtra("comeFromCandidature", comeFromCandidature);
                startActivity(intent);
            });
        }
    }


    private void validateCandidature() {
        // Create the request
        CreateOffreRetenueRequest request = new CreateOffreRetenueRequest(
                "/api/compte_etudiants/" + getCompteId(),
                offre._id
        );
        APIClient.postOffreRetenue(this, request, new ResultatAppel<PostOffreResponse>() {
            @Override
            public void traiterResultat(PostOffreResponse response) {
                // Make a toast
                Toast.makeText(DetailOffreActivity.this, "Candidature validée", Toast.LENGTH_SHORT).show();
                modifierBtn.setVisibility(View.GONE);
                /* TODO: change permission to allow the app to change the state of the offer
                Offre updatedOffre = new Offre(offre);
                updatedOffre.setEtatOffre("/api/etat_offres/2");
                String id = offre._id.replace("/api/offres/", "");
                APIClient.updateOffre(DetailOffreActivity.this, id, updatedOffre, new ResultatAppel<Offre>() {
                    @Override
                    public void traiterResultat(Offre offre) {
                        // Make a toast
                        Toast.makeText(DetailOffreActivity.this, "Offre validée", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void traiterErreur() {
                        Toast.makeText(DetailOffreActivity.this, "Erreur lors de la validation de l'offre", Toast.LENGTH_SHORT).show();
                    }
                });
                 */
            }

            @Override
            public void traiterErreur() {
                Toast.makeText(DetailOffreActivity.this, "Erreur lors de la validation de la candidature", Toast.LENGTH_SHORT).show();
            }
        });
    }






}