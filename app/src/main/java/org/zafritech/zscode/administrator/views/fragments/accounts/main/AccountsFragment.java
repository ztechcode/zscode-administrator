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
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.ApiClient;
import org.zafritech.zscode.administrator.core.api.accounts.AccountsApiService;
import org.zafritech.zscode.administrator.core.api.accounts.models.Account;
import org.zafritech.zscode.administrator.core.utils.DividerItemDecoration;
import org.zafritech.zscode.administrator.core.utils.RecyclerTouchListener;

import java.util.ArrayList;
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
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AccountsFragment extends Fragment {

    private static final String TAG = AccountsFragment.class.getSimpleName();
    private AccountsFragment context;
    private CompositeDisposable disposable = new CompositeDisposable();
    private AccountsApiService apiService;
    private RecyclerView recyclerView;
    private ProgressBar accountsLoading;
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
        accountsLoading = root.findViewById(R.id.accounts_empty_progress_bar);

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

                                toggleEmptyAccountsList();
                            }

                            @Override
                            public void onError(Throwable e) {

                                Log.e(TAG, "onError: " + e.getMessage());
//                                showError(e);
                            }
                        })
        );
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

                    deleteAccount(accountList.get(position).getId().intValue(), position);
                }
            }
        });

        builder.show();
    }

    private void showAccountDetails(final int position) {

        Bundle bundle = new Bundle();
        account = accountList.get(position);
        bundle.putLong("id", account.getId());

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

    private void toggleEmptyAccountsList() {

        if (accountList.size() > 0) {

            accountsLoading.setVisibility(View.GONE);

        } else {

            accountsLoading.setVisibility(View.VISIBLE);
        }
    }

}