package ru.ivanovpv.githubusers.view;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.ViewGroup;

import javax.inject.Inject;

import ru.ivanovpv.githubusers.model.User;

public class UserListAdapter extends PagedListAdapter<User, UserListViewHolder> {

    @Inject
    UserListAdapter() {
        super(UserListAdapter.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return UserListViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    // Allows the adapter to calculate the difference between the old list and new list. This also simplifies animations.
    public static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {

        // Check if items represent the same thing.
        @Override
        public boolean areItemsTheSame(User oldItem, User newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        // Checks if the item contents have changed.
        @Override
        public boolean areContentsTheSame(User oldItem, User newItem) {
            return true; // Assume Repository details don't change
        }
    };
}
