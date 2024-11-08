package network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// Retrofit interface for the API
public interface RetrofitInterface {

    // POST request to login user
    @POST("/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}
