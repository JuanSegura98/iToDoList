package com.example.itodolist;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TaskRowAdapter extends RecyclerView.Adapter<TaskRowAdapter.ViewHolder> {

    public List<Task> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    public void setItems(List<Task> items) {
        this.mData = items;
    }

    // data is passed into the constructor
    TaskRowAdapter(Context context, List<Task> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.task_row_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mData.get(position);
        int percentageCompleted = Math.round((float) task.currentUnits * 100 / task.totalUnits);
        String unitString = task.totalUnits + " " + task.measureUnit;

        Date begin_date = new Date(2000, 01, 01);
        Date end_date = new Date(2000, 01, 01);
        Date current_date =  Calendar.getInstance().getTime();

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            begin_date = format.parse(task.beginDate);
            end_date = format.parse(task.endDate);
        } catch (ParseException e) {
            Log.e("ERROR:Parsing dates", "");
        }

        long daysPassed = TimeUnit.MILLISECONDS.toDays(current_date.getTime() - begin_date.getTime());
        long daysTotal = TimeUnit.MILLISECONDS.toDays(end_date.getTime() - begin_date.getTime());

        int percentageLinear = (int) (((double) daysPassed / (double) daysTotal) * 100);

        if(percentageLinear > 100)
            percentageLinear = 100;

        holder.myTextView.setText(task.name);
        holder.linearProgressView.getLayoutParams().width = convertDpToPx(context, percentageLinear);
        holder.imageView.getLayoutParams().width = convertDpToPx(context, percentageCompleted);
        holder.unitsView.setText(unitString);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Task item, int position) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public int convertDpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        TextView unitsView;
        ImageView imageView;
        ImageView linearProgressView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.taskTitle);
            unitsView = itemView.findViewById(R.id.units);
            imageView = itemView.findViewById(R.id.currentUnitImage);
            linearProgressView = itemView.findViewById(R.id.linearProgressImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Task getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}



