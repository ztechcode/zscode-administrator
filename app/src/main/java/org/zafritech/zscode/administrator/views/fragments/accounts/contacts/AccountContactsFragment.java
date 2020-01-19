package org.zafritech.zscode.administrator.views.fragments.accounts.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.zafritech.zscode.administrator.R;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class AccountContactsFragment extends Fragment {

    private Button buttonSaveContact;

    public AccountContactsFragment newInstance()
    {
        return new AccountContactsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_accounts_contacts, container, false);

        buttonSaveContact = root.findViewById(R.id.button_accounts_save_contact);

        buttonSaveContact.setOnClickListener(view -> {

            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_account_credentials);

        });

        return root;
    }

}