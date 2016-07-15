package com.example.leonardooliveira.boaviagem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardooliveira.boaviagem.R;

public class BoaViagemActivity extends AppCompatActivity {

    private EditText edUsuario;
    private EditText edSenha;
    private static final String MANTER_CONECTADO = "manter_conectado";
    private CheckBox manterConectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boa_viagem);

        edUsuario = (EditText)findViewById(R.id.edUsuario);
        edSenha = (EditText)findViewById(R.id.edSenha);
        manterConectado = (CheckBox)findViewById(R.id.cbConectar);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean conectado = preferences.getBoolean(MANTER_CONECTADO, false);

        if(conectado){
            startActivity(new Intent(this, DashboardActivity.class));
        }
    }

    public void entrarOnClick(View v){
        String usuarioInformado = edUsuario.getText().toString();
        String senhaInformada = edSenha.getText().toString();

        if("leitor".equals(usuarioInformado) && "123".equals(senhaInformada)){
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(MANTER_CONECTADO, manterConectado.isChecked());
            editor.commit();
            startActivity(new Intent(this, DashboardActivity.class));
        }
        else{
            String msgErro = getString(R.string.erro_login);
            Toast toast = Toast.makeText(this, msgErro, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
