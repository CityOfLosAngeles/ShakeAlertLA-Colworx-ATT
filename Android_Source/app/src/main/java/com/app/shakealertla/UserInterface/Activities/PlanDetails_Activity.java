package com.app.shakealertla.UserInterface.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.app.shakealertla.Models.Plan;
import com.app.shakealertla.R;
import com.app.shakealertla.Services.KitService;
import com.app.shakealertla.Services.PlanService;

public class PlanDetails_Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        setupComponents();
    }

    ImageView back, submit;
    TextView title, info;
    EditText notes;
    Switch completed;
    Plan plan;
    int position;
    boolean preventAutoSave = false;

    @Override
    public void initializeComponents() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        info = findViewById(R.id.info);
        notes = findViewById(R.id.notes);
        completed = findViewById(R.id.completed);
        submit = findViewById(R.id.submit);
        if (getIntent().hasExtra("plan"))
            plan = (Plan) getIntent().getSerializableExtra("plan");

        position = getIntent().getIntExtra("position", -1);
    }

    int earlierCompleted;
    String earlierNotes = "";

    @Override
    public void setupListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                updateDBandSendResult();
                finish();
            }
        });

        title.setText(plan.Plan);
        title.setContentDescription(plan.Plan);
        info.setText(Html.fromHtml(plan.Info));
        info.setMovementMethod(LinkMovementMethod.getInstance());
        notes.setText(plan.Notes != null ? plan.Notes : "");
        completed.setChecked(plan.Completed != 0);
        earlierCompleted = plan.Completed;
        earlierNotes = plan.Notes != null ? plan.Notes : "";
        completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if (!preventAutoSave) {
                    preventAutoSave = true;
                }else */submit.performClick();
            }
        });
    }

    private void updateDBandSendResult() {
        plan.Notes = (notes.getText().toString() != null && !notes.getText().toString().matches("")) ? notes.getText().toString() : "";
        plan.Completed = completed.isChecked() ? 1 : 0;
        if (earlierCompleted != plan.Completed || !earlierNotes.matches(plan.Notes)) {
            if (position == -1)//if its a kit plan then it would never be -1
                PlanService.updatePlan(plan);
            else { //kit plan
                Intent intent = new Intent();
                intent.putExtra("position", position);
                intent.putExtra("plan", plan);
                setResult(20, intent);
                KitService.updateKitPlan(plan);
            }
//                    AppUtils.Toast("Updated");

        }

    }

    public void closeKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    //    @Override
//    protected void onDestroy() {
//        Intent intent = new Intent();
//        intent.putExtra("position", position);
//        intent.putExtra("plan", plan);
//        setResult(20, intent);
//        super.onDestroy();
//    }


    @Override
    public void onBackPressed() {
        updateDBandSendResult();
        super.onBackPressed();
    }
}
