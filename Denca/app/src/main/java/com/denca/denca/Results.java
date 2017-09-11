package com.denca.denca;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.ContentValues.TAG;

/**
 * Created by mdnah on 9/9/2017.
 */

public class Results extends Activity {


    private static final String BASE_URL = "http://23cd4f62.ngrok.io";
    private ProgressDialog progressDialog;
    private TextView percentage;
    private TextView doneBtn;
    private ListView rightWrongList;

    private ArrayList<RightWrong> rightWrongs;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            String path = getIntent().getStringExtra("path");

            File myFile = new File(path);
            RequestParams params = new RequestParams();
            try {
                params.put("file", myFile);
            } catch(FileNotFoundException e) {
                Log.d("Crash", e.getMessage());
            }


            AsyncHttpClient client = new AsyncHttpClient();
            client.post(BASE_URL, params, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try{
                        JSONArray array = response.getJSONArray("rightWrongs");
                        for(int i=0; i<array.length(); i++){
                            rightWrongs.add(new RightWrong(array.getJSONObject(i).getString("right"), array.getJSONObject(i).getString("wrong")));
                        }

                        RightWrongAdapter rightWrongAdapter = new RightWrongAdapter();
                        rightWrongList.setAdapter(rightWrongAdapter);
                    } catch (JSONException e){
                        Log.d("Crash", e.getMessage());
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("Crash", Integer.toString(statusCode));
                    Log.d("Crash", responseString);
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });

        }
    }





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        percentage = (TextView) findViewById(R.id.percentage);
        doneBtn = (TextView) findViewById(R.id.doneBtn);
        rightWrongList = (ListView) findViewById(R.id.rightWrongList);

        rightWrongs = new ArrayList<>();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Results.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



//        upLoad2Server(path);

        verifyStoragePermissions(this);

    }




    private class RightWrongAdapter extends ArrayAdapter<RightWrong> {

        public RightWrongAdapter() {
            super(getApplicationContext(), R.layout.right_wrong_item, rightWrongs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final RightWrong rightWrong = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.right_wrong_item, parent, false);
            }


            ImageView rightImg = (ImageView) convertView.findViewById(R.id.rightImg);
            ImageView leftImg = (ImageView) convertView.findViewById(R.id.leftImg);

            Picasso.with(Results.this).load(rightWrong.getRightUrl()).into(rightImg);
            Picasso.with(Results.this).load(rightWrong.getWrongUrl()).into(leftImg);

            return convertView;
        }

    }




}
