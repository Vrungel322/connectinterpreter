package com.artycake.bewarnedandroidsimplevideocall.interfaces;

import com.artycake.bewarnedandroidsimplevideocall.models.OpenTokTokenResponse;

/**
 * Created by artycake on 10/11/17.
 */

public interface TokenReceived {
    void onTokenReceived(OpenTokTokenResponse response);

    void onErrorReceived(Error error);
}
