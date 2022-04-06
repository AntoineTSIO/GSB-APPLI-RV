package fr.gsb.rv.entites;

public class Echantillons {

    private String vis_matricule;
    private int rap_num;
    private String med_nomcommercial;
    private int off_quantite;

    public Echantillons() {
    }

    public Echantillons(String vis_matricule, int rap_num, String med_nomcommercial, int off_quantite) {
        this.vis_matricule = vis_matricule;
        this.rap_num = rap_num;
        this.med_nomcommercial = med_nomcommercial;
        this.off_quantite = off_quantite;
    }

    public String getVis_matricule() {
        return vis_matricule;
    }

    public void setVis_matricule(String vis_matricule) {
        this.vis_matricule = vis_matricule;
    }

    public int getRap_num() {
        return rap_num;
    }

    public void setRap_num(int rap_num) {
        this.rap_num = rap_num;
    }

    public String getMed_nomcommercial() {
        return med_nomcommercial;
    }

    public void setMed_nomcommercial(String med_nomcommercial) {
        this.med_nomcommercial = med_nomcommercial;
    }

    public int getOff_quantite() {
        return off_quantite;
    }

    public void setOff_quantite(int off_quantite) {
        this.off_quantite = off_quantite;
    }

    @Override
    public String toString() {
        return "Echantillons{" +
                "vis_matricule='" + vis_matricule + '\'' +
                ", rap_num=" + rap_num +
                ", med_nomcommercial='" + med_nomcommercial + '\'' +
                ", off_quantite=" + off_quantite +
                '}';
    }
}
