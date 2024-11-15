package anjali.example.finbanlacetracker.views.activites;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.Date;

import anjali.example.finbanlacetracker.R;
import anjali.example.finbanlacetracker.databinding.ActivityRegistrationBinding;
import anjali.example.finbanlacetracker.models.User;
import anjali.example.finbanlacetracker.viewmodels.MainViewModel;

public class RegistrationActivity extends AppCompatActivity {
    ActivityRegistrationBinding binding;
    public MainViewModel viewModel;
    TextView user_exists_txt;
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        user_exists_txt = binding.userExistTxt;
        submit_btn = binding.btnRegister;

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString();
                String email = binding.email.getText().toString();
                String occupation = binding.occupation.getText().toString();
                String ph_number = binding.phNumber.getText().toString();
                String password = binding.password.getText().toString();
                String confirm_password = binding.confirmPassword.getText().toString();

                if (password.equals(confirm_password)) {
                    User user = new User(name, email, occupation, ph_number, password, new Date().getTime());
                    viewModel.addUser(user);

                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(RegistrationActivity.this, "Please Enter The Same Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        user_exists_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}