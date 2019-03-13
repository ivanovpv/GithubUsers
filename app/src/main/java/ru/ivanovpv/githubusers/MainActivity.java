package ru.ivanovpv.githubusers;

import android.arch.lifecycle.Observer;
import android.arch.paging.PagedList;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import ru.ivanovpv.githubusers.github.GithubController;
import ru.ivanovpv.githubusers.github.GithubDataSource;
import ru.ivanovpv.githubusers.model.User;
import ru.ivanovpv.githubusers.tools.MainThreadExecutor;
import ru.ivanovpv.githubusers.tools.RequestFailure;
import ru.ivanovpv.githubusers.view.UserListAdapter;

public class MainActivity extends AppCompatActivity {
    @Inject
    GithubController githubController;
    @Inject
    MainThreadExecutor executor;
    @Inject
    UserListAdapter adapter;
    @Inject
    GithubDataSource dataSource;


    @BindView(R.id.recycler_view )
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        initImageLoader();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDataSource();
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    private void initDataSource() {

        // Configure paging
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(true) // Show empty views until data is available
                .build();

        // Build PagedList
        PagedList<User> list =
                new PagedList.Builder<>(dataSource, config) // Can pass `pageSize` directly instead of `config`
                        // Do fetch operations on the main thread. We'll instead be using Retrofit's
                        // built-in enqueue() method for background api calls.
                        .setFetchExecutor(executor)
                        // Send updates on the main thread
                        .setNotifyExecutor(executor)
                        .build();

        // Required only once. Paging will handle fetching and updating the list.
        adapter.submitList(list);

        dataSource.getRequestFailureLiveData().observe(this, requestFailure -> {
            if (requestFailure == null) return;

            Snackbar.make(findViewById(android.R.id.content), requestFailure.getErrorMessage(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", view -> {
                        // Retry the failed request
                        requestFailure.getRetryable().retry();
                    }).show();
        });
    }

    private void initImageLoader() {
        final DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(android.R.drawable.stat_sys_download)
                .showImageForEmptyUri(android.R.drawable.ic_dialog_alert)
                .showImageOnFail(android.R.drawable.stat_notify_error)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) //filled width
                .build();

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
