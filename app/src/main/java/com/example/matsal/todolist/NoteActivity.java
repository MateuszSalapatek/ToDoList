package com.example.matsal.todolist;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private String path = Environment.getExternalStorageDirectory().toString() + "/ToDo_NoteLists";
    private EditText etNoteContent;
    DataBaseOwner dbo = new DataBaseOwner(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNoteContent = findViewById(R.id.eTContentText);
    }

    public void addNote(View view) {
        createDir();
        createFile();
        finish();
    }

    public void createDir() {
        File path = new File(this.path);
        if(!path.exists()){
            try{
                path.mkdir();
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void createFile(){
        File file = new File(path + "/" + System.currentTimeMillis() + ".txt");
        FileOutputStream fOut;
        OutputStreamWriter myOutWriter;
        dbo.addNote(etNoteContent.getText().toString(),null);
        try{
            fOut = new FileOutputStream(file);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(etNoteContent.getText().toString());
            myOutWriter.close();
            fOut.close();

        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), R.string.lPleaseCreateNote, Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
