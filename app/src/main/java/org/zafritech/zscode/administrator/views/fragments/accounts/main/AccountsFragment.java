package org.zafritech.zscode.administrator.views.fragments.accounts.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.ApiClient;
import org.zafritech.zscode.administrator.core.api.accounts.AccountsApiService;
import org.zafritech.zscode.administrator.core.api.accounts.models.Account;
import org.zafritech.zscode.administrator.core.api.notes.models.Note;
import org.zafritech.zscode.administrator.core.utils.DividerItemDecoration;
import org.zafritech.zscode.administrator.core.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AccountsFragment extends Fragment {

    private static final String TAG = AccountsFragment.class.getSimpleName();
    private AccountsFragment context;
    private CompositeDisposable disposable = new CompositeDisposable();
    private AccountsApiService apiService;
    private RecyclerView recyclerView;
    private ArrayList<Account> accountList = new ArrayList<>();
    private Account account;
    private AccountsAdapter mAdapter;
    private Bundle bundle;

    public static AccountsFragment newInstance()
    {

        return new AccountsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        final Bundle bundle = new Bundle();

        View root = inflater.inflate(R.layout.fragment_accounts, null);
        recyclerView = root.findViewById(R.id.accounts_recycler_view);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });

        apiService = ApiClient.getClient(getActivity().getApplicationContext()).create(AccountsApiService.class);

        fetchAllAccounts();

        mAdapter = new AccountsAdapter(getActivity(), accountList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                showAccountDetails(position);
            }

            @Override
            public void onLongClick(View view, int position) {

                showActionsDialog(position);
            }
        }));

//        fetchAllAccounts();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_accounts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_search_accounts:

                Snackbar.make(getView(), "Selected menu: Search from Accounts Fragment", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;

            default:
                return false;
        }
    }

    @Override
    public String toString() {

        return "ZTS Accounts";
    }

    private void fetchAllAccounts() {

//        populateList();

        disposable.add(

                apiService.fetchAllAccounts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<Account>>() {

                            @Override
                            public void onSuccess(List<Account> accounts) {

                                System.out.println("Success! Account count: " + accounts.size());

                                accountList.clear();
                                accountList.addAll(accounts);
                                mAdapter.notifyDataSetChanged();

                                // TODO: Create toggle to replace no accounts found layout!
                            }

                            @Override
                            public void onError(Throwable e) {

                                Log.e(TAG, "onError: " + e.getMessage());
//                                showError(e);
                            }
                        })
        );
    }

    public void populateList() {

        accountList = new ArrayList<>();

        Account user1 = new Account();
        user1.setPhotoUrl("https://source.unsplash.com/random?w=100");
        user1.setFirstName("Luke");
        user1.setFirstName("Sibisi");
        user1.setEmail("admin@zafritech.net");
        user1.setJoined("2020-01-12");
        user1.setOnline("10 days ago");
        user1.setRoles("TECH | ADMIN");
        accountList.add(user1);

        Account user2 = new Account();
        user2.setPhotoUrl("https://source.unsplash.com/random?w=100");
        user2.setFirstName("Mbali Sibisi");
        user2.setEmail("mbalis@zafritech.net");
        user2.setJoined("2020-01-12");
        user2.setOnline("5 days ago");
        user2.setRoles("ADMIN");
        accountList.add(user2);

        Account user3 = new Account();
        user3.setPhotoUrl("https://source.unsplash.com/random?w=100");
        user3.setFirstName("Khosi Segole-Sibisi");
        user3.setEmail("khosis@zafritech.net");
        user3.setJoined("2020-01-12");
        user3.setOnline("Online");
        user3.setRoles("USER");
        accountList.add(user3);

        Account user4 = new Account();
        user4.setPhotoUrl("https://source.unsplash.com/random?w=100");
        user4.setFirstName("Ndumiso Sibisi");
        user4.setEmail("ndumisos@zafritech.net");
        user4.setJoined("2020-01-12");
        user4.setOnline("2 hours ago");
        user4.setRoles("USER");
        accountList.add(user4);

        Account user5 = new Account();
        user5.setPhotoUrl("https://source.unsplash.com/random?w=100");
        user5.setFirstName("Siyabonga Sayi");
        user5.setEmail("siyas@zafritech.net");
        user5.setJoined("2020-01-12");
        user5.setOnline("7 hours ago");
        user5.setRoles("USER");
        accountList.add(user5);

        Account user6 = new Account();
        user6.setPhotoUrl("https://source.unsplash.com/random?w=100");
        user6.setFirstName("Karabo Ndimande");
        user6.setEmail("karabon@zafritech.net");
        user6.setJoined("2020-01-12");
        user6.setOnline("5 minutes ago");
        user6.setRoles("USER");
        accountList.add(user6);

        Account user7 = new Account();
        user7.setPhotoUrl("https://source.unsplash.com/random?w=100");
        user7.setFirstName("Lwazi Lukuluba");
        user7.setEmail("lwazil@zafritech.net");
        user7.setJoined("2002-18-18");
        user7.setOnline("10 years ago");
        user7.setRoles("USER");
        accountList.add(user7);

        Account user8 = new Account();
        user8.setPhotoUrl("https://source.unsplash.com/random?w=100");
        user8.setFirstName("Andile Lukuluba");
        user8.setEmail("andilel@zafritech.net");
        user8.setJoined("2009-02-24");
        user8.setOnline("2 weeks ago");
        user8.setRoles("TECH | ADMIN");
        accountList.add(user8);

    }

    private void showActionsDialog(final int position) {

        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {

                    showAccountDetails(position);

                } else {

                    deleteAccount(accountList.get(position).getId(), position);
                }
            }
        });

        builder.show();
    }

    private void showAccountDetails(final int position) {

        Bundle bundle = new Bundle();
        account = accountList.get(position);
        bundle.putString("name", account.getFirstName() + " " + account.getLastName());

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_account_credentials, bundle);
    }

    private void createAccount(Account account) {

        Log.e(TAG, "createAccount: " + account.getEmail());

    }

    private void updateAccount(Account account) {

        Log.e(TAG, "createAccount: " + account.getId() + ", " + account.getEmail());

    }

    private void deleteAccount(final int accountId, final int position) {

        Log.e(TAG, "deleteAccount: " + accountId + ", " + position);

    }
}