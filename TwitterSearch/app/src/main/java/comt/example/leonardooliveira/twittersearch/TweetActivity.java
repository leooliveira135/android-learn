package comt.example.leonardooliveira.twittersearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Leonardo Oliveira on 04/07/2016.
 */
public class TweetActivity extends AppCompatActivity {
    public static final String TXT = "texto";
    public static final String USER = "usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet);

        TextView txtUser = (TextView)findViewById(R.id.txtUser);
        TextView txtTexto = (TextView)findViewById(R.id.txtTexto);

        String usuario = getIntent().getStringExtra(USER);
        String texto = getIntent().getStringExtra(TXT);

        txtUser.setText(usuario);
        txtTexto.setText(texto);
    }
}
