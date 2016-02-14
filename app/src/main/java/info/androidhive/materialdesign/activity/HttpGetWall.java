package info.androidhive.materialdesign.activity;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import info.androidhive.materialdesign.model.ListViewWall;

/**
 * Created by kha on 14/02/16.
 */
public class HttpGetWall extends AsyncTask<Void, Integer, Void> {

    private final HomeFragment fragment;
    private String url;
    private ArrayList<ListViewWall> listWall;

    public HttpGetWall(Fragment fragment) {
        super();
        this.fragment = (HomeFragment)fragment;
        this.url = "http://10.188.122.135:8000/api/walls";
    }


    @Override
    protected void onPreExecute() {
        Log.i("add", "onPreExecute");
        super.onPreExecute();
        listWall = new ArrayList<>();
    }
    @Override
    protected Void doInBackground(Void... params) {

        String readJSON = getJSON(url);
        //System.out.println(readJSON);
        try{
            JSONObject jsonObject = new JSONObject(readJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("walls");
            for(int i=0; i<jsonArray.length();i++){
                JSONObject values = jsonArray.getJSONObject(i);
                String name = values.getString("name");
                String id = values.getString("_id");
                String date = values.getString("date");
                System.out.println(id +" "+ name);
                listWall.add(new ListViewWall(id,name,date));
            }
            Log.i(MainActivity.class.getName(), jsonObject.getString("walls"));
            fragment.setListWall(listWall);
        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }

    public String getJSON(String address){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(address);
        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
            } else {
                Log.e(MainActivity.class.toString(), "Failed et JSON object");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return builder.toString();
    }

}
