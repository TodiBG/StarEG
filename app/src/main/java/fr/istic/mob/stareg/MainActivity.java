package fr.istic.mob.stareg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import fr.istic.mob.stareg.database.StarContract;
import fr.istic.mob.stareg.workers.StarAPIObserver;
import fr.istic.mob.stareg.workers.Downloader;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private static final String PROGRES = "PROGRESS";
    public static  int PROGRESSVLUE = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startPeriodicService() ;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            String zipUri = intent.getExtras().getString("zipUri");

            if (!(zipUri == null || zipUri.isEmpty())) {
                Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
                Data.Builder data = new Data.Builder();
                data.putString("uri", zipUri);
                OneTimeWorkRequest saveRequest = new OneTimeWorkRequest.Builder(Downloader.class)
                                .setInputData(data.build())
                                .setConstraints(constraints)
                                .build();
                WorkManager.getInstance(getApplicationContext()).enqueue(saveRequest);
            }
        }

        finish();
    }



    private void  startPeriodicService() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        preferences = getApplicationContext().getSharedPreferences("fr.istic.mob.stareg", Context.MODE_PRIVATE);

        if ((connManager.getActiveNetworkInfo() != null) && connManager.getActiveNetworkInfo().isConnected()) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();
            PeriodicWorkRequest saveRequest =
                    new PeriodicWorkRequest.Builder(StarAPIObserver.class, 30, TimeUnit.MINUTES)
                            .setConstraints(constraints)
                            .setInitialDelay(500,TimeUnit.MILLISECONDS)
                            .build();
            WorkManager.getInstance(getApplicationContext()).enqueue(saveRequest);
        }else{
            Toast.makeText(this,R.string.internet_problem,Toast.LENGTH_SHORT).show(); ;
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}