package ec.com.innovasystem.sintonizados.adapter;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ec.com.innovasystem.sintonizados.Bo.AuspiciantesBo;
import ec.com.innovasystem.sintonizados.Bo.GaleriaBo;
import ec.com.innovasystem.sintonizados.R;
import ec.com.innovasystem.sintonizados.actividades.MainActivity;
import ec.com.innovasystem.sintonizados.fragment.Galeria;

/**
 * Created by InnovaCaicedo on 17/6/2016.
 */
public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.MetaViewHolder> {

    public List<GaleriaBo> items;
    public int id;
    private Context context;
    private Galeria fragment;
    public GaleriaAdapter(Context context, Galeria galeria)
    {
        this.context=context;
        this.fragment=galeria;
        this.items=new ArrayList<GaleriaBo>();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public MetaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_galeria, viewGroup, false);
        return new MetaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MetaViewHolder viewHolder, final int position) {
        final GaleriaBo gale = items.get(position);
        Picasso.with(context)
                .load(gale.getFoto())
                .placeholder(R.color.transparent_white_15_porc)
                        //.transform(new RoundedTransformation(50, 2))// optional
                .resize(1080, 580)
                .error(R.color.transparent_white_15_porc)               // optional
                .into(viewHolder.imgGaleria);
        viewHolder.imgGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("clickkk", " ingresadooo i "+position);
               fragment.verDatos(gale.getFoto(), gale.getTexto(), gale.getFecha());
            }
        });
    }

    public class MetaViewHolder extends RecyclerView.ViewHolder
    {
        // Campos respectivos de un item
        public ImageView imgGaleria;
        public LinearLayout content;
        //otros

        public MetaViewHolder(final View v) {
            super(v);
//            Typeface fontTitle = Typeface.createFromAsset(context.getResources().getAssets(), "AkzidenzGrotesk-LightCond.otf");
            imgGaleria = (ImageView)v.findViewById(R.id.imgGaleria);
            content = (LinearLayout)v.findViewById(R.id.ly_content_papa);

            // nombre_papa.setTypeface(fontTitle);
            // lugarNacimientoPonificado.setTypeface(fontTitle);
            // fecha_ejercicio = (TextView)v.findViewById(R.id.tv_fecha_ejercicio);

        }

    }

}
