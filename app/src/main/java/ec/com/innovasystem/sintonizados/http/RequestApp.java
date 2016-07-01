package ec.com.innovasystem.sintonizados.http;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Creado por Andres Cantos el 13/noviembre/2015
 */
public class RequestApp extends JsonObjectRequest {


    public RequestApp(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public RequestApp(int metodo,String url,JSONObject jsonrequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(metodo,url,jsonrequest,listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put(
                "Authorization",
                String.format("Basic %s", Base64.encodeToString(
                        String.format("%s:%s", "WsIrcAppMo", "pruebasapp").getBytes(), Base64.DEFAULT)));
                Log.i("Authorization", ""+ String.format("Basic %s", Base64.encodeToString(String.format("%s:%s", "WsIrcAppMo", "pruebasapp").getBytes(), Base64.DEFAULT)));
        return params;
        /*
        return super.getHeaders();
        */
    }





}
