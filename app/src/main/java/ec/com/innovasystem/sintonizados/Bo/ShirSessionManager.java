package ec.com.innovasystem.sintonizados.Bo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ShirSessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = Context.MODE_PRIVATE;
    private static final String PREF_NAME = "preferenciasnutricionistasapp";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NOMBRE = "nombreusuariologin";
    public static final String IMG_USER = ".jpg";
    public static final int    USER_ID = 10;
    public static final String KEY_USER = "usuarioususu";
    public static final String REGISTRO = "false";
    public static final String KEY_TELEFONO= "2365435";
    public static final String KEY_FACEBOOK="facebook";
    public static final String KEY_FECHA = "25/08/2005";


    public ShirSessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.commit();
    }

    public void createLoginSessionFacebook(int userId,
                                   String nombre,
                                   String nombreUsuario,
                                   String img, String telefono, String userFacebook) {
        Log.i("crearLogin", " telefono " + telefono + " imagen " + img );
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(String.valueOf(USER_ID), userId);
        editor.putString(KEY_USER, nombre);
        editor.putString(KEY_NOMBRE, nombreUsuario);
        editor.putString(IMG_USER,img);
        editor.putString(KEY_TELEFONO, telefono);
        editor.putString(KEY_FACEBOOK, userFacebook);
        editor.commit();
    }

    public void createLoginSessionFacebookSinId(String nombre,
                                   String img, String correo, String telefono, String ciudad) {
        Log.i("crearLogin2", " nombre " + nombre + " usuario");
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER, nombre);
        editor.putString(IMG_USER,img);
        editor.putString(KEY_TELEFONO, telefono);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public void logoutUser(){
        removercredenciales();
    }
    public void removercredenciales(){
        editor.remove(IS_LOGIN);
        editor.remove(KEY_USER);
        editor.remove(KEY_NOMBRE);
        editor.remove(IMG_USER);
        editor.remove(KEY_TELEFONO);
        editor.remove(KEY_FACEBOOK);

        editor.commit();
    }

    public MrParametros obtenerUserData(){
        MrParametros objparam = new MrParametros();
        objparam.setIsLogin(pref.getBoolean(IS_LOGIN, false));
        objparam.setUserId(pref.getInt(String.valueOf(USER_ID), 0));
        objparam.setNombre_usuario(pref.getString(KEY_NOMBRE, ""));
        objparam.setUrlFoto(pref.getString(IMG_USER, ""));
        objparam.setUser(pref.getString(KEY_USER, ""));
        objparam.setTelefono(pref.getString(KEY_TELEFONO, ""));
        objparam.setFacebook(pref.getString(KEY_FACEBOOK, ""));
        Log.i("userData"," nombre de usuario "+pref.getString(KEY_NOMBRE, ""));
        return objparam;
    }

    public boolean usuarioRegistrado(){
        return pref.getBoolean(REGISTRO,false);
    }

    public void crearRegistro(){
        editor.putBoolean(REGISTRO, true);
        editor.commit();
    }


}
