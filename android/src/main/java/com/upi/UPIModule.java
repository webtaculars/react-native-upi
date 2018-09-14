package com.upi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class UPIModule extends ReactContextBaseJavaModule  {

  private static final int UPI_CLIENT_REQUEST = 100;

  private Promise mUPIPromise;

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      if(requestCode == UPI_CLIENT_REQUEST) {
        if (mUPIPromise != null) {
          if (resultCode == Activity.RESULT_CANCELED) {
            mUPIPromise.resolve("UPI client was cancelled");
          } else if (resultCode == Activity.RESULT_OK) {
            if (intent == null) {
              mUPIPromise.resolve("No respose from UPI client");
            } else {
              String res = intent.getStringExtra("response");
              mUPIPromise.resolve(res);
            }
          }
          mUPIPromise = null;
        }
      }
    }
  };

  public UPIModule(ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addActivityEventListener(mActivityEventListener);
  }

  @Override
  public String getName() {
      return "UPIModule";
  }

  @ReactMethod
  public void openUPIClient(String url, final Promise promise) {
    Activity currentActivity = getCurrentActivity();

    if (currentActivity == null) {
      promise.reject("ACTIVITY_DOES_NOT_EXIST", "Activity does not exist");
      return;
    }

    // Store the promise to resolve/reject when client returns data
    mUPIPromise = promise;

    try {
      Uri uri = Uri.parse(url);
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);

      //start activity with mentioned intent and as we need result we will use startActivityForResult
      currentActivity.startActivityForResult(intent, UPI_CLIENT_REQUEST);
    } catch (Exception e) {
      mUPIPromise.reject("FAILED_TO_SHOW_UPI_CLIENT", e);
      mUPIPromise = null;
    }
  }

};
