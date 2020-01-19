package org.zafritech.zscode.administrator.views.fragments.accounts.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.views.fragments.accounts.common.AccountItem;
import org.zafritech.zscode.administrator.views.fragments.accounts.common.AccountsClickInterface;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AccountsFragment extends Fragment {

    private AccountsFragment context;
    private RecyclerView recyclerView;
    private ArrayList<AccountItem> accountArrayList;
    private AccountItem accountItem;
    private AccountsRecyclerViewAdapter adapter;
    private AccountsClickInterface onClickInterface;

    public static AccountsFragment newInstance()
    {
        return new AccountsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        final Bundle bundle = new Bundle();

        View root = inflater.inflate(R.layout.fragment_accounts, null);
        FloatingActionButton fab = root.findViewById(R.id.fab);
        recyclerView = root.findViewById(R.id.accounts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        populateList();

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });

        onClickInterface = (view, position) -> {

            accountItem = accountArrayList.get(position);
            bundle.putString("name", accountItem.getName());

            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_account_credentials, bundle);
        };

        adapter = new AccountsRecyclerViewAdapter(getActivity(), accountArrayList, onClickInterface);
        recyclerView.setAdapter(adapter);

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

    public void populateList() {

        accountArrayList = new ArrayList<>();

        AccountItem user1 = new AccountItem();
        user1.setImgIcon("https://source.unsplash.com/random?w=100");
        user1.setName("Luke Sibisi");
        user1.setEmail("admin@zafritech.net");
        user1.setJoined("2020-01-12");
        user1.setOnline("10 days ago");
        user1.setRoles("TECH | ADMIN");
        accountArrayList.add(user1);

        AccountItem user2 = new AccountItem();
        user2.setImgIcon("https://source.unsplash.com/random?w=100");
        user2.setName("Mbali Sibisi");
        user2.setEmail("mbalis@zafritech.net");
        user2.setJoined("2020-01-12");
        user2.setOnline("5 days ago");
        user2.setRoles("ADMIN");
        accountArrayList.add(user2);

        AccountItem user3 = new AccountItem();
        user3.setImgIcon("https://source.unsplash.com/random?w=100");
        user3.setName("Khosi Segole-Sibisi");
        user3.setEmail("khosis@zafritech.net");
        user3.setJoined("2020-01-12");
        user3.setOnline("Online");
        user3.setRoles("USER");
        accountArrayList.add(user3);

        AccountItem user4 = new AccountItem();
        user4.setImgIcon("https://source.unsplash.com/random?w=100");
        user4.setName("Ndumiso Sibisi");
        user4.setEmail("ndumisos@zafritech.net");
        user4.setJoined("2020-01-12");
        user4.setOnline("2 hours ago");
        user4.setRoles("USER");
        accountArrayList.add(user4);

        AccountItem user5 = new AccountItem();
        user5.setImgIcon("https://source.unsplash.com/random?w=100");
        user5.setName("Siyabonga Sayi");
        user5.setEmail("siyas@zafritech.net");
        user5.setJoined("2020-01-12");
        user5.setOnline("7 hours ago");
        user5.setRoles("USER");
        accountArrayList.add(user5);

        AccountItem user6 = new AccountItem();
        user6.setImgIcon("https://source.unsplash.com/random?w=100");
        user6.setName("Karabo Ndimande");
        user6.setEmail("karabon@zafritech.net");
        user6.setJoined("2020-01-12");
        user6.setOnline("5 minutes ago");
        user6.setRoles("USER");
        accountArrayList.add(user6);

        AccountItem user7 = new AccountItem();
        user7.setImgIcon("https://source.unsplash.com/random?w=100");
        user7.setName("Lwazi Lukuluba");
        user7.setEmail("lwazil@zafritech.net");
        user7.setJoined("2002-18-18");
        user7.setOnline("10 years ago");
        user7.setRoles("USER");
        accountArrayList.add(user7);

        AccountItem user8 = new AccountItem();
        user8.setImgIcon("https://source.unsplash.com/random?w=100");
        user8.setName("Andile Lukuluba");
        user8.setEmail("andilel@zafritech.net");
        user8.setJoined("2009-02-24");
        user8.setOnline("2 weeks ago");
        user8.setRoles("TECH | ADMIN");
        accountArrayList.add(user8);

    }
}