package uniandrade.br.edu.com.popseries.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.model.Usuario;

/**
 * Created by pnda on 25/03/18.
 *
 */

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.ViewHolder> {

    private List<Usuario> mUserList;
    private Context mContext;

    public AmigosAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public AmigosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        //DIALOG
        private Dialog myDialog;
        private TextView txtClosePopup;
        private ImageView imgPopup;
        private TextView txtNomePopup, txtEmailPopup;

        private ViewHolder(final View itemView) {
            super(itemView);

            imgUserPhoto = itemView.findViewById(R.id.imgUserPhotoAmg);
            txtUserName = itemView.findViewById(R.id.txtUserNameAmg);
            txtUserEmail = itemView.findViewById(R.id.txtUserEmailAmg);

            //DIALOG
            myDialog = new Dialog(mContext);
            myDialog.setContentView(R.layout.custom_popup_amigos);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            txtClosePopup = myDialog.findViewById(R.id.txtClosePopup);
            imgPopup = myDialog.findViewById(R.id.imgCustomPopup);
            txtNomePopup = myDialog.findViewById(R.id.txtNameCustomPopup);
            txtEmailPopup = myDialog.findViewById(R.id.txtEmailCustomPopup);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if ( pos != RecyclerView.NO_POSITION ){

                        txtNomePopup.setText(mUserList.get(pos).getNome());
                        txtEmailPopup.setText(mUserList.get(pos).getEmail());
                        Picasso.with(mContext)
                                .load(mUserList.get(pos).getPhoto()).noFade()
                                .into(imgPopup);

                        txtClosePopup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDialog.dismiss();
                            }
                        });
                        myDialog.show();
                    }
                }
            });

        }
    }
}
