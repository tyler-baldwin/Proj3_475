package com.example.proj3_475;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private Context ctx;
    private LayoutInflater layoutInflater;
    ArrayList<pet> pets ;
    private JSONArray jsonArray;

    String info_choice = "CNU - Defender";
    String URL = "https://www.pcs.cnu.edu/~kperkins/pets/";

    class RViewHolder extends RecyclerView.ViewHolder {
        private static final int UNINITIALIZED = -1;
        ImageView iv;
        TextView tv;
        int position = UNINITIALIZED;     //start off uninitialized, set it when we are populating
        //with a view in onBindViewHolder

        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.imgView);
            tv = (TextView) itemView.findViewById(R.id.errorText);
        }
    }

    private class DownloadIMG extends AsyncTask<String, Void, Bitmap> {
        private RViewHolder myVh;
        //since myVH may be recycled and reused
        //we have to verify that the result we are returning
        //is still what the viewholder wants
        private int original_position;

        private String TAG = "DownloadImage";
        private int statusCode = 0;
        private static final int DEFAULTBUFFERSIZE = 50;
        private static final int NODATA = -1;

        public DownloadIMG(RViewHolder myVh) {
            //hold on to a reference to this viewholder
            //note that its contents (specifically iv) may change
            //iff the viewholder is recycled
            this.myVh = myVh;

            //make a copy to compare later, once we have the image
            this.original_position = myVh.position;
        }

        @Override
        protected Bitmap doInBackground(String... param) {
            //TODO should this really be img
//            Toast.makeText(ctx.getApplicationContext(), img[0], android.widget.Toast.LENGTH_SHORT).show();

            Log.w(TAG, param[0]);
            return downloadBitmap(param[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // super.onPostExecute(result);
            //got a result, if the following are NOT equal
            // then the view has been recycled and is being used by another
            // number DO NOT MODIFY
            if (this.myVh.position == this.original_position) {
                myVh.iv.setImageBitmap(result);
            }
        }

        public Bitmap downloadBitmap(String downloadURL) {
            try {
                java.net.URL url1 = new URL(downloadURL);
                // this does no network IO
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                // this opens a connection, then sends GET & headers
                connection.connect();
                statusCode = connection.getResponseCode();
                if (statusCode / 100 != 2) {
                    Log.e(TAG, "Error-connection.getResponseCode returned " + Integer.toString(statusCode));
                    return null;
                }
                InputStream is = connection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                // the following buffer will grow as needed
                ByteArrayOutputStream baf = new ByteArrayOutputStream(DEFAULTBUFFERSIZE);
                int current = 0;
                // wrap in finally so that stream bis is sure to close
                try {
                    while ((current = bis.read()) != NODATA) {
                        baf.write((byte) current);
                    }
                    // convert to a bitmap
                    byte[] imageData = baf.toByteArray();
                    return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                } finally {
                    // close resource no matter what exception occurs
                    bis.close();
                }
            } catch (Exception exc) {
                return null;
            }
        }


    }

    public RecyclerViewAdapter(Context ctx) {
        this.ctx = ctx;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //call this when we need to create a brand new PagerViewHolder
        View view = layoutInflater.inflate(R.layout.swipelayout, parent, false);
        return new RViewHolder(view);   //the new one
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //passing in an existing instance, reuse the internal resources
        //pass our data to our ViewHolder.
        RViewHolder viewHolder = (RViewHolder) holder;

        //set to some default image
        viewHolder.iv.setImageResource(R.drawable.error404);
        viewHolder.tv.setText("Image : " + position);
        viewHolder.position = position;       //remember which image this view is bound to

        //launch a thread to 'retreive' the image

//        if (pets.size() != 0) {
//            runDownloadImage(viewHolder, pets.get(position).file);
//        }
//        DownloadJSON task1 = new DownloadJSON(URL);
//        task1.execute();
        runDownloadJSON();
        if (pets!= null) {
            runDownloadImage((RViewHolder) holder, pets.get(position).file);
        } else {
            Toast.makeText(ctx.getApplicationContext(), "fuck a duck", android.widget.Toast.LENGTH_SHORT).show();
        }
//        DownloadIMG myTask = new DownloadIMG(new String[]{"p0.png"},viewHolder);
//        myTask.execute(new String[]{URL});
    }

    public void runDownloadImage(@NonNull RViewHolder holder, String imageFile) {
        String fullImageURL = URL + imageFile;
        new DownloadIMG(holder).execute(new String[]{fullImageURL});
    }



    @Override
    public int getItemCount() {
        return 10;
    }

    public void runDownloadJSON() {
        String fullJSONURL = URL + "pets.json";
        Toast.makeText(ctx.getApplicationContext(), fullJSONURL, android.widget.Toast.LENGTH_LONG).show();
        new DownloadJSON().execute(fullJSONURL);
    }

    private class DownloadJSON extends AsyncTask<String, Void, String> {

        private String myURL;
        private int statusCode = 0;
        private String myQuery = "";
        private String TAG = "DownloadJSON";

        @Override
        protected String doInBackground(String... strings) {
            myURL = strings[0];

            try {
                URL url = new URL(myURL);
                // this does no network IO
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // wrap in finally so that stream bis is sure to close
                // and we disconnect the HttpURLConnection
                BufferedReader in = null;
                try {
                    // this opens a connection, then sends GET & headers
                    connection.connect();
                    // lets see what we got make sure its one of
                    // the 200 codes (there can be 100 of them
                    // http_status / 100 != 2 does integer div any 200 code will = 2
                    statusCode = connection.getResponseCode();
                    if (statusCode / 100 != 2) {
                        Log.e(TAG, "Error-connection.getResponseCode returned "
                                + Integer.toString(statusCode));
                        return null;
                    }

                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()), 8096);

                    // the following buffer will grow as needed
                    String myData;
                    StringBuffer sb = new StringBuffer();

                    while ((myData = in.readLine()) != null) {
                        sb.append(myData);
                        Log.w("Data", myData);
                    }
                    return sb.toString();

                } finally {
                    // close resource no matter what exception occurs
                    in.close();
                    connection.disconnect();
                }
            } catch (Exception exc) {
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String jsonArray) {
            Toast.makeText(ctx.getApplicationContext(), "post" + jsonArray, android.widget.Toast.LENGTH_SHORT).show();
            processJSON(jsonArray);
            super.onPostExecute(jsonArray);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    public void processJSON(String string) {
        try {
            Toast.makeText(ctx.getApplicationContext(), "process" + string, android.widget.Toast.LENGTH_SHORT).show();

            pets.clear();
            JSONObject jsonObject = new JSONObject(string);
            jsonArray = jsonObject.getJSONArray("pets");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String name = object.getString("name");
                String file = object.getString("file");

                pets.add(new pet(name, file));
            }
        } catch (Exception e) {
            Log.e("Error", "JSON error");
            e.printStackTrace();
        }
    }

    class pet {
        public String name;
        public String file;

        public pet(String name, String file) {
            this.name = name;
            this.file = file;
        }
    }


}



