package com.example.shenliping.mytesttwo;

import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.*;
import java.io.*;
import java.util.*;
import java.util.zip.Checksum;


public class MainActivity extends AppCompatActivity {

    private int i;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText edit1, edit2, showF,count,row_name1, row_number1, row_sex1, row_hobby1;
    private Button btnW, btnR,btnF,btnD;
    private Button add, select, delete, update1, select1, select2,edit,del_all;
    private RadioButton man, woman;
    private CheckBox c1, c2, c3,checkBox;
    private MyDatabaseHelper dbHeler;
    private TableLayout tablelayout;
    private LinearLayout li4,li5;
    private RelativeLayout re5;
    private RadioGroup sexa;
    private Spinner city;
    private List<CheckBox> checkBoxList = new ArrayList<>();
    private ListView lists;
    private int checkNum; // 记录选中的条目数量

    private ArrayList<Student> studentlist;
    private ArrayList<Student> studentlistOne;

    private QueryAdapter queryadApter;
    ArrayList<HashMap<String, Object>> listData; //  key-value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit1 = (EditText) findViewById(R.id.edit1);
        edit2 = (EditText) findViewById(R.id.edit2);
        showF = (EditText) findViewById(R.id.showF);
        count = (EditText) findViewById(R.id.count);


        sexa = (RadioGroup) findViewById(R.id.sexa);
        woman = (RadioButton) findViewById(R.id.woman);
        man = (RadioButton) findViewById(R.id.man);

        c1 = (CheckBox) findViewById(R.id.c1);
        c2 = (CheckBox) findViewById(R.id.c2);
        c3 = (CheckBox) findViewById(R.id.c3);

        checkBox = (CheckBox) findViewById(R.id.checkBox);

       checkBoxList.add(c1);
        checkBoxList.add(c2);
        checkBoxList.add(c3);

        city = (Spinner) findViewById(R.id.city);
        //下拉列表的处理
        city.setOnItemSelectedListener(new OnItemSelectedListenerImpl());

        btnW = (Button) findViewById(R.id.write1);
        btnR = (Button) findViewById(R.id.read1);
        btnF = (Button) findViewById(R.id.file);
        btnD = (Button) findViewById(R.id.database);

        edit=(Button) findViewById(R.id.edit);//编辑
        del_all=(Button) findViewById(R.id.del_all);//全选

        li4 = (LinearLayout) findViewById(R.id.LinearLayout4);
        li5 = (LinearLayout) findViewById(R.id.LinearLayout5);
        re5 = (RelativeLayout) findViewById(R.id.RelativeLayout5);

        lists=(ListView)findViewById(R.id.listview);
        //长按删除
        lists.setOnCreateContextMenuListener(listviewLongPress);
        lists.setAdapter(queryadApter);


        add = (Button) findViewById(R.id.add);
        select = (Button) findViewById(R.id.select);
        delete = (Button) findViewById(R.id.delete);
        update1 = (Button) findViewById(R.id.update);
        //函数查询
        select1 = (Button) findViewById(R.id.select1);
        //条件查询
        select2 = (Button) findViewById(R.id.select2);

        //创建数据库Student5874
      dbHeler = new MyDatabaseHelper(this, "Student5874.db", null, 4);
        //dbHeler = new MyDatabaseHelper(this, "User.db", null, 2);
        // dbHeler.getWritableDatabase();

        tablelayout = (TableLayout) findViewById(R.id.tableLayout);
        //row_name1,row_number1,row_sex1,row_course1
      /*  row_name1 = (EditText) findViewById(R.id.row_name1);
        row_number1 = (EditText) findViewById(R.id.row_number1);
        row_sex1 = (EditText) findViewById(R.id.row_sex1);
        row_hobby1 = (EditText) findViewById(R.id.row_hobby1);*/

        final String name = edit1.getText().toString();
        final String number = edit2.getText().toString();


        final String cb1 = c1.getText().toString();
        final String cb2 = c2.getText().toString();
        final String cb3 = c3.getText().toString();

        // 存储数据的数组列表
        //ArrayList<HashMap<String, Object>> listData; //  key-value

