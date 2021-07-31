package com.dicoding.consumerapp2;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements com.dicoding.consumerapp2.LoadNotesCallback {
    private RecyclerView rvFav;
    private com.dicoding.consumerapp2.FavoriteAdapter adapter;
    private static final String TAG = com.dicoding.consumerapp2.MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Consumer Favorites");
        }

        rvFav = findViewById(R.id.rv_fav);
        rvFav.setLayoutManager(new LinearLayoutManager(this));
        rvFav.setHasFixedSize(true);
        adapter = new com.dicoding.consumerapp2.FavoriteAdapter(this);
        rvFav.setAdapter(adapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(com.dicoding.consumerapp2.DatabaseContract.FavColumns.CONTENT_URI, true, myObserver);

        new LoadNotesAsync(this, this).execute();


    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(ArrayList<User> details) {
        Log.d(TAG,"exist"+ details);
        if(details.size() > 0){
            adapter.setListUsers(details);
        }
        else {
            adapter.setListUsers(new ArrayList<User>());
        }
    }

    private static class LoadNotesAsync extends AsyncTask<Void, Void, ArrayList<User>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallBack;

        private LoadNotesAsync(Context context, com.dicoding.consumerapp2.LoadNotesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallBack = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallBack.get().preExecute();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(com.dicoding.consumerapp2.DatabaseContract.FavColumns.CONTENT_URI, null, null, null, null);
            return MapHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<User> details) {
            super.onPostExecute(details);
            weakCallBack.get().postExecute(details);
        }
    }
    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadNotesAsync(context, (com.dicoding.consumerapp2.LoadNotesCallback) context).execute();
        }
    }

}

interface LoadNotesCallback {
    void preExecute();
    void postExecute(ArrayList<User> details);
}
