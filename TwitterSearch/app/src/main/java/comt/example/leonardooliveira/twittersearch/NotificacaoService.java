package comt.example.leonardooliveira.twittersearch;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Leonardo Oliveira on 04/07/2016.
 */
public class NotificacaoService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);
        long delayInicial = 0;
        long periodo = 10;
        TimeUnit unit = TimeUnit.MINUTES;
        pool.scheduleAtFixedRate(new NotificacaoTask(), delayInicial, periodo, unit);

        return START_STICKY;
    }

    private boolean estaConectado(){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info.isConnected();
    }

    private class NotificacaoTask implements Runnable{

        private String baseUrl = "http://api.twitter.com/1.1/search/tweets.json";
        private String refreshUrl = "?q=@android";

        @Override
        public void run() {
            if (!estaConectado()){
                return;
            }
            try{
                String json = HTTPUtils.acessar(baseUrl + refreshUrl);
                JSONObject jsonObject = new JSONObject(json);
                refreshUrl = jsonObject.getString("refresh_url");
                JSONArray resultados = jsonObject.getJSONArray("results");

                for(int i=0; i < resultados.length(); i++){
                    JSONObject tweet = resultados.getJSONObject(i);
                    String txt = tweet.getString("text");
                    String user = tweet.getString("from_user");

                    criaNotificacao(user, txt, i);
                }
            }
            catch(Exception e){}
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void criaNotificacao(String user, String txt, int id){
        int icon = R.mipmap.ic_launcher;
        String aviso = getString(R.string.aviso);
        long data = System.currentTimeMillis();
        String titulo = user + " " + getString(R.string.titulo);

        Context context = getApplicationContext();
        Intent intent = new Intent(context, TweetActivity.class);
        intent.putExtra(TweetActivity.USER, user.toString());
        intent.putExtra(TweetActivity.TXT, txt.toString());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(icon);
        builder.setContentTitle(titulo);
        builder.setContentText(aviso);

        TaskStackBuilder stack = TaskStackBuilder.create(this);
        stack.addParentStack(TweetActivity.class);
        stack.addNextIntent(intent);

        builder.setContentIntent(pendingIntent);

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nm = (NotificationManager)getSystemService(ns);
        nm.notify(id, builder.build());
    }
}
