package fr.gsb.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fr.gsb.rv.entites.Visiteur;
import fr.gsb.rv.technique.Ip;
import fr.gsb.rv.technique.Session;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "GSB MAIN ACTIVITY";
    TextView tvVisiteur;
    TextView tvMatricule;
    TextView tvMdp;
    TextView tvErreur;
    EditText etMatricule;
    EditText etMdp;
    Button bSeConnecter;
    Button bSeDeconnecter;
    Button bAnnuler;
    String matricule;
    String mdp;


    Visiteur visiteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMatricule = findViewById(R.id.tvMatricule);
        etMatricule = findViewById(R.id.etMatricule);
        tvMdp = findViewById(R.id.tvMdp);
        etMdp = findViewById(R.id.etMdp);
        bSeConnecter = findViewById(R.id.bValider);
        bAnnuler = findViewById(R.id.bAnnuler);
        tvErreur = findViewById(R.id.tvErreur);


        bSeConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matricule = etMatricule.getText().toString();
                Log.i("APP-RV", "Matricule : " + matricule);
                mdp = etMdp.getText().toString();
                Log.i("APP-RV", "Mdp : " + mdp);
                try {
                    seConnecter(matricule, mdp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                annuler();
            }
        });
    }

    public void seConnecter(String matricule, String mdp){

        String url = "http://"+ Ip.getIp()+":5000/visiteurs/"+matricule+"/"+mdp; //Connexion au Web Service REST afin d'accéder à la base de données
        Visiteur vis = new Visiteur();

        Response.Listener<JSONObject> ecouteurReponse = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    vis.setNom(response.getString("vis_nom"));
                    vis.setMatricule(response.getString("vis_matricule"));
                    vis.setMdp(response.getString("vis_mdp"));
                    vis.setPrenom(response.getString("vis_prenom"));
                    Session.ouvrir(vis);
                    Log.i("APP-RV", "Info HTTP : " + "Connecté ! " + vis);
                    tvErreur.setVisibility(View.INVISIBLE);

                    Bundle paquet = new Bundle();
                    Gson gson = new Gson();
                    String SessionJson = gson.toJson(Session.getSession());
                    paquet.putString("sessionJSON", SessionJson);

                    Intent intentionMenuRv = new Intent(MainActivity.this, MenuRvActivity.class);
                    intentionMenuRv.putExtras(paquet);
                    startActivity(intentionMenuRv);

                }catch (JSONException e){
                    Log.e("APP-RV", "Erreur JSON : " + e.getMessage());
                    tvErreur.setVisibility(View.VISIBLE);
                    etMdp.getText().clear();
                }
            }
        };

        Response.ErrorListener ecouteurErreur = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APP-RV", "Erreur HTTP : " + error.getMessage());
                tvErreur.setVisibility(View.VISIBLE);
                etMdp.getText().clear();
            }
        };

        JsonObjectRequest requete = new JsonObjectRequest(Request.Method.GET, url, null, ecouteurReponse, ecouteurErreur);
        RequestQueue fileRequetes = Volley.newRequestQueue(this);
        fileRequetes.add(requete);

    }

    public void annuler(){
        etMatricule.getText().clear();
        etMdp.getText().clear();
    }


}