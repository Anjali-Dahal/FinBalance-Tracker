package anjali.example.finbanlacetracker.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import anjali.example.finbanlacetracker.databinding.FragmentProfileDetailBinding;
import anjali.example.finbanlacetracker.models.User;
import anjali.example.finbanlacetracker.viewmodels.MainViewModel;

public class ProfileDetailFragment extends Fragment {
    FragmentProfileDetailBinding binding;
    User user;
    TextView profileName, profileEmail;
    EditText editName, editOccupation, editPhoneNumber;
    Button saveBtn;
    MainViewModel viewModel;

    public ProfileDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileDetailBinding.inflate(getLayoutInflater());
        profileName = binding.profileName;
        profileEmail = binding.profileEmail;

        editName = binding.editName;
        editOccupation = binding.editOccupation;
        editPhoneNumber = binding.editPhNumber;

        saveBtn = binding.btnSave;

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        user = getActivity().getIntent().getParcelableExtra("user");

        if (user != null) {
            Log.d("User: ", user.getEmail());
        }else {
            Log.d("User:", "Null");
        }

        profileName.setText(user.getName());
        profileEmail.setText(user.getEmail());
        editName.setText(user.getName());
        editOccupation.setText(user.getOccupation());
        editPhoneNumber.setText(user.getPh_number());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User u = viewModel.getUser(user.getEmail());
                User toEdit = new User(editName.getText().toString(), user.getEmail(), editOccupation.getText().toString(), editPhoneNumber.getText().toString(), u.getPassword(), u.getId());

                viewModel.updateUser(toEdit);
                Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}