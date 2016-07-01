package ec.com.innovasystem.sintonizados.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Junior on 26/02/2016.
 */
public class RecursoVolley {
    private  static RecursoVolley objcolaPeticionesVolley = null;
    private RequestQueue colaVolley = null;

    /*constructor privado crea la cola de peticioneshttp*/
    private RecursoVolley(Context ctx){
        colaVolley =  Volley.newRequestQueue(ctx);
    }

    public static RecursoVolley getInstancia(Context ctx){
        if(objcolaPeticionesVolley == null){
            objcolaPeticionesVolley = new RecursoVolley(ctx);
        }
        return objcolaPeticionesVolley;
    }

    public RequestQueue getColaVolley() {
        return colaVolley;
    }
}
