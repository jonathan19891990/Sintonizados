package ec.com.innovasystem.sintonizados.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ec.com.innovasystem.sintonizados.Bo.ShirSessionManager;
import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.adapter.CustomAdapter;
import ec.com.innovasystem.sintonizados.http.BaseActivity;
import ec.com.innovasystem.sintonizados.http.RequestApp;
import util.ValidatorUtil;

public class EscogerSorteo extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private Spinner spinner;
    public List<String> lista1;
    private int idSpinner;
    private EditText textNombre;
    private EditText textCorreo;
    private EditText textTelefono;
    private EditText textCiudad;
    private EditText textDireccion;
    private Button btnCrear;
    public boolean nombre, email, telefono, direccion, ciudad, sorteo;
    String[] name;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle parametros = new Bundle();
        parametros.putInt("layout", R.layout.activity_escoger_sorteo);
        parametros.putInt("verbtnatras", 1);
        parametros.putInt("vertoolbar", 1);
        super.onCreate(parametros);
        final ShirSessionManager pref = new ShirSessionManager(this);
        spinner=(Spinner)findViewById(R.id.spinner);
        lista1= new ArrayList<String>();
        traerInfoSpinner();
        customAdapter=new CustomAdapter(getApplicationContext(), lista1);
        spinner.setAdapter(customAdapter);
        spinner.setOnItemSelectedListener(this);
        textNombre=(EditText)findViewById(R.id.textNombre);
        Log.i("editar ","nombre "+pref.obtenerUserData().getNombre_usuario()+" correo "+pref.obtenerUserData().getUser().length());
        if(pref.obtenerUserData().getNombre_usuario().trim().length()>0)
        {
            textNombre.setText(pref.obtenerUserData().getNombre_usuario());
            textNombre.setEnabled(false);
        }
        else
        {
            textNombre.setEnabled(true);
        }
        textCorreo=(EditText)findViewById(R.id.textCorreo);
        if(pref.obtenerUserData().getUser().trim().length()>0)
        {
            textCorreo.setText(pref.obtenerUserData().getUser());
            textCorreo.setEnabled(false);
        }
        else
        {
            textCorreo.setEnabled(true);
        }
        btnCrear=(Button)findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(this);
        textTelefono=(EditText)findViewById(R.id.textTelefono);
        textCiudad=(EditText)findViewById(R.id.textCiudad);
        textDireccion=(EditText)findViewById(R.id.textDireccion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void traerInfoSpinner()
    {
        try
        {

            String newURL="http://" + getString(R.string.url) + "/SintonizadosWS/servicio/sorteo/slider";
            try {
                RequestApp request = new RequestApp(newURL, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonobj) {
                        String resultado = jsonobj.optString("resultado").trim();
                        Log.i("resultado", " " + resultado);
                        if (resultado.equalsIgnoreCase("ok")) {

                            JSONArray lista = jsonobj.optJSONArray("lista");
                            Log.i("url", "tamaño lista " + lista);
                            lista1.add(0,"Seleccione Sorteo");
                            for (int i = 0; i < lista.length(); i++) {
                                try {

                                    lista1.add(lista.getJSONObject(i).getString("nombre"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            customAdapter.notifyDataSetChanged();
                            if(lista.length()<=0)
                            {
                                Log.i("lista"," "+lista.length());
                                Toast.makeText(getApplicationContext(), "No hay valores", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Log.i("lista", " "+lista.length());
                                //llamarActividadPrincipal();
                            }


                            // Log.i("practica", "valor " + aud.getUrlRosario());
                        } else {
                            Toast.makeText(getApplicationContext(), "Problemas con internet", Toast.LENGTH_SHORT).show();
                        }

                        onConnectionFinished();
                        //que hacer cuando devuelve los datos
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //que hacer cuando ocurrer algune error
                        onConnectionFailed(error.toString());
                    }
                });
                agregarPeticionHttp(request);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        idSpinner=position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {//telefono, direccion, ciudad, sorteo;
            case R.id.btnCrear:
                nombre = ValidatorUtil.Texto(textNombre.getText().toString());
                if (!nombre)
                    textNombre.setError("Minimo 4 caracteres");

                email = ValidatorUtil.validateEmail(textCorreo.getText().toString());
                if (!email)
                    textCorreo.setError("Correo Incorrecto");

                if(textTelefono.getText().toString().trim().length()>=6)
                {
                   telefono=true;
                }
                else
                {
                    textTelefono.setError("Minimo 4 caracteres");
                }
                ciudad=ValidatorUtil.Texto(textCiudad.getText().toString());
                if(!ciudad)
                {
                    textCiudad.setError("Minimo 4 caracteres");
                }
                direccion=ValidatorUtil.Texto(textDireccion.getText().toString());
                if(!direccion)
                {
                    textDireccion.setError("Minimo 4 caracteres");
                }
                if(idSpinner<=0)
                {
                    Toast.makeText(EscogerSorteo.this,"Escoja una opción",Toast.LENGTH_SHORT).show();
                }
                if (nombre == true  && email == true && telefono == true && ciudad==true && idSpinner>0 && direccion==true) {
                    Log.i("cargar", "upload foto ");
                    guardarSorteoUsuario(textCorreo.getText().toString().trim(), textTelefono.getText().toString().trim(), textCiudad.getText().toString().trim()
                    , textDireccion.getText().toString().trim());
                }
                break;
        }
    }

    public void guardarSorteoUsuario(String email, String telefono, String ciudad, String direccion)
    {
        final ShirSessionManager pref = new ShirSessionManager(this);
        String newURL ="http://"+getString(R.string.url)+"/SintonizadosWS/servicio/sorteousuario/guardar?";
        try {
            newURL= newURL+"id="+pref.obtenerUserData().getUserId() +"&correo="+URLEncoder.encode(email, "utf-8")+"&telefono="
                    +URLEncoder.encode(telefono, "utf-8")+"&ciudad="+URLEncoder.encode(ciudad, "utf-8")+"&direccion="+URLEncoder.encode(direccion, "utf-8")
                    + "&sorteo="+idSpinner;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("URL","".concat(newURL));
        RequestApp request = new RequestApp(newURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonobj) {
                String resultado = jsonobj.optString("resultado").trim();
                if(resultado.equalsIgnoreCase("ok")){
                    llamarPantallaListoSorteo();
                }
                else
                {
                    Toast.makeText(EscogerSorteo.this,"Usted ya se encuentra participando", Toast.LENGTH_SHORT).show();
                }
                onConnectionFinished();
                //que hacer cuando devuelve los datos
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //que hacer cuando ocurrer algune error
                onConnectionFailed(error.toString());
            }
        });
        agregarPeticionHttp(request);
    }

    public void llamarPantallaListoSorteo()
    {
        try
        {
            Intent intent= new Intent(getApplicationContext(), ListoSorteo.class);
            startActivity(intent);
            finish();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
