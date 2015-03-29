package ziramba.treinaweb_primeiroprojeto;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by paulo_000 on 28/03/2015.
 */
public class Tela1 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela1);

        Button button = (Button)findViewById(R.id.buttonCarregar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadImageAsyncTask().execute(" ");
            }
        });

    }

    private Bitmap DownloadImage(String url) {
        Bitmap bitmap = null;
        InputStream inputStream = null;

        try {
            inputStream = NetworkUtils.OpenHttpConnection(url,this);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e){
            Log.d("HttpConnection", e.getLocalizedMessage());
        }

        return bitmap;
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            return DownloadImage(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            ImageView img = (ImageView) findViewById(R.id.imageViewWeb);
            img.setImageBitmap(result);
        }
    }
}
