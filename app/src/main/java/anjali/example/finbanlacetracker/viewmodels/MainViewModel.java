package anjali.example.finbanlacetracker.viewmodels;

import android.app.Application;
import android.app.TaskInfo;
import android.hardware.usb.UsbRequest;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import anjali.example.finbanlacetracker.models.Transaction;
import anjali.example.finbanlacetracker.models.User;
import anjali.example.finbanlacetracker.utils.Constants;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {

    public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();
    public MutableLiveData<RealmResults<Transaction>> categoriesTransactions = new MutableLiveData<>();

    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();

    public MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    Realm realm;
    Calendar calendar;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
//        setupDatabase();
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .allowWritesOnUiThread(true)
                        .deleteRealmIfMigrationNeeded()
                        .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
    }

    public Map<String, List<Transaction>> getTransactions(String type){
        calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        // Retrieve all transactions for the current year
        Calendar yearStart = Calendar.getInstance();
        yearStart.set(currentYear, Calendar.JANUARY, 1, 0, 0, 0);

        Calendar yearEnd = Calendar.getInstance();
        yearEnd.set(currentYear, Calendar.DECEMBER, 31, 23, 59, 59);

        RealmResults<Transaction> results = realm.where(Transaction.class)
                .greaterThanOrEqualTo("date", yearStart.getTime())
                .lessThanOrEqualTo("date", yearEnd.getTime())
                .equalTo("type", type.toUpperCase())
                .findAll();

        // Group transactions by month
        Map<String, List<Transaction>> groupedByMonth = new HashMap<>();

        for (Transaction transaction : results) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(transaction.getDate());
            String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()); // e.g., "January"

            // Add the transaction to the appropriate month group
            groupedByMonth.computeIfAbsent(month, k -> new ArrayList<>()).add(transaction);
        }

        return groupedByMonth;
    }

    public void getTransactions(Calendar calendar, String type) {
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        RealmResults<Transaction> newTransactions = null;
        if(Constants.SELECTED_TAB_STATS == Constants.DAILY) {
            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", type)
                    .findAll();

        } else if(Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH,0);
            Date startTime = calendar.getTime();

            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", type)
                    .findAll();
        }

        categoriesTransactions.setValue(newTransactions);
    }


    public void getTransactions(Calendar calendar) {
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        double income = 0;
        double expense = 0;
        double total = 0;
        RealmResults<Transaction> newTransactions = null;
        if(Constants.SELECTED_TAB == Constants.DAILY) {
            // Select * from transactions
            // Select * from transactions where id = 5

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.INCOME)
                    .sum("amount")
                    .doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount")
                    .doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .sum("amount")
                    .doubleValue();

        } else if(Constants.SELECTED_TAB == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH,0);

            Date startTime = calendar.getTime();


            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.INCOME)
                    .sum("amount")
                    .doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount")
                    .doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .sum("amount")
                    .doubleValue();
        } else if(Constants.SELECTED_TAB == Constants.SUMMARY) {
            calendar.set(Calendar.DAY_OF_MONTH,0);

            Date startTime = calendar.getTime();


            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.INCOME)
                    .sum("amount")
                    .doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount")
                    .doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .sum("amount")
                    .doubleValue();
        }

        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(total);
        transactions.setValue(newTransactions);
//        RealmResults<Transaction> newTransactions = realm.where(Transaction.class)
//                .equalTo("date", calendar.getTime())
//                .findAll();

    }

    public void addTransaction(Transaction transaction) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transaction);
        // some code here
        realm.commitTransaction();
    }

    public void addUser(User user) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    public User getUser(String email) {
        return realm.where(User.class).equalTo("email", email).findFirst();
    }

    public boolean updateUser(User user) {
        realm.beginTransaction(); // Start the transaction
        try {
            User toEdit = realm.where(User.class)
                    .equalTo("email", user.getEmail())
                    .findFirst();

            if (toEdit == null) {
                realm.cancelTransaction(); // Roll back if no user is found
                return false;
            }

            // Modify the user object

            toEdit.setName(user.getName());
            toEdit.setOccupation(user.getOccupation());
            toEdit.setPh_number(user.getPh_number());

            Log.d("User Name", toEdit.getName());
            Log.d("User Occupation", toEdit.getOccupation());
            Log.d("User Phone", toEdit.getPh_number());
            realm.commitTransaction(); // Commit the transaction
            return true;
        } catch (Exception e) {
            realm.cancelTransaction(); // Roll back in case of an error
            e.printStackTrace();
            return false;
        }
    }

    public boolean changePassword(User user) {
        realm.beginTransaction(); // Start the transaction
        try {
            User toEdit = realm.where(User.class)
                    .equalTo("email", user.getEmail())
                    .findFirst();

            if (toEdit == null) {
                realm.cancelTransaction(); // Roll back if no user is found
                return false;
            }

            // Modify the user object
            toEdit.setPassword(user.getPassword());

            realm.commitTransaction(); // Commit the transaction
            return true;
        } catch (Exception e) {
            realm.cancelTransaction(); // Roll back in case of an error
            e.printStackTrace();
            return false;
        }
    }

    public void deleteTransaction(Transaction transaction) {
        realm.beginTransaction();
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransactions(calendar);
    }

    public void addTransactions() {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Business", "Cash", "Some note here", new Date(), 500, new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE, "Investment", "Bank", "Some note here", new Date(), -900, new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Rent", "Other", "Some note here", new Date(), 500, new Date().getTime()));
        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Business", "Card", "Some note here", new Date(), 500, new Date().getTime()));
        // some code here
        realm.commitTransaction();
    }

    public RealmResults<Transaction> findTransactions(String query){
        return realm.where(Transaction.class).contains("category", query, Case.INSENSITIVE).findAll();
    }

    void setupDatabase() {
        realm = Realm.getDefaultInstance();
    }

}
