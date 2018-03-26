package uniandrade.br.edu.com.popseries.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.model.Usuario;
import uniandrade.br.edu.com.popseries.views.ProfileActivity;

/**
 * Created by pnda on 25/03/18.
 */

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.ViewHolder> {

    private List<Usuario> mUserList;
    private Context mContext;

    public AmigosAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public AmigosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amigos_layout, parent, false);
        return new AmigosAdapter.ViewHolder(view);
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


        private ViewHolder(View itemView) {
            super(itemView);

            imgUserPhoto = itemView.findViewById(R.id.imgUserPhotoAmg);
            txtUserName = itemView.findViewById(R.id.txtUserNameAmg);
            txtUserEmail = itemView.findViewById(R.id.txtUserEmailAmg);
        }
    }
}
