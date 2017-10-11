package com.artycake.bewarnedandroidsimplevideocall.interfaces;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by artycake on 10/11/17.
 */

public interface FirebaseValueListener {
    void onDataChanged(DataSnapshot dataSnapshot);
}