        studentlist = new ArrayList<Student>();
        studentlistOne = new ArrayList<Student>();

        queryadApter = new QueryAdapter( MainActivity.this,studentlist);

        final String manV = man.getText().toString();
        final String womanV = woman.getText().toString();

        //点击文件操作时，关于文件的操作布局4和5显示出来
        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re5.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                if (li4.getVisibility() == View.GONE) {
                    li4.setVisibility(View.VISIBLE);
                }
                if (li5.getVisibility() == View.GONE) {
                    li5.setVisibility(View.VISIBLE);
                }
            }
        });
        //点击数据库操作时，关于文件的隐藏起来数据库的操作显示出来
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (re5.getVisibility() == View.GONE) {
                    li4.setVisibility(View.GONE);
                    li5.setVisibility(View.GONE);
                    re5.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                }


            }
        });

        //写入文件
        btnW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit1.getText().toString();
                String password = edit2.getText().toString();
                final  String city1=city.getSelectedItem().toString();
                save(name);
                save(password);

                //对单选按钮的处理
                for (int i = 0; i < sexa.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) sexa.getChildAt(i);
                    if (radioButton.isChecked()) {
                        save(radioButton.getText().toString());
                    }}
                //对复选框处理
                StringBuffer sb = new StringBuffer();
                for (CheckBox checkbox : checkBoxList) {
                    if (checkbox.isChecked()) {
                        save(checkbox.getText().toString());
                    }
                }
                save(city1);
            }
        });

        //读文件
        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showing();
            }
        });

        //点击编辑按钮
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isShow = queryadApter.isShow();

                if (edit.getText().equals("编辑")) {
                    if (del_all.getVisibility() == View.GONE) {
                        del_all.setVisibility(View.VISIBLE);
                    }
                    edit.setText("取消");
                    queryadApter.setShow(!isShow);

                    queryadApter.notifyDataSetChanged();

                }
                else if(edit.getText().equals("取消")) {
                    if (del_all.getVisibility() == View.VISIBLE) {
                        del_all.setVisibility(View.GONE);
                    }
                    edit.setText("编辑");
                    queryadApter.setShow(isShow);

                    queryadApter.notifyDataSetChanged();
                   // Log.d("MainActivity", "成功");
                }
              //  queryadApter.notifyDataSetChanged();
            }
        });
            //全选按钮
        del_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Map<Integer, Boolean> isCheck = queryadApter.getMap();
                if (del_all.getText().equals("全选")) {
                    del_all.setText("全不选");
                    del_all.setTextColor(Color.YELLOW);
                    // 遍历list的长度，将MyAdapter中的map值全部设为true
                    for (int i=0; i<studentlist.size(); i++) {
                        QueryAdapter.getIsSelected().put(i, true);

                        studentlist.get(i).setChecked(true);
                    //   Log.d(  "MainActivity.this",String.valueOf(i));
                    }
                    // 数量设为list的长度
                    checkNum = studentlist.size();
                    // 刷新listview显示
                    queryadApter.notifyDataSetChanged();

                }else if (del_all.getText().equals("全不选")) {
                    // 遍历list的长度，将已选的按钮设为未选
                    for (int i = 0; i <studentlist.size(); i++) {
                        if (QueryAdapter.getIsSelected().get(i)) {
                            QueryAdapter.getIsSelected().put(i, false);
                          //  studentlist.get(i).setChecked(false);
                            checkNum--;// 数量减1
                        }
                    }
                    // 通知刷新适配器
                    queryadApter.notifyDataSetChanged();
                    del_all.setText("全选");
                    del_all.setTextColor(Color.YELLOW);
                }
        }});

        lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
