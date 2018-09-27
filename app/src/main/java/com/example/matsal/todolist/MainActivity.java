package com.example.matsal.todolist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ArrayList<HashMap<Integer,String>> noteLists=new ArrayList<>();
    private ListView lv;
    private String path = Environment.getExternalStorageDirectory().toString() + "/ToDo_NoteLists";
    private ArrayAdapter aa;
    DataBaseOwner dbo = new DataBaseOwner(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });

        lv = findViewById(R.id.lVToDoList);
        registerForContextMenu(lv);

        //obsługa zaznaczenia kilku checkboxów//
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteLists listItem = (NoteLists) lv.getItemAtPosition(position);
                Integer id1 = listItem.getId();
                Toast.makeText(getApplicationContext(), ""+id1, Toast.LENGTH_LONG).show();
                if ( lv.isItemChecked(position)){
                      view.setBackgroundColor(Color.LTGRAY);
                  }
                  else{
                      view.setBackgroundColor(Color.WHITE);
                  }
//                lv.setItemChecked(position,true);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_content, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info  = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Toast.makeText(getApplicationContext(), info.position, Toast.LENGTH_LONG).show();
        switch(item.getItemId()){
            case R.id.iDelete:
//                old method - with delete fiels
//                deleteNote(noteLists.get(info.position));
//                noteLists.remove(info.position);
//                aa.notifyDataSetChanged();
                dbo.deleteNote(info.position);
                noteLists.remove(info.position);
                aa.notifyDataSetChanged();
                Log.d("note","remove data: DBid - " + info.position +1+
                        ", note - "+ noteLists.remove(info.position));
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAllContent() throws IOException {
        noteLists.clear();

//        old method with file
//        try {
//            File folder = new File(path);
//            String[] file = folder.list(new FilenameFilter() {
//                @Override
//                public boolean accept(File dir, String name) {
//                    return name.endsWith(".txt");
//                }
//            });
//
//            for (int i = 0; i < file.length; i++) {
//                BufferedReader br = new BufferedReader(new FileReader(path + "/" + file[i]));
//                String line = br.readLine();
//                noteLists.add(line.toString() + file[i]);
//                aa = new ArrayAdapter(this, android.R.layout.select_dialog_multichoice,  noteLists);
//            }
//            //predefiniowany layout z checkboxami//
////            aa = new ArrayAdapter(this, android.R.layout.select_dialog_multichoice, (List) noteLists);
//            lv.setAdapter(aa);
//        }catch (Exception e){
//            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
//        }

        Cursor cur = dbo.getAllData();
        ArrayList<NoteLists> NoteLists = new ArrayList<>();
        while (cur.moveToNext()) {
            HashMap<Integer, String> hashMap = new HashMap<>();//create a hashmap to store the data in key value pair
            hashMap.put(cur.getInt(0), cur.getString(1));
            NoteLists.add(new NoteLists(cur.getString(1), cur.getInt(0)));
            noteLists.add(hashMap);//add the hashmap into arrayList
            Toast.makeText(getApplicationContext(), cur.getInt(0) +"-"+cur.getString(1), Toast.LENGTH_LONG).show();
        }
        aa = new ArrayAdapter(this, android.R.layout.select_dialog_multichoice, NoteLists);
        lv.setAdapter(aa);
    }

//    old method to file
    private void deleteNote(String note){
        try{
            File folder = new File(path);
            String[] file = folder.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt");
                }
            });
            for (int i = 0; i < file.length; i++) {
                BufferedReader br = new BufferedReader(new FileReader(path + "/" + file[i]));
                if(note.equals(br.readLine().toString())){
                    File f = new File(path + "/" + file[i]);
                    f.delete();
                }
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        Log.d("aktywność","onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("aktywność","onResume");
        try {
            getAllContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("aktywność","onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.d("aktywność","onRestart");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.d("aktywność","onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("aktywność","onDestroy");
        super.onDestroy();
    }
}
