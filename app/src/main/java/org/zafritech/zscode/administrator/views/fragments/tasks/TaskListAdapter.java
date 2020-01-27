package org.zafritech.zscode.administrator.views.fragments.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.tasks.models.Task;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<Task> mTasks;

    public TaskListAdapter(Context context, List<Task> mTasks) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mTasks = mTasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.row_tasks_list_item, parent, false);

        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        if (mTasks != null) {

            Task current = mTasks.get(position);
            holder.taskItemView.setText(current.getDetails());
            holder.taskItemCategoryTag.setText(current.getCategory());

        } else {

            holder.taskItemView.setText("No category found");
        }
    }

    @Override
    public int getItemCount() {

        if (mTasks != null) return mTasks.size();

        else return 0;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private final TextView taskItemView;
        private final TextView taskItemCategoryTag;

        public TaskViewHolder(@NonNull View itemView) {

            super(itemView);

            taskItemView = itemView.findViewById(R.id.task_item_title);
            taskItemCategoryTag = itemView.findViewById(R.id.tags_categories);
        }
    }
}
