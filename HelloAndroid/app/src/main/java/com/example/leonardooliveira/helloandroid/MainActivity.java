package com.example.leonardooliveira.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText edNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.edNome = (EditText)findViewById(R.id.edNome);
    }

    public void surpreenderUsuario(View v){
        Intent intent = new Intent(SaudacaoActivity.ACAO_EXIBIR_SAUDACAO);
        intent.addCategory(SaudacaoActivity.CATEGORIA_SAUDACAO);
        String texto = edNome.getText().toString();
        intent.putExtra(SaudacaoActivity.EXTRA_NOME_USUARIO, texto);
        startActivity(intent);
    }
}
