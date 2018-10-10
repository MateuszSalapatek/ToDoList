package com.example.matsal.todolist;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private String path = Environment.getExternalStorageDirectory().toString() + "/ToDo_NoteLists";
    private EditText etNoteContent;
    private Button bAddNote;
    DataBaseOwner dbo = new DataBaseOwner(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNoteContent = findViewById(R.id.eTContentText);
        bAddNote = findViewById(R.id.bAddNote);
    }

    public void addNote(View view) {
        if(etNoteContent.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), R.string.sFillTheNote, Toast.LENGTH_SHORT).show();
            return;
        }
        dbo.addNote(etNoteContent.getText().toString(),null);
        Log.d("noteLog", "\ncreate note view - text: " + etNoteContent.getText().toString());
        finish();
    }
}
