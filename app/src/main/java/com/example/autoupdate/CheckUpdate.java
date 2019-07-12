package com.example.autoupdate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class CheckUpdate {

  Context context;
  ProgressDialog updateCheckerProgressDialog;
  double responseVersion = -1.0;
  double currentAppVersion = -1.0;

  CheckUpdate(Context context) {
    this.context = context;
    initialize();
  }

  public void checkUpdates() {  // Call this method.
    try {
      new UpdateCheckTask().execute();
    } catch (Exception e) {
      Log.e("Exception", Log.getStackTraceString(e));
    }
  }

  private void initialize() {
    updateCheckerProgressDialog = new ProgressDialog(context);
    updateCheckerProgressDialog.setMessage("Checking new Updates...");
    updateCheckerProgressDialog.setIndeterminate(true);
    updateCheckerProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    updateCheckerProgressDialog.setCancelable(false);
    getCurrentAppVersion();
  }

  private void serverRequest() {
    String url = "http://cemonel.github.io/pepperV.json";
    RequestQueue queue = Volley.newRequestQueue(context);

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
      (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
          try {
            JSONArray responseArray = response.getJSONArray("pepperVersion");

            System.out.println(responseArray.
              getJSONObject(0).
              getInt("version"));

            setResponseVersion((double) responseArray.getJSONObject(0).getInt("version"));

          } catch (Exception e) {
            //TODO handle this exception
          }
        }
      }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          setResponseVersion(1);
          AlertDialog.Builder connectionAlert = new AlertDialog.Builder(context);
          connectionAlert.setTitle("Connection Error");
          connectionAlert.setMessage("Error occurred while checking updates. Try again later.");
          connectionAlert.setCancelable(false);
          connectionAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
          });
          connectionAlert.show();
        }
      });

    queue.add(jsonObjectRequest);
  }

  public void setResponseVersion(double response) {
    this.responseVersion = response;
  }

  private void getCurrentAppVersion() {
    PackageManager manager = context.getPackageManager();
    try {
      PackageInfo info = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
      currentAppVersion = (double) info.versionCode;

    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  public class UpdateCheckTask extends AsyncTask<Void, Void, Void> {  // don't need async actually

    @Override
    protected Void doInBackground(Void... Void) {
      serverRequest();
      while (responseVersion == -1) ; // Wait response to set positive
      return null;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      updateCheckerProgressDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      updateCheckerProgressDialog.dismiss();
      if (responseVersion > currentAppVersion && currentAppVersion != -1.0) {
        System.out.println("Update will start.");
        System.out.printf("responseVersion: %f --- currentAppVersion: %f", responseVersion, currentAppVersion );
        new DownloadUpdate2(context);
      } else {
        //TODO show no new updates alert
        System.out.println("No new updates.");
      }
    }
  }
}

