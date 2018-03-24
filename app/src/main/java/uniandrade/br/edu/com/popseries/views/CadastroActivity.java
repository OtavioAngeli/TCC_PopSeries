package uniandrade.br.edu.com.popseries.views;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import uniandrade.br.edu.com.popseries.R;
import uniandrade.br.edu.com.popseries.config.ConfigFirebase;
import uniandrade.br.edu.com.popseries.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private static final String userDefaultPhoto = "https://firebasestorage.googleapis.com/v0/b/tccpopseries.appspot.com/o/defaultPhoto%2FImagemPerfil.jpg?alt=media&token=770c73ba-d1b8-47e8-8d54-c33aa98dbecd";

    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText confirmaSennha;
    private Button botaoCadastrar;
    private Usuario usuario;
    private ProgressBar progressBar;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Toolbar toolbar = findViewById(R.id.toolbarCadastro);
        toolbar.setTitle("Cadastrar-se");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        nome = findViewById(R.id.editCadastroNome);
        email = findViewById(R.id.editCadastroEmail);
        senha = findViewById(R.id.editCadastroSenha);
        confirmaSennha = findViewById(R.id.editCadastroConfirmaSenha);
        botaoCadastrar = findViewById(R.id.btnCadastrar);
        progressBar = findViewById(R.id.progressBarCadastro);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                usuario.setPhoto(userDefaultPhoto);
                if ( usuario.getNome().equals("") ||
                        usuario.getEmail().equals("") || usuario.getSenha().equals("")
                        || confirmaSennha.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else if ( !usuario.getSenha().equals( confirmaSennha.getText().toString() ) ) {
                    Toast.makeText(getApplicationContext(), "As senhas não correspondem", Toast.LENGTH_SHORT).show();
                }else {
                    cadastrarUsuario();
                }
            }
        });

    }

    private void cadastrarUsuario(){
        botaoCadastrar.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful() ){
                    Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso !", Toast.LENGTH_LONG ).show();
                    usuario.setId( task.getResult().getUser().getUid() );
                    usuario.salvar();
                    finish();
                }else{
                    String erro = "";
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Escolha uma senha que contenha, letras e números.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email indicado não é válido.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Já existe uma conta com esse e-mail.";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário: " + erro, Toast.LENGTH_LONG ).show();
                    botaoCadastrar.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

    }

}
