package com.artycake.bewarnedandroidsimplevideocall.managers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artycake.bewarnedandroidsimplevideocall.interfaces.AuthStateChanged;
import com.artycake.bewarnedandroidsimplevideocall.interfaces.FirebaseValueListener;
import com.artycake.bewarnedandroidsimplevideocall.models.FirebaseTrialMinutes;
import com.artycake.bewarnedandroidsimplevideocall.models.FirebaseVideoCall;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by artycake on 10/11/17.
 */

public class FirebaseManager {

    private static final String TAG = FirebaseManager.class.getSimpleName();

    private static final String TRIAL_MINUTES_CHILD = "trial-minutes";
    private static final String VIDEO_CALLS_CHILD = "video-calls";

    private DatabaseReference trialMinutesReference = FirebaseDatabase.getInstance().getReference(TRIAL_MINUTES_CHILD);
    private DatabaseReference videoCallsReference = FirebaseDatabase.getInstance().getReference(VIDEO_CALLS_CHILD);

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AuthStateChanged authStateChanged;

    private String callerId;
    private DataSnapshot currentSnapshot;

    private static FirebaseManager instance;

    public static FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    private FirebaseManager() {
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String userId = null;
                if (user != null) {
                    userId = user.getUid();
                    callerId = user.getUid();
                }
                if (authStateChanged != null) {
                    authStateChanged.onAuthStateChanged(userId);
                }
            }
        };
        auth.addAuthStateListener(authStateListener);
    }

    public void setAuthStateChanged(AuthStateChanged authStateChanged) {
        this.authStateChanged = authStateChanged;
    }

    public void login() {
        auth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                        }
                    }
                });
    }

    public void checkLeftMinutes(String deviceId, final FirebaseValueListener listener) {
        Query trialQuery = trialMinutesReference.orderByChild("deviceId").equalTo(deviceId);
        trialQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listener.onDataChanged(dataSnapshot);
                currentSnapshot = dataSnapshot;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                listener.onDataChanged(dataSnapshot);
                currentSnapshot = dataSnapshot;
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateLeftMinutes(FirebaseTrialMinutes trialMinutes) {
        DatabaseReference node;
        if (trialMinutes.getKey() != null) {
            node = trialMinutesReference.child(trialMinutes.getKey());
        } else {
            node = trialMinutesReference.push();
        }
        node.setValue(trialMinutes);
    }

    public FirebaseVideoCall makeCall(String sessionId) {
        FirebaseVideoCall call = new FirebaseVideoCall();
        call.setCallerId(this.callerId);
        call.setCallName(this.getNewCallName());
        call.setStatus(FirebaseVideoCall.Status.NEW.toString());
        call.setTimestampStart(new Date().getTime());
        call.setSessionId(sessionId);

        DatabaseReference newCallNode = videoCallsReference.push();
        newCallNode.setValue(call);

        call.setKey(newCallNode.getKey());

        return call;
    }


    public void cancelCall(FirebaseVideoCall call) {
        DatabaseReference callNode = videoCallsReference.child(call.getKey());

        call.setStatus(FirebaseVideoCall.Status.CANCELED.toString());
        call.setTimestampEnd(new Date().getTime());

        callNode.setValue(call);
    }

    private String getNewCallName() {
        String date = (new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())).format(new Date());
        return String.format(
                Locale.getDefault(),
                "%s (%s) - %s",
                "test",
                "application",
                date
        );
    }

    public DataSnapshot getCurrentSnapshot() {
        return currentSnapshot;
    }
}
