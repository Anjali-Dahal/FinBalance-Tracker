package anjali.example.finbanlacetracker.views.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;


import anjali.example.finbanlacetracker.R;
import anjali.example.finbanlacetracker.databinding.ActivityMainBinding;
import anjali.example.finbanlacetracker.models.Transaction;
import anjali.example.finbanlacetracker.utils.Constants;
import anjali.example.finbanlacetracker.utils.Helper;
import anjali.example.finbanlacetracker.viewmodels.MainViewModel;
import anjali.example.finbanlacetracker.views.fragments.StatsFragment;
import anjali.example.finbanlacetracker.views.fragments.TransactionsFragment;
import io.realm.RealmResults;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Calendar calendar;
    /*
    0 = Daily
    1 = Monthly
    2 = Calendar
    3 = Summary
    4 = Notes
     */



   public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Transactions");

        Constants.setCategories();
        calendar = Calendar.getInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new TransactionsFragment());
        transaction.commit();

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.transactions) {
                    getSupportFragmentManager().popBackStack();
                } else if(item.getItemId() == R.id.stats){
                    transaction.replace(R.id.content, new StatsFragment());
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                return true;
            }
        });
        Log.d("INTENT", this.getIntent().getAction());
        handlelIntent(this.getIntent());

    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        handlelIntent(intent);
    }

    private void handlelIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Search Query was: " + query, Toast.LENGTH_SHORT).show();
        }
    }

    public void getTransactions() {
        viewModel.getTransactions(calendar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.id_search).getActionView();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
        searchView.setSearchableInfo(searchableInfo);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RealmResults<Transaction> searchedTransactions = viewModel.findTransactions(query);
                Log.d("Transactions", searchedTransactions.size() + "");
                for ( Transaction transaction : searchedTransactions ) {
                    Log.d("Transaction Type", transaction.getCategory());
                    Toast.makeText(binding.content.getContext(), "Transaction : " + transaction.getType(), Toast.LENGTH_SHORT);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: handle text changes
                return false;
            }
        });
        return true;
    }
}