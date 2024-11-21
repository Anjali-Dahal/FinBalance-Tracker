package anjali.example.finbanlacetracker.views.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import anjali.example.finbanlacetracker.R;
import anjali.example.finbanlacetracker.databinding.ActivityProfileBinding;
import anjali.example.finbanlacetracker.models.User;
import anjali.example.finbanlacetracker.viewmodels.MainViewModel;
import anjali.example.finbanlacetracker.views.fragments.ProfileFragment;
import anjali.example.finbanlacetracker.views.fragments.TransactionsFragment;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    User user;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setContentView(binding.getRoot());
        user = getIntent().getParcelableExtra("user");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_profile, new ProfileFragment());
        user = viewModel.getUser(user.getEmail());
        getIntent().putExtra("user", user);
        transaction.commit();
    }


}