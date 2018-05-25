package uniandrade.br.edu.com.popseries.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.helper.Base64Custom;
import uniandrade.br.edu.com.popseries.model.Comentario;

/**
 * Created by pnda on 28/04/18.
 *
 */

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ViewHolder>{
    private List<Comentario> mComentarioList;
    private Context mContext;

    public ComentariosAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ComentariosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comentario_serie_layout, parent, false);

        return new ComentariosAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(ComentariosAdapter.ViewHolder holder, int position) {
        Comentario comentario = mComentarioList.get(position);

        holder.txtUserName.setText(comentario.getUser_name());
        holder.txtComentario.setText(comentario.getComentario());
        holder.txtDateComment.setText(comentario.getDate_comment());

        Picasso.with(mContext)
                .load(comentario.getUser_photo()).noFade()
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
        private TextView txtReportComment;
        private TextView txtDateComment;


        private ViewHolder(final View itemView) {
            super(itemView);

            imgUserPhoto = itemView.findViewById(R.id.imgUserPhotoComentario);
            txtUserName = itemView.findViewById(R.id.txtUserCommentName);
            txtComentario = itemView.findViewById(R.id.txtUserComment);
            txtReportComment = itemView.findViewById(R.id.txtReportComment);
            txtDateComment = itemView.findViewById(R.id.txtDateComment);

            txtReportComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if ( pos != RecyclerView.NO_POSITION ){
                        int position = getAdapterPosition();
                        Comentario comentario = mComentarioList.get(position);
                        abrirAviso( comentario );
                    }
                }
            });
        }
    }

    private void abrirAviso(final Comentario comentario) {
        //atributo da classe.
        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //define o titulo
        builder.setTitle("Reportar Comentário");
        builder.setIcon(R.drawable.ic_alert);
        //define a mensagem
        builder.setMessage("Deseja realmente reportar este comentário ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                reportComment( comentario );
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void reportComment(Comentario comentario) {
        String id = Base64Custom.encodeBase64(comentario.getUser_email());
        DatabaseReference reportComment = ConfigFirebase.getFirebase()
                .child("comentarios_reportados").child( comentario.getSerie_id() );
        DatabaseReference reportComments = ConfigFirebase.getFirebase()
                .child("comentarios_reportados").child( comentario.getSerie_id() ).child( id );

        reportComment.child("serie_name").setValue(comentario.getSerie_name());
        reportComment.child("serie_poster").setValue(comentario.getSerie_poster());
        reportComment.child("serie_id").setValue(comentario.getSerie_id());
        reportComments.setValue( comentario );
    }

}
