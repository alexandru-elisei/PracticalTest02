package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02.Constants;
import practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02.Utilities;

public class ClientThread extends Thread {

    private int port;
    private String url;
    private TextView body_textview;

    private Socket socket;

    public ClientThread(int port, String url, TextView body_textview) {
        this.port = port;
        this.url = url;
        this.body_textview = body_textview;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("127.0.0.1", port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(this.url);
            printWriter.flush();
            String body = "";
            String b;
            while ((b = bufferedReader.readLine()) != null) {
                body += b;
            }
            final String fb = body;
            this.body_textview.post(new Runnable() {
                @Override
                public void run() {
                    body_textview.setText(fb);
                }
            });
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
