package uniandrade.br.edu.com.popseries.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.helper.Base64Custom;
import uniandrade.br.edu.com.popseries.helper.Preferencias;
import uniandrade.br.edu.com.popseries.model.Usuario;

public class UserProfileActivity extends AppCompatActivity {

    private TextView profileUserName, profileUserEmail;
    private ImageView profileUserPhoto, profileUserCapa;
    private Button btnRemoverAmigo;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileUserCapa = findViewById(R.id.imgUserCapaProfile);
        profileUserPhoto = findViewById(R.id.imgUserPhotoProfile);
        profileUserName = findViewById(R.id.txtUserNameProfile);
        profileUserEmail = findViewById(R.id.txtUserEmailProfile);
        btnRemoverAmigo = findViewById(R.id.btnRemoverAmigo);

        Intent intent = getIntent();

        if (intent != null){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                userEmail = bundle.getString("user_email");
            }
        }

        setUserData( Base64Custom.encodeBase64(userEmail) );

        btnRemoverAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removerAmigo( Base64Custom.encodeBase64(userEmail) );
            }
        });
    }

    private void removerAmigo(String uIDFriend) {
        Preferencias preferencias = new Preferencias(UserProfileActivity.this);
        String uID = Base64Custom.encodeBase64( preferencias.getEmail() );
        DatabaseReference databaseReference = ConfigFirebase.getFirebase()
                .child("amigos").child( uID ).child( uIDFriend );
        try {
            databaseReference.removeValue();
            Toast.makeText(UserProfileActivity.this, "Amigo removido com Sucesso! ", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            Toast.makeText(UserProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUserData(String uID) {
        DatabaseReference userReference = ConfigFirebase.getFirebase().child("usuarios").child( uID );
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    String userName = usuario.getNome();
                    String userEmail = usuario.getEmail();
                    profileUserName.setText(userName);
                    profileUserEmail.setText(userEmail);
                    Picasso.with(UserProfileActivity.this)
                            .load(usuario.getPhoto()).noFade()
                            .into(profileUserPhoto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
