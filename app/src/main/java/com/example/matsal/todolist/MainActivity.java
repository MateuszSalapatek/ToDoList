package com.example.matsal.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<HashMap<Integer,String>> noteLists=new ArrayList<>();
    private ListView lv;
    private ArrayAdapter aa;
    private Button bDeleteAll, bCreateNote;
    DataBaseOwner dbo = new DataBaseOwner(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("function","onCreate");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        lv = findViewById(R.id.lVToDoList);
        bDeleteAll = findViewById(R.id.bDeleteNotes);
        bCreateNote = findViewById(R.id.bAddNote);
        registerForContextMenu(lv);


        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteLists listItem = (NoteLists) lv.getItemAtPosition(position);
                Integer id1 = listItem.getId();
                if ( lv.isItemChecked(position)) {
                    view.setBackgroundColor(Color.LTGRAY);
                    bDeleteAll.setVisibility(View.VISIBLE);
                    dbo.checkNote(id1);
                }else {
                    view.setBackgroundColor(Color.WHITE);
                    if (getCheckedNotesIds().isEmpty()) {
                        bDeleteAll.setVisibility(View.INVISIBLE);
                        dbo.uncheckNote(id1);
                    }
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d("function","onCreateContextMenu" );
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_content, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("function","onContextItemSelected" );
        AdapterView.AdapterContextMenuInfo info  = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        try {
            if (item.getItemId() == R.id.iDelete) {
                NoteLists listItem = (NoteLists) lv.getItemAtPosition(info.position);
                dbo.deleteNote(listItem.getId());
                onResume();
                Log.d("noteLog", "\nremove note - id: " + listItem.getId() +
                        ", note: " + listItem + ", list view position: " + info.position);
            }else if (item.getItemId() == R.id.iChangeNote) {
                String note =  lv.getItemAtPosition(info.position).toString();
                NoteLists listItem = (NoteLists) lv.getItemAtPosition(info.position);
                Integer id = listItem.getId();

                Intent intent = new Intent(MainActivity.this, NoteActivity.class);

                //to pass additional value for new activity
                intent.putExtra("EDIT_TEXT", note);
                intent.putExtra("ID",id);
                //////////////////////////////////////////
                startActivity(intent);
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("function","onCreateOptionsMenu" );
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("function","onOptionsItemSelected" );
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

    public void getAllContent(){
        try {
            //to remember the position of checked notes
            ArrayList<Integer> checkedField = new ArrayList<>();
            int i = 0;
            //

            Log.d("function","getAllContent" );
            Cursor cur = dbo.getAllData();
            ArrayList<NoteLists> NoteLists = new ArrayList<>();
            while (cur.moveToNext()) {
                HashMap<Integer, String> hashMap = new HashMap<>();//create a hashmap to store the data in key value pair
                hashMap.put(cur.getInt(0), cur.getString(1));
                NoteLists.add(new NoteLists(cur.getString(1), cur.getInt(0)));
                noteLists.add(hashMap);//add the hashmap into arrayList
                if( cur.getInt(3) == 1 ){
                    checkedField.add(i);
                }
                i++;
            }
//            aa = new ArrayAdapter(this, android.R.layout.select_dialog_multichoice, NoteLists);
            aa = new ArrayAdapterCustom(this, android.R.layout.select_dialog_multichoice, NoteLists,checkedField);
            lv.setAdapter(aa);

            for (int k = 0; k<checkedField.size(); k++){
                lv.setItemChecked(checkedField.get(k), true);
                bDeleteAll.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Integer> getCheckedNotesIds() {
        try {
            Log.d("function","getCheckedNotesIds" );
            ArrayList<Integer> dbIds = new ArrayList<>();
            NoteLists listItem;
            for (int i = 0; i < noteLists.size(); i++) {
                if (lv.isItemChecked(i)) {
                    listItem = (NoteLists) lv.getItemAtPosition(i);
                    dbIds.add(listItem.getId());
                }
            }
            Log.d("noteLog", "qty of checked notes: " + dbIds.size());
            return dbIds;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public ArrayList<Integer> getUnCheckedNotesIds() {
        try {
            Log.d("function","getUnCheckedNotesIds" );
            ArrayList<Integer> dbIds = new ArrayList<>();
            NoteLists listItem;
            for (int i = 0; i < noteLists.size(); i++) {
                if (!lv.isItemChecked(i)) {
                    listItem = (NoteLists) lv.getItemAtPosition(i);
                    dbIds.add(listItem.getId());
                }
            }
            Log.d("noteLog", "qty of checked notes: " + dbIds.size());
            return dbIds;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    
    public void onClickDeleteCheckedNotes(View view) {
        try {
            Log.d("function","onClickDeleteCheckedNotes" );
            ArrayList<Integer> ids;
            ids = getCheckedNotesIds();
            for (int i = 0; i < ids.size(); i++) {
                dbo.deleteNote(ids.get(i));
                Log.d("noteLog","\nremove note - id: " + ids.get(i));
            }
            bDeleteAll.setVisibility(View.INVISIBLE);
            onResume();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCreateNote(View view) {
        Intent intent = new Intent(MainActivity.this, NoteActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        Log.d("function","onStart" );
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("function","onResume");
        try {
            getAllContent();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        noteLists.clear();
        if(!(getUnCheckedNotesIds() == null)) {
            for (int i = 0; i < getUnCheckedNotesIds().size(); i++) {
                dbo.uncheckNote(getUnCheckedNotesIds().get(i));
            }
        }
        if(!(getCheckedNotesIds() == null)) {
            for (int i = 0; i < getCheckedNotesIds().size(); i++) {
                dbo.checkNote(getCheckedNotesIds().get(i));
            }
        }
        Log.d("function","onPause ");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.d("function","onRestart ");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.d("function","onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("function","onDestroy");
        super.onDestroy();
    }
}
