package med.mental.mentalmed.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth auth;

    public static DatabaseReference getFirebase() {
        if (referenciaFirebase == null)
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();

        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao() {
        if (auth == null) auth = FirebaseAuth.getInstance();

        return auth;
    }
}
