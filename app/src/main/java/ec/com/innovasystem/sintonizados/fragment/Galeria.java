package ec.com.innovasystem.sintonizados.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ec.com.innovasystem.sintonizados.Bo.AuspiciantesBo;
import ec.com.innovasystem.sintonizados.Bo.GaleriaBo;
import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.actividades.MainActivity;
import ec.com.innovasystem.sintonizados.adapter.AuspciantesAdapter;
import ec.com.innovasystem.sintonizados.adapter.GaleriaAdapter;
import ec.com.innovasystem.sintonizados.http.RequestApp;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Galeria.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class Galeria extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public int a=0,b=16,c=a+b;
    private RecyclerView lista;
    private RecyclerView.LayoutManager lManager;
    private GaleriaAdapter adapter;
    private Context context;
    private View rootView=null;
    private FloatingActionButton btnMas;
    private OnFragmentInteractionListener mListener;

    public Galeria() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment_galeria, container, false);
        context = rootView.getContext();
        Bundle parametros = new Bundle();
        parametros.putInt("layout", R.layout.activity_main);
        parametros.putInt("verbtnatras", 1);
        parametros.putInt("vertoolbar", 1);
        btnMas=(FloatingActionButton)rootView.findViewById(R.id.btnMas);

        btnMas.setOnClickListener(this);
        lista=(RecyclerView)rootView.findViewById(R.id.rv_listas);
        lManager = new LinearLayoutManager(context);
        GridLayoutManager gridLayoutManager          // HACERLO EN BLOQUE TIPO GALERIA 3 COLUMNAS
                = new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false);
        adapter= new GaleriaAdapter(getActivity().getApplicationContext(), this);
        lista.setLayoutManager(gridLayoutManager);
        lista.setAdapter(adapter);
        btnMas.attachToRecyclerView(lista);
        traerListaAuspiciantes();
        return rootView;
    }

    public void verDatos(String imagen, String texto, String fecha)
    {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        final Dialog dialog2 = new Dialog(context);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawableResource(R.color.blanco);
        dialog2.setContentView(R.layout.dialog2);
        ImageView image=(ImageView)dialog2.findViewById(R.id.img_url);
        Picasso.with(context)
                .load(imagen)
                .placeholder(R.color.transparent_white_15_porc)
                .error(R.color.transparent_white_15_porc)               // optional
                .into(image);
        String mes= verMes(fecha.trim().split("-")[1]);
        String anio=fecha.trim().split("-")[0];
        String dia=fecha.trim().split("-")[2];
        Log.i("texto mes","mes es "+mes+" año "+anio+" dia "+dia);
        TextView fech=(TextView)dialog2.findViewById(R.id.textFecha);
        fech.setText(dia+" de "+mes+" del "+anio);
        TextView text = (TextView) dialog2.findViewById(R.id.textDialog);
        text.setText(texto);
        dialog2.show();
    }

    public String verMes(String mes)
    {
        String textMes="";
        switch (mes)
        {
            case "01":
                textMes="enero";
                break;
            case "02":
                textMes="febrero";
                break;
            case "03":
                textMes="marzo";
                break;
            case "04":
                textMes="abril";
                break;
            case "05":
                textMes="mayo";
                break;
            case "06":
                textMes="junio";
                break;
            case "07":
                textMes="julio";
                break;
            case "08":
                textMes="agosto";
                break;
            case "09":
                textMes="septiembre";
                break;
            case "10":
                textMes="octubre";
                break;
            case "11":
                textMes="noviembre";
                break;
            case "12":
                textMes="diciembre";
                break;
        }
        return textMes;
    }

    public void traerListaAuspiciantes()
    {
        try {
            a=a*b;
            String newURL = "http://" + getString(R.string.url) + "/SintonizadosWS/servicio/traer/galeria?";
            newURL= newURL+"empieza="+a+"&termina="+b;
            Log.i("url", "url " + newURL);
            RequestApp request = new RequestApp(newURL, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonobj) {
                    String resultado = jsonobj.optString("resultado").trim();
                    if (resultado.equalsIgnoreCase("ok")) {
                        //leer la lista
                        JSONArray lista = jsonobj.optJSONArray("lista");
                        Log.i("lista","lista tamaño "+lista.length());
                        for (int i = 0; i < lista.length(); i++) {
                            GaleriaBo gale = new GaleriaBo();
                            try {
                                gale.setId(lista.getJSONObject(i).getInt("id"));
                                gale.setFoto(lista.getJSONObject(i).getString("foto"));
                                gale.setTexto(lista.getJSONObject(i).getString("texto"));
                                gale.setFecha(lista.getJSONObject(i).getString("fecha"));
                                adapter.items.add(gale);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();

                        if (lista.length() > 0) {

                        } else {

                        }
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

            a++;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnMas:
                traerListaAuspiciantes();
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
