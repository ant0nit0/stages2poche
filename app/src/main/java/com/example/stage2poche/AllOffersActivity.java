package com.example.stage2poche;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.stage2poche.api.APIClient;
import com.example.stage2poche.api.ResultatAppel;
import com.example.stage2poche.entities.offres.Offre;
import com.example.stage2poche.entities.offres.OffreConsultee;
import com.example.stage2poche.responses.OffresResponse;

import java.util.ArrayList;
import java.util.List;

public class AllOffersActivity extends StageAppActivity {

    private TextView accueilTV, appNameTV, nbOffresTV;
    private RecyclerView recyclerView;
    private List<Offre> allOffers = new ArrayList<>();
    private int nbOffres = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_offers);

        // Init components
        accueilTV = findViewById(R.id.list_Accueil);
        appNameTV = findViewById(R.id.list_MyStages);
        recyclerView = findViewById(R.id.list_recyclerview);
        nbOffresTV = findViewById(R.id.list_NbOffresEnLigne);

        // Init data
        // We try to get the data from the intent
        // If it fails, we get the data from the API

        /*try {
            allOffers = getIntent().getParcelableArrayListExtra("allOffers", Offre.class);
            // Set the number of offers
            int nbOffres = allOffers.size();
            initializeRecyclerView(nbOffres); // Initialize the RecyclerView after getting data from the intent
        } catch (Exception e) {
            APIClient.getOffres(AllOffersActivity.this, new ResultatAppel<OffresResponse>() {
                @Override
                public void traiterResultat(OffresResponse offres) {
                    allOffers.addAll(offres.offres); // Store all offers
                    int nbOffres = allOffers.size();
                    initializeRecyclerView(nbOffres); // Initialize the RecyclerView after getting data from the API
                }
                @Override
                public void traiterErreur() {
                }
            });
        }
         */
        ArrayList<Offre> offersFromIntent = getIntent().getParcelableArrayListExtra("allOffers", Offre.class);
        if (offersFromIntent != null) {
            allOffers = new ArrayList<>(offersFromIntent);
        } else {
            allOffers = new ArrayList<>();
        }
        // Set the number of offers
        nbOffres = allOffers.size();

        if (offersFromIntent == null) {
            APIClient.getOffres(this, new ResultatAppel<OffresResponse>() {
                @Override
                public void traiterResultat(OffresResponse offres) {
                    allOffers.addAll(offres.offres); // Store all offers
                    nbOffres = allOffers.size();
                    initializeRecyclerView(nbOffres); // Initialize the RecyclerView after getting data from the API
                }
                @Override
                public void traiterErreur() {
                }
            });
        } else {
            initializeRecyclerView(nbOffres); // Initialize the RecyclerView after getting data from the intent
        }

        // Set listeners to go back to main
        accueilTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllOffersActivity.this, MainActivity.class);
                // Clear the stack
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        appNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllOffersActivity.this, MainActivity.class);
                // Clear the stack
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void initializeRecyclerView(int nbOffres) {
        // Set the text
        if(nbOffres == 0) {
            nbOffresTV.setText("Il n'y a actuellement aucune offre en ligne");
        } else if (nbOffres == 1) {
            nbOffresTV.setText("Il n'y a qu'une seule offre en ligne");
        } else {
            nbOffresTV.setText(nbOffres + " offres sont actuellement en ligne");
        }
        // Init recyclerview
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        AllOffersAdapter adapter = new AllOffersAdapter(this, allOffers);
        recyclerView.setAdapter(adapter);
    }


}