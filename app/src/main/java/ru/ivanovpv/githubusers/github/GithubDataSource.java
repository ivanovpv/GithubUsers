package ru.ivanovpv.githubusers.github;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ivanovpv.githubusers.tools.RequestFailure;
import ru.ivanovpv.githubusers.tools.Retryable;
import ru.ivanovpv.githubusers.model.User;

public class GithubDataSource extends PageKeyedDataSource<Integer, User> {
    private static final Logger logger= LoggerFactory.getLogger(GithubDataSource.class);
    private final MutableLiveData<RequestFailure> requestFailureLiveData;

    @Inject
    GithubController controller;

    @Inject
    public GithubDataSource() {
        this.requestFailureLiveData = new MutableLiveData<>();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, User> callback) {
        controller.start(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()) {
                    List<User> usersList = response.body();
                    usersList.forEach(user -> logger.info("Id="+user.getId()+", login="+user.getLogin()+", avatar="+user.getAvatarUrl()));
                    callback.onResult(
                            usersList, // List of users
                            null,
                            usersList.get(usersList.size()-1).getId()
                    );
                } else {
                    logger.error("Error="+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                logger.error("Failure detected", t);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, User> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, User> callback) {
        controller.startSince(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()) {
                    List<User> usersList = response.body();
                    usersList.forEach(user -> logger.info("Id="+user.getId()+", login="+user.getLogin()+", avatar="+user.getAvatarUrl()));
                    callback.onResult(
                            usersList, // List of users
                            usersList.get(usersList.size()-1).getId()
                    );
                } else {
                    logger.error("Error="+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                logger.error("Failure detected", t);
            }
        }, params.key);
    }

    public LiveData<RequestFailure> getRequestFailureLiveData() {
        return requestFailureLiveData;
    }

    private void handleError(Retryable retryable, Throwable t) {
        requestFailureLiveData.postValue(new RequestFailure(retryable, t.getMessage()));
    }

}
