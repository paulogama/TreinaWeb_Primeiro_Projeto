package ziramba.treinaweb_primeiroprojeto;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by paulo_000 on 29/03/2015.
 */
public class Tela4 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela4);

        Button button = (Button)findViewById(R.id.buttonCarregar);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new ReadJsonAsync().execute("https://android-avancado-treinaweb.appspot.com/_ah/api/app/v1/cliente");
            }
        });
    }

    public String[] readJson(String url) {
        InputStream inputStream = null;
        String[] strArray = null;

        try {
            inputStream = NetworkUtils.OpenHttpConnection(url,this);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputStr);
            }

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());

            JSONArray jsonArray = jsonObject.getJSONArray("items");
            strArray = new String[jsonArray.length()];

            for (int i=0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                strArray[i] = jObj.getString("nome");
            }
        } catch (IOException e) {
            Log.d("readJson",e.getLocalizedMessage());
        } catch (JSONException e) {
            Log.d("readJson", e.getLocalizedMessage());
        }

        return strArray;
    }

    private class ReadJsonAsync extends AsyncTask<String,Void,String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            return readJson(params[0]);
        }

        protected void onPostExecute(String[] result) {
            ListView listView = (ListView)findViewById(R.id.listViewClientes);

            try {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,result);
                listView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
