package ec.com.innovasystem.sintonizados.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ec.com.innovasystem.sintonizados.Bo.ShirSessionManager;
import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.http.BaseActivity;
import ec.com.innovasystem.sintonizados.http.HttpsTrustManager;
import ec.com.innovasystem.sintonizados.http.RecursoVolley;
import ec.com.innovasystem.sintonizados.http.RequestApp;

public class ChatVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener{

    // TODO: change this to your own Firebase URL
    private static final String FIREBASE_URL = "https://project-6216169142926604048.firebaseio.com/";//"https://Sintonizados.firebaseio-demo.com";//https://android-chat.firebaseio-demo.com";
    private static final int RECOVERY_REQUEST = 1;
    private static final String CONFIG_YOUTUBEKEY = "AIzaSyAki8b_eIgReNS6VdgKZhHAMc-UwHBHgHA";
    private String VIDEO_URL;
    String ced;
    RecursoVolley objvolley;
    RequestQueue colaPeticioneshttp;
    Toolbar toolbar;
    ProgressDialog barraprogreso;
    private String mUsername;
    private String facebook;
    private String imagen;
    private int idusuario;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
    private ListView listView;
    private YouTubePlayerView youTubeView;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatvideo_activity);
        objvolley = RecursoVolley.getInstancia(this);
        colaPeticioneshttp = objvolley.getColaVolley();
        /*Esta linea podria ir dentro del Application*/
        Firebase.setAndroidContext(this);
        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(this);
        llamarStreamingLink();
        //Configuracion video de Youtube
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(CONFIG_YOUTUBEKEY, this);

        setupUsername();

        setTitle("Chatting as " + mUsername);

        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat");
        // Setup our input methods. Enter key on the keyboard or pushing the send button
        listView = (ListView)findViewById(R.id.listachat);
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Log.i("oncreate","oncreate video "+VIDEO_URL);
      // VIDEO_URL = "nuUFKS0ffMc";nuUFKS0ffMc
    }

    @Override
    public void onStart() {
        super.onStart();
        //final ListView listView = getListView();
        // muestro 50 mensajes solamente
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.chat_message, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // indicacion del status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    //Toast.makeText(ChatVideoActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(ChatVideoActivity.this, "Desconectado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //nnaa
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void setupUsername() {
        final ShirSessionManager pref = new ShirSessionManager(this);
        Log.i("usuario","usuario "+pref.obtenerUserData().getNombre_usuario());
        mUsername = pref.obtenerUserData().getNombre_usuario();
        imagen=pref.obtenerUserData().getUrlFoto();
        idusuario=pref.obtenerUserData().getUserId();
        facebook=pref.obtenerUserData().getFacebook();
        Log.i("chat face", " chat face prueba " + facebook);
        if (mUsername == null) {
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
            mUsername = "JavaUser" + r.nextInt(100000);
            //pref.edit().putString("username", mUsername).commit();

        }
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        Log.i("send facebook"," facebook send "+facebook);
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, mUsername,imagen, idusuario, facebook);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");
        }
    }

    public void llamarStreamingLink()
    {
        try
        {

            String newURL="http://" + getString(R.string.url) + "/SintonizadosWS/servicio/traer/videochat";
            try {
                RequestApp request = new RequestApp(newURL, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonobj) {
                        String resultado = jsonobj.optString("resultado").trim();
                        Log.i("resultado", " " + resultado);
                        if (resultado.equalsIgnoreCase("ok")) {

                            JSONArray lista = jsonobj.optJSONArray("lista");
                            Log.i("url", "tamaño lista " + lista);
                            for (int i = 0; i < lista.length(); i++) {
                                try {
                                    String link=lista.getJSONObject(i).getString("url");
                                    VIDEO_URL =link;
                                    Log.i("video volley","video desde web service "+VIDEO_URL);
                                    break;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if(lista.length()<=0)
                            {
                                Log.i("lista"," "+lista.length());
                                Toast.makeText(getApplicationContext(), "No hay streaming en estos momentos", Toast.LENGTH_SHORT).show();
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
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        Log.i("en youtube","video youtube");
        try
        {
            if (!b) {
                Log.i("en youtube", "dentro de b video youtube " + VIDEO_URL);
                if(VIDEO_URL!=null)
                {
                    youTubePlayer.cueVideo(VIDEO_URL.trim()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
                }
                else
                {
                    Toast.makeText(ChatVideoActivity.this,"Este video online no se encuentra disponible en este momento",Toast.LENGTH_SHORT).show();
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {

                        @Override
                        public void run() {

                            finish()    ;
                            // If you want to call Activity then call from here for 5 seconds it automatically call and your image disappear....
                        }
                    }, 5000);
                }

            }
        }
        catch (Exception e)
        {
            Toast.makeText(ChatVideoActivity.this,"Este video online no se encuentra disponible en este momento",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
            Toast.makeText(ChatVideoActivity.this, "Este video online no se encuentra disponible en este momento", Toast.LENGTH_SHORT).show();
        } else {
            String error = String.format("Error reproduciendo el video", youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            getYouTubePlayerProvider().initialize(CONFIG_YOUTUBEKEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
        }
    }
}
