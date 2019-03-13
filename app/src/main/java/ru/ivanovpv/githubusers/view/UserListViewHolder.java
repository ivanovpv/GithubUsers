package ru.ivanovpv.githubusers.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.ivanovpv.githubusers.R;
import ru.ivanovpv.githubusers.model.User;

public class UserListViewHolder extends RecyclerView.ViewHolder {
    private static final Logger logger= LoggerFactory.getLogger(UserListViewHolder.class);
    @BindView(R.id.row_layout)
    ViewGroup rowLayout;
    @BindView(R.id.user_id)
    TextView userId;
    @BindView(R.id.user_login)
    TextView login;
    @BindView(R.id.user_avatar)
    ImageView userAvatar;

    private User user;


    private UserListViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public static UserListViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserListViewHolder(view);
    }

    public void bindTo(User user) {
        if (user != null) {
            this.user=user;
            userId.setText(""+user.getId());
            login.setText(user.getLogin());
            asyncLoadImage(user.getAvatarUrl(), userAvatar);
        } else {
            userId.setText("N/A");
            login.setText("N/A");
            asyncLoadImage(null, userAvatar);
        }
    }

    @OnClick(R.id.row_layout)
    public void onClickRowLayout(View view) {
        if(user!=null && (user.getPageUrl()!=null || !user.getPageUrl().trim().isEmpty())) {
            logger.info("User with id=" + user.getId() + " - click detected!");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(user.getPageUrl()));
            view.getContext().startActivity(browserIntent);
        }
    }

    private void asyncLoadImage(String url, final ImageView imageView) {
        ImageLoader.getInstance().displayImage(url, imageView);
    }

}
