package ec.com.innovasystem.sintonizados.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ec.com.innovasystem.sintonizados.Bo.ShirSessionManager;
import ec.com.innovasystem.sintonizados.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ShirSessionManager pref = new ShirSessionManager(this);
        if(pref.isLoggedIn())
        {

            Log.i("boolLogueo", "esta guardado el login");
            this.finish();
            llamarActividadPrincipal();
        }
        else {
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(Splash.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timerThread.start();
        }
    }

    public void llamarActividadPrincipal() {
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
