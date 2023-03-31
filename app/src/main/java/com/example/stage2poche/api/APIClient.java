package com.example.stage2poche.api;

import android.util.Log;

import com.example.stage2poche.StageAppActivity;
import com.example.stage2poche.entities.Candidature;
import com.example.stage2poche.entities.CompteEtudiant;
import com.example.stage2poche.entities.Entreprise;
import com.example.stage2poche.entities.offres.Offre;
import com.example.stage2poche.requests.CandidatureRequest;
import com.example.stage2poche.requests.CreateOffreConsulteeRequest;
import com.example.stage2poche.requests.CreateOffreRetenueRequest;
import com.example.stage2poche.responses.CandidaturesResponse;
import com.example.stage2poche.responses.CreateCandidatureResponse;
import com.example.stage2poche.responses.OffresConsulteesResponse;
import com.example.stage2poche.responses.OffresResponse;
import com.example.stage2poche.responses.OffresRetenuesResponse;
import com.example.stage2poche.responses.PostOffreResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    //https://square.github.io/retrofit/?utm_source=developer.android.com&utm_medium=referral
    //https://www.digitalocean.com/community/tutorials/retrofit-android-example-tutorial

     private static final String BASE_URL = "http://192.168.141.201:8000/";
    //private static final String BASE_URL = "http://127.0.0.1:8000/";

    // Init http receptor
    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    // Instance retrofit
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // ADDED
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public static APIService getAPIService() {
        return retrofit.create(APIService.class);
    }

    /**
     * Get a student account, using the API
     * @param activity the activity that call this method
     * @param id the id of the student account
     * @param cllbck the callback to call when the result is ready
     */
    public static void getCompteEtudiant(StageAppActivity activity, String id, ResultatAppel<CompteEtudiant> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<CompteEtudiant> call = apiInterface.doGetCompteEtudiant(getBearer(activity), id);
        APIClient.<CompteEtudiant>doCall(call, cllbck);
    }

    // __________________________________________ OFFERS _______________________________________________ \\

    /**
     * Get the offers, using the API
     * @param activity the activity that call this method
     * @param cllbck the callback to call when the result is ready
     */
    public static void getOffres(StageAppActivity activity, ResultatAppel<OffresResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<OffresResponse> call = apiInterface.doGetOffres(getBearer(activity));
        APIClient.<OffresResponse>doCall(call, cllbck);
    }

    /**
     * Get an offer thanks to its id, using the API
     * @param activity the activity that call this method
     * @param id the id of the offer
     * @param cllbck the callback to call when the result is ready
     */
    public static void getOffre(StageAppActivity activity, int id, ResultatAppel<Offre> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<Offre> call = apiInterface.doGetOffre(getBearer(activity), id);
        APIClient.<Offre>doCall(call, cllbck);
    }

    /**
     * Update an offer, using the API
     * @param activity the activity that call this method
     * @param id the id of the offer
     * @param cllbck the callback to call when the result is ready
     */
    public static void updateOffre(StageAppActivity activity, String id, Offre offre, ResultatAppel<Offre> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<Offre> call = apiInterface.doUpdateOffre(getBearer(activity), id, offre);
        APIClient.<Offre>doCall(call, cllbck);
    }

    /**
     * Get the offers that have been seen, using the API
     * @param activity the activity that call this method
     * @param cllbck the callback to call when the result is ready
     */
    public static void getOffresConsultees(StageAppActivity activity, ResultatAppel<OffresConsulteesResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<OffresConsulteesResponse> call = apiInterface.doGetOffresConsultees(getBearer(activity));
        APIClient.doCall(call, cllbck);
    }

    /**
     * Get the offers that have been selected, using the API
     * @param activity the activity that call this method
     * @param cllbck the callback to call when the result is ready
     */
    public static void getOffresRetenues(StageAppActivity activity, ResultatAppel<OffresRetenuesResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<OffresRetenuesResponse> call = apiInterface.doGetOffresRetenues(getBearer(activity));
        APIClient.doCall(call, cllbck);
    }


    /**
     * Create an OffreRetenue, using the API
     * @param activity the activity that call this method
     * @param req the request to send, {compteEtudiant: "string", offre: "string"}
     */
    public static void postOffreRetenue(StageAppActivity activity, CreateOffreRetenueRequest req, ResultatAppel<PostOffreResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<PostOffreResponse> call = apiInterface.doPostOffreRetenue(getBearer(activity), req);
        APIClient.doCall(call, cllbck);
    }

    /**
     * Create an OffreConsultee, using the API
     * @param activity the activity that call this method
     * @param req the request to send, {compteEtudiant: "string", offre: "string"}
     * @param cllbck the callback to call when the result is ready
     */
    public static void postOffreConsultee(StageAppActivity activity, CreateOffreConsulteeRequest req, ResultatAppel<PostOffreResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<PostOffreResponse> call = apiInterface.doPostOffreConsultee(getBearer(activity), req);
        APIClient.doCall(call, cllbck);
    }

    // __________________________________________ CANDIDATURES _______________________________________________ \\

    /**
     * Get the candidatures, using the API
     * @param activity the activity that call this method
     * @param cllbck the callback to call when the result is ready
     */
    public static void getCandidatures(StageAppActivity activity, ResultatAppel<CandidaturesResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<CandidaturesResponse> call = apiInterface.doGetCandidatures(getBearer(activity));
        APIClient.doCall(call, cllbck);
    }

    /**
     * Post a candidature, using the API and the class CandidatureRequest
     */
    public static void postCandidature(StageAppActivity activity, CandidatureRequest req, ResultatAppel<CreateCandidatureResponse> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<CreateCandidatureResponse> call = apiInterface.doPostCandidature(getBearer(activity), req);
        APIClient.doCall(call, cllbck);
    }

    /**
     * Update a candidature, using the API and the class CandidatureRequest
     * @param activity the activity that call this method
     * @param id the id of the candidature to update
     * @param req the request to send, {compteEtudiant: "string", offre: "string", typeAction:"string", dateAction:"string", etatCandidature: "string"}
     * @param cllbck the callback to call when the result is ready
     */
    public static void updateCandidature(StageAppActivity activity, String id, CandidatureRequest req,ResultatAppel<Candidature> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<Candidature> call = apiInterface.doUpdateCandidature(getBearer(activity), id, req);
        APIClient.doCall(call, cllbck);
    }

    /**
     * Delete a Candidature, according to the given id, using the API
     * @param activity the activity that call this method
     * @param id the id of the candidature
     * @param cllbck the callback to call when the result is ready
     */
    public static void deleteCandidature(StageAppActivity activity, String id, ResultatAppel<Candidature> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<Candidature> call = apiInterface.doDeleteCandidature(getBearer(activity), id);
        APIClient.doCall(call, cllbck);
    }

    // __________________________________________ ENTREPRISES _______________________________________________ \\

    /**
     * Get a entreprise accroding to the given id, using the API
     * @param activity the activity that call this method
     * @param id the id of the entreprise
     * @param cllbck the callback to call when the result is ready
     */
    public static void getEntreprise(StageAppActivity activity, String id, ResultatAppel<Entreprise> cllbck) {
        APIService apiInterface = activity.getApiInterface();

        Call<Entreprise> call = apiInterface.doGetEntreprise(getBearer(activity), id);
        APIClient.doCall(call, cllbck);
    }


    // __________________________________________ BEARER _______________________________________________ \\

    /**
     * Get the bearer token, using the token of the activity
     * @param activity the activity that call this method
     * @return the bearer token "Bearer <token>"
     */
    private static String getBearer(StageAppActivity activity) {
        return "Bearer " + activity.getToken();
    }

    /**
     * Do a call to the API, and call the callback when the result is ready
     * @param call the call to do
     * @param callback the callback to call when the result is ready
     * @param <T> the type of the result
     */
    private static <T> void doCall(Call<T> call, ResultatAppel<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if(callback != null) {
                    callback.traiterResultat(response.body());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());
                if(callback != null) {
                    callback.traiterErreur();
                }
            }
        });
    }

}
