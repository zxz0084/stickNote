package com.swufestu.sticknote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class QueryActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView byId = (RecyclerView)findViewById(R.id.Rece);
        byId.setHasFixedSize(true);
        byId.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

    }
}

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.BaseViewHolder> {
    private ArrayList<String> dataList = new ArrayList<>();
    public void replaceAll(ArrayList<String> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }
    @Override
    public DemoAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.one, parent, false));
    }
    @Override
    public void onBindViewHolder(DemoAdapter.BaseViewHolder holder, int position) {
        holder.setData(dataList.get(position));
    }
    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }
    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
        void setData(Object data) {
        }
    }
    private class OneViewHolder extends BaseViewHolder {
        private ImageView ivImage;
        public OneViewHolder(View view) {
            super(view);
            ivImage = (ImageView) view.findViewById(R.id.ivImage);
            int width = ((Activity) ivImage.getContext()).getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = ivImage.getLayoutParams();
            //设置图片的相对于屏幕的宽高比
            params.width = width/3;
            params.height =  (int) (200 + Math.random() * 400) ;
            ivImage.setLayoutParams(params);
        }
        @Override
        void setData(Object data) {
            if (data != null) {
                String text = (String) data;
                Glide.with(itemView.getContext()).load(text).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_launcher).crossFade().into(ivImage);
            }
        }
    }

    class NormalAdapter extends BaseAdapter {
        LayoutInflater inflater;

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return NoteAPP.LISTADAPTER.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ListHolder viewHolder = null;
            // 分组模式
            if (inflater == null) {
                inflater = (LayoutInflater) queryActivity
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
            }
            viewHolder = new ListHolder();
            // 上一个时间点
            // 与上一个时间比较
            String time = NoteAPP.HEAD.get(position);
            String create = (NoteAPP.LISTADAPTER.get(position)
                    .get("createDate"));
            if (!time.equals(create)) {
                convertView = inflater.inflate(R.layout.listview, null);
                viewHolder.id = (TextView) convertView
                        .findViewById(R.id.id);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.title);
                viewHolder.createDate = (TextView) convertView
                        .findViewById(R.id.createDate);
            } else {
                convertView = inflater.inflate(R.layout.showhint, null);
                viewHolder.listHead = (TextView) convertView
                        .findViewById(R.id.listHead);
            }
            if (viewHolder.id != null) {
                viewHolder.id.setText(NoteAPP.LISTADAPTER.get(position).get(
                        "id"));
                viewHolder.title.setText(NoteAPP.LISTADAPTER.get(position).get(
                        "title"));
                viewHolder.createDate.setText(NoteAPP.LISTADAPTER.get(position)
                        .get("createDate"));
            } else {
                viewHolder.listHead.setText(NoteAPP.LISTADAPTER.get(position)
                        .get("createDate"));
            }
            return convertView;
        }
    }

}

