package ec.com.innovasystem.sintonizados.http;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import ec.com.innovasystem.sintonizados.R;

/**
 * Creado por Andres Cantos el 13/11/2015
 */
public class BaseActivity extends AppCompatActivity {
    String ced;
    RecursoVolley objvolley;
    RequestQueue colaPeticioneshttp;
    Toolbar toolbar;
    ProgressDialog barraprogreso;
    //private PreferenciasDD preferenciadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    preferenciadd = new PreferenciasDD(this);
       /* int mostrarbotonatras =1;
        int mapeartoolbar = 1;
        mostrarbotonatras = savedInstanceState.getInt("verbtnatras",1);
        mapeartoolbar = savedInstanceState.getInt("vertoolbar",1);
*/
        setContentView(savedInstanceState.getInt("layout"));
        objvolley = RecursoVolley.getInstancia(this);
        colaPeticioneshttp = objvolley.getColaVolley();

        /*if(mapeartoolbar==1) {

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            //toolbar.setSubtitle("Ingreso Factura");
            setSupportActionBar(toolbar);

            if(mostrarbotonatras==1)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            else
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        }
*/
    }

    public void agregarPeticionHttp(RequestApp request) {
        HttpsTrustManager.allowAllSSL();
        Log.i("https","Si se ejecuto.");
        if (request != null) {
            request.setTag(this);
            if (colaPeticioneshttp == null)
                colaPeticioneshttp = objvolley.getColaVolley();
           // request.setRetryPolicy(new DefaultRetryPolicy(60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            onPreStartConnection();
            colaPeticioneshttp.add(request);
        }
    }

    public void agregarPeticionHttpSinLoad(RequestApp request) {
        HttpsTrustManager.allowAllSSL();
        if (request != null) {
            request.setTag(this);
            if (colaPeticioneshttp == null)
                colaPeticioneshttp = objvolley.getColaVolley();
            //request.setRetryPolicy(new DefaultRetryPolicy(60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            //onPreStartConnection();
            colaPeticioneshttp.add(request);
        }
    }

    public void onPreStartConnection() {
        //this.setSupportProgressBarIndeterminateVisibility(true);
        if(barraprogreso==null) barraprogreso = new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
        barraprogreso.show();
        barraprogreso.setContentView(R.layout.progressdialogeneral);
        barraprogreso.setCancelable(false);
        //barraprogreso.setProgressStyle(Widge);
    }

    public void onConnectionFinished() {
        //this.setProgressBarIndeterminateVisibility(false);
        if(barraprogreso!=null) barraprogreso.dismiss();
    }

    public void onConnectionFailed(String error) {
        //this.setProgressBarIndeterminateVisibility(false);
        if(barraprogreso!=null) barraprogreso.dismiss();
        Log.e("multicash", "error volley: " + error);
        Toast.makeText(this,"Sin Internet", Toast.LENGTH_SHORT).show();
    }



    public void onConnectionFailed() {
        //this.setProgressBarIndeterminateVisibility(false);
        if(barraprogreso!=null) barraprogreso.dismiss();
        Toast.makeText(getApplicationContext(),"Sin Internet",Toast.LENGTH_SHORT).show();
    }

   /*public PreferenciasDD getPreferenciacb() {
        return preferenciadd;
    }*/

}
