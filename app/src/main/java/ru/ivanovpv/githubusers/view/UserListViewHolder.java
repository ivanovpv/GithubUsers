package ru.ivanovpv.githubusers.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.ivanovpv.githubusers.R;
import ru.ivanovpv.githubusers.model.User;
import ru.ivanovpv.githubusers.presenter.IPresenter;
import ru.ivanovpv.githubusers.presenter.IUserRowView;

public class UserListViewHolder extends RecyclerView.ViewHolder implements IUserRowView {
    private static final Logger logger= LoggerFactory.getLogger(UserListViewHolder.class);
    @BindView(R.id.row_layout)
    ViewGroup rowLayout;
    @BindView(R.id.user_id)
    TextView userId;
    @BindView(R.id.user_login)
    TextView login;
    @BindView(R.id.user_avatar)
    ImageView userAvatar;
    private IPresenter presenter;

    private User user;


    private UserListViewHolder(View view, IPresenter presenter) {
        super(view);
        ButterKnife.bind(this, view);
        this.presenter=presenter;
    }

    public static UserListViewHolder create(ViewGroup parent, IPresenter presenter) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserListViewHolder(view, presenter);
    }

    public void bindTo(User user) {
        this.user=user;
    }

    @OnClick(R.id.row_layout)
    public void onClickRowLayout(View view) {
        presenter.onClickListItem(user);
    }

    @Override
    public void setUserId() {
        if (user != null)
            userId.setText(""+user.getId());
        else
            userId.setText("N/A");
    }

    @Override
    public void setAvatar() {
        if (user != null)
            presenter.getFetchable().getImageLoader().loadImage(user.getAvatarUrl(), userAvatar);
        else
            presenter.getFetchable().getImageLoader().loadImage(null, userAvatar);
    }

    @Override
    public void setLogin() {
        if (user != null)
            login.setText(""+user.getLogin());
        else
            login.setText("N/A");
    }
}
