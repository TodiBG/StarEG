package fr.istic.mob.stareg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import fr.istic.mob.stareg.workers.CheckerForUpdates;
import fr.istic.mob.stareg.workers.Downloader;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        prefs = getApplicationContext().getSharedPreferences("fr.istic.mob.stareg", Context.MODE_PRIVATE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {

            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();
            PeriodicWorkRequest saveRequest =
                    new PeriodicWorkRequest.Builder(CheckerForUpdates.class, 2, TimeUnit.SECONDS)
                            .setConstraints(constraints)
                            .setInitialDelay(500,TimeUnit.MILLISECONDS)
                            .build();
            WorkManager.getInstance(getApplicationContext())
                    .enqueue(saveRequest);
        }else{
            Toast.makeText(this,R.string.internet_problem,Toast.LENGTH_SHORT).show(); ;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        System.out.println("onNewIntent");
        if (intent.getExtras() != null) {
            System.out.println("intent.getExtras() != null");
            String uriToZip = intent.getExtras().getString("uriToZip");
            if (uriToZip != null && !uriToZip.isEmpty()) {

                System.out.println("uriToZip != null && !uriToZip.isEmpty()");
                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();
                Data.Builder data = new Data.Builder();
                data.putString("uri", uriToZip);
                OneTimeWorkRequest saveRequest =
                        new OneTimeWorkRequest.Builder(Downloader.class)
                                .setInputData(data.build())
                                .setConstraints(constraints)
                                .build();
                WorkManager.getInstance(getApplicationContext())
                        .enqueue(saveRequest);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {

        } catch (Exception e) { e.printStackTrace();}
    }

}