package fr.gsb.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.gsb.rv.entites.RapportVisite;
import fr.gsb.rv.technique.Session;

public class ListeRvActivity extends AppCompatActivity{

    ListView lvRapportsVisite;
    String mois;
    String annee;
    String matricule;
    ArrayList<RapportVisite> listeRapportsVisite = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_rv);

        Bundle paquet = this.getIntent().getExtras();
        mois = paquet.getString("numMois");
        annee = paquet.getString("annee");
        matricule = Session.getSession().getLeVisiteur().getMatricule();
        lvRapportsVisite = (ListView) findViewById(R.id.lvRapportsVisite);

        getRapports(matricule, mois, annee);
    }

    public void getRapports (String matricule, String mois, String annee){

        String url = "http://10.0.2.2:5000/rapports/" + matricule + "/" + mois + "/" + annee; //Connexion au Web Service REST afin d'accéder à la base de données

        Response.Listener<JSONArray> ecouteurReponse = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Création de la liste de rapports de visite en fonction du matricule et pour un mois donné d'une année donnée
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject element = response.getJSONObject(i);
                        RapportVisite rapVisite = new RapportVisite();

                        rapVisite.setVis_matricule(matricule);
                        rapVisite.setRap_numero(Integer.parseInt(element.getString("rap_num")));
                        rapVisite.setRap_date_visite(element.getString("rap_date_visite"));
                        rapVisite.setRap_bilan(element.getString("rap_bilan"));
                        rapVisite.setPra_nom(element.getString("pra_nom"));
                        rapVisite.setPra_prenom(element.getString("pra_prenom"));
                        rapVisite.setPra_cp(element.getString("pra_cp"));
                        rapVisite.setPra_ville(element.getString("pra_ville"));

                        listeRapportsVisite.add(rapVisite);

                    } catch (JSONException e) {
                        Log.e("APP-RV", "Erreur Liste RV : " + e.getMessage());
                    }
                }
                Log.i("APP-RV", "Liste : " + listeRapportsVisite);

                if (listeRapportsVisite.size() != 0) {
                    //Changement de l'ordre des rapports de visite
                    Collections.reverse(listeRapportsVisite);
                    synchronized (listeRapportsVisite) {
                        listeRapportsVisite.notify();
                    }
                    ArrayAdapter<RapportVisite> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listeRapportsVisite);
                    lvRapportsVisite.setAdapter(adapter);
                    lvRapportsVisite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        //Envoi du paquet contenant les données du rapport de visite à l'activité "VisuRvActivity"
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.i("APP-RV", "Rapport sélectionné");
                            Intent intent = new Intent(ListeRvActivity.this, VisuRvActivity.class);
                            Bundle paquet = new Bundle();
                            RapportVisite rapportCourant = listeRapportsVisite.get(position);
                            paquet.putInt("rap_num", rapportCourant.getRap_numero());
                            paquet.putString("rap_bilan", rapportCourant.getRap_bilan());
                            paquet.putString("pra_cp", rapportCourant.getPra_cp());
                            paquet.putString("pra_nom", rapportCourant.getPra_nom());
                            paquet.putString("rap_date_visite", rapportCourant.getRap_date_visite());
                            paquet.putString("pra_prenom", rapportCourant.getPra_prenom());
                            paquet.putString("pra_ville", rapportCourant.getPra_ville());
                            intent.putExtras(paquet);
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        //Ecouteur d'erreur permettant de renvoyer le message d'erreur en cas de non fonctionnement de l'activité
        Response.ErrorListener ecouteurErreur = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APP-RV", "Erreur Liste RV : " + error.getMessage());
            }
        };
        JsonArrayRequest requete = new JsonArrayRequest(Request.Method.GET, url, null, ecouteurReponse, ecouteurErreur);
        RequestQueue fileRequete = Volley.newRequestQueue(this);
        fileRequete.add(requete);
    }
}