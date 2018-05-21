package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02.Constants;
import practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02.ClientThread;
import practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    EditText port_edittext, url_edittext;
    Button start_button, get_button;
    TextView body_textview;

    ServerThread serverThread = null;
    ClientThread clientThread = null;

    private StartButtonClickListener startButtonClickListener = new StartButtonClickListener();
    private class StartButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = port_edittext.getText().toString();
            if (serverPort == null) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02.ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private GetButtonClickListener getButtonClickListener = new GetButtonClickListener();
    private class GetButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = port_edittext.getText().toString();
            if (serverPort == null) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = url_edittext.getText().toString();
            if (url == null) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            clientThread = new practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02.ClientThread(
                    Integer.parseInt(serverPort), url, body_textview
            );
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        port_edittext = findViewById(R.id.port_edittext);
        url_edittext = findViewById(R.id.url_edittext);

        start_button = findViewById(R.id.start_button);
        get_button = findViewById(R.id.get_button);

        body_textview = findViewById(R.id.body_textview);

        start_button.setOnClickListener(startButtonClickListener);
        get_button.setOnClickListener(getButtonClickListener);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
