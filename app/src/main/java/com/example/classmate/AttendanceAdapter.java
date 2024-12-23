package com.example.classmate;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private List<Subject> subjectList;
    private OnAttendanceListener onAttendanceListener;

    public interface OnAttendanceListener {
        void onPresentClick(int position,Subject subject);
        void onAbsentClick(int position,Subject subject);
        void onDeleteClick(int position,Subject subject);
    }

    public AttendanceAdapter(List<Subject> subjects,OnAttendanceListener listener){
        this.subjectList=subjects;
        this.onAttendanceListener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject_attendance, parent, false);
        return new ViewHolder(view, onAttendanceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        Subject subject=subjectList.get(position);
        holder.bind(subject);
    }

    @Override
    public int getItemCount(){
        return subjectList.size();
    }

    public void updateSubjects(List<Subject> subjects){
        this.subjectList=subjects;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, attendancePresent, attendanceAbsent, percentageText;
        CircularProgressIndicator progressIndicator;
        Button presentButton, absentButton;
        OnAttendanceListener listener;

        ViewHolder(View itemView, OnAttendanceListener listener) {
            super(itemView);
            this.listener=listener;

            subjectName=itemView.findViewById(R.id.subject_name);
            attendancePresent=itemView.findViewById(R.id.attendance_present);
            attendanceAbsent=itemView.findViewById(R.id.attendance_absent);
            progressIndicator=itemView.findViewById(R.id.progress_indicator);
            percentageText=itemView.findViewById(R.id.percentage_text);
            presentButton=itemView.findViewById(R.id.button);
            absentButton=itemView.findViewById(R.id.button2);

            setupClickListeners();
        }

        void bind(Subject subject) {
            subjectName.setText(subject.getName());
            attendancePresent.setText("Presents: " + subject.getTotalPresent());
            attendanceAbsent.setText("Absents: " + subject.getTotalAbsent());

            int percentage=(int) subject.getAttendancePercentage();
            progressIndicator.setProgress(percentage);
            percentageText.setText(percentage + "%");
        }

        private void setupClickListeners(){
            presentButton.setOnClickListener(v -> {
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    listener.onPresentClick(position, null);
                }
            });

            absentButton.setOnClickListener(v -> {
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    listener.onAbsentClick(position, null);
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    listener.onDeleteClick(position, null);
                    return true;
                }
                return false;
            });
        }
    }
}
