package anjali.example.finbanlacetracker.views.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;


import anjali.example.finbanlacetracker.R;
import anjali.example.finbanlacetracker.databinding.ActivityMainBinding;
import anjali.example.finbanlacetracker.models.User;
import anjali.example.finbanlacetracker.utils.Constants;
import anjali.example.finbanlacetracker.viewmodels.MainViewModel;
import anjali.example.finbanlacetracker.views.fragments.SearchResultFragment;
import anjali.example.finbanlacetracker.views.fragments.StatsFragment;
import anjali.example.finbanlacetracker.views.fragments.TransactionsFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Calendar calendar;
    User user;

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

        user = getIntent().getParcelableExtra("user");

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.transactions) {
                    getSupportFragmentManager().popBackStack();
                } else if(item.getItemId() == R.id.stats){
                    transaction.replace(R.id.content, new StatsFragment());
                    transaction.addToBackStack(null);
                }else if (item.getItemId() == R.id.profile) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }else if (item.getItemId() == R.id.summary) {
                    Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
                transaction.commit();
                return true;
            }
        });
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
                Bundle bundle = new Bundle();
                bundle.putString("query", query);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack("search");
                SearchResultFragment searchResultFragment = new SearchResultFragment();
                searchResultFragment.setArguments(bundle);
                transaction.replace(R.id.content, searchResultFragment);
                transaction.commit();

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