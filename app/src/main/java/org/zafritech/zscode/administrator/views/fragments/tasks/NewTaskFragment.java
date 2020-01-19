package org.zafritech.zscode.administrator.views.fragments.tasks;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.data.db.tasks.Word;
import org.zafritech.zscode.administrator.data.db.tasks.WordViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class NewTaskFragment extends Fragment {

    public static final String EXTRA_REPLY = "org.zafritech.zscode.tasks.wordlistsql.REPLY";

    private WordViewModel mWordViewModel;
    private EditText mEditWordView;

    public static NewTaskFragment newInstance()
    {

        return new NewTaskFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        View view = inflater.inflate(R.layout.fragment_task_new, container, false);
        mEditWordView = view.findViewById(R.id.edit_task);
        final Button button = view.findViewById(R.id.button_task_save);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(mEditWordView.getText())) {

                    Word word = new Word(mEditWordView.getText().toString());

                    mWordViewModel.insert(word);

                    // Hide keypad after editing
                    mEditWordView.setFocusable(false);
                    mEditWordView.setFocusableInTouchMode(true);
                }

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_tasks);

            }

        });

        return view;
    }

}
