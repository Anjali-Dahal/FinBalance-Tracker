package anjali.example.finbanlacetracker.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import anjali.example.finbanlacetracker.R;
import anjali.example.finbanlacetracker.databinding.FragmentProfileBinding;
import anjali.example.finbanlacetracker.models.User;
import anjali.example.finbanlacetracker.views.activites.LoginActivity;
import anjali.example.finbanlacetracker.views.activites.MainActivity;
import anjali.example.finbanlacetracker.views.activites.ProfileActivity;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    ImageView edit_profile;
    CardView logout, about;
    User user;
    TextView name, email;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (shouldExit()) {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }else {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean shouldExit() {
        // Logic to determine if back press should exit the fragment
        if (logout.isPressed()) {
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        edit_profile = binding.editProfile;
        logout = binding.logout;
        about = binding.about;
        name = binding.name;
        email = binding.email;

        Intent intent = getActivity().getIntent();

        if (intent != null) {
            user = intent.getParcelableExtra("user");
            Log.d("User", user.getEmail());
        }

        if (user != null) {
            name.setText(user.getName());
            email.setText(user.getEmail());
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                    transaction.replace(R.id.content_profile, new ProfileDetailFragment());
                    transaction.commit();
                }catch (NullPointerException e) {
                    Log.e("Profile Fragment", "Activity Not Found");
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                try {
                    getActivity().finish();
                }catch (NullPointerException e){
                    Log.e("Profile Fragment", "Error Getting Activity");
                }
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "About", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}