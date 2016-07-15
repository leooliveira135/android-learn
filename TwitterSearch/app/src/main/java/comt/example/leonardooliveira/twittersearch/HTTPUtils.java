package comt.example.leonardooliveira.twittersearch;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by Leonardo Oliveira on 03/07/2016.
 */
public class HTTPUtils {
    public static String acessar(String endereco){
        try {
            URL url = new URL(endereco);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            Scanner scanner = new Scanner(is);
            String conteudo = scanner.useDelimiter("\\A").next();
            scanner.close();
            return conteudo;
        }
        catch (Exception e){
            return null;
        }
    }
}
