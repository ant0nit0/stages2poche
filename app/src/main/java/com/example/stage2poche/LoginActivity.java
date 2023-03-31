package com.example.stage2poche;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stage2poche.entities.Auth;
import com.example.stage2poche.entities.CompteEtudiant;
import com.example.stage2poche.responses.ComptesEtudiantsResponse;
import com.example.stage2poche.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends StageAppActivity {

    // View
    private EditText usernameET, passwordET;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Init view
        usernameET = findViewById(R.id.login_username);
        passwordET = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_login);

        // Set listener
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {

        String login = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        Auth auth = new Auth(login, password);

        Call<LoginResponse> call = apiInterface.login(auth);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    setAuthData(login, response.body().token);
                    getInformationEtudiant(login);
                } else {
                    Toast.makeText(LoginActivity.this, "Identifiant ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
                Log.e("TAG", t.getMessage());
            }
        });
    }


    private void getInformationEtudiant(String login) {
        String token = getToken();

        // Get all accounts
        Call<ComptesEtudiantsResponse> call = apiInterface.doGetComptesEtudiants("Bearer " + token);
        call.enqueue(new Callback<ComptesEtudiantsResponse>() {
            @Override
            public void onResponse(Call<ComptesEtudiantsResponse> call, Response<ComptesEtudiantsResponse> response) {
                if (response.isSuccessful()) {
                    for (CompteEtudiant compteEtudiant : response.body().compteEtudiants) {
                        if (compteEtudiant.login.equals(login)) {
                            setCompteId(compteEtudiant.id);
                            break;
                        }
                    }
                    // Launch main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // Clear the stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    // Else, display error
                    Toast.makeText(LoginActivity.this, "Erreur lors de la récupération des données de l'utilisateur.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ComptesEtudiantsResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                call.cancel();
                Log.e("TAG - etu", t.getMessage());
            }
        });
    }

}
