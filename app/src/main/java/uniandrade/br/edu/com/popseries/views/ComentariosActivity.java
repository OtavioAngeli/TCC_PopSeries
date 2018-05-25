package uniandrade.br.edu.com.popseries.views;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.adapter.ComentariosAdapter;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.helper.Base64Custom;
import uniandrade.br.edu.com.popseries.helper.Preferencias;
import uniandrade.br.edu.com.popseries.model.Comentario;
import uniandrade.br.edu.com.popseries.model.Usuario;

public class ComentariosActivity extends AppCompatActivity {


    private ValueEventListener valueEventListenerComentarios;
    //*****   FIREBASE   *****
    private DatabaseReference databaseReference = ConfigFirebase.getFirebase();
    private Query firebaseQuery;
    private FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAutenticacao();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private Usuario usuario = new Usuario();

    private ImageView imgUserPhoto;
    private EditText txtComentario;
    private Button button;
    private int serie_id;
    private String serie_name;
    private ComentariosAdapter comentariosAdapter;
    private List<Comentario> comentarioList;
    private String serie_poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        Toolbar toolbar = findViewById(R.id.toolbarComentarios);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Ação para voltar pra tela inicial
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        if (intent != null){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                serie_name = bundle.getString("original_title");
                serie_id = bundle.getInt("serie_id");
                serie_poster = bundle.getString("thumbnail");
                setTitle( serie_name );
            }
        }

        button = findViewById(R.id.btnPublicarComentário);
        imgUserPhoto = findViewById(R.id.imgUserPhotoC);
        txtComentario = findViewById(R.id.txtComentario);

        verificarUsuarioLogado();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewComentarios);
        comentariosAdapter = new ComentariosAdapter(ComentariosActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ComentariosActivity.this);

        recyclerView.setAdapter(comentariosAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        comentarioList = new ArrayList<>();

        listarComentarios();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtUserNewComent = txtComentario.getText().toString();
                if ( !txtUserNewComent.isEmpty() ){
                    publicarComentario(txtUserNewComent);
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseQuery.addValueEventListener( valueEventListenerComentarios );
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseQuery.removeEventListener( valueEventListenerComentarios );
        Log.i("ValueEventListener", "onStop");
    }

    private void listarComentarios() {
        databaseReference = ConfigFirebase.getFirebase().child("comentarios_series").child( Integer.toString( serie_id ) );
        firebaseQuery = databaseReference.orderByChild("timestamp");

        valueEventListenerComentarios = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                comentarioList.clear();
                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    Comentario comentario = dados.getValue( Comentario.class );
                    comentarioList.add( comentario );
                    comentariosAdapter.adicionarListaComentarios(comentarioList);
                }
                comentariosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void verificarUsuarioLogado() {
        if (firebaseAuth.getCurrentUser() != null){
            carregarFotoUsuario();
        }else {
            desabilitaComentario();
        }
    }

    private void desabilitaComentario() {
        button.setEnabled(false);
        txtComentario.setEnabled(false);
        txtComentario.setHint("Efetue login para comentar");
    }

    private void carregarFotoUsuario() {
        String identificadorUsuario = Base64Custom.encodeBase64( firebaseUser.getEmail() );
        DatabaseReference userReference = databaseReference.child("usuarios").child( identificadorUsuario );
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    usuario = dataSnapshot.getValue(Usuario.class);
                    Picasso.with(ComentariosActivity.this)
                            .load(usuario.getPhoto()).noFade().into(imgUserPhoto);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void publicarComentario(String txtUserNewComent) {
        //Recuperar identificador usuario logado (base64)
        Preferencias preferencias = new Preferencias(ComentariosActivity.this);
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        databaseReference = ConfigFirebase.getFirebase();

        DatabaseReference comentarioUsuario =
                databaseReference.child("comentarios_usuarios")
                        .child( identificadorUsuarioLogado ).child( Integer.toString(serie_id) );
        DatabaseReference comentarioSerie =
                databaseReference.child("comentarios_series")
                        .child( Integer.toString(serie_id) ).child( identificadorUsuarioLogado );

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        String currentDate = sdf.format(new Date());

        //Map<String, String> timestamp = ServerValue.TIMESTAMP;

        Comentario comentario = new Comentario();
        comentario.setUser_name( preferencias.getNome() );
        comentario.setUser_email( preferencias.getEmail() );
        comentario.setComentario( txtUserNewComent );
        comentario.setSerie_name( serie_name );
        comentario.setUser_photo( preferencias.getUrlPhoto() );
        comentario.setSerie_poster( serie_poster );
        comentario.setSerie_id( String.valueOf(serie_id) );
        comentario.setDate_comment( currentDate );
        //comentario.setTimestamp(timestamp);
        comentarioUsuario.setValue( comentario );
        comentarioSerie.setValue( comentario );

        txtComentario.setText("");
        Snackbar.make(findViewById(R.id.activity_comentarios), "Comentário publicado", Snackbar.LENGTH_SHORT).show();
    }

}
