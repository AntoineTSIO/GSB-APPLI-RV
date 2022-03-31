package fr.gsb.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import fr.gsb.rv.entites.Visiteur;

public class MenuRvActivity extends AppCompatActivity {

    //Définition des différents objets Java
    Button bConsulterRv;
    Button bSaisirRv;
    TextView tvVisiteur;
    Visiteur vis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_rv);

        //Affectation des boutons et du textview aux objets Java
        bConsulterRv = findViewById(R.id.bConsulter);
        bSaisirRv = findViewById(R.id.bSaisir);
        tvVisiteur = findViewById(R.id.tvVisiteur);

        Bundle paquet = this.getIntent().getExtras();
        Gson gson = new Gson();
        String sessionJSON = paquet.getString("sessionJSON");
        tvVisiteur.setText(sessionJSON);

        bConsulterRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentionConsulterRv = new Intent(MenuRvActivity.this, RechercheRvActivity.class);
                startActivity(intentionConsulterRv);
            }
        });

        bSaisirRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentionSaisirRv = new Intent(MenuRvActivity.this, SaisieRvActivity.class);
                startActivity(intentionSaisirRv);
            }
        });
    }
}