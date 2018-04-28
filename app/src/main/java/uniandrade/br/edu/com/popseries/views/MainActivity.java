package uniandrade.br.edu.com.popseries.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.fragments.AssistidosFragment;
import uniandrade.br.edu.com.popseries.fragments.AssistirMaisTardeFragment;
import uniandrade.br.edu.com.popseries.fragments.FavoritosFragment;
import uniandrade.br.edu.com.popseries.fragments.SeriesFragment;
import uniandrade.br.edu.com.popseries.helper.Base64Custom;
import uniandrade.br.edu.com.popseries.helper.Preferencias;
import uniandrade.br.edu.com.popseries.model.Usuario;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private int i;
    private TextView txtUserNameMenu, txtUserEmailMenu;
    private ImageView imgUserPhotoMenu;

    private GoogleApiClient googleApiClient;

    //*****   FIREBASE   *****
    private DatabaseReference databaseReference = ConfigFirebase.getFirebase();
    private FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAutenticacao();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, new SeriesFragment()).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_serie);

        txtUserNameMenu = navigationView.getHeaderView(0).findViewById(R.id.txtUserNameMenu);
        txtUserEmailMenu = navigationView.getHeaderView(0).findViewById(R.id.txtUserEmailMenu);
        imgUserPhotoMenu = navigationView.getHeaderView(0).findViewById(R.id.imgUserPhotoMenu);


        final BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() != i){
                    i = item.getItemId();
                    switch (item.getItemId()){
                        case R.id.nav_bottom_series:
                            item.setEnabled(true);
                            setTitle(R.string.app_name);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_main, new SeriesFragment()).commit();
                            break;
                        case R.id.nav_bottom_favoritos:
                            setTitle("Favoritos");
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_main, new FavoritosFragment()).commit();
                            break;
                        case R.id.nav_bottom_assistidos:
                            setTitle("Assistidos");
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_main, new AssistidosFragment()).commit();
                            break;
                        case R.id.nav_bottom_favorito4:
                            setTitle("Assistir Mais Tarde");
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_main, new AssistirMaisTardeFragment()).commit();
                            break;
                    }
                }
                return true;
            }
        });

    }

    private Usuario setUserData() {
        String identificadorUsuario = Base64Custom.encodeBase64( firebaseUser.getEmail() );
        DatabaseReference userReference = databaseReference.child("usuarios").child( identificadorUsuario );
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Preferencias preferencias = new Preferencias(MainActivity.this);
                    usuario = dataSnapshot.getValue(Usuario.class);
                    String userName = usuario.getNome();
                    String userEmail = usuario.getEmail();
                    txtUserNameMenu.setText(userName);
                    txtUserEmailMenu.setText(userEmail);
                    Picasso.with(MainActivity.this)
                            .load(usuario.getPhoto()).noFade()
                            .into(imgUserPhotoMenu);

                    preferencias.salvarNomeEmail( userName, userEmail, usuario.getPhoto() );
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return usuario;
    }

    private boolean verificarUsuarioLogado() {
        if (firebaseAuth.getCurrentUser() != null){
//            Toast.makeText(getApplicationContext(), "USUARIO LOGADO", Toast.LENGTH_SHORT).show();
            setUserData();
            return true;
        }else {
//            Toast.makeText(getApplicationContext(), "USUARIO NÃO LOGADO", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (verificarUsuarioLogado()){
            getMenuInflater().inflate(R.menu.menu_user_logado, menu);
        }else {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }else if (id == R.id.menu_pesquisa){
            Intent intent = new Intent(MainActivity.this, PesquisarActivity.class);
            startActivity(intent);
        }else if (id == R.id.action_logout){
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    Toast.makeText(getApplicationContext(), "Log Out realizado com sucesso !", Toast.LENGTH_SHORT).show();
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goLogInScreen() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_serie) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, new SeriesFragment()).commit();
        } else if (id == R.id.nav_amigos) {
            if (verificarUsuarioLogado()){
                Intent intent = new Intent(getApplicationContext(), AmigosActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), "Area destinada apenas para membros, por favor faça o login", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_perfil) {
            if (verificarUsuarioLogado()){
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profile);
            }else {
                Toast.makeText(getApplicationContext(), "Área destinada apenas para membros, por favor faça o login", Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.nav_ajuda) {
            Intent ajuda = new Intent(getApplicationContext(), AjudaActivity.class);
            startActivity(ajuda);
        } else if (id == R.id.nav_sobre) {
            Intent sobre = new Intent(getApplicationContext(), SobreActivity.class);
            startActivity(sobre);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}