package fr.gsb.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import fr.gsb.rv.technique.Session;

public class VisuRvActivity extends AppCompatActivity {

    int rap_num;
    String rap_bilan;
    String pra_cp;
    String pra_nom;
    String rap_date_visite;
    String pra_prenom;
    String pra_ville;
    int rap_coef_confiance;
    String rap_motif;

    TextView tvRapNum;
    TextView tvRapBilan;
    TextView tvPraCp;
    TextView tvPraNom;
    TextView tvRapDateVisite;
    TextView tvPraPrenom;
    TextView tvPraVille;
    TextView tvCoefConfiance;
    TextView tvMatriculeVisiteur;
    TextView tvMotif;
    Button bEchantillons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visu_rv);

        Bundle paquetRv = this.getIntent().getExtras();
        rap_num = paquetRv.getInt("rap_numero");
        rap_bilan = paquetRv.getString("rap_bilan");
        pra_cp = paquetRv.getString("pra_cp");
        pra_nom = paquetRv.getString("pra_nom");
        rap_date_visite = paquetRv.getString("rap_date_visite");
        pra_prenom = paquetRv.getString("pra_prenom");
        pra_ville = paquetRv.getString("pra_ville");
        rap_coef_confiance = paquetRv.getInt("rap_coef_confiance");
        rap_motif = paquetRv.getString("rap_motif");

        tvRapNum = findViewById(R.id.tvNumRapportVisite);
        tvRapBilan = findViewById(R.id.tvBilan);
        tvPraCp = findViewById(R.id.tvCodePostal);
        tvPraNom = findViewById(R.id.tvNomPraticien);
        tvRapDateVisite = findViewById(R.id.tvDateVisite);
        tvPraPrenom = findViewById(R.id.tvPrenomPraticien);
        tvPraVille = findViewById(R.id.tvVille);
        tvCoefConfiance = findViewById(R.id.tvCoefConfiance);
        tvMotif = findViewById(R.id.tvMotif);
        tvMatriculeVisiteur = findViewById(R.id.tvMatriculeVisiteur);

        tvMatriculeVisiteur.setText(tvMatriculeVisiteur.getText() + Session.getSession().getLeVisiteur().getMatricule());
        tvRapNum.setText(tvRapNum.getText() + String.valueOf(rap_num));
        tvRapBilan.setText(tvRapBilan.getText() + rap_bilan);
        tvPraCp.setText(tvPraCp.getText() + pra_cp);
        tvPraNom.setText(tvPraNom.getText() + pra_nom);
        tvRapDateVisite.setText(tvRapDateVisite.getText() + rap_date_visite);
        tvPraPrenom.setText(tvPraPrenom.getText() + pra_prenom);
        tvPraVille.setText(tvPraVille.getText() + pra_ville);
        tvCoefConfiance.setText(tvCoefConfiance.getText() + String.valueOf(rap_coef_confiance));
        tvMotif.setText(tvMotif.getText() + rap_motif);

    }
}