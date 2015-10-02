/*
 * Copyright 2010-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.jaguarsoft.mac.favouriteplaces.backend_sdk;

import com.amazonaws.mobileconnectors.apigateway.annotation.Operation;
import com.amazonaws.mobileconnectors.apigateway.annotation.Service;

import com.jaguarsoft.mac.favouriteplaces.backend_sdk.model.LocationVote;

//@Service(endpoint = "https://9v0rdn68rk.execute-api.eu-west-1.amazonaws.com/v1")
@Service(endpoint = "http://192.168.0.100:8080")
public interface FavoritePlacesClient {
    
    /**
     * 
     * 
     * @return Empty
     */
    @Operation(path = "/get-ratings", method = "GET")
    LocationVote[] getRatingsGet();

    /**
     *
     *
     * @return Empty
     */
    @Operation(path = "/put-rating", method = "POST")
    void putRatingPost(LocationVote locationVote);
    
}
