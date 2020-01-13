package med.mental.mentalmed.telas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import med.mental.mentalmed.R;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.Usuario;
import med.mental.mentalmed.util.Util;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ExtendedEditText email;
    private ExtendedEditText senha;

    private DatabaseReference refUsuario;
    private FirebaseAuth auth;

    private ValueEventListener valueEventListenerUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verificarUsuarioLogado();

        email = findViewById(R.id.edit_login_email);
        senha = findViewById(R.id.edit_login_senha);
        final Button botaoLogar = findViewById(R.id.bt_logar);

        email.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_NEXT) email.requestFocus();
            return false;
        });

        senha.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) botaoLogar.performClick();
            return false;
        });

        botaoLogar.setOnClickListener(v -> {
            //Validação de campos
            if (!email.getText().toString().isEmpty() && !senha.getText().toString().isEmpty())
                validarLogin(email.getText().toString(), senha.getText().toString());
            else
                Util.msg(LoginActivity.this, "Um ou mais campos estão vazios");
        });
    }

    private void verificarUsuarioLogado() {
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.getCurrentUser();

        if (auth.getCurrentUser() != null) abrirTelaAdministrador();
    }

    private void validarLogin(final String email, final String senha) {
        progressDialog = Util.mostrarProgressDialog(this, "Entrando...");

        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.signInWithEmailAndPassword(
                email, senha).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {

                        final String id = task.getResult().getUser().getUid();

                        refUsuario = ConfiguracaoFirebase.getFirebase()
                                .child("usuarios")
                                .child(id);

                        valueEventListenerUsuario = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                                if (usuario != null) {
                                    Preferencias preferencias = new Preferencias(LoginActivity.this);

                                    abrirTelaAdministrador();
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };

                        refUsuario.addListenerForSingleValueEvent(valueEventListenerUsuario);

                    } else {
                        progressDialog.dismiss();
                        String erroExcecao;

                        try {
                            throw Objects.requireNonNull(task.getException());

                        } catch (FirebaseAuthInvalidUserException e) {
                            erroExcecao = "E-mail não existe ou desativado!";

                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erroExcecao = "Senha digitada incorreta!";

                        } catch (IllegalArgumentException e) {
                            erroExcecao = "Campos vazios!";

                        } catch (FirebaseApiNotAvailableException e) {
                            erroExcecao = "É necessário o 'Google Play Services' para prosseguir!";

                        } catch (FirebaseNetworkException e) {
                            erroExcecao = "Sem conexão!";
                            e.printStackTrace();

                        } catch (Exception e) {
                            erroExcecao = "Ao efetuar o login!";
                            e.printStackTrace();
                        }

                        Toast.makeText(LoginActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void abrirTelaAdministrador() {
        Intent intent = new Intent(LoginActivity.this, Administrador.class);
        startActivity(intent);
        finish();
    }
}
