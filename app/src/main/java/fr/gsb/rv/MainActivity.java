package fr.gsb.rv;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fr.gsb.rv.entites.Visiteur;
import fr.gsb.rv.technique.Session;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "GSB MAIN ACTIVITY";
    TextView tvVisiteur;
    TextView tvMatricule;
    TextView tvMdp;
    EditText etMatricule;
    EditText etMdp;
    Button bSeConnecter;
    Button bSeDeconnecter;
    Button bAnnuler;

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

        bSeConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seConnecter(etMatricule.getText().toString() , etMdp.getText().toString());
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
        String url = "http://172.20.38.1:5000/visiteurs/"+matricule+"/"+mdp;
        Visiteur visiteur = new Visiteur();

        Response.Listener<JSONObject> ecouteurResponse = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    visiteur.setMatricule(response.getString("vis_matricule"));
                    visiteur.setNom(response.getString("vis_nom"));
                    visiteur.setPrenom(response.getString("vis_prenom"));
                    visiteur.setMdp(response.getString("vis_mdp"));
                }
            }
        }
    }

    public void annuler(){
        etMatricule.getText().clear();
        etMdp.getText().clear();
    }


}