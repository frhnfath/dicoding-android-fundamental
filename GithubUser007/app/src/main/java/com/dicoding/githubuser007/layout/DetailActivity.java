package com.dicoding.githubuser007.layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dicoding.githubuser007.R;
import com.dicoding.githubuser007.adapter.FragmentAdapter;
import com.dicoding.githubuser007.adapter.FragmentSectionAdapter;
import com.dicoding.githubuser007.helper.UserHelper;
import com.dicoding.githubuser007.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.dicoding.githubuser007.database.DatabaseContract.FavoriteColumns.AVATAR;
import static com.dicoding.githubuser007.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.dicoding.githubuser007.database.DatabaseContract.FavoriteColumns.FOLLOWERS;
import static com.dicoding.githubuser007.database.DatabaseContract.FavoriteColumns.FOLLOWING;
import static com.dicoding.githubuser007.database.DatabaseContract.FavoriteColumns.REPOSITORY;
import static com.dicoding.githubuser007.database.DatabaseContract.FavoriteColumns.USERNAME;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_USER = "extra_user";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int RESULT_DELETE = 301;
    private int position;
    private boolean isEdit = false;
    private UserHelper userHelper;
    FloatingActionButton btnFav;
    FloatingActionButton btnUnfav;
    TextView tvName;
    TextView tvFollower;
    TextView tvRepository;
    TextView tvFollowing;
    ImageView imgUser;



    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        btnFav = findViewById(R.id.btnFavorite);
        btnFav.setOnClickListener(this);
        btnUnfav = findViewById(R.id.btnUnfav);
        btnUnfav.setOnClickListener(this);
        User user = getIntent().getParcelableExtra(EXTRA_USER);
        getUsernames(user.getName());
        Log.d(TAG, "called" +user.getName());
        Log.d(TAG, "called " +user);
        FragmentSectionAdapter fragmentSectionAdapter = new FragmentSectionAdapter(this, getSupportFragmentManager());
        fragmentSectionAdapter.username = user.getName();
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(fragmentSectionAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        getSupportActionBar().setElevation(0);
        tvName = findViewById(R.id.tv_username);
        imgUser = findViewById(R.id.img_user);
        tvFollower = findViewById(R.id.tv_follower);
        tvFollowing = findViewById(R.id.tv_following);
        tvRepository = findViewById(R.id.tv_repository);
        userHelper = UserHelper.getInstance(getApplicationContext());
        userHelper.open();
    }

    private void getUsernames(String username){
        AsyncHttpClient client = new AsyncHttpClient();
        String url ="https://api.github.com/users/"+username;
        client.addHeader("Authorization", "token 9414975867e2bdfb98f8e12505f36baa28193f40");
        client.addHeader("User-Agent","request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    Log.d(TAG, "Detail success");
                    JSONObject jsonObject = new JSONObject(result);
                    user.setName(jsonObject.getString("login"));
                    user.setRepo(jsonObject.getString("public_repos"));
                    user.setFollowers(jsonObject.getString("followers"));
                    user.setFollowing(jsonObject.getString("following"));
                    user.setAvatar(jsonObject.getString("avatar_url"));
                    tvName.setText(user.getName());
                    Log.d(TAG, "example "+ user.getName());
                    tvFollowing.setText(user.getFollowing());
                    tvFollower.setText(user.getFollowers());
                    tvRepository.setText(user.getRepo());
                    Glide.with(getApplicationContext()).load(user.getAvatar()).into(imgUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String resp = new String(responseBody);
                Log.d(TAG, "onFailure: api failed "+resp);
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                } Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_POSITION, position);
        String username = user.getName();
        Log.d(TAG, "this is detail " + username);
        String avatar = user.getAvatar();
        Log.d(TAG, "this is avatar " + avatar);
        String followers = user.getFollowers();
        String following = user.getFollowing();
        String repo = user.getRepo();
        ContentValues values = new ContentValues();
        values.put(USERNAME, username);
        values.put(FOLLOWING, following);
        values.put(FOLLOWERS, followers);
        values.put(AVATAR, avatar);
        values.put(REPOSITORY, repo);
        Log.d(TAG, "this is value " + values);

        if (view.getId() == R.id.btnFavorite) {
            getContentResolver().insert(CONTENT_URI, values);
            Log.d(TAG, "added "+getContentResolver().insert(CONTENT_URI, values));
            Toast.makeText(DetailActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
            Intent back = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(back);
        }

        if (view.getId() == R.id.btnUnfav){
            long result = userHelper.deleteById(user.getName());
            if(result > 0){
                setResult(RESULT_DELETE, intent);
                Toast.makeText(DetailActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(back);
            } else {
                Toast.makeText(DetailActivity.this, "Not added to favorite yet",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userHelper.close();
    }
}