//                QueryAdapter.ViewHolder holder = (QueryAdapter.ViewHolder) view.getTag();
//                // 改变CheckBox的状态
//                holder.CheckBox.toggle();
//                // 将CheckBox的选中状况记录下来
//                QueryAdapter.getIsSelected().put(position, holder.CheckBox.isChecked()); // 同时修改map的值保存状态
//
//                //Log.d(  "MainActivity.this","position"+String.valueOf(position));
//                // 调整选定条目
//                if (holder.CheckBox.isChecked() == true) {
//                    checkNum++;
//                    Log.d(  "MainActivity.this","position"+String.valueOf(position));
//                } else {
//                    checkNum--;
//                }
            }
        });


        //添加数据
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHeler.getWritableDatabase();
                ContentValues values = new ContentValues();
                final String name = edit1.getText().toString();
                final String number = edit2.getText().toString();
               final  String city1=city.getSelectedItem().toString();
                values.put("name", name);
                values.put("number", number);
                values.put("city", city1);
                if (man.isChecked()) {
                    values.put("sex", manV);
                } else {
                    values.put("sex", womanV);
                }
                StringBuffer sb = new StringBuffer();
               // String aa=null;

                for (CheckBox checkbox : checkBoxList) {
                    if (checkbox.isChecked()) {
                        sb.append(checkbox.getText().toString() + ",");
                    }
                }
                values.put("hobby", sb.toString());

                db.insert("student", null, values);
                Log.d("MainActivity", sb.toString()+"添加成功");
                //   db.execSQL("insert into student(name,password,sex,hobby)values(?,?,?,?)",new String[]{name,password,sex,hobby});
            }
        });

        //更新
        update1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHeler.getWritableDatabase();
                ContentValues values = new ContentValues();
                final String name = edit1.getText().toString();
                final String number = edit2.getText().toString();
                final  String city1=city.getSelectedItem().toString();
                values.put("name", name);
                values.put("city", city1);
                //  values.put("password",password);
                if (man.isChecked()) {
                    values.put("sex", manV);
                } else {
                    values.put("sex", womanV);
                }
                StringBuffer sb = new StringBuffer();
                // String aa=null;
                for (CheckBox checkbox : checkBoxList) {
                    if (checkbox.isChecked()) {
                        sb.append(checkbox.getText().toString() + ",");
                    }
                }
                values.put("hobby", sb.toString());
                db.update("student", values, "number=?", new String[]{number});
                Log.d("MainActivity", "修改成功");
            }
        });

        //删除
        delete.setOnClickListener(new View.OnClickListener() {
            SQLiteDatabase db = dbHeler.getWritableDatabase();
            @Override
            public void onClick(View v) {
            //  int j=  studentlist.size();
                //把选中的条目要删除的条目放在deleSelect这个集合中
//                for(int j=0;j<checkNum;j++){
//              for (int i = 0; i < studentlist.size(); i++) {
//                    if (QueryAdapter.getIsSelected().get(i)) {
//                      db.delete("student", "number=?", new String[]{studentlist.get(i).getNumber()});
//                        queryadApter.removeData(i);
//
//               }}
//                    db.delete("student", "number=?", new String[]{studentlist.get(i).getNumber()});
//                    queryadApter.removeData(i);
//                }

                  // 获取到条目数量，map.size = list.size,所以
                int count = queryadApter.getCount();
                        // 遍历
                for (int i = 0; i < count; i++) {
                       // 删除有两个map和list都要删除 ,计算方式
                    int position = i - (count - queryadApter.getCount());
                         // 判断状态 true为删除
                    if (QueryAdapter.getIsSelected().get(i)!= null && QueryAdapter.getIsSelected().get(i)) {
                        // listview删除数据
                        db.delete("student", "number=?", new String[]{studentlist.get(position).getNumber()});
                        queryadApter.removeData(position);
                    }}
                queryadApter.notifyDataSetChanged();
                   edit.setText("编辑");
                   del_all.setText("全选");

                Log.d("MainActivity", "删除成功");
            }
        });

        //查询全部
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHeler.getWritableDatabase();
                Cursor cursor = db.query("student",null, null, null, null, null, null);
                //清空list
                studentlist.clear();
                //studentlist = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String number = cursor.getString(cursor.getColumnIndex("number"));
                        String sex = cursor.getString(cursor.getColumnIndex("sex"));
                        String hobby = cursor.getString(cursor.getColumnIndex("hobby"));
                        String city = cursor.getString(cursor.getColumnIndex("city"));

                        Student st = new Student(name,number,sex,hobby,city);
                     /*   Log.d("MainActivity", "student name is " + name);
                        Log.d("MainActivity", "student number is " + number);
                        Log.d("MainActivity", "student sex is " + sex);
                        Log.d("MainActivity", "student hobby is " + hobby);*/
                      Log.d("MainActivity", "查询成功");
                            studentlist.add(st);
                    } while (cursor.moveToNext());


