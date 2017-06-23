package kr.spyec.spyecvote.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import kr.spyec.spyecvote.fragment.LoadingFragment;
import kr.spyec.spyecvote.R;
import kr.spyec.spyecvote.fragment.ResultFragment;
import kr.spyec.spyecvote.fragment.VoteFragment;

//최예찬, 메인액티비티(투표현황), 6월5일

public class MainActivity extends AppCompatActivity {


    private int currentTabId = R.id.tab_vote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new VoteFragment()).commit();


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if(currentTabId == tabId) return;

                Fragment fragment = null;
                switch(tabId){
                    case R.id.tab_vote:
                        fragment = new VoteFragment();
                        break;
                    case R.id.tab_result:
                        fragment = new ResultFragment();
                        break;
                    case R.id.tab_friends:
                        Toast.makeText(MainActivity.this, "구현되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        fragment = new LoadingFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container_main, fragment).commit();
                currentTabId = tabId;
            }
        });

    }
}