package fr.gsb.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.gsb.rv.entites.Praticien;
import fr.gsb.rv.technique.Ip;

public class SaisieRvActivity extends AppCompatActivity {

    //Création des variables reliées au layout
    TextView tvDateVisite;
    Spinner spPraticien;
    Spinner spMotif;
    Spinner spCoefConfiance;
    EditText etBilan;
    Button bDateVisite;
    Button bValider;
    Button bAnnuler;

    //Création d'un ecouteur pour le choix de la date de visite avec une portée de package
    DatePickerDialog.OnDateSetListener ecouteurDate;
    String dateSelectionnee;

    //Création des listes utilisées par les Spinner
    String[] listeMotifs = {"Premier rendez-vous","Rendez-vous semestriel","Visite de contrôle","Visite après longue durée","Visite en urgence"};
    ArrayList<Integer> listeCoefConfiance = new ArrayList<>();
    ArrayList<Praticien> listePraticiens = new ArrayList<>();
    int praticienSelectionne;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie_rv);

        tvDateVisite = findViewById(R.id.tvDateVisite);
        spPraticien = findViewById(R.id.spPraticien);
        spMotif = findViewById(R.id.spMotif);
        spCoefConfiance = findViewById(R.id.spCoefConfiance);
        etBilan = findViewById(R.id.etBilan);
        bDateVisite = findViewById(R.id.bDateVisite);
        bAnnuler = findViewById(R.id.bAnnuler);
        bDateVisite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("APP-RV Saisie","Modification Date Saisie");
                selectDateVisite(view);
            }
        });
        bAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Renvoi vers le Menu en cas d'annulation
                Log.i("APP-RV Saisie", "Annulation, retour au menu");
                Intent intentAnnuler = new Intent(SaisieRvActivity.this, MenuRvActivity.class);
                startActivity(intentAnnuler);
            }
        });

        ecouteurDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateSelectionnee = String.format(
                        "%02d/%02d/%04d",
                        day,
                        month +1,
                        year
                );
                tvDateVisite.setText( dateSelectionnee );
            }
        };

        //Implémentation de la liste déroulante des motifs
        ArrayAdapter<String> listeMotifsArrayAdapter = new ArrayAdapter<>(SaisieRvActivity.this, android.R.layout.simple_spinner_item, listeMotifs);
        listeMotifsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMotif.setAdapter(listeMotifsArrayAdapter);
        spMotif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("APP-RV Saisie","Motif sélectionné");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("APP-RV Saisie","Aucun Motif sélectionné");
            }
        });

        //Implémentation de la liste déroulante des coefficients de confiance
        for(int i=1; i<=5; i++){
            listeCoefConfiance.add(i);
        }
        ArrayAdapter<Integer> listeCoefConfianceArrayAdapter = new ArrayAdapter<>(SaisieRvActivity.this, android.R.layout.simple_spinner_item, listeCoefConfiance);
        listeCoefConfianceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCoefConfiance.setAdapter(listeCoefConfianceArrayAdapter);
        spCoefConfiance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("APP-RV Saisie","Coef Confiance sélectionné");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("APP-RV Saisie","Aucun Coef Confiance sélectionné");
            }
        });
        getListePraticiens();
    }
    //Modification de la date de visite
    public void selectDateVisite(View view){
        LocalDate dateCourante = LocalDate.now();
        int jour = dateCourante.getDayOfMonth();
        int mois = dateCourante.getMonthValue();
        int annee = dateCourante.getYear();
        new DatePickerDialog(this, ecouteurDate, jour, mois, annee).show();
    }
    public ArrayList<Praticien>getListePraticiens(){
        String url = "http://"+ Ip.getIp()+":5000/praticiens"; //Connexion au Web Service REST afin d'accéder à la base de données;
        Response.Listener<JSONArray> ecouteurReponsePraticiens = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject element = response.getJSONObject(i);
                        Praticien unPraticien = new Praticien();
                        unPraticien.setNumero(element.getInt("pra_num"));
                        unPraticien.setNom(element.getString("pra_nom"));
                        unPraticien.setPrenom(element.getString("pra_prenom"));
                        unPraticien.setVille(element.getString("pra_ville"));
                        listePraticiens.add(unPraticien);
                        Log.i("APP-RV Saisie","Praticien "+i+": "+unPraticien);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter<Praticien> listePraticiensArrayAdapter = new ArrayAdapter<Praticien>(SaisieRvActivity.this, android.R.layout.simple_spinner_item, listePraticiens);
                listePraticiensArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spPraticien.setAdapter(listePraticiensArrayAdapter);
                spPraticien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i("APP-RV Saisie","Praticien n°"+listePraticiens.get(i).getNumero()+" dans la liste choisi");
                        praticienSelectionne = listePraticiens.get(i).getNumero();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.e("APP-RV Saisie","Aucun Praticien sélectionné");
                    }
                });
            }
        };
        Response.ErrorListener ecouteurErreurPraticiens = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SaisieRvActivity.this, "ERREUR, veuillez réessayer", Toast.LENGTH_SHORT).show();
                Log.e("APP-RV Saisie",error.getMessage());
            }
        };
        Request<JSONArray> requetePraticiens = new JsonArrayRequest(Request.Method.GET, url, null, ecouteurReponsePraticiens, ecouteurErreurPraticiens);
        RequestQueue fileRequetePraticiens = Volley.newRequestQueue(SaisieRvActivity.this);
        fileRequetePraticiens.add(requetePraticiens);
        return listePraticiens;
    }

    public void envoyerSaisie(View view) throws JSONException {

    }
}