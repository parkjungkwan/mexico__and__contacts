package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.*;
import static app.rstone.com.contactsapp.Main.*;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        Context _this = MemberList.this;
        ItemList query = new ItemList(_this);
        ListView memberList = findViewById(R.id.memberList);
        memberList.setAdapter(
                new MemberAdapter(_this,
                        (ArrayList<Main.Member>)new ListService(){

            @Override
            public List<?> perform() {
                return query.execute();
            }
        }.perform()));
        memberList.setOnItemClickListener(
            (AdapterView<?> p, View v, int i, long l)->{
                Intent intent = new Intent(_this,MemberDetail.class);
                Main.Member m = (Main.Member)memberList.getItemAtPosition(i);
                Log.d("선택한 SEQ :::",m.seq+"");
                intent.putExtra("seq",m.seq+"");
                startActivity(intent);
            }
        );

    }
    private class MemberListQuery extends Main.QueryFactory {
        SQLiteOpenHelper helper;
        public MemberListQuery(Context _this) {
            super(_this);
            helper = new Main.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemList extends MemberListQuery {
        String id,pw;
        public ItemList(Context _this) {
            super(_this);
        }

        public ArrayList<Main.Member> execute(){
            ArrayList<Main.Member> list = new ArrayList<>();
            Cursor c = this.getDatabase().rawQuery(
                    " SELECT * FROM MEMBER ",
                        null
            );
            Main.Member m = null;
            if(c !=null){
                while(c.moveToNext()){
                    m = new Main.Member();
                    m.seq = Integer.parseInt(c.getString(c.getColumnIndex(MEMSEQ)));
                    m.name = c.getString(c.getColumnIndex(MEMNAME));
                    m.pw = c.getString(c.getColumnIndex(MEMPW));
                    m.email = c.getString(c.getColumnIndex(MEMEMAIL));
                    m.phone = c.getString(c.getColumnIndex(MEMPHONE));
                    m.addr = c.getString(c.getColumnIndex(MEMADDR));
                    m.photo = c.getString(c.getColumnIndex(MEMPHOTO));
                    list.add(m);
                }
                Log.d("등록된 회원수가",list.size()+"");
            }else{
                Log.d("등록된 회원이","없습니다.");
            }
            return list;
       /*     return super
                    .getDatabase()
                    .rawQuery(String.format(
                            " SELECT * FROM %s " +
                                    " WHERE %s LIKE '%s' AND %s LIKE '%s' ",
                            MEMTAB,
                            MEMSEQ,
                            id,
                            MEMPW,
                            pw
                    ),null)
                    .moveToNext()
                    ;*/
        }
    }
    private class MemberAdapter extends BaseAdapter{
        ArrayList<Main.Member> list;
        LayoutInflater inflater;

        public MemberAdapter(Context _this,ArrayList<Main.Member> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(_this);
        }
        private int[] photos = {
                R.drawable.profile_1,
                R.drawable.profile_2,
                R.drawable.profile_3,
                R.drawable.profile_4,
                R.drawable.profile_5
        };

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v==null){
                v = inflater.inflate(R.layout.member_item,null);
                holder = new ViewHolder();
                holder.profile = v.findViewById(R.id.profile);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHolder)v.getTag();
            }
            holder.profile.setImageResource(photos[i]);
            holder.name.setText(list.get(i).name);
            holder.phone.setText(list.get(i).phone);
            return v;
        }
    }
    static class ViewHolder{
        ImageView profile;
        TextView name,phone;
    }
}
