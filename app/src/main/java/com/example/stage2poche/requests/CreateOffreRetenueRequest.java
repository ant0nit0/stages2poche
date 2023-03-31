package com.example.stage2poche.requests;

public class CreateOffreRetenueRequest {
    private String compteEtudiant;
    private String offre;

    public CreateOffreRetenueRequest(String compteEtudiant, String offre) {
        this.compteEtudiant = compteEtudiant;
        this.offre = offre;
    }

    public String getCompteEtudiant() {
        return compteEtudiant;
    }

    public String getOffre() {
        return offre;
    }

    public void setCompteEtudiant(String compteEtudiant) {
        this.compteEtudiant = compteEtudiant;
    }

    public void setOffre(String offre) {
        this.offre = offre;
    }

    @Override
    public String toString() {
        return "CreateOffreConsulteeRequest{" +
                "compteEtudiant='" + compteEtudiant + '\'' +
                ", offre='" + offre + '\'' +
                '}';
    }
}
