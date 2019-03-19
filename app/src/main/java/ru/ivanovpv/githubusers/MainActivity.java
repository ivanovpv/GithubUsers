package ru.ivanovpv.githubusers;

import android.arch.paging.PagedList;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ivanovpv.githubusers.github.GithubDataSource;
import ru.ivanovpv.githubusers.model.User;
import ru.ivanovpv.githubusers.presenter.IFetchable;
import ru.ivanovpv.githubusers.view.IImageLoader;
import ru.ivanovpv.githubusers.presenter.IPresenter;
import ru.ivanovpv.githubusers.view.UserListAdapter;

public class MainActivity extends AppCompatActivity implements IFetchable<User>, IImageLoader {
    private static final Logger logger= LoggerFactory.getLogger(MainActivity.class);
    @Inject
    UserListAdapter adapter;
    @Inject
    IPresenter presenter;

    @BindView(R.id.recycler_view )
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Me.getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter.fetchList(this);
    }

    @Override
    public void submitList(PagedList<User> list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.submitList(list);
    }

    @Override
    public void setFailureObserver(GithubDataSource dataSource) {
        dataSource.getRequestFailureLiveData().observe(this, requestFailure -> {
            if (requestFailure == null) return;

            Snackbar.make(this.findViewById(android.R.id.content), requestFailure.getErrorMessage(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", view -> {
                        // Retry the failed request
                        requestFailure.getRetryable().retry();
                    }).show();
        });
    }

    @Override
    public void onClickListItem(User user) {
        if(user!=null && (user.getPageUrl()!=null || !user.getPageUrl().trim().isEmpty())) {
            logger.info("User with id=" + user.getId() + " - click detected!");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(user.getPageUrl()));
            this.startActivity(browserIntent);
        }
    }

    public IImageLoader getImageLoader() {
        return this;
    }

    @Override
    public void initLoader() {
        //nothing so far
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        Picasso.with(this).load(url).into(imageView);
    }
}
