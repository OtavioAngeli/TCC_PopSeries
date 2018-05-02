package uniandrade.br.edu.com.popseries.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.model.Comentario;

/**
 * Created by pnda on 29/04/18.
 *
 */

public class MeusComentariosAdapter extends RecyclerView.Adapter<MeusComentariosAdapter.ViewHolder>{
    private List<Comentario> mComentarioList;
    private Context mContext;

    public MeusComentariosAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public MeusComentariosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comentario_usuario_layout, parent, false);

        return new MeusComentariosAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(MeusComentariosAdapter.ViewHolder holder, int position) {
        Comentario comentario = mComentarioList.get(position);

        holder.txtUserName.setText(comentario.getSerie_name());
        holder.txtComentario.setText(comentario.getComentario());

        Picasso.with(mContext)
                .load( comentario.getSerie_poster() ).noFade()
                .into(holder.imgUserPhoto);

    }

    @Override
    public int getItemCount() {
        return (mComentarioList == null) ? 0 : mComentarioList.size();
    }

    public void adicionarListaComentarios(List<Comentario> listComents) {
        mComentarioList = new ArrayList<>();
        mComentarioList.addAll(listComents);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUserPhoto;
        private TextView txtUserName;
        private TextView txtComentario;


        private ViewHolder(final View itemView) {
            super(itemView);

            imgUserPhoto = itemView.findViewById(R.id.imgSeriePhoto);
            txtUserName = itemView.findViewById(R.id.txtSerieName);
            txtComentario = itemView.findViewById(R.id.txtUserComents);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = getAdapterPosition();
//                    if ( pos != RecyclerView.NO_POSITION ){
//
//                        txtNomePopup.setText(mComentarioList.get(pos).getNome());
//                        txtEmailPopup.setText(mComentarioList.get(pos).getEmail());
//                        Picasso.with(mContext)
//                                .load(mComentarioList.get(pos).getPhoto()).noFade()
//                                .into(imgPopup);
//
//                    }
//                }
//            });

        }
    }
}
