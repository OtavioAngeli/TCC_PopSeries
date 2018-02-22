package uniandrade.br.edu.com.popseries.adapter;

import android.content.Context;
import android.content.Intent;
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
        SeriesResults.ResultsBean seriesResults = mSerieList.get(position);
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
        if (seriesResults.getFirst_air_date().equals("")){
            holder.txtReleaseDate.setText("Lançamento: Desconhecido" );
        }else{
            holder.txtReleaseDate.setText("Lançamento: " + seriesResults.getFirst_air_date());
        }
    }

    @Override
    public int getItemCount() {
        return (mSerieList == null) ? 0 : mSerieList.size();
//        return 5;
    }

    public void adicionarListaSeries(List<SeriesResults.ResultsBean> listSeries) {
        mSerieList = new ArrayList<>();
        mSerieList.addAll(listSeries);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgThumbnail;
        private TextView txtNomeSerie;
        private TextView txtReleaseDate;

        private ViewHolder(View ItemView){
            super(ItemView);

            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            txtNomeSerie = itemView.findViewById(R.id.txtNomeSerie);
            txtReleaseDate = itemView.findViewById(R.id.txtReleaseDate);

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
//                        Intent intent = new Intent(mContext, DetalhesActivity.class);
//                        intent.putExtra("original_title", mSerieList.get(pos).getName());
//                        mContext.startActivity(intent);
                    }
                }
            });

        }
    }

}
