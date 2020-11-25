package fr.istic.mob.stareg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import fr.istic.mob.stareg.workers.StarAPIObserver;
import fr.istic.mob.stareg.workers.Downloader;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    public static String JSON_NOT_FOUND  ;
    public static ProgressBar downloadProgressbar  ;
    public static TextView  download_state ;
    public static ProgressBar extractionProgressbar  ;
    public static TextView  extraction_state ;
    public static String download_state_msg ;
    public static LinearLayout  download_info ;
    public static LinearLayout  update_ok ;
    public static LinearLayout  unzi_info ;
    public static LinearLayout  data_imported ;
    private static final String PROGRESS = "PROGRESS";
    public static  int PROGRESSVLUE = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getString(R.string.jsonfile_not_found) ;
        download_state_msg = this.getString(R.string.download_info) ;

        download_info =  (LinearLayout) findViewById(R.id.download_info) ;
        update_ok =  (LinearLayout)findViewById(R.id.update_ok) ;
        unzi_info =  (LinearLayout)findViewById(R.id.unzi_info) ;
        data_imported =  (LinearLayout)findViewById(R.id.data_imported) ;
        downloadProgressbar   =  (ProgressBar)findViewById(R.id.downloadProgressbar) ;
       download_state =  (TextView) findViewById(R.id.download_state) ;
        extractionProgressbar   =  (ProgressBar)findViewById(R.id.extractionProgressbar) ;
        extraction_state =  (TextView) findViewById(R.id.extraction_state) ;

        download_info.setVisibility(View.INVISIBLE);
        update_ok.setVisibility(View.INVISIBLE);
        unzi_info.setVisibility(View.INVISIBLE);
        data_imported.setVisibility(View.INVISIBLE);
        startPeriodicService() ;
    }

    /**
     *To set progress bar's value in the layout. Mais thisnmethode doesn't work well yet as we expect.
     * We have to improve it
     */
    public  void setLayoutProgressbar (OneTimeWorkRequest workRequest){
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
                            //download_state.setText( getString(R.string.download_state)  + " "+value+"%") ;

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