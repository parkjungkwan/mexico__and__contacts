package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.rstone.com.contactsapp.Main.*;

import static app.rstone.com.contactsapp.Main.MEMADDR;
import static app.rstone.com.contactsapp.Main.MEMEMAIL;
import static app.rstone.com.contactsapp.Main.MEMNAME;
import static app.rstone.com.contactsapp.Main.MEMPHONE;
import static app.rstone.com.contactsapp.Main.MEMPHOTO;
import static app.rstone.com.contactsapp.Main.MEMPW;
import static app.rstone.com.contactsapp.Main.MEMSEQ;
import static app.rstone.com.contactsapp.Main.MEMTAB;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        Context _this = MemberDetail.this;
        Intent intent = this.getIntent();
        String seq = intent.getExtras().getString("seq");
        Log.d("넘어온 seq 값:::",seq);
        String seq2 = intent.getStringExtra("seq");
        Log.d("넘어온 seq2 값:::",seq2);
        ItemDetail query = new ItemDetail(_this);
        query.id = seq;
        Main.Member m = (Main.Member) new RetrieveService() {
            @Override
            public Object perform() {
                return query.execute();
            }
        }.perform();
        Log.d("검색된 이름:","");
        Log.d("검색된 이미지:",m.photo);
        ImageView profile = findViewById(R.id.profile);
        profile.setImageDrawable(
                getResources()
                .getDrawable(
                        getResources()
                        .getIdentifier(this.getPackageName()
                                        +":drawable/"+m.photo,null,
                                null
                        )
                        ,_this.getTheme()));
        TextView name = findViewById(R.id.name);
        name.setText(m.name);
        TextView phone = findViewById(R.id.phone);
        phone.setText(m.phone);
        TextView email = findViewById(R.id.email);
        email.setText(m.email);
        TextView addr = findViewById(R.id.addr);
        addr.setText(m.addr);
        findViewById(R.id.callBtn).setOnClickListener(
                (View v)->{}
        );
        findViewById(R.id.dialBtn).setOnClickListener(
                (View v)->{}
        );
        findViewById(R.id.smsBtn).setOnClickListener(
                (View v)->{}
        );
        findViewById(R.id.emailBtn).setOnClickListener(
                (View v)->{}
        );
        findViewById(R.id.albumBtn).setOnClickListener(
                (View v)->{}
        );
        findViewById(R.id.movieBtn).setOnClickListener(
                (View v)->{}
        );
        findViewById(R.id.mapBtn).setOnClickListener(
                (View v)->{}
        );
        findViewById(R.id.musicBtn).setOnClickListener(
                (View v)->{}
        );
        findViewById(R.id.updateBtn).setOnClickListener(
                (View v)->{
                    Intent moveUpdate = new Intent(_this,MemberUpdate.class);
                    moveUpdate.putExtra("spec",
                            m.seq+","+
                            m.name +","+
                            m.pw +","+
                            m.email +","+
                            m.phone +","+
                            m.addr +","+
                            m.photo );
                    startActivity(moveUpdate);
                }
        );
        findViewById(R.id.listBtn).setOnClickListener(
                (View v)->{}
        );



    }
    private class DetailQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public DetailQuery(Context _this) {
            super(_this);
            helper = new Main.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemDetail extends DetailQuery{
        String id;
        public ItemDetail(Context _this) {
            super(_this);
        }
        public Main.Member execute(){
            Main.Member m = null;
            Cursor c = this.getDatabase()
                    .rawQuery(
                        String.format(
                          " SELECT * FROM %s WHERE %s LIKE '%s'  ",
                           MEMTAB, MEMSEQ, id
                        ),null

                    );
            if(c != null){
                if(c.moveToNext()){
                    m = new Main.Member();
                    m.seq = Integer.parseInt(c.getString(c.getColumnIndex(MEMSEQ)));
                    m.name = c.getString(c.getColumnIndex(MEMNAME));
                    m.pw = c.getString(c.getColumnIndex(MEMPW));
                    m.email = c.getString(c.getColumnIndex(MEMEMAIL));
                    m.phone = c.getString(c.getColumnIndex(MEMPHONE));
                    m.addr = c.getString(c.getColumnIndex(MEMADDR));
                    m.photo = c.getString(c.getColumnIndex(MEMPHOTO));
                }
            }
            return m;
        }

    }
}
