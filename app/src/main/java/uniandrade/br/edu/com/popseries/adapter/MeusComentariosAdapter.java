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
import uniandrade.br.edu.com.popseries.helper.Preferencias;
import uniandrade.br.edu.com.popseries.model.Avaliacao;
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    abrirAviso( position );
                    return true;
                }
            });

        }
    }

    private void abrirAviso(int position) {
        final Comentario comentario = mComentarioList.get(position);
        //atributo da classe.
        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //define o titulo
        builder.setTitle("Remover Comentário");
        builder.setIcon(R.drawable.ic_alert);
        //define a mensagem
        builder.setMessage("Deseja realmente excluir este comentário ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                excluirAvaliacao( comentario.getSerie_id() );
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(mContext, "Não " + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void excluirAvaliacao(String serie_id) {
        Preferencias preferencias = new Preferencias(mContext);
        DatabaseReference comentarioUsuario = ConfigFirebase.getFirebase().child("comentarios_usuarios")
                .child( preferencias.getIdentificador() ).child( serie_id );
        DatabaseReference comentarioSerie = ConfigFirebase.getFirebase().child("comentarios_series")
                .child( serie_id ).child( preferencias.getIdentificador() );

        try {
            comentarioUsuario.removeValue();
            comentarioSerie.removeValue();
            Toast.makeText(mContext, "Comentário Excluido com Sucesso! ", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}