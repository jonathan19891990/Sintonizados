package ec.com.innovasystem.sintonizados.actividades;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ec.com.innovasystem.sintonizados.Bo.ShirSessionManager;
import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.http.BaseActivity;
import ec.com.innovasystem.sintonizados.http.RequestApp;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private TextView textsoyNuevo;
    private ImageView imgOjo;
    private AppCompatEditText mEmailView;
    private AppCompatEditText mPasswordView;
    private int flag=0;
    private View mProgressView;
    private View mLoginFormView;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        Bundle parametros = new Bundle();
        parametros.putInt("layout", R.layout.activity_login);
        parametros.putInt("verbtnatras", 1);
        parametros.putInt("vertoolbar", 1);
        super.onCreate(parametros);
        final ShirSessionManager pref = new ShirSessionManager(this);
        if(pref.isLoggedIn())
        {

            Log.i("boolLogueo","esta guardado el login");
            this.finish();
            llamarActividadPrincipal();
        }
        else {
            imgOjo = (ImageView) findViewById(R.id.imgOjo);
            imgOjo.setOnClickListener(this);
            FacebookSdk.sdkInitialize(this.getApplicationContext());

            callbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(final LoginResult loginResult) {
                            String foto;
                            String newURL = "http://" + getString(R.string.url) + "/SintonizadosWS/servicio/login/defacebook?id=".concat(loginResult.getAccessToken().getUserId());
                            Log.i("URL", "".concat(newURL));
                            RequestApp request = new RequestApp(newURL, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonobj) {
                                    String resultado = jsonobj.optString("resultado").trim();
                                    if (resultado.equalsIgnoreCase("ok")) {

                                        JSONArray lista = jsonobj.optJSONArray("lista");
                                        Log.i("facebook", " tamaño de lista " + lista.length());
                                        for (int i = 0; i < lista.length(); i++) {
                                            try {
                                                Log.i("face", "probandoooo facebok " + lista.getJSONObject(i));
                                                String email;
                                                int id = lista.getJSONObject(i).getInt("id");
                                                String usuario = lista.getJSONObject(i).getString("usuario");
                                                String nombre = lista.getJSONObject(i).getString("nombre");
                                                String foto = lista.getJSONObject(i).getString("foto");
                                                String user_facebook;
                                                Log.i("login_usuario", "usuario " + usuario + " nombre " + nombre + " foto " + foto);
                                                String telefono;
                                                if (jsonobj.isNull("usuario")) {
                                                    telefono = "";
                                                } else {
                                                    telefono = lista.getJSONObject(i).getString("usuario");
                                                }
                                                if (lista.getJSONObject(i).isNull("userFacebook")) {
                                                    user_facebook = "";
                                                } else {
                                                    user_facebook = lista.getJSONObject(i).getString("userFacebook");
                                                }

                                                pref.createLoginSessionFacebook(id, usuario, nombre, foto, telefono, user_facebook);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (lista.length() > 0) {
                                            Log.i("llamaractvidad", "llamaractividad desde boton " + lista.length());

                                            llamarActividadPrincipal();
                                            LoginActivity.this.finish();
                                        } else {
                                            Log.i("not exist", "  no hay en base");
                                            GraphRequest request = GraphRequest.newMeRequest(
                                                    loginResult.getAccessToken(),
                                                    new GraphRequest.GraphJSONObjectCallback() {
                                                        @Override
                                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                                            Log.v("LoginActivity", response.toString());

                                                            try {
                                                                // Log.i("facebook", " " + object.getString("picture"));
                                                                String id = object.getString("id");
                                                                String name = "";
                                                                if (object.has("name")) {
                                                                    name = object.getString("name");
                                                                } else {
                                                                    Log.i("name", "no hay name");
                                                                }
                                                                String city = "";
                                                                if (object.has("city")) {
                                                                    city = object.getString("city");
                                                                    Log.i("city", " city " + city);
                                                                } else {
                                                                    Log.i("city", "no hay city");
                                                                }
                                                                String phone = "";
                                                                if (object.has("phone")) {
                                                                    phone = object.getString("phone");
                                                                    Log.i("phone", " phone " + phone);
                                                                } else {
                                                                    Log.i("phone", "no hay phone");
                                                                }
                                                                String email = "";
                                                                if (object.has("email")) {

                                                                    email = object.getString("email");
                                                                } else {
                                                                    Log.i("email", "no hay email");
                                                                }
                                                                char gender = '\u0000';
                                                                String birthday = "";//object.getString("birthday")==null
                                                                if (object.has("birthday")) {

                                                                    birthday = object.getString("birthday");
                                                                } else {
                                                                    Log.i("birthday", "no hay cumpleaños");
                                                                }
                                                                URL imageURL = null;
                                                                imageURL = new URL("https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?width=" + 150 + "&height=" + 150);
                                                                llamarFacebookGuardar(name, birthday, id, email, imageURL.toString(), phone, city);
                                                                Log.i("loginFace", " login " + loginResult.getAccessToken().getUserId());
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });
                                            Bundle parameters = new Bundle();
                                            parameters.putString("fields", "id,name,email,gender, birthday, picture");
                                            Log.i("login  ", " ");
                                            request.setParameters(parameters);
                                            request.executeAsync();
                                        }
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

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(FacebookException error) {
                            Log.e("error", "Error login");
                            Log.d("myTag", error.getMessage());
                        }
                    });


            Button btn_fb_login = (Button) findViewById(R.id.btn_fb_login);

            btn_fb_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
                }
            });

            textsoyNuevo=(TextView)findViewById(R.id.textsoyNuevo);
            textsoyNuevo.setOnClickListener(this);
            // Set up the login form.
            mEmailView = (AppCompatEditText) findViewById(R.id.email);
            //populateAutoComplete();

            mPasswordView = (AppCompatEditText) findViewById(R.id.password);
            mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
        }
    }

    public void llamarFacebookGuardar(final String nombre, String cumpleanios, final String id, final String email, final String url_foto, final String phone, final String city)
    {
        try
        {
            final ShirSessionManager pref = new ShirSessionManager(this);
            String newURL ="http://"+getString(R.string.url)+"/SintonizadosWS/servicio/login/guardardatos?";
            try {

                newURL= newURL+"nombre="+URLEncoder.encode(nombre, "utf-8") +"&nacimiento="+URLEncoder.encode(cumpleanios, "utf-8")+"&id="
                        +URLEncoder.encode(id, "utf-8")+"&usuario="+URLEncoder.encode(email, "utf-8")+"&url_foto="+URLEncoder.encode(url_foto, "utf-8");
                Log.i("url","url "+newURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("LOGIN_URL", "" + newURL);
            RequestApp request = new RequestApp(newURL, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonobj) {
                    String resultado = jsonobj.optString("resultado").trim();
                    if(resultado.equalsIgnoreCase("ok")){
                        Log.i("resultado", " " + nombre);
                        pref.createLoginSessionFacebookSinId(nombre, url_foto, email, phone, city);
                        //llamarActividadPrincipal();
                        llamarLoginFacebook(id);
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
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void llamarLoginFacebook(String id)
    {
        final ShirSessionManager pref = new ShirSessionManager(this);
        String newURL ="http://"+getString(R.string.url)+"/SintonizadosWS/servicio/login/defacebook?id=".concat(id);
        Log.i("URL","".concat(newURL));
        RequestApp request = new RequestApp(newURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonobj) {
                String resultado = jsonobj.optString("resultado").trim();
                if(resultado.equalsIgnoreCase("ok")){

                    //leer la lista
                    JSONArray lista = jsonobj.optJSONArray("lista");
                    Log.i("tamañoLista", "tamaño " + lista.length());
                    for (int i = 0; i < lista.length(); i++) {
                        //aud = new Audio_rosario();
                        try {
                            int ids=lista.getJSONObject(i).getInt("id");
                            String email;
                            String usuario = lista.getJSONObject(i).getString("usuario");
                            String nombre= lista.getJSONObject(i).getString("nombre");
                            String foto= lista.getJSONObject(i).getString("foto");
                            String telefono;
                            String ciudad;
                            String user_facebook;
                            if(lista.getJSONObject(i).isNull("usuario"))
                            {
                                telefono = "";
                            }
                            else
                            {
                                telefono = lista.getJSONObject(i).getString("usuario");
                            }
                            if (lista.getJSONObject(i).isNull("userFacebook")) {
                                user_facebook = "";
                            } else {
                                user_facebook = lista.getJSONObject(i).getString("userFacebook");
                            }
                            Log.i("userFacebook123"," usuario "+usuario+" nombre "+nombre+" telefono "+telefono);
                            pref.createLoginSessionFacebook(ids,usuario,nombre, foto, telefono, user_facebook);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(lista.length()>0)
                    {
                        Log.i("llamarActividad"," llamando actividad principal");
                        LoginActivity.this.finish();
                        llamarActividadPrincipal();
                    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

  /*  private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }
*/
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }
*/

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
           // showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
            traerLogin(email, password);
        }
    }

    public void traerLogin(String email, String password)
    {
        final ShirSessionManager pref = new ShirSessionManager(getApplicationContext());
        String newURL="http://"+getString(R.string.url)+"/SintonizadosWS/servicio/usuario/login?";
        try {
            try {

                newURL = newURL + "usuario=" + URLEncoder.encode(email, "utf-8") + "&contrasenia=" + URLEncoder.encode(password, "utf-8");
                Log.i("url2", "url " + newURL);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
                                String email;
                                String telefono;
                                String ciudad;
                                String user_facebook;
                                Log.i("url", "url probandoo2 " + lista.getJSONObject(i));
                                int ids = lista.getJSONObject(i).getInt("id");
                                String usuario = lista.getJSONObject(i).getString("usuario");
                                String nombre = lista.getJSONObject(i).getString("nombre");
                                String foto = lista.getJSONObject(i).getString("foto");
                                if (lista.getJSONObject(i).isNull("userFacebook")) {
                                    user_facebook = "facebook";
                                } else {
                                    user_facebook = lista.getJSONObject(i).getString("userFacebook");
                                }
                                if(lista.getJSONObject(i).isNull("telefono"))
                                {
                                    Log.i("telef","telefono null");
                                    telefono = "";
                                }
                                else
                                {
                                    telefono = lista.getJSONObject(i).getString("telefono");
                                }
                                if(lista.getJSONObject(i).isNull("ciudad"))
                                {
                                    Log.i("city"," city is null");
                                    ciudad = "";
                                }
                                else {
                                    ciudad = lista.getJSONObject(i).getString("ciudad");
                                }
                                Log.i("userFacebook123", " usuario " + usuario + " ciudad " + ciudad + " telefono " + telefono);
                                pref.createLoginSessionFacebook(ids, usuario, nombre, foto, telefono,user_facebook);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(lista.length()<=0)
                        {
                            Log.i("lista"," "+lista.length());
                            Toast.makeText(getApplicationContext(),"No existe usuario/Contraseña mal escrita",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Log.i("lista"," "+lista.length());
                            llamarActividadPrincipal();
                        }


                        // Log.i("practica", "valor " + aud.getUrlRosario());
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario/Contraseña mal escrita", Toast.LENGTH_SHORT).show();
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

    public void llamarActividadPrincipal() {
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
   /* @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }*/


    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

   /* @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }
*/

    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.imgOjo:

                if(flag==0)
                {
                    mPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    flag=1;
                }
                else if(flag==1)
                {
                    mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    flag=0;
                }

                break;
            case R.id.textsoyNuevo:
                Intent intent= new Intent(this, SoyNuevo.class);
                startActivity(intent);
                break;
        }
    }

  /*  private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }*/


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        final ShirSessionManager pref = new ShirSessionManager(getApplicationContext());
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String newURL="http://"+getString(R.string.url)+"/SintonizadosWS/servicio/usuario/login?";
            try {
                try {

                    newURL = newURL+"usuario="+ URLEncoder.encode(mEmail, "utf-8")+"&contrasenia="+URLEncoder.encode(mPassword,"utf-8");
                    Log.i("url2", "url " + newURL);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                RequestApp request = new RequestApp(newURL, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonobj) {
                        String resultado = jsonobj.optString("resultado").trim();
                        Log.i("resultado"," "+resultado);
                        if(resultado.equalsIgnoreCase("ok")){

                            JSONArray lista = jsonobj.optJSONArray("lista");
                            Log.i("url", "tamaño lista "+lista.length() );
                            for (int i = 0; i < lista.length(); i++) {
                                try {
                                    String email;
                                    Log.i("url", "url probandoo2 " + lista.getJSONObject(i));
                                    int ids=lista.getJSONObject(i).getInt("id");
                                    String usuario= lista.getJSONObject(i).getString("user_facebook");
                                    String nombre= lista.getJSONObject(i).getString("nombre");
                                    String foto= lista.getJSONObject(i).getString("foto");
                                    if(lista.getJSONObject(i).isNull("email"))
                                    {
                                        email="";
                                    }
                                    else
                                    {
                                        email=lista.getJSONObject(i).getString("email");
                                    }
                                    Log.i("userFacebook123", " usuario " + usuario + " nombre " + nombre + " foto " + foto + " emsail " + email);
                                   // pref.createLoginSessionFacebook(ids, usuario, nombre, foto, email);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                            if(lista.length()<=0)
                            {
                                Log.i("lista"," "+lista.length());
                                Toast.makeText(getApplicationContext(), "No existe usuario/Contraseña mal escrita", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Log.i("lista"," si hay valor "+lista.length());
                                //llamarActividadPrincipal();
                            }

                            // Log.i("practica", "valor " + aud.getUrlRosario());
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Usuario ya existe",Toast.LENGTH_SHORT).show();
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
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
           // showProgress(false);

            if (success) {

                finish();
            } else {
               Toast.makeText(getApplicationContext(),"Usuario y/o Contraseña incorrecta",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }
}

