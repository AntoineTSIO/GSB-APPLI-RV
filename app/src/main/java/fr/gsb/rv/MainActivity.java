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


        bSeConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    seConnecter(view);
                } catch (UnsupportedEncodingException e) {
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

    public void seConnecter(View view) throws UnsupportedEncodingException {
        String id = this.etMatricule.getText().toString();
        String mdp = this.etMdp.getText().toString();

        String matricule = URLEncoder.encode(id, "UTF-8");
        String url = String.format("http://172.20.38.2:5000/visiteurs/%s/%s", id, mdp);

        Response.Listener<JSONObject> ecouteurResponse = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("APP-RV","Résponse HTTP : " + response);
                try{
                    MainActivity.this.visiteur = new Visiteur();
                    MainActivity.this.visiteur.setMatricule( response.getString("vis_matricule") );
                    MainActivity.this.visiteur.setNom( response.getString("vis_nom") );
                    MainActivity.this.visiteur.setPrenom( response.getString("vis_prenom") );
                    MainActivity.this.visiteur.setMdp( mdp );
                    Log.i("APP-RV", "Objet visiteur :" + visiteur.toString());

                } catch (JSONException e) {
                    Log.e("APP-RV", "Erreur JSON : " + e.getMessage());
                    e.printStackTrace();
                }
                Log.i("APP-RV", "Objet visiteur :" + visiteur.toString());


                if( MainActivity.this.visiteur.getMatricule() != null ) {
                    Session.ouvrir(MainActivity.this.visiteur);
                    Session session = Session.getSession();
                }
                MainActivity.this.bSeConnecter.setEnabled(false);
                MainActivity.this.bAnnuler.setEnabled(false);
                Toast.makeText(MainActivity.this, "Connexion réussie", Toast.LENGTH_LONG).show();
            }
        };
    }

    public void annuler(){
        etMatricule.getText().clear();
        etMdp.getText().clear();
    }


}