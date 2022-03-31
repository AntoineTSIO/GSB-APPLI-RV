package fr.gsb.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RechercheRvActivity extends AppCompatActivity {

    private static final String [] lesMois = {"Janvier","Février","Mars","Avril","Mail","Juin","Juillet","Août","Septembre","Octobre","Novembre","Décembre"};

    Button bAfficher;
    Spinner spMois;
    Spinner spAnnee;
    String moisCourant;
    String numMoisCourant;
    String anneeCourante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_rv);

        spMois = findViewById(R.id.spMois);
        spAnnee = findViewById(R.id.spAnnee);

        ArrayAdapter<String> aaMois = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lesMois); //
        aaMois.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMois.setAdapter(aaMois);
        spMois.setSelection(LocalDate.now().getMonthValue() -1);
        spMois.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                moisCourant = lesMois[i];
                String before = "";
                if(i<10){
                    before = "0";
                }
                numMoisCourant = String.valueOf(before + (i+1));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        Integer anneeActuelle = LocalDate.now().getYear();
        List<String> annees = new ArrayList<String>();
        for(int i=0; i<5; i++){
            annees.add( String.valueOf( anneeActuelle - i ) );
        }

        ArrayAdapter<String> anneeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, annees);
        anneeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAnnee.setAdapter(anneeArrayAdapter);

        spAnnee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                anneeCourante = annees.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bAfficher = findViewById(R.id.bListeRapports);
        bAfficher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validerMoisEtAnnee(view);
            }
        });
    }

    public void validerMoisEtAnnee(View view){
        Intent intent = new Intent(this, ListeRvActivity.class);
        Bundle paquet = new Bundle();
        paquet.putString("mois", this.spMois.getSelectedItem().toString());
        paquet.putString("annee", this.spAnnee.getSelectedItem().toString());
        paquet.putString("numMois", this.numMoisCourant);
        intent.putExtras(paquet);
        startActivity(intent);
    }
}