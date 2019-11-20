package com.example.shenliping.mytesttwo;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.*;
/**
 * Created by shenliping on 2018/11/8.
 */

public class QueryAdapter extends BaseAdapter {
    private Context mContext;
    private boolean isShow = false;
    private ArrayList<Student> studentlist;

    //private static boolean[] checks;    // 保存被选中的状态

  //  private Map<Integer, Boolean> isCheck = new HashMap<Integer, Boolean>();

    private static HashMap<Integer, Boolean> isSelected;
    private LayoutInflater inflater = null;


    /*为ListView设置一个适配器
                            * getCount()返回数据个数
                            * getView()为每一行设置一个条目
                            * */
    public QueryAdapter(Context mContext,ArrayList<Student> studentlist) {
        this.mContext = mContext;
        this.studentlist = studentlist;
        inflater = LayoutInflater.from(mContext);
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
        // 默认为不选中
     //   initCheck(false);
    }

//    // 初始化map集合
//    public void initCheck(boolean flag) {
//        // map集合的数量和list的数量是一致的
//        for (int i = 0; i < studentlist.size(); i++) {
//            // 设置默认的显示
//            isCheck.put(i, flag);
//        }
//   }

    @Override
    public int getCount() {
        return studentlist.size();
    }

    @Override
    public Object getItem(int position) {
        return studentlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    public void clear() {
//
//        notifyDataSetChanged();
//    }

    public boolean isShow() {
        return isShow;
    }
    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        /**对ListView的优化，convertView为空时，创建一个新视图；
         * convertView不为空时，代表它是滚出,
         * 放入Recycler中的视图,若需要用到其他layout，
         * 则用inflate(),同一视图，用fiindViewBy()
         * **/
        //服用convertView
        View view = null;
        ViewHolder viewHolder = null;
        // 判断是不是第一次进来
        if (convertView != null) {
            view = convertView;
            // 直接拿过来用
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(mContext, R.layout.item_database, null);
            viewHolder = new ViewHolder();
            viewHolder.CheckBox = (CheckBox) view.findViewById(R.id.checkBox);
            viewHolder.LL=(LinearLayout)view.findViewById(R.id.row1);
            // 标记，能够复用
            view.setTag(viewHolder);
        }
        if (isShow) {
            viewHolder.CheckBox.setVisibility(View.VISIBLE);
           // Log.d("MainActivity", "成功1");
        } else {
            viewHolder.CheckBox.setVisibility(View.INVISIBLE);
            //Log.d("MainActivity", "成功2");
        }
        viewHolder.CheckBox.setChecked(getIsSelected().get(position));

// 监听checkBox并根据原来的状态来设置新的状态
        viewHolder.CheckBox.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                System.out.println("点击："+position);
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                    notifyDataSetChanged();
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                    notifyDataSetChanged();
                }

            }
        });

        // 根据isSelected来设置checkbox的选中状况




        //找到控件
        TextView name = (TextView)view.findViewById(R.id.item_tv_name);
        TextView number  = (TextView)view.findViewById(R.id.item_tv_number);
        TextView sex = (TextView)view.findViewById(R.id.item_tv_sex);
        TextView hobby = (TextView)view.findViewById(R.id.item_tv_hobby);
        TextView city = (TextView)view.findViewById(R.id.item_tv_city);

        //从studentlist中取出一行数据，position相当于数组下标,可以实现逐行取数据
        Student st = studentlist.get(position);
        //设置内容
        name.setText(st.getName());
        number.setText(st.getNumber());
        sex.setText(st.getSex());
        hobby.setText(st.getHobby());
        city.setText(st.getCity());

        return view;
    }
    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < studentlist.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    // 优化
    public static class ViewHolder {
       // public TextView title;
       public LinearLayout LL;
        public CheckBox CheckBox;
    }
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        QueryAdapter.isSelected = isSelected;
    }

    public void removeData(int position) {
        studentlist.remove(position);
    }
}
