package org.zafritech.zscode.administrator.views.fragments.notes;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;
import org.json.JSONObject;
import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.ApiClient;
import org.zafritech.zscode.administrator.core.api.tasks.TasksApiService;
import org.zafritech.zscode.administrator.core.api.tasks.models.Note;
import org.zafritech.zscode.administrator.core.utils.DividerItemDecoration;
import org.zafritech.zscode.administrator.core.utils.RecyclerTouchListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NotesFragment extends Fragment {

    private static final String TAG = NotesFragment.class.getSimpleName();
    private TasksApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    TextView noNotesView;

    public static NotesFragment newInstance() {

        return new NotesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_notes, null);
        coordinatorLayout =  view.findViewById(R.id.notes_coordinator_layout);
        noNotesView =  view.findViewById(R.id.notes_txt_empty_notes_view);
        recyclerView = view.findViewById(R.id.notes_recycler_view);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                showNoteDialog(false, null, -1);
            }
        });

        apiService = ApiClient.getClient(getActivity().getApplicationContext()).create(TasksApiService.class);

        mAdapter = new NotesAdapter(getActivity(), notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, final int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

                showActionsDialog(position);
            }
        }));

        fetchAllNotes();

        return view;
    }

    private void fetchAllNotes() {

        disposable.add(

                apiService.fetchAllNotes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<List<Note>, List<Note>>() {

                            @Override
                            public List<Note> apply(List<Note> notes) throws Exception {
                                // TODO - note about sort
                                Collections.sort(notes, new Comparator<Note>() {

                                    @Override
                                    public int compare(Note n1, Note n2) {
                                        return n2.getId() - n1.getId();
                                    }
                                });

                                return notes;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<List<Note>>() {

                            @Override
                            public void onSuccess(List<Note> notes) {

                                notesList.clear();
                                notesList.addAll(notes);
                                mAdapter.notifyDataSetChanged();

                                toggleEmptyNotes();
                            }

                            @Override
                            public void onError(Throwable e) {

                                Log.e(TAG, "onError: " + e.getMessage());
                                showError(e);
                            }
                        })
        );
    }

    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity().getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.fragment_note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && note != null) {

            inputNote.setText(note.getText());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {

                    Toast.makeText(getActivity(), "Enter note!", Toast.LENGTH_SHORT).show();
                    return;

                } else {

                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && note != null) {

                    // update note by it's id
                    updateNote(note.getId(), inputNote.getText().toString(), position);

                } else {

                    // create new note
                    createNote(inputNote.getText().toString());

                }
            }
        });
    }

    private void showActionsDialog(final int position) {

        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {

                    showNoteDialog(true, notesList.get(position), position);

                } else {

                    deleteNote(notesList.get(position).getId(), position);
                }
            }
        });

        builder.show();
    }

    private void createNote(String note) {

        disposable.add(apiService.createNote(new Note(note))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Note>() {

                            @Override
                            public void onSuccess(Note note) {

                                if (!TextUtils.isEmpty(note.getError())) {
                                    Toast.makeText(getActivity().getApplicationContext(), note.getError(), Toast.LENGTH_LONG).show();
                                    return;
                                }

                                Log.d(TAG, "new note created: " + note.getId() + ", " + note.getText() + ", " + note.getTimestamp());

                                // Add new item and notify adapter
                                notesList.add(0, note);
                                mAdapter.notifyItemInserted(0);

                                toggleEmptyNotes();
                            }

                            @Override
                            public void onError(Throwable e) {

                                Log.e(TAG, "onError: " + e.getMessage());
                                showError(e);
                            }
                        }));
    }

    private void updateNote(int noteId, final String note, final int position) {

        disposable.add(apiService.updateNote(new Note(noteId, note))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {

                            @Override
                            public void onComplete() {

                                Log.d(TAG, "Note updated!");

                                Note n = notesList.get(position);
                                n.setText(note);

                                // Update item and notify adapter
                                notesList.set(position, n);
                                mAdapter.notifyItemChanged(position);
                            }

                            @Override
                            public void onError(Throwable e) {

                                Log.e(TAG, "onError: " + e.getMessage());
                                showError(e);
                            }
                        }));
    }

    private void deleteNote(final int noteId, final int position) {

        Log.e(TAG, "deleteNote: " + noteId + ", " + position);

        disposable.add(apiService.deleteNote(noteId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Note deleted! " + noteId);

                                // Remove and notify adapter about item deletion
                                notesList.remove(position);
                                mAdapter.notifyItemRemoved(position);

                                Toast.makeText(getActivity(), "Note deleted!", Toast.LENGTH_SHORT).show();

                                toggleEmptyNotes();
                            }

                            @Override
                            public void onError(Throwable e) {

                                Log.e(TAG, "onError: " + e.getMessage());
                                showError(e);
                            }
                        })
        );
    }

    private void toggleEmptyNotes() {

        if (notesList.size() > 0) {

            noNotesView.setVisibility(View.GONE);

        } else {

            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    private void whiteNotificationBar(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getActivity().getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    private void showError(Throwable e) {

        String message = "";

        try {

            if (e instanceof IOException) {

                message = "No internet connection!";

            } else if (e instanceof HttpException) {

                HttpException error = (HttpException) e;
                String errorBody = error.response().errorBody().string();
                JSONObject jObj = new JSONObject(errorBody);

                message = jObj.getString("error");

            }

        } catch (IOException e1) {

            e1.printStackTrace();

        } catch (JSONException e1) {

            e1.printStackTrace();

        } catch (Exception e1) {

            e1.printStackTrace();
        }

        if (TextUtils.isEmpty(message)) {

            message = "Unknown error occurred! Check LogCat.";
        }

        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

}
