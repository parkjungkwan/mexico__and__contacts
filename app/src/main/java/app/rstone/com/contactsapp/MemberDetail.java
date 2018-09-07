package app.rstone.com.contactsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        Intent intent = this.getIntent();
        String seq = intent.getExtras().getString("seq");
        Log.d("넘어온 seq 값:::",seq);
        String seq2 = intent.getStringExtra("seq");
        Log.d("넘어온 seq2 값:::",seq2);
    }
}
