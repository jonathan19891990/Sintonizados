package ec.com.innovasystem.sintonizados.fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.actividades.EscogerSorteo;
import ec.com.innovasystem.sintonizados.actividades.MainActivity;
import ec.com.innovasystem.sintonizados.chatapp.ChatVideoActivity;
import ec.com.innovasystem.sintonizados.http.RequestApp;


public class Home extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private View rootView=null;
    private Context context;
    private ViewFlipper viewFlipper;
    private float lastX;
    private Typeface typeface;
    private TextView textNombre;
    private TextView textTexto;
    private TextView textCabina;
    private ImageButton btnSorteo;
    private ImageButton btnContacto;
    private ImageButton btnCabina;
    private CircleImageView img_url;
    private OnFragmentInteractionListener mListener;

    public Home() {
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
        rootView =inflater.inflate(R.layout.fragment_home, container, false);
        context = rootView.getContext();
        textNombre=(TextView)rootView.findViewById(R.id.textNombre);
        typeface=Typeface.createFromAsset(getActivity().getAssets(), "tipografia/MyriadPro-Light.otf");
        textTexto=(TextView)rootView.findViewById(R.id.textTexto);
        textCabina=(TextView)rootView.findViewById(R.id.textCabina);
        traerCabinaInvitado();
        textCabina.setTypeface(typeface);

        textCabina.setTextColor(getActivity().getResources().getColor(R.color.negro));
        textCabina.setSelected(true);
        btnSorteo=(ImageButton)rootView.findViewById(R.id.btnSorteo);
        btnSorteo.setOnClickListener(this);
        btnContacto=(ImageButton)rootView.findViewById(R.id.btnContacto);
        btnContacto.setOnClickListener(this);
        btnCabina=(ImageButton)rootView.findViewById(R.id.btnCabina);
        btnCabina.setOnClickListener(this);
        /*textCabina.setPadding(10, 15, 0, 15);
        textCabina.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textCabina.setTextSize(20);
        textCabina.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textCabina.setSingleLine();
        textCabina.setMarqueeRepeatLimit(10);
        textCabina.setFocusable(true);
        textCabina.setHorizontallyScrolling(true);
        textCabina.setFocusableInTouchMode(true);
        textCabina.requestFocus();
        textCabina.setLayoutParams(new TableRow.LayoutParams(450, TableRow.LayoutParams.FILL_PARENT));*/
        img_url=(CircleImageView)rootView.findViewById(R.id.img_url);
        viewFlipper = (ViewFlipper) rootView.findViewById(R.id.viewflipper);
        traerImagenesFlipper();
        traerInvitado();
        return rootView;
    }

    public void traerCabinaInvitado()
    {
        String newURL = "http://" + getString(R.string.url) + "/SintonizadosWS/servicio/cabina/invitado";
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
                        try {

                            textCabina.setText(lista.getJSONObject(i).getString("texto"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (lista.length() > 0) {

                    } else {
                        textCabina.setText("No hay invitados el día de hoy en nuestro programa");
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

    public void traerInvitado()
    {
        String newURL = "http://" + getString(R.string.url) + "/SintonizadosWS/servicio/invitado/traer";
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
                        try {
                            textNombre.setText(lista.getJSONObject(i).getString("nombre"));
                            textTexto.setText(lista.getJSONObject(i).getString("texto"));
                            Picasso.with(getActivity())
                                    .load(lista.getJSONObject(i).getString("foto"))
                                    .placeholder(R.color.transparent_white_15_porc)
                                    .error(R.color.transparent_white_15_porc)               // optional
                                    .into(img_url);
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

    public void traerImagenesFlipper()
    {
        try {

            String newURL = "http://" + getString(R.string.url) + "/SintonizadosWS/servicio/publicidad/traer";
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
                            lLayout.setBackground(getResources().getDrawable(
                                    R.color.opaco_95));
                            //lLayout.setPadding(10, 10, 10, 10);
                            lLayout.setLayoutParams(params);


                            TextView description = new TextView(getActivity().getBaseContext());
                            description.setMaxLines(2);
                            try {
                                description.setText(lista.getJSONObject(i).getString("texto"));
                                description.setPadding(15,10,0,10);
                                description.setTextSize(15);
                                description.setTextColor(getActivity().getResources().getColor(R.color.blanco));
                                description.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            Log.i("URL", "" + lista.getJSONObject(i).getString("texto"));
                            Picasso.with(getActivity().getBaseContext())
                                    .load(lista.getJSONObject(i).getString("urlFoto"))
                                    .placeholder(R.color.transparent_white_15_porc)
                                    .error(R.color.CELESTE)               // optional
                                    .into(image);

                                image.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                                image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                description.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.FILL_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT));
                                lLayoutBotones.setGravity(Gravity.CENTER_VERTICAL);

                                lLayout.addView(description);
                                relativePadre.addView(image);
                                relativePadre.addView(lLayoutBotones);
                                relativePadre.addView(lLayout);
                                viewFlipper.startFlipping();
                                viewFlipper.setFlipInterval(16000);
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
            case R.id.btnSorteo:
                Intent intent= new Intent(getActivity(), EscogerSorteo.class);
                startActivity(intent);
                break;
            case R.id.btnContacto:
                Comentario mainFragment= new Comentario();
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, mainFragment);
                //transaction.addToBackStack(null); eliminar fragmento anterior
                // Commit the transaction
                transaction.commit();
                break;
            case R.id.btnCabina:
                Intent intent2= new Intent(getActivity(), ChatVideoActivity.class);
                startActivity(intent2);
                break;
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
