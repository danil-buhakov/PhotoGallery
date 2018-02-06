package com.book.dan.photogallery;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobPollService extends JobService {
    private static final String TAG = "JobPollService";

    PollTask mPollTask;

    @Override
    public boolean onStartJob(JobParameters params) {
        mPollTask = new PollTask();
        mPollTask.execute(params);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if(mPollTask!=null){
            mPollTask.cancel(true);
        }
        return false;
    }

    private class PollTask extends AsyncTask<JobParameters,Void,Void>{

        @Override
        protected Void doInBackground(JobParameters... jobParameters) {
            JobParameters jobParams = jobParameters[0];
            String query = QueryPreferances.getStoredQuery(JobPollService.this);
            String lastResultId = QueryPreferances.getLastResultId(JobPollService.this);

            List<GalleryItem> items;
            if(query==null){
                items = new FlickrFetchr().fetchRecentPhotos();
            } else{
                items = new FlickrFetchr().searchPhotos(query);
            }

            if(items.size()==0)
                return null;
            String resultId = items.get(0).getId();
            if(resultId.equals(lastResultId)){
                Log.i(TAG,"Got an old result: " + resultId);
            } else{
                Log.i(TAG, "Got a new result: " + resultId);

                Resources resources = getResources();
                Intent i = PhotoGalleryActivity.newIntent(JobPollService.this);
                PendingIntent pi = PendingIntent.getActivity(JobPollService.this,0,i,0);

                Notification notification = new NotificationCompat.Builder(JobPollService.this)
                        .setTicker(resources.getString(R.string.new_pictures_title))
                        .setSmallIcon(android.R.drawable.ic_menu_report_image)
                        .setContentTitle(resources.getString(R.string.new_pictures_title))
                        .setContentText(resources.getString(R.string.new_pictures_text))
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .build();

                NotificationManagerCompat manager = NotificationManagerCompat.from(JobPollService.this);
                manager.notify(0,notification);
            }
            QueryPreferances.setLastResultId(JobPollService.this,resultId);

            jobFinished(jobParams,false);
            return null;
        }
    }
}
