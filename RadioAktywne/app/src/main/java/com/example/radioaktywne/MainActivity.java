package com.example.radioaktywne;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO Add AudioFocus manager and NotificationBar

    private FloatingActionButton fabPlay;
    private FloatingActionButton fabPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        /**
         * Bypass javaSSLCert Exception
         */
        disableSSLCertificateChecking();


        fabPlay = findViewById(R.id.fab_play);
        fabPause = findViewById(R.id.fab_pause);

        fabPlay.setOnClickListener(this);
        fabPause.setOnClickListener(this);
        fabPause.setEnabled(false);

    }
    @Override
    public void onClick(View view) {
        if (view == fabPlay) {
            Snackbar.make(view, R.string.now_listening, Snackbar.LENGTH_LONG).show();
            Intent intent = new Intent(this, RadioPlayer.class);
            intent.setAction(Intent.ACTION_MAIN);
            startService(intent);
            fabPlay.setEnabled(false);
            fabPause.setEnabled(true);

        } else if (view == fabPause) {
            Snackbar.make(view, R.string.stop_listening, Snackbar.LENGTH_LONG).show();
            stopService(new Intent(this, RadioPlayer.class));
            fabPlay.setEnabled(true);
            fabPause.setEnabled(false);
        }

    }

    private void disableSSLCertificateChecking(){
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {// Not implemented
            }
            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {// Not implemented
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d("tag","settings action");
        }
        else if (id == R.id.facebook_action){
            Log.d("tag","facebook action bar");
            browserIntent(getResources().getString(R.string.facebook_address));
        }
        else if (id == R.id.instagram_action){
            Log.d("tag","instagram action");
            browserIntent(getResources().getString(R.string.instagram_address));
        }
        else if (id == R.id.home_action){
            Log.d("tag","home_action");
            browserIntent(getResources().getString(R.string.home_address));
        }

        return super.onOptionsItemSelected(item);
    }
    private void browserIntent(String address){
        Log.d("tag","Loading address : "+address);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
        startActivity(intent);
    }
/**
 *
 */


}
