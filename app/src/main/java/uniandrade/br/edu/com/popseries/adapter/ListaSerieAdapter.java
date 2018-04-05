package uniandrade.br.edu.com.popseries.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.model.Date;
import uniandrade.br.edu.com.popseries.views.DetalhesActivity;
import uniandrade.br.edu.com.popseries.api.SeriesResults;

/**
 * Created by pnda on 14/12/17.
 * Classe adapter para disponibilizar as séries em lista
 */

public class  ListaSerieAdapter extends RecyclerView.Adapter<ListaSerieAdapter.ViewHolder> {

    private List<SeriesResults.ResultsBean> mSerieList;
    private Context mContext;

    public ListaSerieAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public ListaSerieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_series_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListaSerieAdapter.ViewHolder holder, int position) {
        Date date = new Date();
        SeriesResults.ResultsBean seriesResults = mSerieList.get(position);
        //AVALIAÇÃO
        String avaliacao = Double.toString(seriesResults.getVote_average());
        holder.txtAvaliacao.setText(avaliacao);

        //THUMBNAIL
        if (seriesResults.getPoster_path().isEmpty()){
            //holder.imgCapaSerie.setImageResource();
        } else {
            Picasso.with(mContext)
                    .load(seriesResults.getPoster_path())
                    .into(holder.imgThumbnail);
        }

        // NOME
        holder.txtNomeSerie.setText(seriesResults.getName());

        //DATA LANÇAMENTO
        String data = seriesResults.getFirst_air_date();
        if (data == null || data.isEmpty()){
            holder.txtReleaseDate.setText(R.string.date_realesed_unknow);
        }else{
            date.setData(data);
            holder.txtReleaseDate.setText(date.getYear());
        }

        //OVERVIEW
        holder.txtOverview.setText(seriesResults.getOverview());

    }

    @Override
    public int getItemCount() {
        return (mSerieList == null) ? 0 : mSerieList.size();
    }

    public void adicionarListaSeries(List<SeriesResults.ResultsBean> listSeries) {
        mSerieList = new ArrayList<>();
        mSerieList.addAll(listSeries);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtAvaliacao;
        private ImageView imgThumbnail;
        private TextView txtNomeSerie;
        private TextView txtReleaseDate;
        private TextView txtOverview;

        private ViewHolder(View ItemView){
            super(ItemView);

            txtAvaliacao = itemView.findViewById(R.id.txtAvaliacao);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            txtNomeSerie = itemView.findViewById(R.id.txtNomeSerie);
            txtReleaseDate = itemView.findViewById(R.id.txtReleaseDate);
            txtOverview = itemView.findViewById(R.id.txtOverview);

            final Bundle bundle = new Bundle();

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = getAdapterPosition();
                    if ( pos != RecyclerView.NO_POSITION ){
                        SeriesResults.ResultsBean clickedDataItem = mSerieList.get(pos);
                        Toast.makeText(view.getContext(), clickedDataItem.getOriginal_name(), Toast.LENGTH_SHORT).show();

                    }
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if ( pos != RecyclerView.NO_POSITION ){
                        SeriesResults.ResultsBean seriesResults = mSerieList.get(pos);
                        Intent intent = new Intent(mContext, DetalhesActivity.class);
                        bundle.putInt("serie_id", seriesResults.getId());
                        bundle.putString("poster", seriesResults.getBackdrop_path());
                        bundle.putString("original_title", seriesResults.getName());
                        bundle.putString("overview", seriesResults.getOverview());
                        bundle.putString("apiRate", Double.toString(seriesResults.getVote_average()));
                        bundle.putString("data_lancamento", seriesResults.getFirst_air_date());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }
            });

        }
    }

}
