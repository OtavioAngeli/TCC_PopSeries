package uniandrade.br.edu.com.popseries.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.model.Avaliacao;
import uniandrade.br.edu.com.popseries.model.Comentario;

/**
 * Created by pnda on 29/04/18.
 *
 */

public class MinhasAvaliacoesAdapter extends RecyclerView.Adapter<MinhasAvaliacoesAdapter.ViewHolder>{
    private List<Avaliacao> mAvaliacaoList;
    private Context mContext;

    public MinhasAvaliacoesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public MinhasAvaliacoesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avaliacao_serie_layout, parent, false);

        return new MinhasAvaliacoesAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(MinhasAvaliacoesAdapter.ViewHolder holder, int position) {
        Avaliacao avaliacao = mAvaliacaoList.get(position);
        float notaAvaliacao = avaliacao.getAvaliacao();

        holder.txtSerieName.setText(avaliacao.getSerie_name());
        holder.notaAvaliacao.setRating( notaAvaliacao );


        Picasso.with(mContext)
                .load( avaliacao.getSerie_poster() ).noFade()
                .into(holder.imgUserPhoto);

    }

    @Override
    public int getItemCount() {
        return (mAvaliacaoList == null) ? 0 : mAvaliacaoList.size();
    }

    public void adicionarListaAvaliacao(List<Avaliacao> listComents) {
        mAvaliacaoList = new ArrayList<>();
        mAvaliacaoList.addAll(listComents);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUserPhoto;
        private TextView txtSerieName;
        private RatingBar notaAvaliacao;

        private ViewHolder(final View itemView) {
            super(itemView);

            imgUserPhoto = itemView.findViewById(R.id.imgAvaliacaoSeriePhoto    );
            txtSerieName = itemView.findViewById(R.id.txtAvaliacaoSerieName);
            notaAvaliacao = itemView.findViewById(R.id.notaAvaliacao);

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
