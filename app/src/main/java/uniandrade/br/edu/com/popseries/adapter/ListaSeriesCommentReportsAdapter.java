package uniandrade.br.edu.com.popseries.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import uniandrade.br.edu.com.popseries.model.Comentario;
import uniandrade.br.edu.com.popseries.model.CommentReport;
import uniandrade.br.edu.com.popseries.model.Serie;

/**
 * Created by pnda on 24/05/18.
 *
 */

public class ListaSeriesCommentReportsAdapter extends RecyclerView.Adapter<ListaSeriesCommentReportsAdapter.ViewHolder>{
    private List<CommentReport> mComentarioList;
    private Context mContext;

    public ListaSeriesCommentReportsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ListaSeriesCommentReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.series_comments_reports, parent, false);

        return new ListaSeriesCommentReportsAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(ListaSeriesCommentReportsAdapter.ViewHolder holder, int position) {
        CommentReport comentario = mComentarioList.get(position);

        holder.txtSerieNameCommentReport.setText(comentario.getSerie_name());
        holder.txtNumCommentReport.setText(String.valueOf(comentario.getNum_comment_reports()));

        Picasso.with(mContext)
                .load(comentario.getSerie_poster()).noFade()
                .into(holder.imgSeriePhotoComment);

    }

    @Override
    public int getItemCount() {
        return (mComentarioList == null) ? 0 : mComentarioList.size();
    }

    public void adicionarListaComentarios(List<CommentReport> listSeries) {
        mComentarioList = new ArrayList<>();
        mComentarioList.addAll(listSeries);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgSeriePhotoComment;
        private TextView txtSerieNameCommentReport;
        private TextView txtNumCommentReport;

        private ViewHolder(final View itemView) {
            super(itemView);

            imgSeriePhotoComment = itemView.findViewById(R.id.imgSeriePhotoCommentReport);
            txtSerieNameCommentReport = itemView.findViewById(R.id.txtSerieNameCommentReport);
            txtNumCommentReport = itemView.findViewById(R.id.txtNumCommentsReports);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if ( pos != RecyclerView.NO_POSITION ){
                        int position = getAdapterPosition();
                        CommentReport comentario = mComentarioList.get(position);
                        abrirComentarios( comentario );
                    }
                }
            });
        }
    }

    private void abrirComentarios(CommentReport comentario) {
        Toast.makeText(mContext, comentario.getSerie_id(), Toast.LENGTH_SHORT).show();
    }
}
