package uniandrade.br.edu.com.popseries.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.api.SeriesResults;

/**
 * Created by pnda on 01/03/18.
 */

public class SimilarSerieAdapter extends RecyclerView.Adapter<SimilarSerieAdapter.ViewHolder> {

    private List<SeriesResults.ResultsBean> mSerieList;
    private Context mContext;

    public SimilarSerieAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public SimilarSerieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.similar_series_layout, parent, false);
        return new SimilarSerieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SeriesResults.ResultsBean seriesResults = mSerieList.get(position);

        //THUMBNAIL
        if (seriesResults.getPoster_path().isEmpty()){
            //holder.imgCapaSerie.setImageResource();
        } else {
            Picasso.with(mContext)
                    .load(seriesResults.getPoster_path())
                    .into(holder.imgThumbnailSimilarSeries);
        }

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

        private ImageView imgThumbnailSimilarSeries;

        private ViewHolder(View ItemView){
            super(ItemView);

            imgThumbnailSimilarSeries = itemView.findViewById(R.id.imgThumbnailSimilarSeries);

        }
    }
}
