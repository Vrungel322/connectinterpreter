package com.getbewarned.connectinterpreter.managers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.getbewarned.connectinterpreter.interfaces.AuthStateChanged;
import com.getbewarned.connectinterpreter.interfaces.FirebaseValueListener;
import com.getbewarned.connectinterpreter.models.FirebaseTrialMinutes;
import com.getbewarned.connectinterpreter.models.FirebaseVideoCall;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

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
        trialMinutesReference.child(deviceId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataChanged(dataSnapshot);
                currentSnapshot = dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    public void updateLeftMinutes(FirebaseTrialMinutes trialMinutes) {
        DatabaseReference node = trialMinutesReference.child(trialMinutes.getDeviceId());
        node.setValue(trialMinutes);
    }

    public FirebaseVideoCall makeCall(String sessionId, String callName, String reason) {
        FirebaseVideoCall call = new FirebaseVideoCall();
        call.setCallerId(this.callerId);
        call.setCallName(callName);
        call.setStatus(FirebaseVideoCall.Status.NEW.toString());
        call.setTimestampStartUTC(ServerValue.TIMESTAMP);
        call.setSessionId(sessionId);
        call.setReason(reason);

        DatabaseReference newCallNode = videoCallsReference.push();
        newCallNode.setValue(call);

        call.setKey(newCallNode.getKey());

        return call;
    }

    public FirebaseVideoCall makeMissedCall(String callName, String reason) {
        FirebaseVideoCall call = new FirebaseVideoCall();
        call.setCallerId(this.callerId);
        call.setCallName(callName);
        call.setStatus(FirebaseVideoCall.Status.CANCELED.toString());
        call.setTimestampStartUTC(ServerValue.TIMESTAMP);
        call.setTimestampEndUTC(ServerValue.TIMESTAMP);
        call.setReason(reason);

        DatabaseReference newCallNode = videoCallsReference.push();
        newCallNode.setValue(call);

        call.setKey(newCallNode.getKey());

        return call;
    }


    public void cancelCall(final FirebaseVideoCall call) {
        final DatabaseReference callNode = videoCallsReference.child(call.getKey());

        callNode.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    call.setStatus(FirebaseVideoCall.Status.CANCELED.toString());
                    call.setTimestampEndUTC(ServerValue.TIMESTAMP);
                    mutableData.setValue(call);
                    return Transaction.success(mutableData);
                }
                FirebaseVideoCall videoCall = mutableData.getValue(FirebaseVideoCall.class);
                if (!videoCall.getStatus().equals(FirebaseVideoCall.Status.NEW.toString())) {
                    return Transaction.abort();
                }
                videoCall.setStatus(FirebaseVideoCall.Status.CANCELED.toString());
                videoCall.setTimestampEndUTC(ServerValue.TIMESTAMP);
                mutableData.setValue(videoCall);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }

        });
    }

    public DataSnapshot getCurrentSnapshot() {
        return currentSnapshot;
    }

    public void reduceMinutes(String deviceId, final long milliseconds) {
        trialMinutesReference.child(deviceId).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                FirebaseTrialMinutes trialMinutes = mutableData.getValue(FirebaseTrialMinutes.class);
                if (trialMinutes == null) {
                    return Transaction.abort();
                }
                if (trialMinutes.getMinutes() <= 0) {
                    trialMinutes.setMinutes(0);
                    trialMinutes.setExtra(Math.max(trialMinutes.getExtra() - milliseconds, 0));
                } else {
                    trialMinutes.setMinutes(Math.max(trialMinutes.getMinutes() - milliseconds, 0));
                }
                mutableData.setValue(trialMinutes);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    databaseError.toException().printStackTrace();
                }
            }
        });
    }
}