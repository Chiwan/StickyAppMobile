package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;



/**
 * Created by kha on 14/02/16.
 */
@Deprecated
public class HttpUpload extends AsyncTask<Void, Integer, Void> {

    private Context context;
    private String imgPath;

    private HttpClient client;


    //private String url = "http://10.188.122.135:8000/api/wall/56af3555fe6c34e91d435f3c/postit";
    private String url;
    public HttpUpload(Context context, String imgPath, String idWall) {
        super();
        this.context = context;
        this.imgPath = imgPath;
        this.url = "http://10.188.122.135:8000/api/wall/"+idWall+"/postit";
    }

    @Override
    protected void onPreExecute() {
        //Set timeout parameters
        int timeout = 10000;
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
        HttpConnectionParams.setSoTimeout(httpParameters, timeout);

        //We'll use the DefaultHttpClient
        client = new DefaultHttpClient(httpParameters);
        /*
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("Uploading Picture...");
        pd.setCancelable(false);
        pd.show();*/
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            File file = new File(imgPath);

            //Create the POST object
            HttpPost post = new HttpPost(url);

            post.setHeader(null);
            //Create the multipart entity object and add a progress listener
            //this is a our extended class so we can know the bytes that have been transfered
            /*
            MultipartEntity entity = new MyMultipartEntity(new ProgressListener()
            {


                @Override
                public void transferred(long num)
                {
                    //Call the onProgressUpdate method with the percent completed
                    publishProgress((int) ((num / (float) totalSize) * 100));
                    Log.d("DEBUG", num + " - " + totalSize);
                }
            });
            */
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            //Add the file to the content's body
            if(file.exists()) {
                FileBody cbFile = new FileBody(file);
                entity.addPart("file", cbFile);
            }
            else {
                System.out.println( "File " + imgPath.toString() + " doesn't exist!");
            }

            StringBody height = new StringBody("450", ContentType.MULTIPART_FORM_DATA);
            StringBody width = new StringBody("300", ContentType.MULTIPART_FORM_DATA);

            entity.addPart("height",height);
            entity.addPart("width",width);

            HttpEntity builder = entity.build();
            //After adding everything we get the content's lenght
            //totalSize = entity.getContentLength();

            //We add the entity to the post request
            post.setEntity(builder);

            //Execute post request
            HttpResponse response = client.execute( post );
            int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode == HttpStatus.SC_OK){
                //If everything goes ok, we can get the response
                String fullRes = EntityUtils.toString(response.getEntity());
                Log.d("DEBUG", fullRes);

            } else {
                Log.d("DEBUG", "HTTP Fail, Response Code: " + statusCode);
            }

        } catch (ClientProtocolException e) {
            // Any error related to the Http Protocol (e.g. malformed url)
            e.printStackTrace();

        } catch (IOException e) {
            // Any IO error (e.g. File not found)
            e.printStackTrace();

        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        //Set the pertange done in the progress dialog
        //pd.setProgress((int) (progress[0]));
    }

    @Override
    protected void onPostExecute(Void result) {
        //Dismiss progress dialog
        //pd.dismiss();
        Toast toast = Toast.makeText(context,"Envoyé", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        ((Activity)context).finish();
    }
}
