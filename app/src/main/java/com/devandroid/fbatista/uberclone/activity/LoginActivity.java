package com.devandroid.fbatista.uberclone.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.devandroid.fbatista.uberclone.R;
import com.devandroid.fbatista.uberclone.config.ConfigFirebase;
import com.devandroid.fbatista.uberclone.helper.UserProfile;
import com.devandroid.fbatista.uberclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mEmail, mSenha;
    private String email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.et_email);
        mSenha = findViewById(R.id.et_senha);



    }


    public void logarUsuario(View view) {

        //Verifica se os campos foram digitados corretamente antes de dar inicio na autenticacao
        if (validarCamposLogin()) {
            final FirebaseAuth auth = ConfigFirebase.getFirebaseAuth();
            auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                UserProfile.redirecionarUsuario(LoginActivity.this);



                            } else{
                                String errorMessage = "Houve um erro";
                                try{
                                    throw task.getException();
                                }catch (FirebaseAuthInvalidUserException e){
                                    errorMessage = "Usuario nao cadastrado";
                                }catch (Exception e){
                                    errorMessage = e.getMessage();
                                }

                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
        }
    }

    private boolean validarCamposLogin() {

        email = mEmail.getText().toString();
        senha = mSenha.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, "O campo e-mail precisa ser preenchido!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (senha.isEmpty()) {
            Toast.makeText(this, "A senha precisa ser informada", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;


    }


}
