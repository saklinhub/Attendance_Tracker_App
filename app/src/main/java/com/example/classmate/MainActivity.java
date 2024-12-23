package com.example.classmate;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AttendanceAdapter.OnAttendanceListener {
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private List<Subject> subjectList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper=new DatabaseHelper(this);
        setupRecyclerView();
        setupAddButton();
        loadSubjects();
    }

    private void setupRecyclerView(){
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        subjectList=new ArrayList<>();
        adapter=new AttendanceAdapter(subjectList,this);
        recyclerView.setAdapter(adapter);
    }

    private void setupAddButton(){
        ImageButton addButton=findViewById(R.id.button_add_subject);
        addButton.setOnClickListener(v -> showAddSubjectDialog());
    }

    private void loadSubjects(){
        subjectList.clear();
        subjectList.addAll(dbHelper.getAllSubjects());
        adapter.notifyDataSetChanged();
    }

    private void showAddSubjectDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Add Subject");

        EditText input=new EditText(this);
        input.setHint("Enter subject name");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String subjectName=input.getText().toString().trim();
            if(!subjectName.isEmpty()){
                Subject newSubject=new Subject(subjectName, 0, 0);
                dbHelper.addSubject(newSubject);
                loadSubjects();
            }else{
                Toast.makeText(this, "Subject name cannot be empty",
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel",null);
        builder.show();
    }

    @Override
    public void onPresentClick(int position,Subject subject){
        subject=subjectList.get(position);
        subject.incrementPresent();
        dbHelper.updateSubject(subject);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onAbsentClick(int position,Subject subject){
        subject=subjectList.get(position);
        subject.incrementAbsent();
        dbHelper.updateSubject(subject);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onDeleteClick(int position, Subject subject){
        subject=subjectList.get(position);
        Subject finalSubject=subject;
        new AlertDialog.Builder(this).setTitle("Delete Subject")
                .setMessage("Are you sure you want to delete " + subject.getName() + "?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        dbHelper.deleteSubject(finalSubject.getName());
                        loadSubjects();
                    })
                    .setNegativeButton("Cancel", null).show();
    }
}