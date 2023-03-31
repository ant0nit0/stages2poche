package com.example.stage2poche;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stage2poche.api.APIClient;
import com.example.stage2poche.api.ResultatAppel;
import com.example.stage2poche.entities.Entreprise;
import com.example.stage2poche.entities.offres.Offre;
import com.example.stage2poche.entities.offres.OffreConsultee;
import com.example.stage2poche.requests.CreateOffreConsulteeRequest;
import com.example.stage2poche.responses.OffresConsulteesResponse;
import com.example.stage2poche.responses.PostOffreResponse;

import java.util.ArrayList;
import java.util.List;

public class AllOffersAdapter extends RecyclerView.Adapter<OfferItemViewHolder> {

    private StageAppActivity context;
    private List<Offre> offres = new ArrayList<>();

    public AllOffersAdapter(StageAppActivity context, List<Offre> offres) {
        this.context = context;
        this.offres = offres;
    }


    @Override
    public void onBindViewHolder(OfferItemViewHolder holder, int position) {
        Offre offre = offres.get(position);

        holder.titleTV.setText(offre.intitule);
        String entrepriseId = offre.entreprise.replace("/api/entreprises/", "");
        APIClient.getEntreprise(context, entrepriseId, new ResultatAppel<Entreprise>() {
            @Override
            public void traiterResultat(Entreprise entreprise) {
                if(entreprise != null) {
                    holder.entrepriseTV.setText(entreprise.raisonSociale);
                    holder.villeTV.setText(entreprise.ville);
                } else {
                    holder.entrepriseTV.setText("Entreprise inconnue");
                    holder.villeTV.setText("Ville inconnue");
                }

            }

            @Override
            public void traiterErreur() {
                holder.entrepriseTV.setText("Entreprise inconnue");
                System.out.println("DEBUG : Error while fetching entreprise");
            }
        });

        // Add listener to the card to go to the offer details
        // The listener check if the offer has already been seen by the user
        // If not, it creates a new OffreConsultee in the database
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First, fetch the existing OffreConsultees
                APIClient.getOffresConsultees(context, new ResultatAppel<OffresConsulteesResponse>() {
                    @Override
                    public void traiterResultat(OffresConsulteesResponse response) {
                        boolean alreadyExists = false;

                        // Iterate through the existing OffreConsultees
                        for (OffreConsultee existingOffreConsultee : response.offresConsultees) {
                            // Check if the current OffreConsultee matches the one we want to add
                            if (existingOffreConsultee.offre.equals(offre._id) &&
                                    existingOffreConsultee.compteEtudiant.equals("/api/compte_etudiants/" + context.getCompteId())) {
                                // Set the flag to true if it already exists and break the loop
                                alreadyExists = true;
                                break;
                            }
                        }

                        // If the OffreConsultee doesn't already exist, create a new one
                        if (!alreadyExists) {
                            CreateOffreConsulteeRequest request = new CreateOffreConsulteeRequest(("/api/compte_etudiants/" + context.getCompteId()), offre._id);
                            System.out.println("DEBUG : Creating offre consultee: " + request);
                            APIClient.postOffreConsultee(context, request, new ResultatAppel<PostOffreResponse>() {
                                @Override
                                public void traiterResultat(PostOffreResponse response) {
                                    System.out.println("DEBUG : Offre consultee created: " + response);
                                }

                                @Override
                                public void traiterErreur() {
                                    System.out.println("DEBUG : Error creating offre consultee");
                                    Toast.makeText(context, "Une erreur est survenue lors de la connexion au serveur, veuillez rééssayer", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            System.out.println("DEBUG : Offre consultee already exists");
                        }

                        // Start the new activity
                        Intent intent = new Intent(context, DetailOffreActivity.class);
                        intent.putExtra("offre", offre);
                        context.startActivity(intent);
                    }

                    @Override
                    public void traiterErreur() {
                        System.out.println("DEBUG : Error fetching offres consultees");
                        Toast.makeText(context, "Une erreur est survenue lors de la connexion au serveur, veuillez rééssayer", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @NonNull
    @Override
    public OfferItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.offer_item, parent, false);
        return new OfferItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return offres.size();
    }
}
