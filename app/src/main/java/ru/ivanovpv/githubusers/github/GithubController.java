package ru.ivanovpv.githubusers.github;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.ivanovpv.githubusers.model.User;

public class GithubController {

    private static final Logger logger= LoggerFactory.getLogger(GithubController.class);
    private GithubAPI githubAPI;
    private static final String BASE_URL = "https://api.github.com/";

    @Inject
    public GithubController() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        githubAPI = retrofit.create(GithubAPI.class);
    }


    public void start(Callback<List<User>> callback) {
        Call<List<User>> call = githubAPI.loadUsers();
        call.enqueue(callback);
    }

    public void startSince(Callback<List<User>> callback, int id) {
        Call<List<User>> call = githubAPI.loadUsersSince(id);
        call.enqueue(callback);
    }
}