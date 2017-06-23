package kr.spyec.spyecvote.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

import kr.spyec.spyecvote.DataManager;
import kr.spyec.spyecvote.R;
import kr.spyec.spyecvote.VoteAdapter;
import kr.spyec.spyecvote.VoteItem;
import kr.spyec.spyecvote.activity.AddVoteActivity;

/**
 * Created by Sunrin on 2017-06-23.
 */

public class VoteFragment extends Fragment {


    private RecyclerView recyclerView;
    private VoteAdapter adapter;
    private TextView receiverStatusText;
    private Button receiverSwitchBtn;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vote, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        assert fab != null;

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        adapter = new VoteAdapter(inflater.getContext());
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(inflater.getContext(), AddVoteActivity.class), 3000);
            }
        });

        Button voteResetBtn = (Button) view.findViewById(R.id.btn_vote_reset);
        assert voteResetBtn != null;
        voteResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance().resetVote();
                adapter.notifyDataSetChanged();
            }
        });

        Button allResetBtn = (Button) view.findViewById(R.id.btn_all_reset);
        assert allResetBtn != null;
        allResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance().resetAll();
                adapter.notifyDataSetChanged();
            }
        });

        receiverStatusText = (TextView) view.findViewById(R.id.text_receiver_status);
        receiverSwitchBtn = (Button) view.findViewById(R.id.btn_receiver_switch);
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

        return view;
    }


    MyReceiver myReceiver = null;
    boolean isReceiverRegistered = false;

    @Override
    public void onResume() {
        super.onResume();
        if (myReceiver == null) myReceiver = new MyReceiver();
        getActivity().registerReceiver(myReceiver, new IntentFilter("data.update"));
        isReceiverRegistered = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isReceiverRegistered) {
            getActivity().unregisterReceiver(myReceiver);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3000){
            if(resultCode == Activity.RESULT_OK){
                String mainName = data.getStringExtra("mainName");
                String[] etc = data.getStringArrayExtra("etc");
                adapter.addItem(new VoteItem(mainName, Arrays.asList(etc)));
            }
        }
    }
}
