package fr.gsb.rv.entites;

import java.time.LocalDate;

public class RapportVisite {

    private String vis_matricule;
    private int rap_numero;
    private String rap_date_visite;
    private String rap_bilan;
    private int rap_coef_confiance;
    private String rap_motif;
    private String pra_nom;
    private String pra_prenom;
    private String pra_cp;
    private String pra_ville;

    public RapportVisite() {
    }

    public RapportVisite(String vis_matricule, int rap_numero, String rap_date_visite, String rap_bilan, int rap_coef_confiance, String rap_motif, String pra_nom, String pra_prenom, String pra_cp, String pra_ville) {
        this.vis_matricule = vis_matricule;
        this.rap_numero = rap_numero;
        this.rap_date_visite = rap_date_visite;
        this.rap_bilan = rap_bilan;
        this.rap_coef_confiance = rap_coef_confiance;
        this.rap_motif = rap_motif;
        this.pra_nom = pra_nom;
        this.pra_prenom = pra_prenom;
        this.pra_cp = pra_cp;
        this.pra_ville = pra_ville;
    }

    public String getVis_matricule() {
        return vis_matricule;
    }

    public void setVis_matricule(String vis_matricule) {
        this.vis_matricule = vis_matricule;
    }

    public int getRap_numero() {
        return rap_numero;
    }

    public void setRap_numero(int rap_numero) {
        this.rap_numero = rap_numero;
    }

    public String getRap_date_visite() {
        return rap_date_visite;
    }

    public void setRap_date_visite(String rap_date_visite) {
        this.rap_date_visite = rap_date_visite;
    }

    public String getRap_bilan() {
        return rap_bilan;
    }

    public void setRap_bilan(String rap_bilan) {
        this.rap_bilan = rap_bilan;
    }

    public int getRap_coef_confiance() {
        return rap_coef_confiance;
    }

    public void setRap_coef_confiance(int rap_coef_confiance) {
        this.rap_coef_confiance = rap_coef_confiance;
    }

    public String getRap_motif() {
        return rap_motif;
    }

    public void setRap_motif(String rap_motif) {
        this.rap_motif = rap_motif;
    }

    public String getPra_nom() {
        return pra_nom;
    }

    public void setPra_nom(String pra_nom) {
        this.pra_nom = pra_nom;
    }

    public String getPra_prenom() {
        return pra_prenom;
    }

    public void setPra_prenom(String pra_prenom) {
        this.pra_prenom = pra_prenom;
    }

    public String getPra_cp() {
        return pra_cp;
    }

    public void setPra_cp(String pra_cp) {
        this.pra_cp = pra_cp;
    }

    public String getPra_ville() {
        return pra_ville;
    }

    public void setPra_ville(String pra_ville) {
        this.pra_ville = pra_ville;
    }

    @Override
    public String toString() {
        return "RapportVisite{" +
                "vis_matricule='" + vis_matricule + '\'' +
                ", rap_numero=" + rap_numero +
                ", rap_date_visite='" + rap_date_visite + '\'' +
                ", rap_bilan='" + rap_bilan + '\'' +
                ", rap_coef_confiance=" + rap_coef_confiance +
                ", rap_motif='" + rap_motif + '\'' +
                ", pra_nom='" + pra_nom + '\'' +
                ", pra_prenom='" + pra_prenom + '\'' +
                ", pra_cp='" + pra_cp + '\'' +
                ", pra_ville='" + pra_ville + '\'' +
                '}';
    }
}