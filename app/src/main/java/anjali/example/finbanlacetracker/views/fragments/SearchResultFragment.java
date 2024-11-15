package anjali.example.finbanlacetracker.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import anjali.example.finbanlacetracker.R;
import anjali.example.finbanlacetracker.adapters.TransactionsAdapter;
import anjali.example.finbanlacetracker.databinding.SearchResultLayoutBinding;
import anjali.example.finbanlacetracker.models.Transaction;
import anjali.example.finbanlacetracker.viewmodels.MainViewModel;
import io.realm.RealmResults;

public class SearchResultFragment extends Fragment {
SearchResultLayoutBinding binding;
public MainViewModel mainViewModel;

    public SearchResultFragment() {
        super(R.layout.search_result_layout);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchResultLayoutBinding.inflate(inflater);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        String query = getArguments().getString("query");
        RealmResults<Transaction> searchedTransactions = mainViewModel.findTransactions(query);
        RecyclerView search_result = binding.searchResult;
        TransactionsAdapter adapter = new TransactionsAdapter(requireContext(), searchedTransactions);
        search_result.setLayoutManager(new LinearLayoutManager(requireContext()));
        search_result.setAdapter(adapter);
        return binding.getRoot();
    }
}
