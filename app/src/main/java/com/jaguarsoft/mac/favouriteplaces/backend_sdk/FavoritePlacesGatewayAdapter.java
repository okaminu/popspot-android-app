package com.jaguarsoft.mac.favouriteplaces.backend_sdk;

import com.jaguarsoft.mac.favouriteplaces.backend_sdk.model.LocationVote;
import com.jaguarsoft.mac.favouriteplaces.backend_sdk.model.Status;

import java.io.IOException;

public class FavoritePlacesGatewayAdapter {

    public LocationVote[] getRatings() throws IOException {
        return new RetrofitFactory().create(FavoritePlacesWebService.class).getRatings().execute().body();
    }

    public Status putRating(LocationVote locationVote) throws IOException {
        return new RetrofitFactory().create(FavoritePlacesWebService.class).putRating(locationVote).execute().body();
    }

}