//                    QueryAdapter emptyAdapter = new QueryAdapter(MainActivity.this, new ArrayList<Student>());
//                    lists.setAdapter(emptyAdapter);
                   /* QueryAdapter*/ queryadApter = new QueryAdapter(MainActivity.this,studentlist);
                    lists.setAdapter(queryadApter);


                    //lists.setAdapter(null);
                    //lists.clear();

//                    lists.setAdapter(new BaseAdapter() {
//                        /*
//                         * 为ListView设置一个适配器
//                         * getCount()返回数据个数
//                         * getView()为每一行设置一个条目
//                         * */
//                        @Override
//                        public int getCount() {
//                            return studentlist.size();
//                        }
//
//                        @Override
//                        public Object getItem(int position) {
//                            // return studentlist.get(position);
//                            return null;
//                        }
//
//                        @Override
//                        public long getItemId(int position) {
//                            // return position;
//                            return 0;
//                        }
//
////                        public void clear() {
////                            studentlist.clear();
////                            notifyDataSetChanged();
////                        }
//                        @Override
//                        public View getView(int position, View convertView, ViewGroup parent) {
//                            View view ;
//                            /**对ListView的优化，convertView为空时，创建一个新视图；
//                             * convertView不为空时，代表它是滚出,
//                             * 放入Recycler中的视图,若需要用到其他layout，
//                             * 则用inflate(),同一视图，用fiindViewBy()
//                             * **/
//                            if(convertView == null )
//                            {
//                                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
//                                view = inflater.inflate(R.layout.item_database,null);
//                                //view = View.inflate(getBaseContext(),R.layout.item,null);
//                            }
//                            else
//                            {
//                                view = convertView;
//                            }
//                            //从studentlist中取出一行数据，position相当于数组下标,可以实现逐行取数据
//                            Student st = studentlist.get(position);
//
//                            TextView name = (TextView)view.findViewById(R.id.item_tv_name);
//                            TextView number  = (TextView)view.findViewById(R.id.item_tv_number);
//                            TextView sex = (TextView)view.findViewById(R.id.item_tv_sex);
//                            TextView hobby = (TextView)view.findViewById(R.id.item_tv_hobby);
//                            TextView city = (TextView)view.findViewById(R.id.item_tv_city);
//
//                            name.setText(st.getName());
//                            number.setText(st.getNumber());
//                            sex.setText(st.getSex());
//                            hobby.setText(st.getHobby());
//                            city.setText(st.getCity());
//
//                            return view;
//                        }
//
//                    });

                }
                cursor.close();
            }
        });

        //函数查询
        select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count.setText(String.valueOf(getCount()));
            }
        });

        //条件查询
        select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHeler.getWritableDatabase();
                Cursor cursor = db.query("student",null, "sex='男'", null, null, null, null);
                //清空list
                studentlist.clear();
                studentlistOne.clear();
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String number = cursor.getString(cursor.getColumnIndex("number"));
                        String sex = cursor.getString(cursor.getColumnIndex("sex"));
                        String hobby = cursor.getString(cursor.getColumnIndex("hobby"));
                        String city = cursor.getString(cursor.getColumnIndex("city"));

                        Student st = new Student(name,number,sex,hobby,city);

                        Log.d("MainActivity", "student name is " + name);
                      /*  Log.d("MainActivity", "student number is " + number);
                        Log.d("MainActivity", "student sex is " + sex);
                        Log.d("MainActivity", "student hobby is " + hobby);*/

                        studentlistOne.add(st);
                        Log.d("MainActivity", "条件查询成功");

                    } while (cursor.moveToNext());
                  //  lists.removeAllViews();停止运行
