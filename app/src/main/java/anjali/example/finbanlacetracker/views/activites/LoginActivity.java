package anjali.example.finbanlacetracker.views.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import anjali.example.finbanlacetracker.R;
import anjali.example.finbanlacetracker.databinding.ActivityLoginBinding;
import anjali.example.finbanlacetracker.models.User;
import anjali.example.finbanlacetracker.viewmodels.MainViewModel;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    MainViewModel viewModel;
    TextView sign_up_btn;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        sign_up_btn = binding.signupReg;

        btn_login = binding.btnLogin;

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailLogin.getText().toString();
                String password = binding.passwordLogin.getText().toString();

                User user = viewModel.getUser(email);

                if (user != null) {
                    if (user.getPassword().equals(password)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, "Incorrect Password, Please Try Again!!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "User With This Email Don't Exist, Please Register", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Login Activity", "Registration Button Clicked");

                //TODO: Switch To The Registration Activity
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }
}