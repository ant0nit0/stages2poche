package com.example.stage2poche.api;

import com.example.stage2poche.entities.Auth;
import com.example.stage2poche.entities.Candidature;
import com.example.stage2poche.entities.CompteEtudiant;
import com.example.stage2poche.entities.Entreprise;
import com.example.stage2poche.entities.offres.Offre;
import com.example.stage2poche.requests.CandidatureRequest;
import com.example.stage2poche.requests.CreateOffreConsulteeRequest;
import com.example.stage2poche.requests.CreateOffreRetenueRequest;
import com.example.stage2poche.responses.CandidaturesResponse;
import com.example.stage2poche.responses.ComptesEtudiantsResponse;
import com.example.stage2poche.responses.CreateCandidatureResponse;
import com.example.stage2poche.responses.LoginResponse;
import com.example.stage2poche.responses.OffresConsulteesResponse;
import com.example.stage2poche.responses.OffresResponse;
import com.example.stage2poche.responses.OffresRetenuesResponse;
import com.example.stage2poche.responses.PostOffreResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    @POST("api/auth")
    Call<LoginResponse> login(@Body Auth auth);

    // Comptes étudiant
    @GET("/api/compte_etudiants")
    Call<ComptesEtudiantsResponse> doGetComptesEtudiants(@Header("Authorization") String token);

    @GET("/api/compte_etudiants/{id}")
    Call<CompteEtudiant> doGetCompteEtudiant(@Header("Authorization") String token, @Path("id") String id);

    @PUT("/api/compte_etudiants/{id}")
    Call<CompteEtudiant> doUpdateCompteEtudiant(@Header("Authorization") String token, @Path("id") String id, @Body CompteEtudiant newCompteEtudiant);

    // Offres
    @GET("/api/offres")
    Call<OffresResponse> doGetOffres(@Header("Authorization") String token);

    @GET("/api/offres/{id}")
    Call<Offre> doGetOffre(@Header("Authorization") String token, @Path("id") int i);

    // Update an offer, with the id of the offer and the parameters of body of OffreRequest
    @PUT("/api/offres/{id}")
    Call<Offre> doUpdateOffre(@Header("Authorization") String token, @Path("id") String i, @Body Offre newOffre);

    // Offres consultees
    @GET("/api/offre_consultees")
    Call<OffresConsulteesResponse> doGetOffresConsultees(@Header("Authorization") String token);

    // Add a new OffreConsultee, with the compteEtudiant and the offre as parameters of body
    @POST("/api/offre_consultees")
    Call<PostOffreResponse> doPostOffreConsultee(@Header("Authorization") String token, @Body CreateOffreConsulteeRequest offreConsultee);

    // Offres retenues
    @GET("/api/offre_retenues")
    Call<OffresRetenuesResponse> doGetOffresRetenues(@Header("Authorization") String token);

    // Add a new OffreRetenue, with the compteEtudiant and the offre as parameters of body
    @POST("/api/offre_retenues")
    Call<PostOffreResponse> doPostOffreRetenue(@Header("Authorization") String token, @Body CreateOffreRetenueRequest offreRetenue);

    // Candidatures
    @GET("/api/candidatures")
    Call<CandidaturesResponse> doGetCandidatures(@Header("Authorization") String token);

    // Add a new Candidature, with the parameters of body of CandidatureRequest
    @POST("/api/candidatures")
    Call<CreateCandidatureResponse> doPostCandidature(@Header("Authorization") String token, @Body CandidatureRequest newCandidature);

    // Update a Candidature, with the id of the Candidature and the parameters of body of CandidatureRequest
    @PUT("/api/candidatures/{id}")
    Call<Candidature> doUpdateCandidature(@Header("Authorization") String token, @Path("id") String i, @Body CandidatureRequest newCandidature);

    // Delete a Candidature, with the id of the Candidature
    @DELETE("/api/candidatures/{id}")
    Call<Candidature> doDeleteCandidature(@Header("Authorization") String token, @Path("id") String i);

    // Entreprises
    @GET("/api/entreprises/{id}")
    Call<Entreprise> doGetEntreprise(@Header("Authorization") String token, @Path("id") String i);

}
