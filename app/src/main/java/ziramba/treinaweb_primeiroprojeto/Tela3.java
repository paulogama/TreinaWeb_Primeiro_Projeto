package ziramba.treinaweb_primeiroprojeto;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by paulo_000 on 28/03/2015.
 */
public class Tela3 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela3);

        Button button = (Button)findViewById(R.id.buttonBuscar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.editTextId);
                if(editText.getText().length() > 0) {
                    int id = Integer.parseInt(editText.getText().toString());
                    new AcessWebServiceAsyncTask().execute(id);
                }
            }
        });
    }

    private Map<String,String> getCliente(long id) {
        InputStream is = null;
        Map<String, String> cliente = new HashMap<String, String>();

        try {
            is = NetworkUtils.OpenHttpConnection(
                    "http://android-avancado-treinaweb.appspot.com/api/app/xml/clientes?id=" + id, this);
            Document doc = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;

            try {
                db = dbf.newDocumentBuilder();
                doc = db.parse(is);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(doc != null){
                doc.getDocumentElement().normalize();
                NodeList definitionElements = doc.getElementsByTagName("cliente");

                for (int i = 0; i < definitionElements.getLength(); i++) {
                    Node itemNode = definitionElements.item(i);
                    if (itemNode.getNodeType() == Node.ELEMENT_NODE)
                    {
                        NodeList clienteElements = itemNode.getChildNodes();
                        for(int j = 0; j < clienteElements.getLength(); j++){
                            Node clienteNode = clienteElements.item(j);
                            if(clienteNode.getNodeType() == Node.ELEMENT_NODE){
                                cliente.put(clienteNode.getNodeName(), clienteNode.getTextContent());
                            }
                        }
                    }
                }
            }
        } catch (IOException e1) {
            Log.i("HttpConnection", e1.getLocalizedMessage());
        }

        return cliente;
    }

    private class AcessWebServiceAsyncTask extends AsyncTask<Integer,Void,Map<String,String>> {
        @Override
        protected Map<String,String> doInBackground(Integer... params) {
            return getCliente(params[0]);
        }

        protected void onPostExecute(Map<String,String> result){
            TextView textViewNome = (TextView)findViewById(R.id.textViewNome);
            TextView textViewIdade = (TextView)findViewById(R.id.textViewIdade);
            TextView textViewTelefone = (TextView)findViewById(R.id.textViewTelefone);
            TextView textViewRua = (TextView)findViewById(R.id.textViewRua);
            TextView textViewCidade = (TextView)findViewById(R.id.textViewCidade);
            TextView textViewEstado = (TextView)findViewById(R.id.textViewEstado);

            if(result.containsKey("nome")) {
                textViewNome.setText("Nome: "+result.get("nome"));
                textViewIdade.setText("Idade: "+result.get("idade"));
                textViewTelefone.setText("Telefone: "+result.get("telefone"));
                textViewRua.setText("Rua: "+result.get("rua"));
                textViewCidade.setText("Cidade: "+result.get("cidade"));
                textViewEstado.setText("Estado: "+result.get("estado"));

                findViewById(R.id.viewDados).setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getBaseContext(),"Cliente não encontrado!",Toast.LENGTH_LONG).show();
                findViewById(R.id.viewDados).setVisibility(View.INVISIBLE);
            }
        }
    }

}
