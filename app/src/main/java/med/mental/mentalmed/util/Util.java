package med.mental.mentalmed.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import med.mental.mentalmed.R;

public class Util {

    public static void msg(Context context, String mensagem) {
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }

    public static ProgressDialog mostrarProgressDialog(Context context, String mensagem) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.dialogMentalMed);
        progressDialog.setMessage(mensagem);
        progressDialog.setCancelable(false);
        progressDialog.show();

        return progressDialog;
    }
}
