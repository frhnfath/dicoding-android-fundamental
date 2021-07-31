package com.dicoding.githubuser007.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.githubuser007.R;
import com.dicoding.githubuser007.model.User;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private ArrayList<User> users = new ArrayList<>();
    private Activity activity;
    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_items, parent, false);
        return new ViewHolder(view);
    }

    public void setUsers(ArrayList<User> users) {
        if (users.size() > 0) {
            this.users.size();
        }
        this.users.addAll(users);
        notifyDataSetChanged();
    }
    @Override

    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvFollowers;
        TextView tvFollowing;
        TextView tvRepository;
        ImageView imageAvatar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsernameDetail);
            tvFollowers = itemView.findViewById(R.id.tvFollowersDetail);
            tvFollowing = itemView.findViewById(R.id.tvFollowingDetail);
            tvRepository = itemView.findViewById(R.id.tvRepositoryDetail);
            imageAvatar = itemView.findViewById(R.id.imgAvatarDetail);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        holder.tvUsername.setText(users.get(position).getName());
        holder.tvFollowing.setText(users.get(position).getFollowing());
        holder.tvFollowers.setText(users.get(position).getFollowers());
        holder.tvRepository.setText(users.get(position).getRepo());
        Glide.with(holder.itemView.getContext())
                .load(users.get(position).getAvatar())
                .apply(new RequestOptions())
                .into(holder.imageAvatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(users.get(holder.getAdapterPosition()));
            }
        });
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    public FavoriteAdapter(Activity activity) {
        this.activity = activity;
    }
}
