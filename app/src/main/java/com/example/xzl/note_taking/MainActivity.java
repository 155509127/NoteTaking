package com.example.xzl.note_taking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends Activity {
    WordsDBHelper mDbHelper;
    int updataId=-1;
    Cursor mCursor=null;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   ImageView imgviewp=(ImageView)findViewById(R.id.imageViewphoto);
       // EditText textinput = (EditText) findViewById(R.id.textinput);
        final TextView texttime = (TextView) findViewById(R.id.texttime);
        final Button btncal = (Button) findViewById(R.id.buttoncal);
        final Button btndelall = (Button) findViewById(R.id.butondelall);
        final Button btndelnot=(Button) findViewById(R.id.buttondelete);
        final Button btnnexnot=(Button) findViewById(R.id.buttonnext);
        final Button btnsave=(Button) findViewById(R.id.buttonsave);
        SimpleDateFormat datime=new android.icu.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        texttime.setText(datime.format(new Date()));
        btncal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  dispatchTakePictureIntent();
            /*    SQLiteDatabase db = mDbHelper.getReadableDatabase();
                 String SQL_CREATE_DATABASE = "CREATE TABLE " + Notes.Node.TABLE_NAME + " (" +
                        Notes.Node._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        Notes.Node.COLUMN_NAME_TIME + " TEXT" + "," +
                        Notes.Node.COLUMN_NAME_TEXT + " TEXT" +
                        //         Notes.Node.COLUMN_NAME_Picture + " BINARY"+
                        " )";
                 db.execSQL(SQL_CREATE_DATABASE);
                 */
                Cancer();
                Button btnsave=(Button) findViewById(R.id.buttonsave);
                btnsave.setText("Save");
                updataId=-1;
                TextView texttime = (TextView) findViewById(R.id.texttime);
                SimpleDateFormat datime= null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    datime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    texttime.setText(datime.format(new Date()));
                }
            }
        });
        btndelall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            DeleteAllView();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updataId<0)
                InsertNode();
                else {
                    UpdataNode(updataId);
                }
                TextView texttime = (TextView) findViewById(R.id.texttime);
                SimpleDateFormat datime= null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    datime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    texttime.setText(datime.format(new Date()));
                }
            }
        });
        btndelnot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updataId>0)
                    DeleteNode(updataId);
            }
        });
        btnnexnot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updataId>0)
                if(!mCursor.isLast())
                NextNote();
                getNodebyid(updataId);
            }
        });
        mDbHelper = new WordsDBHelper(this);
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
        int Nid=0;
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.FindByTime:
                Nid=findidbytime();
                break;
            case R.id.FindByText:
                Nid=findidbytext();
                break;
            case R.id.CreatNew:
                    DeleteAllV();
                    Cancer();
                    break;
        }
        getNodebyid(Nid);
        return super.onOptionsItemSelected(item);
    }
   /* static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView phtimgview=(ImageView)findViewById(R.id.imageViewphoto);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
           Bitmap pho=(Bitmap) extras.get("data");
            phtimgview.setImageBitmap(pho);
        }
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }
    //删除记录
    private void DeleteN(int Id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // 定义where子句
        String selection = Notes.Node._ID + " = ?";

        // 指定占位符对应的实际参数
        String[] selectionArgs = {Integer.toString(Id)};
        // Issue SQL statement.
        db.delete(Notes.Node.TABLE_NAME, selection, selectionArgs);

    }
    //删除表
    private void DeleteTable() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // 定义where子句
        db.execSQL("drop table notes");
    }
    //删除当前全部记录
    private void DeleteAllV()
    {

      //  ImageView phtimgview=(ImageView)findViewById(R.id.imageViewphoto);
        EditText textinput = (EditText) findViewById(R.id.textinput);
       // phtimgview.setImageDrawable(null);
        textinput.setText("");
        TextView texttime = (TextView) findViewById(R.id.texttime);
        SimpleDateFormat datime= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            texttime.setText(datime.format(new Date()));
        }
    }
    //插入记录
    private void InsertNode(){
            EditText textinput = (EditText) findViewById(R.id.textinput);
            TextView tvtime = (TextView) findViewById(R.id.texttime);
       //     ImageView photo=(ImageView)findViewById(R.id.imageViewphoto);
          //  ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues value=new ContentValues();
            value.put(Notes.Node.COLUMN_NAME_TIME, tvtime.getText().toString());
            value.put(Notes.Node.COLUMN_NAME_TEXT ,textinput.getText().toString());
      Log.v("TAGPHO","errorINSERT0");
            db.insert(Notes.Node.TABLE_NAME,null,value);
        Log.v("TAGPHO","errorINSERT1");
        /*
        // Ensure that there's a camera activity to handle the intent
        if(photo.getDrawable()!=null) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                if (takePictureIntent.getExtras().get("data") == null)
                    Log.v("TAGPHO", "errorINSERT2");
                else {
                    ((Bitmap) takePictureIntent.getExtras().get("data")).compress(Bitmap.CompressFormat.PNG, 100, baos);
                }

            }

                Object[] args = new Object[]{baos.toByteArray()};
                Cursor pid = db.rawQuery("select * from notes order by id desc limit 1", null);
                pid.moveToFirst();
                String upsql = "update notes set " + Notes.Node.COLUMN_NAME_Picture + "=? where id="+Integer.toString(pid.getInt(pid.getColumnIndex(Notes.Node.COLUMN_NAME_ID)));
        }
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        Button btnsave=(Button) findViewById(R.id.buttonsave);
        btnsave.setText("updata");
        String SELECTLA_SQL = "SELECT * FROM notes order by _id desc limit 1";
       Cursor Pid=db.rawQuery(SELECTLA_SQL,null);
        Pid.moveToLast();
        updataId=Pid.getInt(Pid.getColumnIndex(Notes.Node._ID));
    }
    public void UpdataNode(int Id)
    {
        EditText textinput = (EditText) findViewById(R.id.textinput);
        TextView tvtime = (TextView) findViewById(R.id.texttime);
        //     ImageView photo=(ImageView)findViewById(R.id.imageViewphoto);
     //   ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection =Notes.Node._ID+" = ?";
        // 指定占位符对应的实际参数
        String[] selectionArgs = {Integer.toString(Id)};
        ContentValues value=new ContentValues();
        value.put(Notes.Node.COLUMN_NAME_TIME, tvtime.getText().toString());
        value.put(Notes.Node.COLUMN_NAME_TEXT ,textinput.getText().toString());
        Log.v("TAGGR","updata error"+Integer.toString(Id));
        db.update(Notes.Node.TABLE_NAME,value,selection,selectionArgs);
        Log.v("TAGGR","updata error");
    }
    //查看记录
    public void getNodebyid(int Id) {
        EditText textinput=(EditText) findViewById(R.id.textinput);
      //  ImageView imgviewp=(ImageView)findViewById(R.id.imageViewphoto);
        TextView timet=(TextView) findViewById(R.id.texttime);
     //   byte[] photo = null;
        String Time;
        String Text;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String SELECT_SQL = "SELECT * FROM notes where _id = "+Integer.toString(Id);
        Cursor ICursor = db.rawQuery(SELECT_SQL, null);
        if (ICursor != null) {
            if (ICursor.moveToFirst()) {//just need to query one time
            //    photo = mCursor.getBlob(mCursor.getColumnIndex(Notes.Node.COLUMN_NAME_Picture));//取出图片
                Time=ICursor.getString(ICursor.getColumnIndex(Notes.Node.COLUMN_NAME_TIME));
                Text=ICursor.getString(ICursor.getColumnIndex(Notes.Node.COLUMN_NAME_TEXT));
                textinput.setText(Text);
                timet.setText(Time);
            }
        }
        if(ICursor != null)
            ICursor.close();
        db.close();
        Button btnsave=(Button) findViewById(R.id.buttonsave);
        btnsave.setText("updata");
        updataId=Id;

  /*      ByteArrayInputStream bais = null;
        if (photo != null) {
            bais = new ByteArrayInputStream(photo);
            Drawable pho=(Drawable.createFromStream(bais, "photo"));
            //pho.setBounds(10,10,100,100);
            imgviewp.setImageDrawable(pho);//把图片设置到ImageView对象中
        }
        //appIcon显示的就是之前保存到数据库中的图片*/
    }
    public int findidbytime(){
        int Noteid=0;
        EditText time=(EditText)findViewById(R.id.Input);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String SELECT_SQL = "SELECT * FROM notes where time like ? order by _id desc";
         mCursor=db.rawQuery(SELECT_SQL,new String[]{time.getText().toString()+"%"});
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {//just need to query one time
                //    photo = mCursor.getBlob(mCursor.getColumnIndex(Notes.Node.COLUMN_NAME_Picture));//取出图片
                Noteid=mCursor.getInt(mCursor.getColumnIndex(Notes.Node._ID));
            }
        }
        db.close();
        return Noteid;
    }
    public int findidbytext(){
        int Noteid=0;
        EditText text=(EditText)findViewById(R.id.Input);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String SELECT_SQL = "SELECT * FROM notes where text like ? order by _id desc";
        mCursor=db.rawQuery(SELECT_SQL,new String[]{"%"+text.getText().toString()+"%"});
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {//just need to query one time
                //    photo = mCursor.getBlob(mCursor.getColumnIndex(Notes.Node.COLUMN_NAME_Picture));//取出图片
                Noteid=mCursor.getInt(mCursor.getColumnIndex(Notes.Node._ID));
            }
        }
        db.close();
        return Noteid;
    }
    private void DeleteNode(final int strId){
        new AlertDialog.Builder(this).setTitle("删除当前记录").setMessage("是否真的删除记录?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteN(strId);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
    private void Cancer(){
        new AlertDialog.Builder(this).setTitle("结束编辑").setMessage("是否保存?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(updataId<0)
                    InsertNode();
                else {
                    UpdataNode(updataId);
                }
                Button btnsave=(Button) findViewById(R.id.buttonsave);
                btnsave.setText("Save");
                updataId=-1;
                TextView texttime = (TextView) findViewById(R.id.texttime);
                SimpleDateFormat datime= null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    datime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    texttime.setText(datime.format(new Date()));
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Button btnsave=(Button) findViewById(R.id.buttonsave);
                btnsave.setText("Save");
                updataId=-1;
                TextView texttime = (TextView) findViewById(R.id.texttime);
                SimpleDateFormat datime= null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    datime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    texttime.setText(datime.format(new Date()));
                }
            }
        }).create().show();
    }
    private void DeleteAllView(){
        new AlertDialog.Builder(this).setTitle("删除全部").setMessage("是否真的删除全部?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteAllV();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
    private void NextNote()
    {
        mCursor.moveToNext();
        updataId=mCursor.getInt(mCursor.getColumnIndex(Notes.Node._ID));
    }
}
