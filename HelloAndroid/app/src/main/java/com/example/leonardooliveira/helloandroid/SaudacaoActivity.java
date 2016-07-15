package com.example.leonardooliveira.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Leonardo Oliveira on 28/06/2016.
 */
public class SaudacaoActivity extends AppCompatActivity{

    public static final String EXTRA_NOME_USUARIO = "helloandroid.EXTRA_NOME_USUARIO";
    public static final String ACAO_EXIBIR_SAUDACAO = "helloandroid.ACAO_EXIBIR_USUARIO";
    public static final String CATEGORIA_SAUDACAO = "helloandroid.CATEGORIA_SAUDACAO";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saudacao);

        TextView txtsaudacao = (TextView)findViewById(R.id.txtSaudacao);
        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_NOME_USUARIO)){
            String saudacao = getResources().getString(R.string.saudacao);
            txtsaudacao.setText(saudacao + " " + intent.getStringExtra(EXTRA_NOME_USUARIO));
        }
        else{
            txtsaudacao.setText("O nome do usuário não foi informado");
        }
    }
}
