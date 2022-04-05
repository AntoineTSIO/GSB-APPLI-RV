package fr.gsb.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    TextView tvErreur;

    //Création d'une nouvelle classe permattant la gestion d'un item dans la liste et de faire ressortir uniquement la date et le nom du praticien
    class RapportVisiteAdapter extends ArrayAdapter<RapportVisite>{
        RapportVisiteAdapter() {
            super(
                    ListeRvActivity.this,
                    R.layout.item_liste_rv,
                    R.id.tvItemDateVisite,
                    listeRapportsVisite
            );
        }
        public View getView(int position, View convertView, ViewGroup parent){
            //Récupération de la vue définissant les items du nom et de la date de visite
            View vItem = super.getView(position, convertView, parent);
            //Récupération des TextView correspondant au nom et à la date de visite de chaque praticien
            TextView tvItemNomPraticien = vItem.findViewById(R.id.tvItemNomPraticien);
            TextView tvItemDateVisite = vItem.findViewById(R.id.tvItemDateVisite);
            try{
                //Affectation des valeurs de chaque nom et date de visite aux TextView
                tvItemNomPraticien.setText(listeRapportsVisite.get(position).getPra_nom());
                tvItemDateVisite.setText(listeRapportsVisite.get(position).getRap_date_visite());
            } catch(Exception e){
                Log.e("APP-RV", "Erreur vItem : "+e.getMessage());
            }
            return vItem;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_rv);

        Bundle paquet = this.getIntent().getExtras();
        mois = paquet.getString("numMois");
        annee = paquet.getString("annee");
        matricule = Session.getSession().getLeVisiteur().getMatricule();
        lvRapportsVisite = (ListView) findViewById(R.id.lvRapportsVisite);
        tvErreur = findViewById(R.id.tvErreur);

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
                        rapVisite.setRap_motif(element.getString("rap_motif"));
                        rapVisite.setRap_coef_confiance(Integer.parseInt(element.getString("rap_coef_confiance")));
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
                    //Création de l'adaptateur en appelant la classe créée à la ligne 44
                    RapportVisiteAdapter adapter = new RapportVisiteAdapter();
                    //Changement de l'ordre des rapports de visite
                    Collections.reverse(listeRapportsVisite);
                    synchronized (listeRapportsVisite) {
                        listeRapportsVisite.notify();
                    }
                    lvRapportsVisite.setAdapter(adapter);
                    lvRapportsVisite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        //Envoi du paquet contenant les données du rapport de visite à l'activité "VisuRvActivity"
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intentRv = new Intent(ListeRvActivity.this, VisuRvActivity.class);
                            Bundle paquetRv = new Bundle();
                            RapportVisite rapportCourant = listeRapportsVisite.get(position);
                            Log.i("APP-RV", "Rapport sélectionné : "+rapportCourant);
                            //Insertion des valeurs de l'objet rapportCourant dans un Bundle pour les "envoyer" vers l'activité suivante
                            paquetRv.putInt("rap_num", rapportCourant.getRap_numero());
                            paquetRv.putString("rap_bilan", rapportCourant.getRap_bilan());
                            paquetRv.putString("rap_motif", rapportCourant.getRap_motif());
                            paquetRv.putInt("rap_coef_confiance", rapportCourant.getRap_coef_confiance());
                            paquetRv.putString("pra_cp", rapportCourant.getPra_cp());
                            paquetRv.putString("pra_nom", rapportCourant.getPra_nom());
                            paquetRv.putString("rap_date_visite", rapportCourant.getRap_date_visite());
                            paquetRv.putString("pra_prenom", rapportCourant.getPra_prenom());
                            paquetRv.putString("pra_ville", rapportCourant.getPra_ville());

                            Log.i("APP-RV-DEBUG","paquet :"+paquetRv.getInt("rap_numero")+" Rapport Courant :"+rapportCourant.getRap_numero());
                            intentRv.putExtras(paquetRv);
                            startActivity(intentRv);
                        }
                    });
                }else{
                    tvErreur.setVisibility(View.VISIBLE);
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