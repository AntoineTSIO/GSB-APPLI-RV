package fr.gsb.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.gsb.rv.entites.Echantillons;
import fr.gsb.rv.technique.Ip;
import fr.gsb.rv.technique.Session;

public class VisuEchantActivity extends AppCompatActivity {

    ListView lvEchantillons;
    String matricule;
    Integer rap_num;
    TextView tvErreur;
    ArrayList<Echantillons> listeEchantillons = new ArrayList<>();

    class ItemEchantillonsAdapter extends ArrayAdapter<Echantillons>{
        ItemEchantillonsAdapter(){
            super(
                    VisuEchantActivity.this,
                    R.layout.item_liste_echantillons,
                    R.id.tvItemEchantillon,
                    listeEchantillons
            );
        }
        public View getView(int position, View convertView, ViewGroup parent){
            View vItem = super.getView(position, convertView, parent);
            TextView tvItemEchantillon = vItem.findViewById(R.id.tvItemEchantillon);
            TextView tvItemQuantite = vItem.findViewById(R.id.tvItemQuantite);
            try{
                tvItemEchantillon.setText(listeEchantillons.get(position).getMed_nomcommercial());
                tvItemQuantite.setText(String.valueOf(listeEchantillons.get(position).getOff_quantite()));
            }catch(Exception e){
                Log.e("APP-RV", "Erreur vItem : "+e.getMessage());
            }
            return vItem;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visu_echant);

        Bundle paquetEchantillons = this.getIntent().getExtras();
        rap_num = paquetEchantillons.getInt("rap_num");
        matricule = Session.getSession().getLeVisiteur().getMatricule();
        lvEchantillons = (ListView) findViewById(R.id.lvEchantillonsOfferts);
        tvErreur = findViewById(R.id.tvErreur);

        getEchantillonsOfferts(matricule, rap_num);
    }

    public void getEchantillonsOfferts(String matricule, Integer numRapportVisite){
        String url = "http://"+ Ip.getIp()+":5000/rapports/echantillons/" + matricule + "/" + numRapportVisite; //Connexion au Web Service REST afin d'accéder à la base de données

        Response.Listener<JSONArray> ecouteurReponse = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i<response.length(); i++){
                    try{
                        JSONObject element = response.getJSONObject(i);
                        Echantillons echantillons = new Echantillons();

                        echantillons.setVis_matricule(matricule);
                        echantillons.setRap_num(rap_num);
                        echantillons.setMed_nomcommercial(element.getString("med_nomcommercial"));
                        echantillons.setOff_quantite(element.getInt("off_quantite"));
                        listeEchantillons.add(echantillons);
                    } catch (Exception e) {
                        Log.e("APP-RV", "Erreur Liste Echantillons : " + e.getMessage());
                    }
                }
                Log.i("APP-RV", "Liste : " + listeEchantillons);

                if(listeEchantillons.size()!=0){
                    ItemEchantillonsAdapter adapter = new ItemEchantillonsAdapter();
                    synchronized (listeEchantillons){
                        listeEchantillons.notify();
                    }
                    lvEchantillons.setAdapter(adapter);
                }else{
                    tvErreur.setVisibility(View.VISIBLE);
                }
            }
        };
        //Ecouteur d'erreur permettant de renvoyer le message d'erreur en cas de non fonctionnement de l'activité
        Response.ErrorListener ecouteurErreur = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APP-RV", "Erreur Liste Echantillons Offerts : " + error.getMessage());
            }
        };
        JsonArrayRequest requete = new JsonArrayRequest(Request.Method.GET, url, null, ecouteurReponse, ecouteurErreur);
        RequestQueue fileRequete = Volley.newRequestQueue(this);
        fileRequete.add(requete);
    }
}