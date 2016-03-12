package com.dev.routefinder.app;

import com.dev.routefinder.app.R;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RouteFinderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_finder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.route_finder, menu);
        return true;
    }

    private static String getDataFromUrl(String url) {
        String result = null;
        try {
            URL myurl=new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) myurl
                .openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream is=urlConnection.getInputStream();
            if (is != null) {
                StringBuilder sb = new StringBuilder();
                String line;
                try {
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                } finally {
                    is.close();
                }
                result = sb.toString();
            }
        }catch (Exception e){
            result=null;
        }
        return result;
    }

    public void getDirections(String loginUrl) {
        JSONObject jArray = null;
        String result = "";
        String s;
        String s1;
        String s2;

        try {
          result = getDataFromUrl(loginUrl);
//            HttpGet request = new HttpGet(loginUrl);
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpResponse response = httpClient.execute(request);
//            HttpEntity entityResponse = response.getEntity();
//            result = EntityUtils.toString(entityResponse);

        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        try {
            jArray = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
/*
        try {
            JSONArray array = jArray.getJSONArray("routes");
            JSONObject routes = array.getJSONObject(0);
            JSONArray legs = routes.getJSONArray("legs");
            JSONObject steps = legs.getJSONObject(0);
            JSONArray legs1 = steps.getJSONArray("steps");
            for (int i = 0; i < legs1.length(); i++) { JSONObject steps1 = legs1.getJSONObject(i); if (i == 0) s = steps1.getString("start_location"); else s = steps1.getString("end_location"); // Toast.makeText(getApplicationContext(), " "+s, 6000).show(); s1 = s.substring(s.indexOf(":") + 1); // cut off beginning s = s1.substring(s1.indexOf(":") + 1, s1.indexOf("}")); // isolate // longitude s1 = s1.substring(0, s1.indexOf(",")); // isolate latitude s.replaceAll(" ", ""); // remove spaces s1.replaceAll(" ", ""); s2 = steps1.getString("html_instructions"); // Toast.makeText(getApplicationContext(), ""+s2, 4000).show(); source_dir = new Direction(Double.parseDouble(s), Double.parseDouble(s1), s2); directions.add(source_dir); }// for } catch (Exception e) { Log.e("log_tag", "Error converting result " + e.toString()); } }// getdirections public void getDirections_online(String loginUrl) { JSONObject jArray = null; String result = ""; String s; String s1; String s2; // directions.clear(); try { HttpGet request = new HttpGet(loginUrl); HttpClient httpClient = new DefaultHttpClient(); HttpResponse response = httpClient.execute(request); HttpEntity entityResponse = response.getEntity(); result = EntityUtils.toString(entityResponse); } catch (Exception e) { Log.e("log_tag", "Error converting result " + e.toString()); } try { jArray = new JSONObject(result); } catch (JSONException e) { Log.e("log_tag", "Error parsing data " + e.toString()); } try { JSONArray array = jArray.getJSONArray("routes"); JSONObject routes = array.getJSONObject(0); JSONArray legs = routes.getJSONArray("legs"); JSONObject steps = legs.getJSONObject(0); JSONArray legs1 = steps.getJSONArray("steps"); for (int i = 0; i < legs1.length(); i++) { JSONObject steps1 = legs1.getJSONObject(i);
            addresses = gcd.getFromLocation(lat, lng, 5);

            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                String source1 = address.getAddressLine(1);
                source = source1.substring(0, source1.indexOf(",") );

                //Toast.makeText(getApplicationContext(), source, 4000).show();
                // result.append("" + lat + lng);

            }
            } catch (IOException ex) {
                // editTextShowLocation.append(ex.getMessage().toString());
            } catch (NullPointerException f) {

            }*/
    }

}
