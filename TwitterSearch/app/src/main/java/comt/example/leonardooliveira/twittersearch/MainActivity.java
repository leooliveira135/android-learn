package comt.example.leonardooliveira.twittersearch;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView lvTweet;
    private EditText edBusca;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTweet = (ListView)findViewById(R.id.lvTweet);
        edBusca = (EditText)findViewById(R.id.edBusca);

        new AutenticacaoTask().execute();
    }

    public void buscar(View v){
        String filtro = edBusca.getText().toString();
        if(accessToken == null){
            Toast.makeText(this, "Token não disponível", Toast.LENGTH_LONG).show();
        }
        else {
            new TwitterTask().execute(filtro);
        }
    }

    private class AutenticacaoTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Map<String, String> data = new HashMap<>();
                data.put("grant_type", "client_credentials");
                String json = HttpRequest.post("https://api.twitter.com/oauth2/token")
                        .authorization("Basic: " + gerarChave()).form(data).body();
                JSONObject token = new JSONObject(json);
                accessToken = token.getString("access_token");
            }
            catch (Exception e){
                return null;
            }

            return null;
        }

        private String gerarChave() throws UnsupportedEncodingException {
            String key = "sua key";
            String secret = "seu secret";
            String token = key + ":" + secret;
            String base64 = Base64.encodeToString(token.getBytes(), Base64.NO_WRAP);
            return base64;
        }
    }

    private class TwitterTask extends AsyncTask<String, Void, String[]> {

        ProgressDialog dialog;

        @Override
        protected String[] doInBackground(String... params) {
            try{
                String filtro = params[0];

                if(TextUtils.isEmpty(filtro)){
                    return null;
                }

                String twitterUrl = "http://api.twitter.com/1.1/search/tweets.json?q=";
                String url = Uri.parse(twitterUrl + filtro).toString();
                String conteudo = HttpRequest.get(url).authorization("Bearer " + accessToken).body();
                JSONObject jsonObject = new JSONObject(conteudo);
                JSONArray resultados = jsonObject.getJSONArray("statuses");

                String tweets[] = new String[resultados.length()];
                for (int i=0; i < resultados.length(); i++){
                    JSONObject tweet = resultados.getJSONObject(i);
                    String txt = tweet.getString("text");
                    String user = tweet.getJSONObject("user").getString("screen_name");
                    tweets[i] = user + " - " + txt;
                }
                return tweets;
            }
            catch(Exception e){
                Log.e(getPackageName(), e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Aguarde");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if(strings != null){
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item, strings);
                lvTweet.setAdapter(adapter);
            }

            dialog.dismiss();
        }
    }
}
