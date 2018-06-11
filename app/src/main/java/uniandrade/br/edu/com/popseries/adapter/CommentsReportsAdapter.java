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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.helper.Base64Custom;
import uniandrade.br.edu.com.popseries.helper.Preferencias;
import uniandrade.br.edu.com.popseries.model.Comentario;

/**
 * Created by pnda on 25/05/18.
 *
 */

public class CommentsReportsAdapter extends RecyclerView.Adapter<CommentsReportsAdapter.ViewHolder> {
    private List<Comentario> mComentarioList;
    private Context mContext;

    public CommentsReportsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public CommentsReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_reports_layout, parent, false);

        return new CommentsReportsAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(CommentsReportsAdapter.ViewHolder holder, int position) {
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
        private TextView icCommentOk;


        private ViewHolder(final View itemView) {
            super(itemView);

            imgUserPhoto = itemView.findViewById(R.id.imgUserPhotoComentario);
            txtUserName = itemView.findViewById(R.id.txtUserCommentName);
            txtComentario = itemView.findViewById(R.id.txtUserComment);
            txtReportComment = itemView.findViewById(R.id.txtReportComment);
            txtDateComment = itemView.findViewById(R.id.txtDateComment);
            icCommentOk = itemView.findViewById(R.id.icCommentOk);

            icCommentOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if ( pos != RecyclerView.NO_POSITION ){
                        int position = getAdapterPosition();
                        Comentario comentario = mComentarioList.get(position);
                        abrirAvisoOkay( comentario );
                    }
                }
            });

            txtReportComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if ( pos != RecyclerView.NO_POSITION ){
                        int position = getAdapterPosition();
                        Comentario comentario = mComentarioList.get(position);
                        abrirAvisoExclusao( comentario );
                    }
                }
            });
        }
    }

    private void abrirAvisoOkay(final Comentario comentario){
        //atributo da classe.
        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //define o titulo
        builder.setTitle("Permitir Comentário");
        builder.setIcon(R.drawable.ic_alert);
        //define a mensagem
        builder.setMessage("Deseja realmente permitir este comentário ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                permitirComentario( comentario.getSerie_id(), comentario.getUser_email() );
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

    private void abrirAvisoExclusao(final Comentario comentario) {
        //atributo da classe.
        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //define o titulo
        builder.setTitle("Remover Comentário");
        builder.setIcon(R.drawable.ic_alert);
        //define a mensagem
        builder.setMessage("Deseja realmente remover este comentário ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                excluirComentario( comentario.getSerie_id(), comentario.getUser_email() );
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

    private void excluirComentario(String serieId, String userEmail) {
        String userID = Base64Custom.encodeBase64(userEmail);
        DatabaseReference comentarioUsuario = ConfigFirebase.getFirebase().child("comentarios_usuarios")
                .child( userID ).child( serieId );
        DatabaseReference comentarioSerie = ConfigFirebase.getFirebase().child("comentarios_series")
                .child( serieId ).child( userID );
        DatabaseReference commentReport = ConfigFirebase.getFirebase().child("comentarios_reportados")
                .child( serieId ).child("reportados").child( userID );

        try {
            comentarioUsuario.removeValue();
            comentarioSerie.removeValue();
            commentReport.removeValue();
            Toast.makeText(mContext, "Comentário Excluido com Sucesso! ", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void permitirComentario(String serieId, String userEmail) {
        String userID = Base64Custom.encodeBase64(userEmail);
        DatabaseReference commentReport = ConfigFirebase.getFirebase().child("comentarios_reportados")
                .child( serieId ).child("reportados").child( userID );

        try {
            commentReport.removeValue();
            Toast.makeText(mContext, "Comentário Permitido com Sucesso! ", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

}
