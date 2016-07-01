package ec.com.innovasystem.sintonizados.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.actividades.EscogerSorteo;
import ec.com.innovasystem.sintonizados.actividades.MainActivity;
import ec.com.innovasystem.sintonizados.http.RequestApp;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SorteoSlider.OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class SorteoSlider extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private TextView textParticipando;
    private View rootView=null;
    private Context context;
    private ViewFlipper viewFlipper;
    private Button btnParticipar;
    private OnFragmentInteractionListener mListener;

    public SorteoSlider() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_sorteo_slider, container, false);
        context = rootView.getContext();
        textParticipando=(TextView)rootView.findViewById(R.id.textParticipando);
        String text= "<font color=#000000>Ganar en</font><font color=#1C759F> Sintonizados </font><font color=#000000>es muy fácil, solo debes presionar en participar y llenar el formulario.</font>";
        textParticipando.setText(Html.fromHtml(text));
        btnParticipar=(Button)rootView.findViewById(R.id.btnParticipar);
        btnParticipar.setOnClickListener(this);
        viewFlipper = (ViewFlipper) rootView.findViewById(R.id.viewflipper);
        traerSorteoSlider();
        return rootView;
    }

    public void traerSorteoSlider()
    {
        try {

            String newURL = "http://" + getString(R.string.url) + "/SintonizadosWS/servicio/sorteo/slider";
            Log.i("url", "url " + newURL);
            RequestApp request = new RequestApp(newURL, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonobj) {
                    String resultado = jsonobj.optString("resultado").trim();
                    if (resultado.equalsIgnoreCase("ok")) {
                        //leer la lista
                        JSONArray lista = jsonobj.optJSONArray("lista");
                        Log.i("lista","lista tamaño "+lista);
                        Log.i("lista","lista tamaño "+lista);
                        for (int i = 0; i < lista.length(); i++) {
                            RelativeLayout relativePadre= new RelativeLayout(getActivity().getBaseContext());
                            ImageView image = new ImageView(getActivity().getBaseContext());
                            LinearLayout lLayout = new LinearLayout(getActivity().getBaseContext());
                            LinearLayout lLayoutBotones = new LinearLayout(getActivity().getBaseContext());

                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

                            LinearLayout.LayoutParams paramsBotones = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
                            paramsBotones.gravity = Gravity.BOTTOM;
                            paramsBotones.weight=2;


                            LinearLayout.LayoutParams paramsArrowIz = new LinearLayout.LayoutParams(90,90);
                            paramsArrowIz.weight= 1;
                            paramsArrowIz.gravity = Gravity.LEFT;
                            // paramsArrowIz.addRule(RelativeLayout.ALIGN_LEFT|RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

                            LinearLayout.LayoutParams paramsArrowDe = new LinearLayout.LayoutParams(90,90);
                            // paramsArrowIz.addRule(RelativeLayout.ALIGN_LEFT|RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                            paramsArrowDe.weight= 1;
                            paramsArrowDe.gravity = Gravity.RIGHT;
                            //paramsArrowDe.addRule(RelativeLayout.ALIGN_RIGHT|RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

                            relativePadre.setLayoutParams(params);
                            lLayoutBotones.setLayoutParams(paramsBotones);

                            lLayout.setOrientation(LinearLayout.VERTICAL);
                            //lLayout.setBackground(getResources().getDrawable(
                              //      R.color.opaco_95));
                            //lLayout.setPadding(10, 10, 10, 10);
                            lLayout.setLayoutParams(params);


                            TextView description = new TextView(getActivity().getBaseContext());
                            description.setMaxLines(2);
                            try {
                               /* description.setText(lista.getJSONObject(i).getString("texto"));
                                description.setPadding(15,10,0,10);
                                description.setTextSize(15);
                                description.setTextColor(getActivity().getResources().getColor(R.color.blanco));
                                description.setGravity(View.TEXT_ALIGNMENT_CENTER);*/
                                Log.i("URL", "" + lista.getJSONObject(i).getString("texto"));
                                Picasso.with(getActivity().getBaseContext())
                                        .load(lista.getJSONObject(i).getString("url"))
                                        .placeholder(R.color.transparent_white_15_porc)
                                        .error(R.color.CELESTE)               // optional
                                        .into(image);

                                image.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                                image.setScaleType(ImageView.ScaleType.FIT_XY);

                                description.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.FILL_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT));
                                lLayoutBotones.setGravity(Gravity.CENTER_VERTICAL);

                                lLayout.addView(description);
                                relativePadre.addView(image);
                                relativePadre.addView(lLayoutBotones);
                                relativePadre.addView(lLayout);
                                viewFlipper.startFlipping();
                                viewFlipper.setFlipInterval(3000);
                                viewFlipper.addView(relativePadre);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnParticipar:
                Intent intent= new Intent(getActivity().getApplicationContext(), EscogerSorteo  .class);
                startActivity(intent);
                break;
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
