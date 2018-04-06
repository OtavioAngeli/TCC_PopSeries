package uniandrade.br.edu.com.popseries.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ProfileActivity extends AppCompatActivity {

    private TextView profileUserName, profileUserEmail;
    private ImageView profileUserPhoto, profileUserCapa;

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

        setUserData();

    }

    private Usuario setUserData() {
        String identificadorUsuario = Base64Custom.encodeBase64( firebaseUser.getEmail() );
        DatabaseReference userReference = databaseReference.child("usuarios").child( identificadorUsuario );
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    usuario = dataSnapshot.getValue(Usuario.class);
                    String userName = usuario.getNome();
                    String userEmail = usuario.getEmail();
                    profileUserName.setText(userName);
                    profileUserEmail.setText(userEmail);
                    Picasso.with(ProfileActivity.this)
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
