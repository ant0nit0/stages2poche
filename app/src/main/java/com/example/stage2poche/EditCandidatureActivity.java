package com.example.stage2poche;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.stage2poche.api.APIClient;
import com.example.stage2poche.api.ResultatAppel;
import com.example.stage2poche.entities.Candidature;
import com.example.stage2poche.entities.offres.Offre;
import com.example.stage2poche.enums.EtatCandidature;
import com.example.stage2poche.requests.CandidatureRequest;
import com.example.stage2poche.responses.CandidaturesResponse;
import com.example.stage2poche.responses.CreateCandidatureResponse;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class EditCandidatureActivity extends StageAppActivity implements AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    private Offre offre;
    private Spinner spinnerEtat;
    private ImageView calendarIcon, hourIcon, infoPopUpEtat;
    private Button annulerBtn, validerBtn, abandonnerBtn;
    private TextView dateTV, hourTV, accueilTV, lesOffresArianeTV, offreArianeTV, intituleTV;
    private EditText descriptionET;
    private int year = -1, month = -1, day = -1, hour = -1, minute = -1, etatIndex;
    private String formattedDateTime, typeAction;
    private Candidature candidature;
    boolean isCandidate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_candidature);

        accueilTV = findViewById(R.id.edit_accueil);
        intituleTV = findViewById(R.id.edit_intitule);
        lesOffresArianeTV = findViewById(R.id.edit_ariane_offres);
        offreArianeTV = findViewById(R.id.edit_ariane_offre);
        annulerBtn = findViewById(R.id.edit_annuler_btn);
        validerBtn = findViewById(R.id.edit_valider_btn);
        abandonnerBtn = findViewById(R.id.edit_abandonner_btn);
        descriptionET = findViewById(R.id.edit_type_action_et);
        calendarIcon = findViewById(R.id.edit_calendar_icon); // Récupération de l'ImageView activant le DatePicker
        hourIcon = findViewById(R.id.edit_hour_icon); // Récupération de l'ImageView activant le TimePicker
        dateTV = findViewById(R.id.edit_date_textView); // Récupération de la TextView affichant la date sélectionnée
        hourTV = findViewById(R.id.edit_hour_textView); // Récupération de la TextView affichant l'heure sélectionnée
        spinnerEtat = findViewById(R.id.edit_spinner_etat); // Récupération du Spinner affichant les états de la candidature
        infoPopUpEtat = findViewById(R.id.edit_info_pop_up_etat); // Récupération de l'ImageView activant le PopUp
        spinnerEtat.setOnItemSelectedListener(this); // Définition de l'itemListener sur le spinner


        // Get the Data
        try {
            offre = (Offre) getIntent().getParcelableExtra("offre", Offre.class);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Une erreur est survenue, veuillez rééssayer", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Init
        checkIfAlreadyCandidate();

        // Listeners
        // Fil d'ariane
        offreArianeTV.setOnClickListener(view -> finish());
        lesOffresArianeTV.setOnClickListener(v -> {
            Intent intent = new Intent(EditCandidatureActivity.this, AllOffersActivity.class);
            // Clear the stack
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        accueilTV.setOnClickListener(v -> {
            Intent intent = new Intent(EditCandidatureActivity.this, MainActivity.class);
            // Clear the stack
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        // Date and time
        calendarIcon.setOnClickListener(view -> showDatePickerDialog());
        dateTV.setOnClickListener(view -> showDatePickerDialog());
        hourIcon.setOnClickListener(view -> showTimePickerDialog());
        hourTV.setOnClickListener(view -> showTimePickerDialog());
        // Pop up
        infoPopUpEtat.setOnClickListener(v -> {
            Dialog popup = new Dialog(v.getContext());
            popup.setContentView(R.layout.edit_popup_info);
            popup.show();
            ImageView closeBtn = popup.findViewById(R.id.popup_close_btn);
            closeBtn.setOnClickListener(view -> popup.dismiss());
        });
        // Buttons
        annulerBtn.setOnClickListener(view -> finish());
        validerBtn.setOnClickListener(view -> {
            try {
                valider();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        abandonnerBtn.setOnClickListener(view -> {
            showDeleteConfirmationDialog();
        });

        // Création d'un ArrayAdapter pour le spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.etats_candidature, android.R.layout.simple_spinner_item);
        // Spécification du layout à utiliser lorsque le spinner est déroulé
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Application de l'Adapter au spinner
        spinnerEtat.setAdapter(adapter);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteCandidature();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog, do nothing
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteCandidature(){
        System.out.println("Candidature id : " + candidature._id);
        APIClient.deleteCandidature(this, candidature._id.replace("/api/candidatures/", ""), new ResultatAppel<Candidature>() {
            @Override
            public void traiterResultat(Candidature response) {
                System.out.println("Candidature supprimée");
                System.out.println(response);
                Toast.makeText(EditCandidatureActivity.this, "Candidature supprimée", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void traiterErreur() {
                Toast.makeText(EditCandidatureActivity.this, "Une erreur est survenue, veuillez rééssayer", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void valider() throws ParseException {

        if(!checkDate()) return;
        if(!checkEtat()) return;

        // Create the request
        CandidatureRequest request = new CandidatureRequest(
                "/api/compte_etudiants/" + this.getCompteId(),
                offre._id,
                descriptionET.getText().toString(),
                formattedDateTime,
                ("/api/etat_candidatures/" + etatIndex)
        );

        // Si l'utilisateur n'est pas candidat, on créé une nouvelle candidature
        if(!isCandidate){
            // Send the request
            APIClient.postCandidature(EditCandidatureActivity.this, request, new ResultatAppel<CreateCandidatureResponse>() {
                @Override
                public void traiterResultat(CreateCandidatureResponse response) {
                    System.out.println(response);
                    System.out.println("Response: " + new Gson().toJson(response)); // Log the response in JSON format
                    Toast.makeText(EditCandidatureActivity.this, "Candidature créée", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void traiterErreur() {
                }
            });
        } else {
            String id = candidature._id.replace("/api/candidatures/", "");
            APIClient.updateCandidature(this, id, request,  new ResultatAppel<Candidature>() {
                @Override
                public void traiterResultat(Candidature response) {
                    System.out.println("Candidature mise à jour");
                    System.out.println(response);
                    Toast.makeText(EditCandidatureActivity.this, "Candidature mise à jour", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void traiterErreur() {
                    Toast.makeText(EditCandidatureActivity.this, "Une erreur est survenue, veuillez rééssayer", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean checkEtat(){
        // Check if the user has selected an etat
        if(etatIndex == 0){
            Toast.makeText(this, "Veuillez sélectionner un état", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkDate() {
        // Check if the user has enter a date, and if the date is not in the past
        if((formattedDateTime == null && candidature == null) || (formattedDateTime == null && candidature.dateAction == null)){
            Toast.makeText(this, "Veuillez sélectionner une date", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Get the date to catch
        String date = formattedDateTime == null ? candidature.dateAction : formattedDateTime;
        // Set a calendar to now
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.FRANCE);
        try{
            // Check if the date is in the past
            if(calendar.getTime().after(dateFormat.parse(date))){
                // If so, show a toast and return false
                Toast.makeText(this, "Veuillez sélectionner une date future", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private void checkIfAlreadyCandidate(){
        // Get the offre Id
        String offreId = offre._id;
        // Get all the candidatures
        APIClient.getCandidatures(EditCandidatureActivity.this, new ResultatAppel<CandidaturesResponse>() {
            @Override
            public void traiterResultat(CandidaturesResponse response) {
                // Get the candidatures
                List<Candidature> candidatures = response.candidatures;
                // Check if the user is already candidate
                for (Candidature c : candidatures) {
                    if (c.offre.equals(offreId)) {
                        candidature = c;
                        isCandidate = true;
                        // Get the etat index
                        try {
                            String cId = c.etatCandidature.replace("/api/etat_candidatures/", "");
                            etatIndex = Integer.parseInt(cId);
                            // Set the spinner to the right position
                            spinnerEtat.setSelection(parseStateToDropDown(etatIndex));
                            // Set the input field
                            descriptionET.setText(c.typeAction);
                            // Set the time and Date
                            try {
                                String dateCand = c.dateAction;
                                // Format the date
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.FRANCE);
                                Date date = dateFormat.parse(dateCand);
                                // Extract values to set class variables
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                EditCandidatureActivity.this.year = calendar.get(Calendar.YEAR);
                                EditCandidatureActivity.this.month = calendar.get(Calendar.MONTH);
                                EditCandidatureActivity.this.day = calendar.get(Calendar.DAY_OF_MONTH);
                                EditCandidatureActivity.this.hour = calendar.get(Calendar.HOUR_OF_DAY);
                                EditCandidatureActivity.this.minute = calendar.get(Calendar.MINUTE);
                                // Set the date and time
                                formatDateTimeString();
                                // initView();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            etatIndex = 0;
                        }
                    }
                }
                initView();
            }

            @Override
            public void traiterErreur() {
                initView();
            }
        });
    }

    private void initView(){
        intituleTV.setText(offre.intitule);
        if(!isCandidate) {
            validerBtn.setText("Candidater");
            // If the user is already candidate, we disable the button
            abandonnerBtn.setEnabled(false);
            abandonnerBtn.setAlpha(0.5f);
        }
        else {
            validerBtn.setText("Modifier");
            // If the user is not already candidate, we disable the button
            abandonnerBtn.setEnabled(true);
            abandonnerBtn.setAlpha(1f);
            try {
                Calendar calendar = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.FRANCE);
                calendar.setTime(Objects.requireNonNull(dateFormat.parse(candidature.dateAction)));
                // Set the textviews
                dateTV.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
                hourTV.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        // On récupère l'élément sélectionné
        etatIndex = parseDropDownToState(position);
    }

    /**
     * This class is used to parse the dropdown value to the etatCandidature State.
     * Used because they are not in the same order.
     * @param position the position of the dropdown item
     * @return the state of the candidature
     */
    private int parseDropDownToState(int position){
        switch (position){
            case 1:
                return EtatCandidature.A_ENVOYER.getId();
            case 2:
                return EtatCandidature.ATTENTE_REPONSE.getId();
            case 5:
                return EtatCandidature.REFUSEE.getId();
            case 3:
                return EtatCandidature.ATTENTE_RDV.getId();
            case 4:
                return EtatCandidature.ATTENTE_ENTRETIEN.getId();
            case 6:
                return EtatCandidature.ACCEPTEE.getId();
            default:
                return EtatCandidature.UNKNOWN.getId();
        }
    }

    /**
     * This class is used to parse the etatCandidature State to the dropdown value.
     * Used because they are not in the same order.
     * @param state the state of the etatCandidature
     * @return the according position in the dropdown
     */
    private int parseStateToDropDown(int state){
        switch (state){
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 5;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 6;
            default:
                return 0;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Rien à faire si aucun élément n'est sélectionné
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // On set les variables de classe
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        formatDateTimeString();

        // On récupère la date sélectionnée
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        // On formate la date pour l'afficher dans le TextView
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(calendar.getTime());

        // On affiche la date dans le TextView
        dateTV.setText(formattedDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // On set les variables de classe
        this.hour = hourOfDay;
        this.minute = minute;
        formatDateTimeString();

        // On récupère l'heure sélectionnée
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        // On formate l'heure pour l'afficher dans le TextView
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedTime = sdf.format(calendar.getTime());

        // On affiche l'heure dans le TextView
        hourTV.setText(formattedTime);
    }

    /**
     * This method is used to format the date and time string, with the class variables. (year, month, day, hour, minute)
     * It will set the formattedDateTime variable.
     * The format is : yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     */
    private void formatDateTimeString() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        // Set the desired format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Format the date and time string
        formattedDateTime = sdf.format(calendar.getTime());

        // Use the formattedDateTime as needed
        Log.d("FormattedDateTime", formattedDateTime);
    }




    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, true);
        // Set the minimum time to now
        timePickerDialog.updateTime(hour, minute);
        // Show the TimePickerDialog
        timePickerDialog.show();
    }

}