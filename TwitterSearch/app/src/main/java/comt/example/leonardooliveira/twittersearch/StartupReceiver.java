package comt.example.leonardooliveira.twittersearch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Leonardo Oliveira on 04/07/2016.
 */
public class StartupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent it = new Intent(context, NotificacaoService.class);
        context.startService(it);
    }
}