//                    lists.setAdapter(null);
                     /* QueryAdapter*/ queryadApter = new QueryAdapter(MainActivity.this,studentlistOne);
                    lists.setAdapter(queryadApter);

                }
                cursor.close();
            }
        });



    }


    //文件写入
    public void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try{
            out = openFileOutput("test5874", Context.MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
            Toast.makeText(this,"写入文件!",Toast.LENGTH_LONG).show();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //读文件
public void showing(){
    String inputText=load();
    if(!TextUtils.isEmpty(inputText)){
        showF.setText(inputText);
        showF.setSelection(inputText.length());
    }
}
 public String load(){
        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuffer content=new StringBuffer();
        try{
            in=openFileInput("test5874");
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while((line=reader.readLine())!=null){
                content.append(line);
            }
            Toast.makeText(this,"读取文件成功!",Toast.LENGTH_LONG).show();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    public int getCount() {
        SQLiteDatabase db = dbHeler.getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from student ",null);
        c.moveToFirst();
        return c.getInt(0);

    }

    //下拉框选择事件
    public class OnItemSelectedListenerImpl implements  AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
                String city = parent.getItemAtPosition(position).toString();
       //    Toast.makeText(MainActivity.this, "选择的城市是：" + city, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    }
       //长按listview删除item
        View.OnCreateContextMenuListener listviewLongPress = new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                //lists.setOnClickListener(new AdapterView.OnClickListener(){
                new AlertDialog.Builder(MainActivity.this)
                        // 弹出窗口的最上头文字
                        .setTitle("删除当前数据")
                        //设置弹出窗口的图式
                        .setIcon(android.R.drawable.ic_dialog_info)
                        // 设置弹出窗口的信息
                        .setMessage("确定删除当前记录")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        // 获取位置索引
                                        int mListPos = info.position;
                                        // 将listview中所有的数据都传入hashmap-listData中
                                        Cursor c = dbHeler.getReadableDatabase().rawQuery("select * from student", null);
                                        int columnsSize = c.getColumnCount();
                                        listData = new ArrayList<HashMap<String, Object>>();
                                        while (c.moveToNext()) {
                                            HashMap<String, Object> map = new HashMap<String, Object>();
                                            for (int j = 0; j < columnsSize; j++) {
                                                map.put("id", c.getString(0));
                                                map.put("name", c.getString(1));
                                                map.put("number", c.getString(2));
                                                map.put("sex", c.getString(3));
                                                map.put("hobby", c.getString(4));
                                                map.put("city", c.getString(5));
                                            }
                                            listData.add(map);
                                        }
                                        HashMap<String, Object> map = listData.get(mListPos);
                                        // 获取id
                                        int id = Integer.valueOf((map.get("id").toString()));
                                        queryadApter.removeData(mListPos);
                                        deleteNews(id);
                                        queryadApter.notifyDataSetChanged();
                                        // 移除数据
                                    }
                                }
                        )
                        .setNegativeButton("否",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                // 什么也没做
                            }
                        }).show();
            }
        };
        private boolean deleteNews(int id) {
            String whereClause = "id=?";
            String[] whereArgs = new String[]{String.valueOf(id)};
            try {
                dbHeler.getReadableDatabase().delete("student", whereClause, whereArgs);
//               Cursor cursor = dbHeler.getWritableDatabase().rawQuery("select * from student", null);
//               inflateListView(cursor);
                Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "删除数据库失败", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        /*
      * 刷新数据库列表显示
      * 1. 关联SimpleCursorAdapter与数据库表, 获取数据库表中的最新数据
      * 2. 将最新的SimpleCursorAdapter设置给ListView
      */
//        private void inflateListView(Cursor cursor) {
//            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.item_database, cursor,
//                    new String[]{"name", "number", "sex", "hobby", "city"}, new int[]{R.id.item_tv_name, R.id.item_tv_number, R.id.item_tv_sex, R.id.item_tv_hobby, R.id.item_tv_city}, 1);
//            lists.setAdapter(cursorAdapter);
//}
}
