package kr.spyec.spyecvote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 최예찬 on 2016-08-22.
 */
public class AddVoteActivity extends AppCompatActivity {

    EditText mainNameEdit;
    List<EditText> etcEdits = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vote);

        mainNameEdit = (EditText) findViewById(R.id.edit_mainName);
        etcEdits.add((EditText) findViewById(R.id.edit_etc_1));
        etcEdits.add((EditText) findViewById(R.id.edit_etc_2));
        etcEdits.add((EditText) findViewById(R.id.edit_etc_3));

        Button doneBtn = (Button) findViewById(R.id.btn_done);
        assert doneBtn != null;
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("mainName", mainNameEdit.getText().toString());
                resultIntent.putExtra("etc", new String[]{
                        etcEdits.get(0).getText().toString(),
                        etcEdits.get(1).getText().toString(),
                        etcEdits.get(2).getText().toString()
                });
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

}
