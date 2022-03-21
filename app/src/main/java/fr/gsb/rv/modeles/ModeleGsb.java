package fr.gsb.rv.modeles;

import fr.gsb.rv.entites.Visiteur;

public class ModeleGsb {

    private ModeleGsb modele = null;

    private void ModeleGsb(){
        modele = new ModeleGsb();
    }

    public ModeleGsb getInstance(){
        return modele;
    }

    public Visiteur seConnecter(String matricule, String mdp){
        String okMdp = "azerty";
        String okMatricule = "a131";

        if(matricule.equals(okMatricule) && mdp.equals(okMdp)) {
            Visiteur vis = new Visiteur("a131", "azerty", "Edouard", "Jean");
            return vis;
        }
        return null;
    }

    public void peupler(){
        return;
    }

}
