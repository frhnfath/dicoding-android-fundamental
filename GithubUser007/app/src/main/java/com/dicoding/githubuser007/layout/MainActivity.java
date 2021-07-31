package com.dicoding.githubuser007.layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.dicoding.githubuser007.R;
import com.dicoding.githubuser007.adapter.UserListAdapter;
import com.dicoding.githubuser007.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ArrayList<User> Users = new ArrayList<>();
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = findViewById(R.id.main_search_username);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.rv_main);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.onActionViewExpanded();
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Users.clear();
                    getSearchUsers(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        getList();
    }

    private void getList() {
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users";
        client.addHeader("Authorization", "token 9414975867e2bdfb98f8e12505f36baa28193f40");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        User user = new User();
                        user.setName(jsonObject.getString("login"));
                        user.setAvatar(jsonObject.getString("avatar_url"));
                        Users.add(user);
                    }
                    showList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                String errorMsg;
                switch (statusCode) {
                    case 401:
                        errorMsg = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMsg = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMsg = statusCode + " : Not Found";
                        break;
                    default:
                        errorMsg = statusCode + " : " + error.getMessage();
                        break;
                }
//                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSearchUsers(final String query) {
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/search/users?q="+ query;
        client.addHeader("Authorization", "token e529715a7864d5be419918f7f29aa3bfcbb1968c");
        client.addHeader("User-Agent","request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);
                String result = new String(responseBody);
                Log.d(TAG, result);
                try{
                    JSONObject jsonObject1 = new JSONObject(result);
                    JSONArray jsonArray = jsonObject1.getJSONArray("items");

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        User user = new User();
                        user.setName(jsonObject2.getString("login"));
                        user.setAvatar(jsonObject2.getString("avatar_url"));
                        Users.add(user);
                    }
                    showList();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
}

    private void showList() {
        Log.d(TAG, "inirecycler");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserListAdapter userListAdapter = new UserListAdapter(Users);
        userListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(userListAdapter);
        userListAdapter.setOnItemClickCallback(new UserListAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                showUser(data);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_USER, data);
                Log.d(TAG, "Terpilih"+data);
                startActivity(intent);
            }
        });
    }

    private void showUser(User data) {
        Toast.makeText(this, "You chose " + data.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_language:
                Intent chgLanguage = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(chgLanguage);
                break;
            case R.id.menu_notification:
                Intent notification = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(notification);
                break;
            case R.id.menu_favorite:
                Intent fav = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(fav);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}