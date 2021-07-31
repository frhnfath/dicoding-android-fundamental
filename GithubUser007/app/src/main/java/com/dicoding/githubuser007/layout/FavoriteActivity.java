package com.dicoding.githubuser007.layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.dicoding.githubuser007.R;
import com.dicoding.githubuser007.adapter.FavoriteAdapter;
import com.dicoding.githubuser007.database.DatabaseContract;
import com.dicoding.githubuser007.helper.MapHelper;
import com.dicoding.githubuser007.model.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity implements LoadNotesCallback {
    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    //    private UserHelper userHelper;
    private static final String TAG = FavoriteActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Favorites Activity");
        recyclerView = findViewById(R.id.rv_fav);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new FavoriteAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickCallback(new FavoriteAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_USER, data);
                startActivity(intent);
            }
        });

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler= new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(DatabaseContract.FavoriteColumns.CONTENT_URI, true, myObserver);
        new LoadNotesAsync(this, this).execute();
    }

    @Override
    public void preExecute() {
    }

    @Override
    public void postExecute(ArrayList<User> details) {
        Log.d(TAG,"tes ini ada"+ details);
        if(details.size() > 0){
            adapter.setUsers(details);
        }
        else {
            adapter.setUsers(new ArrayList<User>());
        }
    }

    private static class LoadNotesAsync extends AsyncTask<Void, Void, ArrayList<User>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallBack;

        private LoadNotesAsync(Context context, LoadNotesCallback callback){
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
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.FavoriteColumns.CONTENT_URI, null, null, null, null);
            return MapHelper.mapArrayList(dataCursor);
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
            new LoadNotesAsync(context, (LoadNotesCallback) context).execute();
        }
    }
}

interface LoadNotesCallback {
    void preExecute();
    void postExecute(ArrayList<User> details);
}