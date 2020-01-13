package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import med.mental.mentalmed.R;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.model.Usuario;

public class Administrador extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView tv_nome_usuario;

    private DatabaseReference refUsuario = ConfiguracaoFirebase.getFirebase().child("usuarios");
    private Usuario usuario = new Usuario();
    private String idUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_administrador);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("MentalMed");
        setSupportActionBar(toolbar);

        verificarUsuarioLogado();
        carregarComponentes();
    }

    private void verificarUsuarioLogado() {
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.getCurrentUser();

        if (auth.getCurrentUser() != null)
            carregarPreferencias();
    }

    private void carregarComponentes() {
        tv_nome_usuario = findViewById(R.id.tv_nome_usuario);
    }

    private void carregarPreferencias() {
        idUsuario = auth.getUid();

        refUsuario.child(Objects.requireNonNull(idUsuario)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!idUsuario.equals("")) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                    tv_nome_usuario.setText(usuario.getNome());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_sair) {
            deslogarUsuario();
            return true;
        }
        return super.onOptionsItemSelected(item); //Padrão para Android
    }

    private void deslogarUsuario() {
        auth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
