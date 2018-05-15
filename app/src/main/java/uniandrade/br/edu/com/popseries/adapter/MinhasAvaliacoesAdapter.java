package uniandrade.br.edu.com.popseries.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import uniandrade.br.edu.com.popseries.views.AvaliacaoActivity;

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

            imgUserPhoto = itemView.findViewById(R.id.imgAvaliacaoSeriePhoto);
            txtSerieName = itemView.findViewById(R.id.txtAvaliacaoSerieName);
            notaAvaliacao = itemView.findViewById(R.id.notaAvaliacao);

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
        final Avaliacao avaliacao = mAvaliacaoList.get(position);
        //atributo da classe.
        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //define o titulo
        builder.setTitle("Remover Avaliação");
        builder.setIcon(R.drawable.ic_alert);
        //define a mensagem
        builder.setMessage("Deseja realmente excluir esta avaliação");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                excluirAvaliacao( avaliacao.getSerie_id() );
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
        DatabaseReference avaliacaoUsuario = ConfigFirebase.getFirebase().child("avaliacao_usuarios")
                .child( preferencias.getIdentificador() ).child( serie_id );
        DatabaseReference avaliacaoSerie = ConfigFirebase.getFirebase().child("avaliacao_series")
                .child( serie_id ).child( preferencias.getIdentificador() );

        try {
            avaliacaoUsuario.removeValue();
            avaliacaoSerie.removeValue();
            Toast.makeText(mContext, "Avaliação Excluida ", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
