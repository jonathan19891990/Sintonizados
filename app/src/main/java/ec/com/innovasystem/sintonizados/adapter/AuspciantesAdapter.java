package ec.com.innovasystem.sintonizados.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ec.com.innovasystem.sintonizados.Bo.AuspiciantesBo;
import ec.com.innovasystem.sintonizados.R;

/**
 * Created by InnovaCaicedo on 8/6/2016.
 */
public class AuspciantesAdapter extends RecyclerView.Adapter<AuspciantesAdapter.MetaViewHolder> {

    public List<AuspiciantesBo> items;
    public int id;
    private Context context;

    public AuspciantesAdapter(Context context)
    {
       this.context=context;
        this.items=new ArrayList<AuspiciantesBo>();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public MetaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_auspiciante, viewGroup, false);
        return new MetaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MetaViewHolder viewHolder, int position) {
        final AuspiciantesBo biografia = items.get(position);
        viewHolder.nombre.setText("" + biografia.getNombre().toUpperCase());
        viewHolder.texto.setText(biografia.getTexto());
        Picasso.with(context)
                .load(biografia.getUrl())
                .placeholder(R.color.transparent_white_15_porc)
                //.transform(new RoundedTransformation(50, 2))// optional
                .resize(230, 150)
                .error(R.color.transparent_white_15_porc)               // optional
                .into(viewHolder.imgAuspiciante);

      /*  viewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //metodos = new BioPapasFragment();
                        //metodos.llamarActivity(biografia.getNombre().toUpperCase(), biografia.getDescripcion(), biografia.getFotoDescripcion());
                        Intent irDescBio = new Intent(context,BiografiaPapasActivity.class);
                        irDescBio.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        irDescBio.putExtra("descripcion",biografia.getDescripcion());
                        irDescBio.putExtra("foto",biografia.getFoto());
                        irDescBio.putExtra("nombre", biografia.getNombre());
                        context.startActivity(irDescBio);
                        Log.i("Foto", "" + biografia.getFotoDescripcion());
                    }
                });
            }
        });*/

    }

    public class MetaViewHolder extends RecyclerView.ViewHolder
    {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView texto;
        public ImageView imgAuspiciante;
        public LinearLayout content;
        //otros

        public MetaViewHolder(final View v) {
            super(v);
//            Typeface fontTitle = Typeface.createFromAsset(context.getResources().getAssets(), "AkzidenzGrotesk-LightCond.otf");
            nombre = (TextView) v.findViewById(R.id.nombre);
            texto = (TextView) v.findViewById(R.id.texto);
            imgAuspiciante = (ImageView)v.findViewById(R.id.imgAuspiciante);
            content = (LinearLayout)v.findViewById(R.id.ly_content_papa);

            // nombre_papa.setTypeface(fontTitle);
            // lugarNacimientoPonificado.setTypeface(fontTitle);
            // fecha_ejercicio = (TextView)v.findViewById(R.id.tv_fecha_ejercicio);

        }

    }

}
