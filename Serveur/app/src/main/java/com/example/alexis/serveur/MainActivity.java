package com.example.alexis.serveur;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alexis.serveur.Serveur.Serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView hostIP;
    Button buttonConnexion;
    Button buttonSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hostIP=(TextView) findViewById(R.id.Text_IP);
        buttonConnexion = (Button) findViewById(R.id.Button_Connexion);
        buttonSend = (Button) findViewById(R.id.Button_Send);


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt");
            }
        });



        buttonConnexion .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                    String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                    Log.v("SERVER","Server running at: " + ip + ":" );
                    Serveur serveur = new Serveur(ip, 8080);
                    hostIP.setText("Connecter");

                }
                catch(Exception e){
                    System.out.println("LE SERVEUR NE PEUT PAS ETRE INSTANCIER : ");
                    hostIP.setText("ERREUR");
                }
            }
        });
    }
// Inspirer du vidéo https://www.youtube.com/watch?v=_7r_vdwmW0o
    public class JSONTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params){
            HttpURLConnection connection = null;
            BufferedReader reader = null ;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();

            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(connection != null)
                    connection.disconnect();
                try {
                    if(reader != null)
                        reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            hostIP.setText(result);
        }
    }


}
