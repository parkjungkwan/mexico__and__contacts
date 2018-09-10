package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import static  app.rstone.com.contactsapp.Main.*;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        Context _this = MemberUpdate.this;
        String[] spec = getIntent()
                .getStringExtra("spec")
                .split(",")
                ;
        /*
         m.seq+","+
          m.name +","+
        m.pw +","+
        m.email +","+
        m.phone +","+
         m.addr +","+
         m.photo );
        * */
        ImageView profile = findViewById(R.id.profile);
        profile.setImageDrawable(
                getResources()
                        .getDrawable(
                            getResources()
                            .getIdentifier(this.getPackageName()
                                            +":drawable/"+spec[6],null,
                                            null
                )
                ,_this.getTheme()));
        EditText name = findViewById(R.id.name);
        name.setText(spec[1]);
        EditText email = findViewById(R.id.changeEmail);
        email.setText(spec[3]);
        EditText phone = findViewById(R.id.changePhone);
        phone.setText(spec[4]);
        EditText addr = findViewById(R.id.changeAddress);
        addr.setText(spec[5]);
        findViewById(R.id.confirmBtn).setOnClickListener(
                (View v)->{
                    ItemUpdate query = new ItemUpdate(_this);
                    query.m.name =
                            (name.getText().toString().equals(""))?
                                    spec[1] :
                                    name.getText().toString();
                    query.m.addr =
                            (addr.getText().toString().equals(""))?
                                   spec[5] :
                                    addr.getText().toString();
                    query.m.email =
                            (email.getText().toString().equals(""))?
                                    spec[3] :
                            email.getText().toString();
                    query.m.phone =
                            (phone.getText().toString().equals(""))?
                                    spec[4] :
                            phone.getText().toString();
                    query.m.seq = Integer.parseInt(spec[0]);
                    new StatusService() {
                        @Override
                        public void perform() {
                            query.execute();
                        }
                    }.perform();
                    Intent moveToDetail = new Intent(_this,MemberDetail.class);
                    moveToDetail.putExtra("seq",spec[0]);
                    startActivity(moveToDetail);
                }
        );
        findViewById(R.id.cancelBtn).setOnClickListener(
                (View v)->{
                    Intent moveDetail = new Intent(_this,MemberDetail.class);
                    moveDetail.putExtra("seq",spec[0]);
                    startActivity(moveDetail);
                }
        );
    }
    private class UpdateQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public UpdateQuery(Context _this) {
            super(_this);
            this.helper = new Main.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemUpdate extends UpdateQuery{
        Main.Member m;
        public ItemUpdate(Context _this) {
            super(_this);
            m = new Main.Member();
        }
        public void execute(){
            getDatabase().execSQL(
                 String.format(" UPDATE %s SET " +
                         " %s = '%s' ,"+
                         " %s = '%s' ,"+
                         " %s = '%s' ,"+
                         " %s = '%s' "+
                         " WHERE %s LIKE '%s' ",
                    MEMTAB,
                         MEMNAME,m.name,
                         MEMEMAIL,m.email,
                         MEMPHONE,m.phone,
                         MEMADDR,m.addr,
                         MEMSEQ, m.seq
                 )
            );

        }
    }

}
