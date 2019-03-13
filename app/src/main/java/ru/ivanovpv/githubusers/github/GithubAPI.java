package ru.ivanovpv.githubusers.github;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.ivanovpv.githubusers.model.User;

public interface GithubAPI {
    @GET("users")
    Call<List<User>> loadUsers();

    @GET("users")
    Call<List<User>> loadUsersSince(@Query("since") int since);

}
