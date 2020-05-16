package com.jaguarsoft.mac.favouriteplaces.backend_sdk;

import com.jaguarsoft.mac.favouriteplaces.backend_sdk.model.LocationVote;
import com.jaguarsoft.mac.favouriteplaces.backend_sdk.model.Status;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface FavoritePlacesWebService {

    @GET("/get-ratings")
    Call<LocationVote[]> getRatings();

    @POST("/put-rating")
    Call<Status> putRating(@Body LocationVote locationVote);

}
