package uniandrade.br.edu.com.popseries.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.helper.Base64Custom;
import uniandrade.br.edu.com.popseries.model.Usuario;

public class MyProfileActivity extends AppCompatActivity {

    private TextView profileUserName, profileUserEmail;
    private ImageView profileUserPhoto, profileUserCapa;
    private EditText inputNewName;
    private String newName;
    private Dialog myDialog;
    private String uID;

    //*****   FIREBASE   *****
    private DatabaseReference databaseReference = ConfigFirebase.getFirebase();
    private FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAutenticacao();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    //***** USUÁRIO *****
    private Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileUserCapa = findViewById(R.id.imgCapaPerfil);
        profileUserPhoto = findViewById(R.id.imgUserPhotoPerfil);
        profileUserName = findViewById(R.id.txtNomePerfil);
        profileUserEmail = findViewById(R.id.txtEmailPerfil);

        uID = Base64Custom.encodeBase64( firebaseUser.getEmail() );

        Button btnEditarPerfil = findViewById(R.id.btnEditarPerfil);

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarPerfil();
            }
        });

        setUserData();
    }

    private void editarPerfil() {
        TextView closePopup;
        Button salvar;
        //DIALOG
        myDialog = new Dialog(MyProfileActivity.this);
        myDialog.setContentView(R.layout.dialog_edit_profile);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        closePopup = myDialog.findViewById(R.id.txtClosePopup);
        salvar = myDialog.findViewById(R.id.btnSalvarNewName);
        inputNewName = myDialog.findViewById(R.id.txtNewName);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName = inputNewName.getText().toString();
                salvarEdicao(newName);
            }
        });

        myDialog.show();
    }

    private void salvarEdicao(String newName) {
        if (newName.equals("") || newName.trim().length() == 0){
            inputNewName.setError("Nome Ínvalido");
        }else{
            try{
                DatabaseReference userReference = databaseReference.child("usuarios").child( uID );
                userReference.child("nome").setValue(newName);
            }catch (Exception e){
                Toast.makeText(MyProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }finally {
                myDialog.dismiss();
            }
        }
    }

    private Usuario setUserData() {
        DatabaseReference userReference = databaseReference.child("usuarios").child( uID );
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    usuario = dataSnapshot.getValue(Usuario.class);
                    String userName = usuario.getNome();
                    String userEmail = usuario.getEmail();
                    profileUserName.setText(userName);
                    profileUserEmail.setText(userEmail);
                    Picasso.with(MyProfileActivity.this)
                            .load(usuario.getPhoto()).noFade()
                            .into(profileUserPhoto);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return usuario;
    }

}
