package ziramba.treinaweb_primeiroprojeto;

import java.net.Socket;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Tela5 extends Activity {
    static TextView textViewStatus;
    ConSocketThread conSocket;

    private static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // Verifica mensagem do Handler e mostra na tela
            synchronized (msg) {
                textViewStatus.setText(msg.obj.toString());
            }
        };
    };


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela5);

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);

        Button btnConnect = (Button) findViewById(R.id.buttonConnect);
        btnConnect.setOnClickListener(OnClickButtonConnect);

        Button btnSend = (Button) findViewById(R.id.buttonSendMessage);
        btnSend.setOnClickListener(OnClickButtonSend);

        Button btnDisconnect = (Button) findViewById(R.id.buttonDisconnet);
        btnDisconnect.setOnClickListener(OnClickButtonDisconnect);
    }

    private View.OnClickListener OnClickButtonConnect = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final EditText edIp = (EditText) findViewById(R.id.editTextIp);
            final EditText edPorta = (EditText) findViewById(R.id.editTextPorta);
            final View layoutSend = findViewById(R.id.linearLayoutSend);

            new Thread(new Runnable() {
                public void run() {
                    try {
                        String ip = edIp.getText().toString();
                        int porta = Integer.parseInt(edPorta.getText().toString());
                        Socket socket = new Socket(ip, porta);
                        conSocket = new ConSocketThread(socket, handler);
                        layoutSend.post(new Runnable() {

                            @Override
                            public void run() {
                                layoutSend.setVisibility(View.VISIBLE);
                            }
                        });
                    } catch (Exception e) {
                        // Mostra erro na tela
                        Toast.makeText(getApplicationContext(),
                                "Não foi possível conectar->" + e.getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }).start();
        }
    };

    private View.OnClickListener OnClickButtonSend = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            EditText edMensagem = (EditText) findViewById(R.id.editTextMessage);

            try {
                if(!conSocket.isRunning())
                    conSocket.start();

                String mensagem = edMensagem.getText().toString();
                conSocket.setMessage(mensagem);
            } catch (Exception e) {
                Toast.makeText(getBaseContext(),
                        "Não foi possível iniciar o Serviço para envio de Mensagens.",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    };

    private View.OnClickListener OnClickButtonDisconnect = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                if(conSocket.isRunning())
                    conSocket.disconnect();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(),
                        "Não foi possível desconectar o serviço ",
                        Toast.LENGTH_LONG).show();
            }
        }
    };
}