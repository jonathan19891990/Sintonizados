package ec.com.innovasystem.sintonizados.fragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.actividades.MainActivity;
import ec.com.innovasystem.sintonizados.http.RequestApp;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Sintonizados.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 *
 */
public class Sintonizados extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private View rootView = null;
    private Context context;
    private Button btnKathu;
    private ViewFlipper viewFlipper;
    private Button btnJorge;
    private ImageButton btnFacebook;
    private ImageButton btnTwitter;
    private ImageButton btnInstagram;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public Sintonizados() {
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
        rootView = inflater.inflate(R.layout.fragment_sintonizados, container, false);
        context = rootView.getContext();
        btnKathu = (Button) rootView.findViewById(R.id.btnKathu);
        btnKathu.setOnClickListener(this);
        btnJorge = (Button) rootView.findViewById(R.id.btnJorge);
        btnJorge.setOnClickListener(this);
        btnFacebook=(ImageButton)rootView.findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(this);
        btnTwitter=(ImageButton)rootView.findViewById(R.id.btnTwitter);
        btnTwitter.setOnClickListener(this);
        btnInstagram=(ImageButton)rootView.findViewById(R.id.btnInstagram);
        btnInstagram.setOnClickListener(this);
        viewFlipper = (ViewFlipper) rootView.findViewById(R.id.viewflipper);
        traerSintonizadoSlider();
        return rootView;
    }

    public void traerSintonizadoSlider() {
    try {
        String newURL = "http://" + getString(R.string.url) + "/SintonizadosWS/servicio/traer/sintonizados";
        Log.i("url", "url " + newURL);
        RequestApp request = new RequestApp(newURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonobj) {
                String resultado = jsonobj.optString("resultado").trim();
                if (resultado.equalsIgnoreCase("ok")) {
                    //leer la lista
                    JSONArray lista = jsonobj.optJSONArray("lista");
                    Log.i("lista", "lista tamaño " + lista.length());
                    for (int i = 0; i < lista.length(); i++) {
                        RelativeLayout relativePadre = new RelativeLayout(getActivity().getBaseContext());
                        ImageView image = new ImageView(getActivity().getBaseContext());
                        LinearLayout lLayout = new LinearLayout(getActivity().getBaseContext());
                        LinearLayout lLayoutBotones = new LinearLayout(getActivity().getBaseContext());

                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

                        LinearLayout.LayoutParams paramsBotones = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                        paramsBotones.gravity = Gravity.BOTTOM;
                        paramsBotones.weight = 2;


                        LinearLayout.LayoutParams paramsArrowIz = new LinearLayout.LayoutParams(90, 90);
                        paramsArrowIz.weight = 1;
                        paramsArrowIz.gravity = Gravity.LEFT;
                        // paramsArrowIz.addRule(RelativeLayout.ALIGN_LEFT|RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

                        LinearLayout.LayoutParams paramsArrowDe = new LinearLayout.LayoutParams(90, 90);
                        // paramsArrowIz.addRule(RelativeLayout.ALIGN_LEFT|RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                        paramsArrowDe.weight = 1;
                        paramsArrowDe.gravity = Gravity.RIGHT;
                        //paramsArrowDe.addRule(RelativeLayout.ALIGN_RIGHT|RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

                        relativePadre.setLayoutParams(params);
                        lLayoutBotones.setLayoutParams(paramsBotones);

                        lLayout.setOrientation(LinearLayout.VERTICAL);
                       // lLayout.setBackground(getResources().getDrawable(
                         //       R.color.opaco_95));
                        //lLayout.setPadding(10, 10, 10, 10);
                        lLayout.setLayoutParams(params);



                        try {

                            Picasso.with(getActivity().getBaseContext())
                                    .load(lista.getJSONObject(i).getString("url"))
                                    .placeholder(R.color.transparent_white_15_porc)
                                    .error(R.color.CELESTE)               // optional
                                    .into(image);

                            image.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
                            image.setScaleType(ImageView.ScaleType.CENTER_CROP);


                            lLayoutBotones.setGravity(Gravity.CENTER_VERTICAL);


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

    catch(Exception e)

    {
        e.printStackTrace();

    }
}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnKathu:
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(R.color.blanco);
                dialog.setContentView(R.layout.dialog);

                CircleImageView image=(CircleImageView)dialog.findViewById(R.id.img_url);
                image.setImageDrawable(getResources().getDrawable(R.drawable.kathu));
                TextView textNombre=(TextView)dialog.findViewById(R.id.textNombre);
                textNombre.setText("KATHIUSKA PERALTA");
                TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                text.setText("Con esta nueva etapaque comienza para el popular periodista. Haza se adentrará con más profundidad en los temas  de actualidad que más preocupan a los espectadores, como la nueva forma migratoria e intentará buscar la parte más humana a todos sus invitados, tanto politicos como artistas. Con esta nueva etapaque comienza para el popular periodista. Haza se adentrará con más profundidad en los temas  de actualidad que más preocupan a los espectadores, como la nueva forma migratoria e intentará buscar la parte más humana a todos sus invitados, tanto politicos como artistas. ");
                /*text.setMaxLines(5);
                text.setScroller(new Scroller(context));
                text.setVerticalScrollBarEnabled(true);
                text.setMovementMethod(new ScrollingMovementMethod());*/
                dialog.show();
                break;
            case R.id.btnJorge:
                final Dialog dialog2 = new Dialog(context);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.dialog);
                dialog2.getWindow().setBackgroundDrawableResource(R.color.blanco);
                dialog2.setContentView(R.layout.dialog);
                CircleImageView image2=(CircleImageView)dialog2.findViewById(R.id.img_url);
                image2.setImageDrawable(getResources().getDrawable(R.drawable.jorge));
                TextView textNombre2=(TextView)dialog2.findViewById(R.id.textNombre);
                textNombre2.setText("JORGE VILLAO");
                TextView text2 = (TextView) dialog2.findViewById(R.id.textDialog);
                text2.setText("Con esta nueva etapaque comienza para el popular periodista. Haza se adentrará con más profundidad en los temas  de actualidad que más preocupan a los espectadores, como la nueva forma migratoria e intentará buscar la parte más humana Con esta nueva etapaque comienza para el popular periodista. Haza se adentrará con más profundidad en los temas  de actualidad que más preocupan a los espectadores, como la nueva forma migratoria e intentará buscar la parte más humana");
                /*text3.setMaxLines(5);
                text3.setScroller(new Scroller(context));
                text3.setVerticalScrollBarEnabled(true);
                text3.setMovementMethod(new ScrollingMovementMethod());*/
                dialog2.show();
                break;

            case R.id.btnFacebook:
               /* Uri uri = Uri.parse("https://www.instagram.com/sintonizadosec/");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context,"No se encuentra ese perfil",Toast.LENGTH_SHORT).show();
                }*/
                Log.i("boton face","boton face ");
                String facebookUrl = "https://www.facebook.com/lakathu";
                try {
                    int versionCode = getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) {
                        Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));;
                    } else {
                        // open the Facebook app using the old method (fb://profile/id or fb://pro
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    // Facebook is not installed. Open the browser
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
                }
                break;

            case R.id.btnTwitter:
                Log.i("boton face","boton twitter ");
                String tweetUrl = String.format("https://twitter.com/innovasystemecu",
                        urlEncode("Tweet text"),
                        urlEncode("https://www.google.fi/"));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

// Narrow down to official Twitter app, if available:
                List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                        intent.setPackage(info.activityInfo.packageName);
                    }
                }

                startActivity(intent);
                break;

            case R.id.btnInstagram:
                Log.i("boton face","boton instagram ");
                Uri uri = Uri.parse("https://instagram.com/sintonizadosec");
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent2);
            break;

        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

}
