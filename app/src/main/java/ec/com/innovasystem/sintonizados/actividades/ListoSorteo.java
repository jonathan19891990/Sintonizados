package ec.com.innovasystem.sintonizados.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ec.com.innovasystem.sintonizados.R;

public class ListoSorteo extends AppCompatActivity implements View.OnClickListener{
    private TextView textListo;
    private Button btnListo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listo_sorteo);
        textListo=(TextView)findViewById(R.id.textListo);
        String texto="Ya estás participando, gracias por confiar en nosotros, enviamos una copia a tu correo." +
                "<br><b>Nos vemos pronto!<b>";
        textListo.setText(Html.fromHtml(texto));
        btnListo=(Button)findViewById(R.id.btnListo);
        btnListo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnListo:
                finish();
                break;
        }
    }
}
