package com.example.stage2poche;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.stage2poche.api.APIClient;
import com.example.stage2poche.api.ResultatAppel;
import com.example.stage2poche.entities.Candidature;
import com.example.stage2poche.entities.Entreprise;
import com.example.stage2poche.entities.offres.Offre;
import com.example.stage2poche.entities.offres.OffreConsultee;
import com.example.stage2poche.enums.EtatCandidature;
import com.example.stage2poche.responses.CandidaturesResponse;
import com.example.stage2poche.responses.OffresConsulteesResponse;
import com.example.stage2poche.responses.OffresResponse;
import com.example.stage2poche.responses.OffresRetenuesResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends StageAppActivity {

    private DrawerLayout drawerLayout;
    private ImageView asideMenuQuit, asideMenu;

    private TextView nbOffresConsulteesTV, nbOffresRetenuesTV, nbCandidaturesEnvoyeesTV, totalOffresTV, drawerUsernameTV, drawerLastCoTV;

    private LinearLayout candidaturesLayout;
    private ProgressBar progressBar;

    private Button seeAllOffersButton;

    // De la forme : { Offre.id: Entreprise }
    private Map<String, Entreprise> entreprisesMap = new HashMap<>();
    private List<Offre> allOffers = new ArrayList<>();


    // Liste des candidatures de l'étudiant connecté
    // Ex : { EtatCandidature.ACCEPTEE : [candidature1, candidature2], EtatCandidature.REFUSEE : [candidature3], ... }
    private Map<EtatCandidature, List<Candidature>> candidatures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init view
        nbOffresConsulteesTV = findViewById(R.id.main_offres_consultees);
        nbOffresRetenuesTV = findViewById(R.id.main_offres_retenues);
        nbCandidaturesEnvoyeesTV = findViewById(R.id.main_candidatures_envoyees);
        totalOffresTV = findViewById(R.id.main_nb_total_offres);
        candidaturesLayout = findViewById(R.id.main_candidatures_container);
        seeAllOffersButton = findViewById(R.id.main_see_all_offers);
        progressBar = findViewById(R.id.main_progress_bar);
        drawerLayout = findViewById(R.id.drawer_layout);
        asideMenuQuit = findViewById(R.id.aside_menu_quit);
        asideMenu = findViewById(R.id.aside_menu);
        drawerUsernameTV = findViewById(R.id.drawer_compte_username);
        drawerLastCoTV = findViewById(R.id.drawer_compte_last_co);

        initDrawer();

        // Drawer
        asideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // Définition d'un OnClickListener pour l'ImageView
        asideMenuQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        candidatures = new HashMap<>();

        seeAllOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllOffersActivity.class);
                intent.putParcelableArrayListExtra("allOffers", (ArrayList<Offre>) allOffers);
                startActivity(intent);
            }
        });

        refreshNBOffres();
        refreshCandidatures();
    }


    private void initDrawer(){
        drawerUsernameTV.setText(getLogin());

    }


    @Override
    protected void onRestart() {
        // Refresh the offers and the candidatures when the user comes back to the activity
        super.onRestart();
        entreprisesMap.clear();
        allOffers.clear();
        candidatures.clear();
        refreshNBOffres();
        refreshCandidatures();
    }

    /**
     * Refresh the number of offers
     * Get the number of : total offers, offers consulted, offers retained
     * Set the top Textviews with the number
     */
    private void refreshNBOffres() {
        APIClient.getOffres(this, new ResultatAppel<OffresResponse>() {

            @Override
            public void traiterResultat(OffresResponse offres) {
                totalOffresTV.setText(String.valueOf(offres.offres.size()));
                allOffers.addAll(offres.offres); // Store all offers
            }

            @Override
            public void traiterErreur() {
            }
        });

        APIClient.getOffresConsultees(this, new ResultatAppel<OffresConsulteesResponse>() {

            @Override
            public void traiterResultat(OffresConsulteesResponse response) {
                nbOffresConsulteesTV.setText(response.offresConsultees.size() + " offres consultée" + (response.offresConsultees.size() > 1 ? "s" : ""));
            }

            @Override
            public void traiterErreur() {
            }
        });

        APIClient.getOffresRetenues(this, new ResultatAppel<OffresRetenuesResponse>() {

            @Override
            public void traiterResultat(OffresRetenuesResponse response) {
                nbOffresRetenuesTV.setText(response.offresRetenues.size() + " offres retenue" + (response.offresRetenues.size() > 1 ? "s" : ""));
            }

            @Override
            public void traiterErreur() {
            }
        });
    }

    /**
     * Refresh the candidatures
     * Get the candidatures of the connected student and display them
     * Set the candidatures map
     */
    private void refreshCandidatures() {
        progressBar.setVisibility(View.VISIBLE);
        APIClient.getCandidatures(this, new ResultatAppel<CandidaturesResponse>() {

            @Override
            public void traiterResultat(CandidaturesResponse response) {
                int nbCandidatures = response.candidatures.size();
                // Set the number of candidatures in textview
                nbCandidaturesEnvoyeesTV.setText("Nombre de candidature" + (nbCandidatures > 1 ? "s" : "") + " envoyée" + (nbCandidatures > 1 ? "s : " : " : ") + nbCandidatures);

                // Set the map of candidatures
                for(Candidature candidature : response.candidatures) {
                    // On récupère l'état de la candidature
                    EtatCandidature state = EtatCandidature.fromId(candidature.getEtatCandidatureId());
                    // Si l'état n'est pas dans la map, on l'ajoute
                    if(!candidatures.containsKey(state)) {
                        candidatures.put(state, new ArrayList<>());
                    }
                    // On ajoute la candidature à la liste de l'état
                    candidatures.get(state).add(candidature);

                    // On récupère et enregistre l'offre de la candidature
                    final Offre correspondingOffre;
                    for(Offre offre : allOffers) {
                        if(offre._id.equals(candidature.offre)) { // Offre correspondante trouvée
                            correspondingOffre = offre; // On enregistre l'offre
                            String entrepriseId = correspondingOffre.entreprise.replace("/api/entreprises/", ""); // On récupère l'id de l'entreprise
                            APIClient.getEntreprise(MainActivity.this, entrepriseId, new ResultatAppel<Entreprise>() {
                                @Override
                                public void traiterResultat(Entreprise entreprise) {
                                    entreprisesMap.put(correspondingOffre._id, entreprise); // On enregistre l'entreprise
                                }

                                @Override
                                public void traiterErreur() {
                                    // TODO : Afficher un message d'erreur expliquant qu'il y a eu un pb lors de la recup des données
                                }
                            });
                            break;
                        }
                    }
                }

                // Create the view of the candidatures
                createCandidaturesListView();
            }

            @Override
            public void traiterErreur() {
                // TODO : Afficher un message d'erreur expliquant qu'il y a eu un pb lors de la recup des données
            }
        });
    }


    private void createCandidaturesListView() {
        // The 3 following lines are used to know when all the offers have been fetched, to avoid display errors
        AtomicInteger offersFetchedCount = new AtomicInteger(0);
        int totalOffers = candidatures.values().stream().mapToInt(List::size).sum();
        Map<EtatCandidature, List<CardView>> etatCardViewsMap = new HashMap<>(); // Map servant a stocker les cardview a afficher en fonction des etats

        // On boucle sur les clés de la map générales
        for(EtatCandidature etat : candidatures.keySet()) {
            // On ajoute les candidatures de l'état
            for(Candidature candidature : candidatures.get(etat)) {
                int offreId = candidature.getOffreId();
                // Get the offer
                APIClient.getOffre( this, offreId, new ResultatAppel<Offre>() {
                    @Override
                    public void traiterResultat(Offre response) {
                        // Create the cardview
                        CardView candidatureCardView = createCandidatureView(candidature, response, etat);
                        // Check if the List<CardView> is initialized, if not, initialize it
                        etatCardViewsMap.computeIfAbsent(etat, k -> new ArrayList<>());
                        // Add the CardView to the list
                        etatCardViewsMap.get(etat).add(candidatureCardView);
                        // Increment the counter
                        offersFetchedCount.incrementAndGet();
                        // If all the offers have been fetched, display them
                        if (offersFetchedCount.get() == totalOffers) {
                            progressBar.setVisibility(View.GONE);
                            displayAllCandidatures(etatCardViewsMap);
                        }
                    }

                    @Override
                    public void traiterErreur() {
                    }
                });
            }
        }
    }

    private void displayAllCandidatures(Map<EtatCandidature, List<CardView>> etatCardViewsMap) {
        // On vérifie que le layout est vide
        candidaturesLayout.removeAllViews();

        for (EtatCandidature etat : etatCardViewsMap.keySet()) {
            createEtatTitle(etat);
            for (CardView candidatureCardView : etatCardViewsMap.get(etat)) {
                candidaturesLayout.addView(candidatureCardView);
            }
        }
    }


    private void createEtatTitle(EtatCandidature etat) {
        // Création du LinearLayout pour contenir l'icone et le titre
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        // Création de l'icone
        ImageView icon = new ImageView(this);
        icon.setPadding(0, 24, 20, 0);
        icon.setImageResource(EtatCandidature.getIcon(etat));
        // Création d'un TextView pour l'état de la candidature
        TextView etatTitle = new TextView(this);
        etatTitle.setPadding(0, 20, 0, 0);
        etatTitle.setText(EtatCandidature.getString(etat));
        etatTitle.setTextColor(0xff000000);
        etatTitle.setTextSize(16);
        // Set the font
        Typeface typeface = ResourcesCompat.getFont(this, R.font.poppins_semibold);
        etatTitle.setTypeface(typeface);
        // Ajout de l'icone et du TextView au LinearLayout
        ll.addView(icon);
        ll.addView(etatTitle);
        // Ajout du LinearLayout à la vue
        candidaturesLayout.addView(ll);
    }

    private CardView createCandidatureView(Candidature candidature, Offre offre, EtatCandidature etat) {
        // Création d'une CardView
        CardView card = new CardView(this);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailOffreActivity.class);
                intent.putExtra("offre", offre);
                startActivity(intent);
            }
        });
        card.setRadius(30);
        card.setContentPadding(24, 20, 24, 20);
        card.setCardElevation(5);
        card.setCardBackgroundColor(EtatCandidature.getColor(etat, this));

        // Add a little margin on top of the card
        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(4, 0, 4, 15);
        card.setLayoutParams(cardParams);

        // Création d'un LinearLayout vertical pour contenir les éléments
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Création d'un TextView pour le nom de l'offre
        TextView nomOffre = new TextView(this);
        nomOffre.setText(offre.intitule);
        nomOffre.setTextColor(0xff000000);
        nomOffre.setTextSize(18);
        // Set the font
        Typeface typeface = ResourcesCompat.getFont(this, R.font.poppins_semibold);
        nomOffre.setTypeface(typeface);

        // Création d'un TextView pour l'entreprise
        TextView entreprise = new TextView(this);
        try {
            entreprise.setText(entreprisesMap.get(offre._id).raisonSociale);
        } catch (Exception e) {
            entreprise.setText("Entreprise inconnue");
        }
        // Set the font
        Typeface typefaceSub = ResourcesCompat.getFont(this, R.font.poppins_regular);
        entreprise.setTypeface(typefaceSub);

        // Création d'un TLinearLayout horizontal pour contenir le nom de la ville et le text "voir"
        LinearLayout llBottom = new LinearLayout(this);
        llBottom.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llBottomParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        llBottom.setLayoutParams(llBottomParams);

        // Création d'un TextView pour le nom de la ville
        TextView ville = new TextView(this);
        try {
            ville.setText(entreprisesMap.get(offre._id).ville);
        } catch (Exception e) {
            ville.setText("Ville inconnue");
        }
        // Set the font
        ville.setTypeface(typefaceSub);

        // Création d'un TextView pour le text "voir"
        TextView voir = new TextView(this);
        voir.setText("Voir");
        voir.setTextSize(20);
        voir.setTextColor(getResources().getColor(R.color.dark_blue));
        // Set the font
        Typeface typefaceVoir = ResourcesCompat.getFont(this, R.font.poppins_semibold);
        voir.setTypeface(typefaceVoir);
        // Faire que le texte soit aligné à droite et prenne toute la place restante
        voir.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        LinearLayout.LayoutParams voirParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        voir.setLayoutParams(voirParams);


        // Ajout des éléments au LinearLayout
        llBottom.addView(ville);
        llBottom.addView(voir);

        // Ajout des éléments au LinearLayout principal
        ll.addView(nomOffre);
        ll.addView(entreprise);
        ll.addView(llBottom);

        // Ajout du LinearLayout principal à la CardView
        card.addView(ll);
        return card;
    }

}