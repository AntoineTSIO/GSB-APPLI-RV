package fr.gsb.rv.entites;

import java.time.LocalDate;

public class Praticien {

    private int numero ;
    private String nom ;
    private String prenom ;
    private String ville ;

    public Praticien() {
    }

    public Praticien(int pra_num, String pra_nom, String pra_prenom, String pra_ville) {
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public String toString() {
        return nom + ' ' + prenom;
    }
}
