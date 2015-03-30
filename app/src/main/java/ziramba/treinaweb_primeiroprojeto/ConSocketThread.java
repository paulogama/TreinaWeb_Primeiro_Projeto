package ziramba.treinaweb_primeiroprojeto;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import android.os.Handler;
import android.os.Message;

public class ConSocketThread extends Thread {
    private DataOutputStream out;
    private boolean running = false;
    private Handler handler;
    private String sendMessage;

    public ConSocketThread(Socket socket, Handler hander){
        try{
            this.handler = hander;
            this.out = new DataOutputStream(socket.getOutputStream());
            Message msg = new Message();
            msg.obj = "Conectado!";
            handler.sendMessage(msg);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Message msg;
        setRunning(true);

        while (isRunning()) {
            try {
                if (sendMessage != null) {
                    msg = new Message();
                    msg.obj = "Mensagem enviada!";
                    handler.sendMessage(msg);

                    out.writeUTF(sendMessage);
                    out.flush();
                    sendMessage = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
                msg = new Message();
                msg.obj = e.getMessage();
                handler.sendMessage(msg);
                setRunning(false);
            }

        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message) {
        this.sendMessage = message;
    }

    public boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }

    public void disconnect() throws Exception {
        out.close();
        setRunning(false);
        if (handler != null) {
            Message msg = new Message();
            msg.obj = "Desconectado!";
            handler.sendMessage(msg);
        }
    }

}