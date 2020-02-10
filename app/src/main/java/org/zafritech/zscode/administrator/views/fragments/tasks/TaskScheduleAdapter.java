package org.zafritech.zscode.administrator.views.fragments.tasks;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.tasks.enums.Priority;
import org.zafritech.zscode.administrator.core.api.tasks.models.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TaskScheduleAdapter extends RecyclerView.Adapter<TaskScheduleAdapter.TaskViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<Schedule> mSchedules;

    public TaskScheduleAdapter(Context context, List<Schedule> mSchedules) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mSchedules = mSchedules;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.row_tasks_list_item, parent, false);

        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (mSchedules != null) {

            Schedule current = mSchedules.get(position);
            holder.taskItemView.setText(current.getTask().getDetails());
            holder.taskItemCategoryTag.setText(current.getTask().getCategory().getName().toUpperCase());

            if (current.getTask().getRepeat() != null) {
                holder.taskItemRepeatIcon.setVisibility(View.VISIBLE);
            } else {
                holder.taskItemRepeatIcon.setVisibility(View.GONE);
            }

            if (current.getTask().getTags().size() > 0) {
                holder.taskItemTagsIcon.setVisibility(View.VISIBLE);
            } else {
                holder.taskItemTagsIcon.setVisibility(View.GONE);
            }
            if (current.getTask().getNotes().size() > 0) {
                holder.taskItemNotesIcon.setVisibility(View.VISIBLE);
            } else {
                holder.taskItemNotesIcon .setVisibility(View.GONE);
            }

            if (current.getTask().getPriority().equals(Priority.TRIVIAL)) {
                holder.taskItemPriorityIcon.setVisibility(View.GONE);
            } else if (current.getTask().getPriority().equals(Priority.LOW)) {
                holder.taskItemPriorityIcon.setVisibility(View.GONE);
            } else if (current.getTask().getPriority().equals(Priority.MEDIUM)) {
                holder.taskItemPriorityIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorGreen), PorterDuff.Mode.SRC_IN);
            } else if (current.getTask().getPriority().equals(Priority.HIGH)) {
                holder.taskItemPriorityIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorOrange), PorterDuff.Mode.SRC_IN);
            } else if (current.getTask().getPriority().equals(Priority.CRITICAL)) {
                holder.taskItemPriorityIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorRed), PorterDuff.Mode.SRC_IN);
            }

            try {

                String deadline = current.getDeadline().replace("T", " ");
                Date date = dateFormatter.parse(deadline);

                if (isOverDue(current.getDeadline())) {

                    holder.taskItemDueDate.setText(dateFormatter.format(date) + " - Overdue");
                    holder.taskItemDot.setColorFilter(ContextCompat.getColor(context, R.color.colorRed), PorterDuff.Mode.SRC_IN);
                    holder.taskItemTimerIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorRed), PorterDuff.Mode.SRC_IN);
                    holder.taskItemDueDate.setTextColor(ContextCompat.getColor(context, R.color.colorRed));

                } else {

                    holder.taskItemDueDate.setText(dateFormatter.format(date));
                    holder.taskItemDot.setColorFilter(ContextCompat.getColor(context, R.color.colorGreen), PorterDuff.Mode.SRC_IN);
                    holder.taskItemTimerIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorGreen), PorterDuff.Mode.SRC_IN);
                    holder.taskItemDueDate.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                }

            } catch (ParseException e) {

                e.printStackTrace();
            }

//            if (current.getTask().getNotes() != null) {
//                holder.taskItemNotesIcon.setVisibility(View.VISIBLE);
//            } else {
//                holder.taskItemNotesIcon.setVisibility(View.GONE);
//            }

        } else {

            holder.taskItemView.setText("No category found");
        }
    }

    @Override
    public int getItemCount() {

        if (mSchedules != null) return mSchedules.size();

        else return 0;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private final TextView taskItemView;
        private final TextView taskItemCategoryTag;
        private final TextView taskItemDueDate;
        private final ImageView taskItemDot;
        private final ImageView taskItemRepeatIcon;
        private final ImageView taskItemNotesIcon;
        private final ImageView taskItemTimerIcon;
        private final ImageView taskItemTagsIcon;
        private final ImageView taskItemPriorityIcon;

        public TaskViewHolder(@NonNull View itemView) {

            super(itemView);

            taskItemDot = itemView.findViewById(R.id.item_dot);
            taskItemView = itemView.findViewById(R.id.task_item_title);
            taskItemCategoryTag = itemView.findViewById(R.id.tags_categories);
            taskItemDueDate = itemView.findViewById(R.id.task_item_due_date_text_view);
            taskItemRepeatIcon = itemView.findViewById(R.id.tasks_repeat_icon);
            taskItemNotesIcon = itemView.findViewById(R.id.tasks_notes_icon);
            taskItemTimerIcon = itemView.findViewById(R.id.tasks_timer_icon);
            taskItemTagsIcon = itemView.findViewById(R.id.tasks_tags_icon);
            taskItemPriorityIcon = itemView.findViewById(R.id.tasks_priority_icon);
        }
    }

    private boolean isOverDue(String date) {

        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dueDate = formatter.parse(date);
            Date today = new Date();

            return (dueDate.compareTo(today) < 0) ? true : false;

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return false;
    }
}
