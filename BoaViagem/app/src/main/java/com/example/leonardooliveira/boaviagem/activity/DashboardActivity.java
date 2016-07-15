package com.example.leonardooliveira.boaviagem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.leonardooliveira.boaviagem.R;
import com.example.leonardooliveira.boaviagem.activity.gasto.GastoActivity;
import com.example.leonardooliveira.boaviagem.activity.viagem.ViagemActivity;
import com.example.leonardooliveira.boaviagem.activity.viagem.ViagemListActivity;

/**
 * Created by Leonardo Oliveira on 29/06/2016.
 */
public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_dashboard);
    }

    public void selecionarOpcao(View v){
        /*
            Com base na view clicada, iremos tomar a ação correta
        */

        switch (v.getId()){
            case R.id.ivNovoGasto:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case R.id.ivNovaViagem:
                startActivity(new Intent(this, ViagemActivity.class));
                break;
            case R.id.ivMinhasViagens:
                startActivity(new Intent(this, ViagemListActivity.class));
                break;
            case R.id.ivConfig:
                startActivity(new Intent(this, ConfiguracoesActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }
}
