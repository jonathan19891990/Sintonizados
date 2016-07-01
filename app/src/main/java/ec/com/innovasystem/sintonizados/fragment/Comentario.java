package ec.com.innovasystem.sintonizados.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import ec.com.innovasystem.sintonizados.Bo.AuspiciantesBo;
import ec.com.innovasystem.sintonizados.Bo.ShirSessionManager;
import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.actividades.MainActivity;
import ec.com.innovasystem.sintonizados.http.RequestApp;
import util.ValidatorUtil;


public class Comentario extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Context context;
    private View rootView=null;
    private TextView textNombre;
    private TextView textCorreo;
    private TextView textTelefono;
    private TextView textMensaje;
    private Button btnEnviar;
    public boolean nombre,email, telefono, mensaje;
    private OnFragmentInteractionListener mListener;

    public Comentario() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment_comentario, container, false);
        context = rootView.getContext();
        Bundle parametros = new Bundle();
        parametros.putInt("layout", R.layout.activity_main);
        parametros.putInt("verbtnatras", 1);
        parametros.putInt("vertoolbar", 1);
        super.onCreate(parametros);
        textNombre=(TextView)rootView.findViewById(R.id.textNombre);
        textCorreo=(TextView)rootView.findViewById(R.id.textCorreo);
        textTelefono=(TextView)rootView.findViewById(R.id.textTelefono);
        textMensaje=(TextView)rootView.findViewById(R.id.textMensaje);
        btnEnviar=(Button)rootView.findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(this);
        return rootView;
    }

    public void guardarComentario()
    {
        try {
            ShirSessionManager sesion= new ShirSessionManager(getContext());
            String newURL = "http://" + getString(R.string.url) + "/SintonizadosWS/servicio/guardar/comentario?";
            newURL=newURL+"nombre="+ URLEncoder.encode(textNombre.getText().toString().trim(), "utf-8")
            +"&correo="+URLEncoder.encode(textCorreo.getText().toString().trim(), "utf-8")
            +"&telefono="+URLEncoder.encode(textTelefono.getText().toString().trim(), "utf-8")
            +"&id="+sesion.obtenerUserData().getUserId()
            +"&texto="+URLEncoder.encode(textMensaje.getText().toString().trim(), "utf-8");
            Log.i("url", "url " + newURL);
            RequestApp request = new RequestApp(newURL, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonobj) {
                    String resultado = jsonobj.optString("resultado").trim();
                    if (resultado.equalsIgnoreCase("ok")) {
                        //leer la lista
                        Toast.makeText(getContext(),"Su comentario ha sido enviado",Toast.LENGTH_SHORT).show();
                        textNombre.setText("");
                        textCorreo.setText("");
                        textTelefono.setText("");
                        textMensaje.setText("");
                    }
                    ((MainActivity) getActivity()).onConnectionFinished();
                    //que hacer cuando devuelve los datos
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //que hacer cuando ocurrer algune error
                    ((MainActivity) getActivity()).onConnectionFailed(error.toString());
                }
            });
            ((MainActivity) getActivity()).agregarPeticionHttp(request);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnEnviar:
                nombre= ValidatorUtil.Texto(textNombre.getText().toString().trim());
                if(!nombre)
                {
                    textNombre.setError("Minimo 4 caracteres");
                }
                email=ValidatorUtil.validateEmail(textCorreo.getText().toString().trim());
                if(!email)
                {
                    textCorreo.setError("Correo Incorrecto");
                }
                if(textTelefono.getText().toString().trim().length()>=6)
                {
                    telefono=true;
                }
                else
                {
                    textTelefono.setError("Minimo 4 caracteres");
                }
                if(textMensaje.getText().toString().trim().length()>=20)
                {
                    mensaje=true;
                }
                else
                {
                    textTelefono.setError("Minimo 20 caracteres");
                }
                if(nombre==true && email==true && telefono==true && mensaje==true)
                {
                    Log.i("valores correctoss","valores correctos");
                    guardarComentario();
                }
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
