package anjali.example.finbanlacetracker.views.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import anjali.example.finbanlacetracker.R;
import anjali.example.finbanlacetracker.databinding.ActivityPasswordResetBinding;
import anjali.example.finbanlacetracker.models.User;
import anjali.example.finbanlacetracker.viewmodels.MainViewModel;

public class PasswordReset extends AppCompatActivity {
    ActivityPasswordResetBinding binding;
    EditText email_login, password_login, c_password_login;
    Button btn_change_password, btn_check_email;
    MainViewModel viewModel;
    User toEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordResetBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setContentView(binding.getRoot());

        email_login = binding.emailLogin;
        password_login = binding.passwordLogin;
        c_password_login = binding.cPasswordLogin;

        btn_change_password = binding.btnChangePassword;
        btn_check_email = binding.btnCheckEmail;

        btn_check_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_login.getText().toString();

                User user = viewModel.getUser(email);
                if (user != null) {
                    toEdit = new User(user.getName(), user.getEmail(), user.getOccupation(), user.getPh_number(), user.getPassword(), user.getId());
                    email_login.setVisibility(View.GONE);
                    password_login.setVisibility(View.VISIBLE);
                    c_password_login.setVisibility(View.VISIBLE);
                    btn_check_email.setVisibility(View.GONE);
                    btn_change_password.setVisibility(View.VISIBLE);

                }else {
                    Toast.makeText(getBaseContext(), "User With This Email Not Found, Do Register First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = password_login.getText().toString();
                String c_password = c_password_login.getText().toString();

                if (password.equals(c_password)) {
                    toEdit.setPassword(password);
                    if (viewModel.changePassword(toEdit)) {
                        Intent intent = new Intent(PasswordReset.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getBaseContext(), "Failed To Change The Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}