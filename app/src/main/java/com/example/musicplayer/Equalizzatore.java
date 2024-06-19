package com.example.musicplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Equalizzatore extends AppCompatActivity {

    private List<Eqconfig> eqConfigs;
    private ArrayAdapter<Eqconfig> adapter;
    private EqconfigDao dataBase;
    private Spinner spinner;
    private Button reset, delete, update, create;
    private SeekBar seekbar80hz, seekbar160hz, seekbar240hz, seekbar320hz;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalizzatore_layout);
        sharedPreferences = getSharedPreferences("eqconfig_data", MODE_PRIVATE);
        dataBase = EqconfigDao.getInstance(this);

        spinner = findViewById(R.id.eq_spinner);
        reset = findViewById(R.id.Eq_reset);
        delete = findViewById(R.id.Eq_delete);
        update = findViewById(R.id.Eq_update);
        create = findViewById(R.id.Eq_create);
        seekbar80hz = findViewById(R.id.seekBar80);
        seekbar160hz = findViewById(R.id.seekBar160);
        seekbar240hz = findViewById(R.id.seekBar240);
        seekbar320hz = findViewById(R.id.seekBar320);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar80hz.setProgress(50);
                seekbar160hz.setProgress(50);
                seekbar240hz.setProgress(50);
                seekbar320hz.setProgress(50);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.eq_edittext_layout, null);

                EditText eqConfigEditText = dialogLayout.findViewById(R.id.eq_name_edittext);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Equalizzatore.this);
                dialogBuilder.setTitle("Dai un nome alla tua configurazione");
                dialogBuilder.setView(dialogLayout);
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Eqconfig eqconfig = new Eqconfig(eqConfigEditText.getText().toString(), seekbar80hz.getProgress(), seekbar160hz.getProgress(), seekbar240hz.getProgress(), seekbar320hz.getProgress());
                        dataBase.insert(eqconfig);
                        eqConfigs.clear();
                        eqConfigs.addAll(dataBase.getAll());
                        adapter.notifyDataSetChanged();
                        update_current_config(eqconfig.getNome());
                        set_current_eqconfig(eqconfig);
                        Toast.makeText(Equalizzatore.this, "Configurazione creata con nome " + eqConfigEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
               // alertDialog.setContentView(eqConfigEditText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                alertDialog.show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eqconfig eqconfig = (Eqconfig) spinner.getSelectedItem();
                dataBase.delete(eqconfig);
                eqConfigs.clear();
                eqConfigs.addAll(dataBase.getAll());
                adapter.notifyDataSetChanged();
                update_current_config("Default");
                set_current_eqconfig(dataBase.getByKey(get_current_config()));
                Toast.makeText(Equalizzatore.this, "Configurazione eliminata", Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eqconfig eqconfig = (Eqconfig) spinner.getSelectedItem();
                eqconfig.setHz80value(seekbar80hz.getProgress());
                eqconfig.setHz160value(seekbar160hz.getProgress());
                eqconfig.setHz240value(seekbar240hz.getProgress());
                eqconfig.setHz320value(seekbar320hz.getProgress());
                dataBase.update(eqconfig);
            }


        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                update_current_config(eqConfigs.get(position).getNome());
                set_current_eqconfig(eqConfigs.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        eqConfigs = dataBase.getAll();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eqConfigs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        set_current_eqconfig(dataBase.getByKey(get_current_config()));
    }
    public void update_current_config(String nome) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("eqconfig_data_current_config", nome);
        editor.apply();
    }

    public String get_current_config() {
        return sharedPreferences.getString("eqconfig_data_current_config", "Default");
    }

    public void set_current_eqconfig(Eqconfig eqconfig) {
        spinner.setSelection(eqConfigs.indexOf(eqconfig));
        seekbar80hz.setProgress(eqconfig.getHz80value());
        seekbar160hz.setProgress(eqconfig.getHz160value());
        seekbar240hz.setProgress(eqconfig.getHz240value());
        seekbar320hz.setProgress(eqconfig.getHz320value());
    }

}
