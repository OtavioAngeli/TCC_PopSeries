package uniandrade.br.edu.com.popseries.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.model.Serie;
import uniandrade.br.edu.com.popseries.model.SeriesResult;


/**
 * Created by pnda on 14/12/17.
 * Classe adapter para disponibilizar as séries em lista
 */

public class SerieAdapter {

//    private Context mContext;
//    private List<Serie> serieList;
//
//    public SerieAdapter(Context mContext, List<Serie> serieList ){
//        this.mContext = mContext;
//        this.serieList = serieList;
//    }
//
//    @Override
//    public SerieAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.lista_series_layout, viewGroup, false);
//
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final SerieAdapter.MyViewHolder viewHolder, int i){
//        viewHolder.txtNomeSerie.setText(serieList.get(i).getOriginal_name());
//
//        Glide
//                .with(mContext)
//                .load(serieList.get(i).getPoster_path())
//                .into(viewHolder.imgThumbnail);
//
//    }
//
//    @Override
//    public int getItemCount(){
//        return serieList.size();
//    }
//
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView txtNomeSerie, txtDataLancamento;
//        public ImageView imgThumbnail;
//
//        public MyViewHolder(View view){
//            super(view);
//
//            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
//            txtNomeSerie = itemView.findViewById(R.id.txtNomeSerie);
//            txtDataLancamento = itemView.findViewById(R.id.txtReleaseDate);
//
//        }
//
//    }
}
