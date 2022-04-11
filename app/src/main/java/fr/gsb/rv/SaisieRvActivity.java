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
import java.util.Date;
import java.util.GregorianCalendar;

import fr.gsb.rv.entites.Praticien;
import fr.gsb.rv.technique.Ip;
import fr.gsb.rv.technique.Session;

@SuppressLint("DefaultLocale")
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

//Création des listes utilisées par les Spinner
    String[] listeMotifs = {"Premier rendez-vous","Rendez-vous semestriel","Visite de contrôle","Visite après longue durée","Visite en urgence"};
    ArrayList<Integer> listeCoefConfiance = new ArrayList<>();
    ArrayList<Praticien> listePraticiens = new ArrayList<>();
    int praticienSelectionne;

//Création de la date du jour
    LocalDate dateActuelle = LocalDate.now();
    int jourActuel = dateActuelle.getDayOfMonth();
    int moisActuel = dateActuelle.getMonthValue();
    int anneeActuelle = dateActuelle.getYear();

    String dateSaisie = String.format("%04d-%02d-%02d",anneeActuelle, moisActuel, jourActuel);

//Création d'un ecouteur pour le choix de la date de visite avec une portée de package
    DatePickerDialog.OnDateSetListener ecouteurDate;
    String dateSelectionnee = String.format("%04d-%02d-%02d",anneeActuelle, moisActuel, jourActuel);

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
        bValider = findViewById(R.id.bValider);

        tvDateVisite.setText(String.format("%04d-%02d-%02d",anneeActuelle, moisActuel, jourActuel));

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
        bValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    envoyerSaisie(view);
                    Log.i("APP-RV Saisie","Envoi du formulaire");
                } catch (JSONException e) {
                    Log.e("APP-RV Saisie","Erreur lors de l'envoi de la saisie : "+e.getMessage());
                }
            }
        });

//Implémentaiton de l'écouteur de date permettant de changer la date sélectionnée par le Visiteur
        ecouteurDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateSelectionnee = String.format(
                        "%04d-%02d-%02d",
                        year,
                        month +1,
                        day
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

//Modification de la date de visite via un calendrier affiché sur l'appareil Android du visiteur
    public void selectDateVisite(View view){
        GregorianCalendar dateDuJour = new GregorianCalendar();
        int jour = dateDuJour.get(Calendar.DAY_OF_MONTH);
        int mois = dateDuJour.get(Calendar.MONTH);
        int annee = dateDuJour.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, ecouteurDate, annee, mois, jour);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime()); //Empêche la sélection d'une date ultérieure à la date du jour
        datePickerDialog.show();
    }

//Implémentation de la liste déroulante des praticiens
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

//Envoi des données remplies dans le formulaire au service REST pour un ajout dans la base de données
    public void envoyerSaisie(View view) throws JSONException {
        String matricule = Session.getSession().getLeVisiteur().getMatricule();
        Log.i("APP-RV Envoi saisie","Date : "+dateSelectionnee);
        Log.i("APP-RV Envoi saisie","Praticien : "+spPraticien.getSelectedItem());
        Log.i("APP-RV Envoi saisie","Motif : "+spMotif.getSelectedItem());
        Log.i("APP-RV Envoi saisie","Coef : "+spCoefConfiance.getSelectedItem());
        Log.i("APP-RV Envoi saisie","Bilan : "+etBilan.getText());
        Log.i("APP-RV Envoi saisie","Date Saisie : "+dateSaisie);

        String url = "http://"+Ip.getIp()+":5000/rapports/" + matricule + "/" + dateSelectionnee + "/" + etBilan.getText().toString() + "/" + spCoefConfiance.getSelectedItem().toString() + "/" + dateSaisie + "/" + spMotif.getSelectedItem().toString() + "/" + praticienSelectionne; //Connexion au Web Service REST afin d'envoyer le rapport de visite dans la base de données

        Response.Listener<JSONArray> ecouteurReponse = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("APP-RV Envoi saisie","Reponse POST : " + response);
            }
        };
        Response.ErrorListener ecouteurErreur = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APP-RV Envoi saisie","Erreur HTTP : " + error.getMessage());
            }
        };
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, null, ecouteurReponse, ecouteurErreur);
        RequestQueue fileRequetes = Volley.newRequestQueue(SaisieRvActivity.this);
        fileRequetes.add(request);

        Intent menuRv = new Intent(SaisieRvActivity.this, MenuRvActivity.class);
        startActivity(menuRv);
    }
}