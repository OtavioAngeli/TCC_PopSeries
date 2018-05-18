package uniandrade.br.edu.com.popseries.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.api.SeriesResults;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.helper.Base64Custom;
import uniandrade.br.edu.com.popseries.helper.Preferencias;
import uniandrade.br.edu.com.popseries.model.Amigo;
import uniandrade.br.edu.com.popseries.model.Usuario;

/**
 * Created by pnda on 16/05/18.
 *
 */

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ViewHolder> {

    private List<Usuario> mUserList;
    private Context mContext;

    private DatabaseReference firebase;
    private String identificadorModerador;
    private Dialog myDialog;

    public ListUserAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ListUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.amigos_layout, parent, false);

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Usuario usuario = mUserList.get(position);

        holder.txtUserName.setText(usuario.getNome());
        holder.txtUserEmail.setText(usuario.getEmail());

        Picasso.with(mContext)
                .load(usuario.getPhoto()).noFade()
                .into(holder.imgUserPhoto);

    }

    @Override
    public int getItemCount() {
        return (mUserList == null) ? 0 : mUserList.size();
    }

    public void adicionarListaUsuarios(List<Usuario> listUsers) {
        mUserList = new ArrayList<>();
        mUserList.addAll(listUsers);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUserPhoto;
        private TextView txtUserName;
        private TextView txtUserEmail;

        private ViewHolder(final View itemView) {
            super(itemView);

            imgUserPhoto = itemView.findViewById(R.id.imgUserPhotoAmg);
            txtUserName = itemView.findViewById(R.id.txtUserNameAmg);
            txtUserEmail = itemView.findViewById(R.id.txtUserEmailAmg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if ( pos != RecyclerView.NO_POSITION ){
                        Usuario usuario = mUserList.get(pos);
                        abrirDialog(usuario);
                    }
                }
            });

        }
    }

    private void abrirDialog(final Usuario usuario) {
        //DIALOG VARIAVEIS
        TextView txtClosePopup, txtNomePopup, txtEmailPopup;
        ImageView imgPopup;
        Button btnAdicionarPopup;
        //DIALOG
        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.custom_popup_amigos);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txtClosePopup = myDialog.findViewById(R.id.txtClosePopup);
        imgPopup = myDialog.findViewById(R.id.imgCustomPopup);
        txtNomePopup = myDialog.findViewById(R.id.txtNameCustomPopup);
        txtEmailPopup = myDialog.findViewById(R.id.txtEmailCustomPopup);
        btnAdicionarPopup = myDialog.findViewById(R.id.btnAdicionarAmigo);

        txtNomePopup.setText(usuario.getNome());
        txtEmailPopup.setText(usuario.getEmail());
        Picasso.with(mContext)
                .load(usuario.getPhoto()).noFade()
                .into(imgPopup);

        txtClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        btnAdicionarPopup.setText("Adicionar Moderador");

        btnAdicionarPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarModerador(usuario.getEmail());
            }
        });
        myDialog.show();

    }

    private void adicionarModerador(String email) {
        //Codificar identificador amigo (base64)
        identificadorModerador = Base64Custom.encodeBase64( email );

        firebase = ConfigFirebase.getFirebase().child("usuarios").child(identificadorModerador);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.getValue() != null ){
                    //Recuperar dados do amigo a ser adicionado
                    Usuario usuario = dataSnapshot.getValue( Usuario.class );

                    firebase = ConfigFirebase.getFirebase();
                    firebase = firebase.child("moderadores")
                            .child( identificadorModerador );

                    Amigo amigo = new Amigo();
                    amigo.setId( identificadorModerador );
                    amigo.setPhoto( usuario.getPhoto() );
                    amigo.setNome( usuario.getNome() );
                    amigo.setEmail( usuario.getEmail() );

                    firebase.setValue( amigo );
                    myDialog.dismiss();
                }else {
                    Toast.makeText(mContext, "Usuário não possui cadastro.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
