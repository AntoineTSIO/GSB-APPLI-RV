package fr.gsb.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import fr.gsb.rv.technique.Session;
import fr.gsb.rv.entites.Visiteur;

public class MenuRvActivity extends AppCompatActivity {

    //Définition des différents objets Java
    Button bConsulterRv;
    Button bSaisirRv;
    Button bDeconnexion;
    TextView tvVisiteur;
    Visiteur vis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_rv);

        //Affectation des boutons et du textview aux objets Java
        bConsulterRv = findViewById(R.id.bConsulterRv);
        bSaisirRv = findViewById(R.id.bSaisirRv);
        bDeconnexion = findViewById(R.id.bDeconnexion);
        tvVisiteur = findViewById(R.id.tvVisiteur);

        //Affichage du nom et du prenom du visiteur sur le menu
        try {
            tvVisiteur.setText(tvVisiteur.getText() + " " + Session.getSession().getLeVisiteur().getPrenom() + " " + Session.getSession().getLeVisiteur().getNom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        bConsulterRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("APP-RV Menu","Click vers consulter");
                Intent intentionConsulterRv = new Intent(MenuRvActivity.this, RechercheRvActivity.class);
                startActivity(intentionConsulterRv);
            }
        });

        bSaisirRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("APP-RV Menu","Click vers saisir");
                Intent intentionSaisirRv = new Intent(MenuRvActivity.this, SaisieRvActivity.class);
                startActivity(intentionSaisirRv);
            }
        });

        bDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("APP-RV Menu","Click vers connexion");
                Intent intentionDeconnexion = new Intent(MenuRvActivity.this, MainActivity.class);
                Session.fermer();
                startActivity(intentionDeconnexion);
            }
        });
    }
}