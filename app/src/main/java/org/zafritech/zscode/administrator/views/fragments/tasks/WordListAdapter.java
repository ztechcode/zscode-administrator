package org.zafritech.zscode.administrator.views.fragments.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.data.db.tasks.Word;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;

    private List<Word> mWords;

    WordListAdapter(Context context) {

        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.row_tasks_word, parent, false);

        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter.WordViewHolder holder, int position) {

        if (mWords != null) {

            Word current = mWords.get(position);
            holder.wordItemView.setText(current.getWord());

        } else {

            holder.wordItemView.setText("No Word found");
        }
    }

    @Override
    public int getItemCount() {

        if (mWords != null)
            return mWords.size();

        else return 0;
    }

    void setWords(List<Word> words) {

        mWords = words;
        notifyDataSetChanged();
    }

    class WordViewHolder extends  RecyclerView.ViewHolder {

        private final TextView wordItemView;

        public WordViewHolder(@NonNull View itemView) {

            super(itemView);

            wordItemView = itemView.findViewById(R.id.word_text_view);
        }
    }
}
