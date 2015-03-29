package ziramba.treinaweb_primeiroprojeto;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by paulo_000 on 28/03/2015.
 */
public class Tela2 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela2);

        Button button = (Button)findViewById(R.id.buttonCarregar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTextAsyncTask().execute("http://android-avancado-treinaweb.appspot.com/contents/xml/clientes.xml");
            }
        });

    }

    public String DownloadString(String url) {
        int BUFFER_SIZE =  2000;
        InputStream inputStream = null;

        try {
            inputStream = NetworkUtils.OpenHttpConnection(url,this);
        } catch (IOException e){
            Log.d("HttpConnection",e.getLocalizedMessage());
            return "";
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];

        try {
            while ((charRead = inputStreamReader.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer,0,charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            inputStream.close();
        } catch (IOException e) {
            Log.d("HttpConnection",e.getLocalizedMessage());
            return "";
        }

        return str;

    }

    private class DownloadTextAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return DownloadString(params[0]);
        }

        protected void onPostExecute(String result) {
            TextView textView = (TextView)findViewById(R.id.textViewConteudo);
            textView.setText(result);
        }
    }

}
