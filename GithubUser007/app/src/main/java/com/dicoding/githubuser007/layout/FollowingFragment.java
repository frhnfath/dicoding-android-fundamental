package com.dicoding.githubuser007.layout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dicoding.githubuser007.R;
import com.dicoding.githubuser007.adapter.FragmentAdapter;
import com.dicoding.githubuser007.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String ARG_SECTION_USERNAME = "username";

    // TODO: Rename and change types of parameters
    private String username;
    private ArrayList<User> Users = new ArrayList<>();
    private RecyclerView recyclerView;

    public FollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //     * @param param1 Parameter 1.
     //     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowingFragment newInstance(String username) {
        FollowingFragment followingFragment = new FollowingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SECTION_USERNAME, username);
        followingFragment.setArguments(bundle);
        return followingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = this.getArguments().getString(ARG_SECTION_USERNAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv_ffollowers);
        recyclerView.setHasFixedSize(true);
        getList(username);
        Log.d(TAG, "onViewCreated follower ada"+username);
    }

    private void getList(String username) {
        Log.d(TAG, "getListUser: fragment "+username);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users/"+username+"/following";
        client.addHeader("Authorization", "token e529715a7864d5be419918f7f29aa3bfcbb1968c");
        client.addHeader("User-Agent","request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        User user = new User();
                        user.setName(jsonObject.getString("login"));
                        user.setAvatar(jsonObject.getString("avatar_url"));
                        Users.add(user);
                    }
                    showFollowing();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = new String(responseBody);
                Log.d(TAG, "onFailure - failed " + response);
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(getActivity(), errorMessage,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFollowing() {
        Log.d(TAG, "ShowFollowing");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FragmentAdapter fragmentAdapter = new FragmentAdapter(Users);
        recyclerView.setAdapter(fragmentAdapter);
    }
}