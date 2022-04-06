package fr.gsb.rv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SaisieRvActivity extends AppCompatActivity {

    TextView tvDateVisite;
    Button bDateVisite;
    Spinner spPraticien;
    Spinner spMotif;
    EditText etBilan;
    Spinner spCoefConfiance;
    Button bValider;
    Button bAnnuler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie_rv);

        tvDateVisite = findViewById(R.id.tvDateVisite);
        spPraticien = findViewById(R.id.spPraticien);
        spMotif = findViewById(R.id.spMotif);
        spCoefConfiance = findViewById(R.id.spCoefConfiance);
        etBilan = findViewById(R.id.etBilan);
    }
}