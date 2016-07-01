package ec.com.innovasystem.sintonizados.actividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;
import ec.com.innovasystem.sintonizados.Bo.ShirSessionManager;
import ec.com.innovasystem.sintonizados.chatapp.ChatVideoActivity;
import ec.com.innovasystem.sintonizados.fragment.Auspiciantes;
import ec.com.innovasystem.sintonizados.fragment.Comentario;
import ec.com.innovasystem.sintonizados.fragment.Galeria;
import ec.com.innovasystem.sintonizados.fragment.Home;
import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.fragment.Sintonizados;
import ec.com.innovasystem.sintonizados.fragment.SorteoSlider;
import ec.com.innovasystem.sintonizados.http.BaseActivity;
import util.CustomTypefaceSpan;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Typeface typeface, titulos;
    private MediaPlayer  mediaPlayer;
    private MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle parametros = new Bundle();
        parametros.putInt("layout", R.layout.activity_main);
        super.onCreate(parametros);
        Home homeFrag= new Home();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, homeFrag).commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button showevents=(Button) toolbar  .findViewById(R.id.showevents);
        showevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();

                    try {
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource("http://176.31.246.109:8000/jcradio13");
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });
                        // mediaPlayer.prepare();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    } else if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }

                }
            }
        });

        final ShirSessionManager pref = new ShirSessionManager(this);
        Log.i("main","correo "+pref.obtenerUserData().getUser());

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        //men vez de mostrar HAMBURGUER ICON MOSTRAR MI PROPIA IMAGEN Y AÑADIR EVENTO ONCLICK PARA QUE FUNCIONE
        toggle.setHomeAsUpIndicator(R.mipmap.icono_menu);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });



        //drawer.setDrawerListener(toggle);
        //toggle.syncState();
        typeface=Typeface.createFromAsset(getAssets(), "tipografia/MyriadPro-Regular.otf");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        //ImageView imgEditar=(ImageView) header.findViewById(R.id.idEditar);
        /*imgEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("login facebook","login "+pref.obtenerUserData().getFacebook());
                if(pref.obtenerUserData().getFacebook().trim().equals("facebook"))
                {
                    Toast.makeText(MainActivity.this,"no hay facebook",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this,"facebook",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        TextView textNombre = (TextView) header.findViewById(R.id.textNombre);
        TextView textTelefono=(TextView) header.findViewById(R.id.texTelefono);

        CircleImageView imgFoto=(CircleImageView) header.findViewById(R.id.img_url);
        if (pref.obtenerUserData().getNombre_usuario() == null) {
            textNombre.setText("");
        } else {
            textNombre.setText(pref.obtenerUserData().getNombre_usuario());

        }
        if(pref.obtenerUserData().getEmail()==null)
        {
            textTelefono.setText("");
        }
        else
        {
            textTelefono.setText(pref.obtenerUserData().getEmail());
        }
        if (pref.obtenerUserData().getUrlFoto() == null) {

        } else {
            if(pref.obtenerUserData().getFacebook().equalsIgnoreCase("facebook"))
            {
                try {
                    Log.i("pica","no hay face"+pref.obtenerUserData().getFacebook().length());
                    Picasso.with(this)
                            .load("http://" + getString(R.string.url) +"/SintonizadosWS/servicio/usuario/obtenerimagen?arg0=" + URLEncoder.encode(pref.obtenerUserData().getUrlFoto(), "utf-8"))
                            .placeholder(R.color.transparent_white_15_porc)
                            .error(R.color.transparent_white_15_porc)               // optional
                            .into(imgFoto);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.i("picas","si hay face "+pref.obtenerUserData().getFacebook().length());
                Picasso.with(this)
                        .load(pref.obtenerUserData().getUrlFoto())
                        .placeholder(R.color.transparent_white_15_porc)
                        .error(R.color.transparent_white_15_porc)               // optional
                        .into(imgFoto);
            }
        }



        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);

        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(),  "tipografia/MyriadPro-Regular.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final ShirSessionManager pref= new ShirSessionManager(this);
        if (id == R.id.nav_home) {
            Home homeFrag= new Home();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, homeFrag).commit();
        } else
        if (id == R.id.nav_eventos) {

            // Handle the camera action
        } else if (id == R.id.nav_sintonizados) {
            Sintonizados sin= new Sintonizados();
            getSupportFragmentManager().beginTransaction()
            .replace(R.id.content_frame, sin).commit();
        } else if (id == R.id.nav_galeria) {
            Galeria gal= new Galeria();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, gal).commit();
        } else if (id == R.id.nav_sorteo) {
            SorteoSlider sorteo= new SorteoSlider();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, sorteo).commit();
        }
        else if (id == R.id.nav_cabinas) {
            Intent intent= new Intent(MainActivity.this, ChatVideoActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_contacto) {
            Comentario comen= new Comentario();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, comen).commit();
        }
        else if (id == R.id.nav_auspiciante) {
            Auspiciantes ausp= new Auspiciantes();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, ausp).commit();
        }

        else if (id == R.id.nav_cerrasesion) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Cerrando aplicación")
                    .setMessage("¿Estas seguro que deseas cerrar la aplicación?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(FacebookSdk.isInitialized())
                            {
                                Log.i("facemain","main cerrar sesion face " );
                                LoginManager.getInstance().logOut();
                                pref.logoutUser();

                                finish();
                            }
                            else {
                                Log.i("facemain","logueado en face ");
                                //  LoginManager.getInstance().logOut();
                                pref.logoutUser();

                                finish();
                            }
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
