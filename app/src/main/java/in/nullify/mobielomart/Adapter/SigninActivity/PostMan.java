package in.nullify.mobielomart.Adapter.SigninActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import in.nullify.mobielomart.Activity.SigninActivity.SignInActivity;


/**
 * Created by Aswin on 22-05-2018.
 */



/**
 * Created by Aswin on 22-05-2018.
 */
public class PostMan extends AsyncTask<String, Void, String> {
    SharedPreferences sharedPref;
    private String CallerPM;
    Context context;
    public PostMan(Context context){
        this.context= context;

    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... arg0) {

        try {

            URL url = new URL(arg0[0]);
            int i;

            JSONObject postDataParams = new JSONObject();
            for (i=1;i<arg0.length;i=i+2)
                postDataParams.put(arg0[i], arg0[i+1]);//post cheyyanda values ex: post..("email","a@.com")
            Log.e("params", postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            } else {
                return new String("false : " + responseCode);
            }
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        final SharedPreferences mShared = PreferenceManager.getDefaultSharedPreferences(this.context);
        CallerPM = (mShared.getString("CallerPM", "Nothing Selected"));
        if(CallerPM.equals("signinactiity")) {
            SignInActivity signInActivity=new SignInActivity();
            signInActivity.SignUpResult(result);
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
