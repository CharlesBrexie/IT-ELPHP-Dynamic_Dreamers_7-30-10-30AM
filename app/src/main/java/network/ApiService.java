package network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// Define the response data classes
class ApiResponse {
    String message;
}

public interface ApiService {

    @POST("/register")
    Call<ApiResponse> registerUser(@Body RegisterRequest request);

    @POST("/login")
    Call<ApiResponse> loginUser(@Body LoginRequest request);
}
