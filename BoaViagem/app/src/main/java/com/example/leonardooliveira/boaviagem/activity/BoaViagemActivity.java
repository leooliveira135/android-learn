package com.example.leonardooliveira.boaviagem.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardooliveira.boaviagem.R;
import com.example.leonardooliveira.boaviagem.model.Constantes;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;

import java.io.IOException;

import static com.example.leonardooliveira.boaviagem.model.Constantes.NOME_CONTA;
import static com.example.leonardooliveira.boaviagem.model.Constantes.PREFERENCIAS;
import static com.example.leonardooliveira.boaviagem.model.Constantes.TOKEN_ACESSO;

public class BoaViagemActivity extends AppCompatActivity {

    private EditText edUsuario;
    private EditText edSenha;
    private static final String MANTER_CONECTADO = "manter_conectado";
    private CheckBox manterConectado;
    private SharedPreferences preferences;
    private Account conta;
    private GoogleAccountManager accountManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boa_viagem);

        accountManager = new GoogleAccountManager(this);

        edUsuario = (EditText)findViewById(R.id.edUsuario);
        edSenha = (EditText)findViewById(R.id.edSenha);
        manterConectado = (CheckBox)findViewById(R.id.cbConectar);

        preferences = getSharedPreferences(PREFERENCIAS, MODE_PRIVATE);
        boolean conectado = preferences.getBoolean(MANTER_CONECTADO, false);

        if(conectado){
            solicitarAutorizacao();
        }
    }

    private void iniciarDashboard(){
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void entrarOnClick(View v){
        String usuarioInformado = edUsuario.getText().toString();
        String senhaInformada = edSenha.getText().toString();

        if("leitor".equals(usuarioInformado) && "123".equals(senhaInformada)){
            autenticar(usuarioInformado, senhaInformada);
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

    private void autenticar(final String usuario, String senha){
        conta = accountManager.getAccountByName(usuario);

        if(conta == null){
            Toast.makeText(this, R.string.conta_inexistente, Toast.LENGTH_LONG).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, usuario);
        bundle.putString(AccountManager.KEY_PASSWORD, senha);

        accountManager.getAccountManager().confirmCredentials(conta, bundle, this, new AutenticacaoCallback(), null);
    }

    private class AutenticacaoCallback implements AccountManagerCallback<Bundle>{

        @Override
        public void run(AccountManagerFuture<Bundle> future) {
            try{
                Bundle bundle = future.getResult();
                String nomeConta = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                String tokenAcesso = bundle.getString(AccountManager.KEY_AUTHTOKEN);

                gravarTokenAcesso(nomeConta,tokenAcesso);

                iniciarDashboard();
            }
            catch (OperationCanceledException e){
                //usuário cancelou a operação
            }
            catch (AuthenticatorException e){
                //possível falha no autenticador
            }
            catch (IOException e){
                //possível falha de comunicação
            }
        }
    }

    private void gravarTokenAcesso(String nomeConta, String tokenAcesso){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NOME_CONTA, nomeConta);
        editor.putString(TOKEN_ACESSO, tokenAcesso);
        editor.commit();
    }

    private void solicitarAutorizacao(){
        String tokenAcesso = preferences.getString(TOKEN_ACESSO, null);
        String nomeConta = preferences.getString(NOME_CONTA, null);

        if(tokenAcesso != null){
            accountManager.invalidateAuthToken(tokenAcesso);
            conta = accountManager.getAccountByName(nomeConta);
        }

        accountManager.getAccountManager()
                .getAuthToken(conta, Constantes.AUTH_TOKEN_TYPE, null,
                        this, new AutenticacaoCallback(), null);
    }
}
