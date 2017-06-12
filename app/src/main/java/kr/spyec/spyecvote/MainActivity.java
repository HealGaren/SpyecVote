package kr.spyec.spyecvote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VoteAdapter adapter;
    private TextView receiverStatusText;
    private Button receiverSwitchBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VoteAdapter(this);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddVoteActivity.class), 3000);
            }
        });

        Button voteResetBtn = (Button) findViewById(R.id.btn_vote_reset);
        assert voteResetBtn != null;
        voteResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance().resetVote();
                adapter.notifyDataSetChanged();
            }
        });

        Button allResetBtn = (Button) findViewById(R.id.btn_all_reset);
        assert allResetBtn != null;
        allResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance().resetAll();
                adapter.notifyDataSetChanged();
            }
        });

        Button resultBtn = (Button) findViewById(R.id.btn_result);
        assert resultBtn != null;
        resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResultActivity.class));
            }
        });



        receiverStatusText = (TextView) findViewById(R.id.text_receiver_status);
        receiverSwitchBtn = (Button) findViewById(R.id.btn_receiver_switch);
        assert receiverStatusText != null;
        assert receiverSwitchBtn != null;



        receiverSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance().toggleVoteReceiver();
                updateActiveSwitch();
            }
        });

        updateActiveSwitch();


    }


    MyReceiver myReceiver = null;
    boolean isReceiverRegistered = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (myReceiver == null) myReceiver = new MyReceiver();
        registerReceiver(myReceiver, new IntentFilter("data.update"));
        isReceiverRegistered = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isReceiverRegistered) {
            unregisterReceiver(myReceiver);
            myReceiver = null;
            isReceiverRegistered = false;
        }

    }

    Handler handler = new Handler();

    private class MyReceiver extends BroadcastReceiver {

        private static final String ACTION = "data.update";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION.equals(intent.getAction())) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    private void updateActiveSwitch(){
        if(DataManager.getInstance().isReceiverActive()) {
            receiverStatusText.setText("현재 가동 중");
            receiverSwitchBtn.setText("끄기");
        }
        else {
            receiverStatusText.setText("현재 가동 안 함");
            receiverSwitchBtn.setText("켜기");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3000){
            if(resultCode == RESULT_OK){
                String mainName = data.getStringExtra("mainName");
                String[] etc = data.getStringArrayExtra("etc");
                adapter.addItem(new VoteItem(mainName, Arrays.asList(etc)));
            }
        }
    }
}