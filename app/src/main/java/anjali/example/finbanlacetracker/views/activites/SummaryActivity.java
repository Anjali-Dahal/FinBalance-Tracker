package anjali.example.finbanlacetracker.views.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anjali.example.finbanlacetracker.R;
import anjali.example.finbanlacetracker.databinding.ActivitySummaryBinding;
import anjali.example.finbanlacetracker.models.Transaction;
import anjali.example.finbanlacetracker.models.User;
import anjali.example.finbanlacetracker.viewmodels.MainViewModel;

public class SummaryActivity extends AppCompatActivity {
    ActivitySummaryBinding binding;
    Button summary_calculate_btn;
    RadioButton radio_income, radio_expense;
    EditText months_for_forecast;
    MainViewModel viewModel;
    RadioGroup radioGroup;
    FrameLayout summary_result;
    TextView result_text;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        user = getIntent().getParcelableExtra("user");

        summary_calculate_btn = binding.summaryCalculateBtn;
        radio_income = binding.radioIncome;
        radio_expense = binding.radioExpense;
        months_for_forecast = binding.monthsForForecast;
        radioGroup = binding.radioGroup;

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton radioButton = (RadioButton)radioGroup.findViewById(checkedId);
            }
        });

        summary_calculate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "";
                int forecast_months;

                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(SummaryActivity.this, "", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    RadioButton radioButton = (RadioButton)radioGroup.findViewById(selectedId);
                    type = radioButton.getText().toString();
                }

                forecast_months = Integer.parseInt(months_for_forecast.getText().toString());

                //TODO: Fetch ALL THE TRANSACTIONS BASED ON THE TYPE
                Map<String, List<Transaction>> transactions = viewModel.getTransactions(type);
                Log.d("Type", type);
                Log.d("Months", forecast_months + "");
                Log.d("Total Transactions", transactions.size() + "");


                //TODO: GET THE TOTAL OF EACH MONTH
//                Map<String, Double> month_total = new HashMap<>();
                List<Double> month_totals = new ArrayList<>();
                Double sum = 0.0;


                for (Map.Entry<String, List<Transaction>> entry : transactions.entrySet()) {
                    sum = 0.0;
                    Log.d("Month", entry.getKey());
                    for (Transaction transaction : entry.getValue()) {
                        if (type.equals("Income")) {
                            sum += transaction.getAmount();
                        }else {
                            sum += (transaction.getAmount() * -1);
                        }
                        Log.d("Amount", transaction.getAmount() + "");
                    }
                    month_totals.add(sum);
                }

                //TESTING TO SEE IF THE VALUES ARE CORRECT
//                for (Map.Entry<String, Double> entry : month_total.entrySet()) {
//                    Log.d(entry.getKey(), entry.getValue() + "");
//                }

                //TODO: CALCULATE THE MOVING AVERAGE
                Double next_month_prediction = calculateMovingAverage(month_totals, forecast_months);

                if (next_month_prediction.intValue() == -1) {
                    Toast.makeText(getBaseContext(),"Insufficient Data Values", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("Next Month Prediction", next_month_prediction + "");

                    //TODO: SET THE PREDICTION TO THE VIEW
                    summary_result = binding.summaryResult;
                    result_text = binding.resultText;

                    summary_result.setVisibility(View.VISIBLE);
                    if (type.equals("Income")) {
                        summary_result.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.cash_color));
                    }else {
                        summary_result.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.red));
                    }
                    result_text.setText(String.format("Your Next Month Prediction for %s is Rs. %.2f", type, next_month_prediction));
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    public Double calculateMovingAverage(List<Double> data, int n) {
        if (data.size() < n) {
            return -1.0;
        }

        Double sum = 0.0;

        for (int i = data.size() - n; i < data.size(); i++) {
            sum += data.get(i);
        }
        return sum/n;
    }
}