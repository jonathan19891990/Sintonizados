package ec.com.innovasystem.sintonizados.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ec.com.innovasystem.sintonizados.Bo.AuspiciantesBo;
import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.adapter.AuspciantesAdapter;
import ec.com.innovasystem.sintonizados.actividades.MainActivity;
import ec.com.innovasystem.sintonizados.http.RequestApp;

public class Auspiciantes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView lista;
    private RecyclerView.LayoutManager lManager;
    private AuspciantesAdapter adapter;
    private Context context;
    private View rootView=null;
    public Auspiciantes() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment_auspiciantes, container, false);
        context = rootView.getContext();
        Bundle parametros = new Bundle();
        parametros.putInt("layout", R.layout.activity_main);
        parametros.putInt("verbtnatras", 1);
        parametros.putInt("vertoolbar", 1);
        super.onCreate(parametros);
        lista=(RecyclerView)rootView.findViewById(R.id.rv_listas);
        lManager = new LinearLayoutManager(context);
        lista.setLayoutManager(lManager);
        /*LinearLayoutManager horizontalLayoutManagaer      HACERLO DE MANERA HORIZOTAL
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        lista.setLayoutManager(horizontalLayoutManagaer);*/
       /* GridLayoutManager gridLayoutManager           HACERLO EN BLOQUE TIPO GALERIA 3 COLUMNAS
                = new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false);
        adapter= new AuspciantesAdapter(getActivity().getApplicationContext());
        lista.setLayoutManager(gridLayoutManager);*/
        adapter= new AuspciantesAdapter(getActivity().getApplicationContext());
        lista.setAdapter(adapter);
        traerListaAuspiciantes();
        return rootView;
    }

    public void traerListaAuspiciantes()
    {
        try {

            String newURL = "http://" + getString(R.string.url) + "/SintonizadosWS/servicio/traer/auspiciante";
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
                            AuspiciantesBo aus= new AuspiciantesBo();
                            try {
                                aus.setId(lista.getJSONObject(i).getInt("id"));
                                aus.setNombre(lista.getJSONObject(i).getString("nombre"));
                                aus.setTexto(lista.getJSONObject(i).getString("texto"));
                                aus.setUrl(lista.getJSONObject(i).getString("url"));
                                adapter.items.add(aus);
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


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
