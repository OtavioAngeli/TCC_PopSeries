package uniandrade.br.edu.com.popseries.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import uniandrade.br.edu.com.popseries.R;

/**
 * Created by pnda on 14/12/17.
 * Classe adapter para disponibilizar as séries em lista
 */

public class  ListaSerieAdapter extends RecyclerView.Adapter<ListaSerieAdapter.ViewHolder> {

//    private List<SeriesResults.ItemsBean> mSerieList;
    private Context mContext;

    public ListaSerieAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public ListaSerieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.lista_series_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListaSerieAdapter.ViewHolder holder, int position) {
        // NOME
        holder.txtNomeSerie.setText("Braking Bad");

        //DATA LANÇAMENTO
        holder.txtReleaseDate.setText("Lançamento: Desconhecido");
    }

    @Override
    public int getItemCount() {
//        return (mSerieList == null) ? 0 : mSerieList.size();
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgCapaSerie;
        private TextView txtNomeSerie;
        private TextView txtReleaseDate;

        public ViewHolder(View ItemView){
            super(ItemView);

            imgCapaSerie = itemView.findViewById(R.id.imgCapaSerie);
            txtNomeSerie = itemView.findViewById(R.id.txtNomeSerie);
            txtReleaseDate = itemView.findViewById(R.id.txtReleaseDate);

        }
    }

}
