package uniandrade.br.edu.com.popseries.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.helper.Base64Custom;
import uniandrade.br.edu.com.popseries.model.Usuario;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button btnLoginGoogle;
    private Button btnLogin;
    private EditText txtLoginEmailUsuario;
    private EditText txtLoginSenhaUsuario;
    private ProgressBar progressBar;

    private GoogleApiClient mGoogleApiClient;

    private Usuario usuario;
    private FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnLogin = findViewById(R.id.btnLogin);
        txtLoginEmailUsuario    = findViewById(R.id.txtUserEmailLogin);
        txtLoginSenhaUsuario    = findViewById(R.id.txtUserPasswordLogin);
        progressBar = findViewById(R.id.progressBarLoginGoogle);

        TextView txtCadastrar = findViewById(R.id.txtCadastrar);

        // Abrindo nova tela
        txtCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastro = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(cadastro);
            }
        });
        //Abrindo nova tela

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new Usuario();
                usuario.setEmail( txtLoginEmailUsuario.getText().toString() );
                usuario.setSenha( txtLoginSenhaUsuario.getText().toString() );
                validarLogin();
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        firebaseAuth = ConfigFirebase.getFirebaseAutenticacao();

        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    goMainScreen();
                }
            }
        };
    }

    private void validarLogin(){
        btnLogin.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth = ConfigFirebase.getFirebaseAutenticacao();
        firebaseAuth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ){
                    Toast.makeText(getApplicationContext(), R.string.txtLoginRealizado, Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), R.string.txtErroLogin, Toast.LENGTH_SHORT).show();
                    btnLogin.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goMainScreen();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult: " + result.isSuccess());
        if (result.isSuccess()){
            firebaseAuthWithGoogle(result.getSignInAccount());
        }else {
            Toast.makeText(getApplicationContext(),"Erro ao fazer login", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount signInAccount) {
        usuario = new Usuario();
        progressBar.setVisibility(View.VISIBLE);
        btnLoginGoogle.setVisibility(View.GONE);

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                FirebaseUser usuarioFirebase = task.getResult().getUser();
                String photo;
                String identificadorUsuario = Base64Custom.encodeBase64( signInAccount.getEmail() );
                usuario.setId( identificadorUsuario );
                usuario.setEmail(signInAccount.getEmail());
                usuario.setNome(signInAccount.getDisplayName());
                if (signInAccount.getPhotoUrl() != null){
                    photo = signInAccount.getPhotoUrl().toString();
                    usuario.setPhoto(photo);
                }
                usuario.salvar();

                progressBar.setVisibility(View.GONE);
                btnLoginGoogle.setVisibility(View.VISIBLE);

                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Erro ao autenticar no Firebase", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void goMainScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mFirebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }
}
