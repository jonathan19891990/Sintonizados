package ec.com.innovasystem.sintonizados.actividades;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.http.BaseActivity;
import ec.com.innovasystem.sintonizados.http.RequestApp;
import util.DateDialog;
import util.ValidatorUtil;

public class SoyNuevo extends BaseActivity implements View.OnClickListener {

    private EditText textUsuario;
    private EditText textContrasenia;
    private EditText textVerificar;
    private EditText textNombre;
    private int flag=0;
    private int flag2=0;
    private Button btnFoto;
    private Bitmap bitmap;
    private Bitmap bitmapResize;
    private ImageView imgFoto;
    private ImageView imgOjo;
    private ImageView imgOjo2;
    private Button btnCrear;
    private int PICK_IMAGE_REQUEST = 1;
    public boolean nombre, apellidos,email, telefono, fecha, usuario, repetir, foto, direccion, ciudad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle parametros = new Bundle();
        parametros.putInt("layout", R.layout.activity_soy_nuevo);
        parametros.putInt("verbtnatras", 1);
        parametros.putInt("vertoolbar", 1);
        super.onCreate(parametros);
        textUsuario=(EditText)findViewById(R.id.textUsuario);
        textContrasenia=(EditText)findViewById(R.id.textContrasenia);
        textVerificar=(EditText)findViewById(R.id.textVerificar);
        textNombre=(EditText)findViewById(R.id.textNombre);
        btnFoto=(Button)findViewById(R.id.btnFoto);
        btnFoto.setOnClickListener(this);
        imgOjo=(ImageView)findViewById(R.id.imgOjo);
        imgOjo.setOnClickListener(this);
        imgOjo2=(ImageView)findViewById(R.id.imgOjo2);
        imgOjo2.setOnClickListener(this);
        imgFoto=(ImageView)findViewById(R.id.imgFoto);
        btnCrear=(Button)findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.imgFoto);
                imageView.setBackgroundColor(22000000);
                bitmap=getResizedBitmap(bitmap,300);// android:largeHeap="true"    mayor cantidad de ram destinada a la apliccacion
                //Log.i("imagen2","imagen 2 "+ bitmapResize.toString());
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        Log.i("imagen1","imagen 1 "+ image.toString());
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgOjo:
                if(flag==0)
                {
                    textContrasenia.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    flag=1;
                }
                else if(flag==1)
                {
                    textContrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    flag=0;
                }
                break;

            case R.id.imgOjo2:
                if(flag2==0)
                {
                    textVerificar.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    flag2=1;
                }
                else if(flag2==1)
                {
                    textVerificar.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    flag2=0;
                }
                break;

            case R.id.btnFoto:
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;
            case R.id.btnCrear:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    nombre = ValidatorUtil.Texto(textNombre.getText().toString());
                    if (!nombre)
                        textNombre.setError("Minimo 4 caracteres");

                    email = ValidatorUtil.validateEmail(textUsuario.getText().toString());
                    if (!email)
                        textUsuario.setError("Correo Incorrecto");



                    repetir = ValidatorUtil.Texto(textContrasenia.getText().toString());
                    if (!repetir)
                        textContrasenia.setError("Ingrese por lo menos 4 caracteres");
                    else if (textContrasenia.getText().toString().trim().equalsIgnoreCase(textVerificar.getText().toString().trim())) {
                        repetir = true;
                    } else {
                        repetir = false;
                        textContrasenia.setError("Contraseñas Diferentes");
                    }
                    if (bitmap == null || bitmap.toString().length() <= 0) {
                        foto = false;
                        Toast.makeText(getApplicationContext(), "Escoja una foto", Toast.LENGTH_SHORT).show();
                    } else {
                        foto = true;
                    }
                    if (nombre == true  && email == true && repetir == true && foto == true) {
                        Log.i("cargar","upload foto ");
                        uploadFoto();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
        }

    public String getStringImage(Bitmap bmp){
        String encodedImage="";
        try {
            byte[] imageBytes;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(SoyNuevo.this,"error "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return encodedImage;
    }

    public void uploadFoto() {
        try {


            String newURL = "http://" + getString(R.string.url) + "/SintonizadosWS/servicio/insercion/foto";
            String image = getStringImage(bitmap);
            HashMap<String, String> map = new HashMap<>();// Mapeo previo
            JSONObject jsonpost = null;
            try {
                map.put("arg1", URLEncoder.encode(String.valueOf(textNombre.getText()), "UTF-8"));
                Log.i("texto", "texto verificar " + textVerificar.getText());
                map.put("arg2", URLEncoder.encode(String.valueOf(textVerificar.getText()), "UTF-8"));
                map.put("arg3", URLEncoder.encode(String.valueOf(textUsuario.getText()), "UTF-8"));

                map.put("arg4", URLEncoder.encode(image, "UTF-8"));

                jsonpost = new JSONObject(map);
                Log.i("multicash", jsonpost.toString());
                Log.i("multicash", "imagen: " + map.get("arg6"));
                //       //Log.i("multicash","imei: "+ map.get("arg0"));
            } catch (Exception e) {
                Log.e("multicash", e.getMessage(), e);
                Toast.makeText(SoyNuevo.this,"error "+e.getMessage(),Toast.LENGTH_LONG).show();
                return;
            }
            final RequestApp request = new RequestApp(Request.Method.POST, newURL, jsonpost,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String resultado = response.optString("resultado").trim();
                            if (resultado.equalsIgnoreCase("ok")) {
                                Toast.makeText(getApplicationContext(), "Registro Guardado", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (resultado.equalsIgnoreCase("ERROR")) {
                                Toast.makeText(getApplicationContext(), "Telefono ya ha sido registrado", Toast.LENGTH_SHORT).show();
                            } else if (resultado.equalsIgnoreCase("usuarioincorrecto")) {
                                Toast.makeText(getApplicationContext(), "Usuario ya ha sido registrado", Toast.LENGTH_SHORT).show();
                            }
                            onConnectionFinished();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString() + " ERROR", Toast.LENGTH_LONG).show();
                            //Log.d("State", error.toString());
                            //Log.i("State", error.toString());
                            Log.e("State", error.toString());
                            onConnectionFailed(error.toString());
                        }
                    });
            //RequestQueue requestQueue = Volley.newRequestQueue(this);
            //requestQueue.add(request);
            agregarPeticionHttp(request);

        }
        catch(Exception e)
        {
            Log.e("error", e.getMessage(), e);
            Toast.makeText(SoyNuevo.this,"error "+e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
