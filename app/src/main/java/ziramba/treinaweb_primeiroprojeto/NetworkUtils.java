package ziramba.treinaweb_primeiroprojeto;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by paulo_000 on 28/03/2015.
 */
public class NetworkUtils {
    public static InputStream OpenHttpConnection(String urlString, Context context) throws IOException {
        InputStream inputStream = null;
        int response = -1;

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = (networkInfo != null) && networkInfo.isConnectedOrConnecting();

        if(isConnected) {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();

            if(!(urlConnection instanceof HttpURLConnection))
                throw new IOException("Nao eh uma conexao HTTP");

            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                response = httpURLConnection.getResponseCode();

                if(response == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
            } catch (Exception e) {
                Log.i("HttpConnection",e.getLocalizedMessage());
                throw new IOException("Erro ao se conectar");
            }
        }

        return inputStream;

    }
}
