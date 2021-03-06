package com.devandroid.fbatista.uberclone.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.devandroid.fbatista.uberclone.activity.HomeMotoristaActivity;
import com.devandroid.fbatista.uberclone.activity.HomePassageiroActivity;
import com.devandroid.fbatista.uberclone.config.ConfigFirebase;
import com.devandroid.fbatista.uberclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserProfile {

    public static final String TAG = UserProfile.class.getSimpleName();

    public static FirebaseUser getFirebaseUserLogado(){
        FirebaseAuth auth = ConfigFirebase.getFirebaseAuth();
        FirebaseUser user = auth.getCurrentUser();

        return user;
    }

    public static Usuario getUsuarioLogado(){
        FirebaseUser firebaseUser = getFirebaseUserLogado();
        Usuario usuario = new Usuario();
        usuario.setId(firebaseUser.getUid());
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());

        return usuario;
    }

    public static boolean atualizarNomeUsuario(String nome){

        try {
            FirebaseUser user = getFirebaseUserLogado();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "Erro ao atualizar o perfil do usuario");
                }
            });
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }



    }


    public static void redirecionarUsuario(final Activity activity) {



        if (getFirebaseUserLogado() != null) {
            String idUsuario = getIdUsuario();

            DatabaseReference userReference = ConfigFirebase.getDatabaseReference();
            userReference.child("usuarios").child(idUsuario)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);

                            if (usuario.getTipo().equals("Driver"))
                                activity.startActivity(new Intent(activity, HomeMotoristaActivity.class));
                            else
                                activity.startActivity(new Intent(activity, HomePassageiroActivity.class));

                            activity.finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }








    }


    private static String getIdUsuario() {
        return getFirebaseUserLogado().getUid();
    }
}
