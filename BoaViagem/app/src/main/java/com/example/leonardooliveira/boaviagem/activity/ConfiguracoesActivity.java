package com.example.leonardooliveira.boaviagem.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;

import com.example.leonardooliveira.boaviagem.R;

/**
 * Created by Leonardo Oliveira on 30/06/2016.
 */
public class ConfiguracoesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
