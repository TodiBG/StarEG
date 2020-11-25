package fr.istic.mob.stareg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import fr.istic.mob.stareg.workers.StarAPIObserver;
import fr.istic.mob.stareg.workers.Downloader;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    public static String JSON_NOT_FOUND  ;
    Button startServiceBtn ;
    private static ProgressBar downloadProgressbar  ;
    private static TextView  download_state ;
    private static LinearLayout  download_info ;
    private static final String PROGRESS = "PROGRESS";
    public static  int PROGRESSVLUE = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getString(R.string.jsonfile_not_found) ;

       this. startServiceBtn =  (Button)findViewById(R.id.check_btn) ;
       downloadProgressbar   =  (ProgressBar)findViewById(R.id.downloadProgressbar) ;
        download_state =  (TextView) findViewById(R.id.download_state) ;
        download_info =  (LinearLayout) findViewById(R.id.download_info) ;
        download_info.setVisibility(View.INVISIBLE);

        startServiceBtn.setOnClickListener(startServiceManually );
        startPeriodicService() ;
    }

    /**
     *To set progress bar's value in the layout. Mais thisnmethode doesn't work well yet as we expect.
     * We have to improve it
     */
    public  void setLayoutProgressbar (OneTimeWorkRequest workRequest){

        download_info.setVisibility(View.VISIBLE);
        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this , new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            Data progress = workInfo.getProgress();
                            System.out.println(progress);
                            int value = progress.getInt(PROGRESS, 0) ;
                            downloadProgressbar.setProgress(value);
                            download_state.setText( getString(R.string.download_state)  + " "+value+"%") ;

                        }
                    }

                });
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
                setLayoutProgressbar(saveRequest) ;
            }
        }
    }


    View.OnClickListener startServiceManually  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startPeriodicService();
        }
    };

    private void  startPeriodicService() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        prefs = getApplicationContext().getSharedPreferences("fr.istic.mob.stareg", Context.MODE_PRIVATE);

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