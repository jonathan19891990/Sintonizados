package ec.com.innovasystem.sintonizados.chatapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ec.com.innovasystem.sintonizados.R;

public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    private String mUsername;
    private Activity activity;
    private Context context;
    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
        this.activity=activity;

    }


    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
        String author = chat.getAuthor();
        TextView authorText = (TextView) view.findViewById(R.id.author);
        ImageView imgFoto=(ImageView)view.findViewById(R.id.imgImagen);


        authorText.setText(author + ": ");
        // If the message was sent by this user, color it differently
        Log.i("prueba adapter ", "autor " + author + " usuario " + mUsername);
        Log.i("face","chat "+chat.getFacebook());
        if (author != null && author.equals(mUsername)) {
            authorText.setTextColor(Color.BLUE);

            if(!chat.getFacebook().equalsIgnoreCase("facebook"))
            {
                Log.i("face","facebook");
                Picasso.with(activity)
                        .load(chat.getImagen())
                        .placeholder(R.color.transparent_white_15_porc)
                        .error(R.color.transparent_white_15_porc)               // optional
                        .into(imgFoto);
            }
            else
            {
                try {
                    Picasso.with(activity)
                            .load("http://10.10.1.4:8080/SintonizadosWS/servicio/usuario/obtenerimagen?arg0=" + URLEncoder.encode(chat.getImagen(), "utf-8"))
                            .placeholder(R.color.transparent_white_15_porc)
                        .error(R.color.transparent_white_15_porc)               // optional
                        .into(imgFoto);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        } else {
            authorText.setTextColor(Color.BLACK);
            if(!chat.getFacebook().equalsIgnoreCase("facebook"))
            {
                Log.i("face","facebook");
                Picasso.with(activity)
                        .load(chat.getImagen())
                        .placeholder(R.color.transparent_white_15_porc)
                        .error(R.color.transparent_white_15_porc)               // optional
                        .into(imgFoto);
            }
            else
            {
                try {
                    Picasso.with(activity)

                            .load("http://10.10.1.4:8080/SintonizadosWS/servicio/usuario/obtenerimagen?arg0=" + URLEncoder.encode(chat.getImagen(), "utf-8"))
                            .placeholder(R.color.transparent_white_15_porc)
                            .error(R.color.transparent_white_15_porc)               // optional
                            .into(imgFoto);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
        ((TextView) view.findViewById(R.id.message)).setText(chat.getMessage());
    }
}
