package com.example.digi;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String txtSms, noTeam, noSiteCode, smsCreated, noSMS, okSms;

    Button clTeamCode, clSiteCode, clTroubleFounded, clTroubleFix, clTroubleNeedFix, generateSms, smsCopy;
    ImageButton sendSMSWhatsapp;
    RadioGroup tasks, status;
    RadioButton siteDown, integration, warehouse, noGw, travelling, start, blocked, onGoing, finish;
    LinearLayout ongoingComment, layoutFinish;
    EditText teamCodeBox, siteCodeBox, ongoingTextComment,troubleFoundedBox, troubleFixBox, troubleNeedToFixBox, smsTextBox;

    boolean isSmsCreated = false, warehouseSelected, onGoingSelected;
    int shortDuration, longDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamCodeBox = findViewById(R.id.text_box_team_code_main);
        clTeamCode = findViewById(R.id.btn_team_code_clear_main);
        siteCodeBox = findViewById(R.id.text_box_site_code_main);
        clSiteCode = findViewById(R.id.btn_site_code_clear_main);
        tasks = findViewById(R.id.radio_group_task);
        status = findViewById(R.id.radio_group_status);

        siteDown = findViewById(R.id.radio_btn_site_down);
        integration = findViewById(R.id.radio_btn_integration);
        warehouse = findViewById(R.id.radio_btn_warehouse);
        noGw = findViewById(R.id.radio_btn_non_gw_gw);

        travelling = findViewById(R.id.radio_btn_travelling);
        start = findViewById(R.id.radio_btn_start);
        blocked = findViewById(R.id.radio_btn_blocked);
        onGoing = findViewById(R.id.radio_btn_ongoing);
        finish = findViewById(R.id.radio_btn_finish);

        ongoingComment = findViewById(R.id.ongoing_comment);
        ongoingTextComment = findViewById(R.id.edit_text_ongoing_comment);
        layoutFinish= findViewById(R.id.layout_finish);
        troubleFoundedBox = findViewById(R.id.text_box_trouble_founded_main);
        troubleFixBox = findViewById(R.id.text_box_trouble_fixed);
        troubleNeedToFixBox = findViewById(R.id.text_box_trouble_need_fixed);
        clTroubleFounded = findViewById(R.id.btn_trouble_founded_clear_main);
        clTroubleFix = findViewById(R.id.btn_trouble_fixed_clear_main);
        clTroubleNeedFix = findViewById(R.id.btn_trouble_need_fixed_clear_main);
        generateSms = findViewById(R.id.btn_generate_message);
        smsTextBox = findViewById(R.id.sms_text_box);
        sendSMSWhatsapp = findViewById(R.id.btn_whatsapp_send);
        smsCopy = findViewById(R.id.btn_sms_copy);

        noTeam = getString(R.string.no_team_code_found);
        noSiteCode = getString(R.string.no_site_code_found);
        smsCreated = getString(R.string.sms_created);
        noSMS = getString(R.string.no_sms_found);
        okSms = getString(R.string.sms_founded);

        travelling.setOnClickListener(this);
        start.setOnClickListener(this);
        blocked.setOnClickListener(this);
        onGoing.setOnClickListener(this);
        finish.setOnClickListener(this);

        StartApp();

        clTeamCode.setOnClickListener(view -> teamCodeBox.setText(""));

        clSiteCode.setOnClickListener(v -> siteCodeBox.setText(""));

        clTroubleFounded.setOnClickListener(v -> troubleFoundedBox.setText(""));

        clTroubleFix.setOnClickListener(v -> troubleFixBox.setText(""));

        clTroubleNeedFix.setOnClickListener(v -> troubleNeedToFixBox.setText(""));


        generateSms.setOnClickListener(view -> {
            smsCopy.setVisibility(View.VISIBLE);
            String teamCode = teamCodeBox.getText().toString().toUpperCase() + "_";
            String siteCode = siteCodeBox.getText().toString().toUpperCase() + "_";
            String radioTask = decodeTasks(tasks.getCheckedRadioButtonId()) + "_";
            String radioStatus = decodeStatus((status.getCheckedRadioButtonId()));
            String strTroubleFind = troubleFoundedBox.getText().toString();
            String strTroubleFix = troubleFixBox.getText().toString();
            String strTroubleNeedFix = troubleNeedToFixBox.getText().toString();

            if(warehouseSelected && checkTeam()){
                txtSms = teamCode + radioTask + "travelling";
                ClearBoxes();
                smsTextBox.setText(txtSms);
            }else{
                if(onGoingSelected && checkTeam() && checkSiteCode()){
                    txtSms = teamCode + siteCode + radioTask + radioStatus;
                    ClearBoxes();
                    smsTextBox.setText(txtSms);
                }else{
                    if(checkTeam() && checkSiteCode()) {
                        txtSms = teamCode + siteCode + radioTask + radioStatus +
                                getString(R.string.trouble_found) + strTroubleFind + getString(R.string.trouble_fix) +
                                strTroubleFix + getString(R.string.trouble_to_fix) + strTroubleNeedFix;
                        ClearBoxes();
                        smsTextBox.setText(txtSms);
                    }
                }
            }
        });

        sendSMSWhatsapp.setOnClickListener(view -> {
            if(tasks.getCheckedRadioButtonId() == R.id.radio_btn_warehouse){
                WhatsAppSend(txtSms);
            }else {
                if (!teamCodeBox.getText().toString().isEmpty() || !siteCodeBox.getText().toString().isEmpty() || !isSmsCreated) {
                    WhatsAppSend(txtSms);
                } else {
                    sendToast(noSMS, longDuration);
                }
            }
        });

        smsCopy.setOnClickListener(view -> {
            String outputText = smsTextBox.getText().toString();
            if (outputText.isEmpty()) {
                sendToast(noSMS, shortDuration);

            }else{
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("SMS", outputText);
                clipboardManager.setPrimaryClip(clipData);
                sendToast(okSms, shortDuration);
            }
        });
    }

    String decodeTasks(int task){
        String taskDecoded = "";
        if(task == R.id.radio_btn_site_down){
            taskDecoded = "SITE_DOWN";
            warehouseSelected = false;
        }else if(task == R.id.radio_btn_integration){
            taskDecoded = "INTEGRATION";
            warehouseSelected = false;
        }else if(task == R.id.radio_btn_warehouse){
            taskDecoded = "WAREHOUSE";
            warehouseSelected = true;
        }else if(task == R.id.radio_btn_non_gw_gw){
            taskDecoded = "NON_GW->GW";
            warehouseSelected = false;
        }
        return taskDecoded;
    }

    String decodeStatus(int status){
        String statusDecoded = "";
        if(status == R.id.radio_btn_travelling){
            statusDecoded = "TRAVELLING";
            onGoingSelected = false;
        }else if(status == R.id.radio_btn_start){
            statusDecoded = "START";
            onGoingSelected = false;
        }else if(status == R.id.radio_btn_blocked){
            statusDecoded = "BLOCKED";
            onGoingSelected = false;
        }else if(status == R.id.radio_btn_ongoing){
            statusDecoded = "ONGOING";
            onGoingSelected = true;
        }else if(status == R.id.radio_btn_finish){
            statusDecoded = "FINISH";
            onGoingSelected = false;
        }
        return statusDecoded;
    }


    @Override
    public void onClick(View view){
        if(view.getId() == finish.getId()) {
            layoutFinish.setVisibility(View.VISIBLE);
        } else {layoutFinish.setVisibility(View.GONE);}

        if(view.getId() == onGoing.getId()){
            ongoingComment.setVisibility(View.VISIBLE);
        }else{ongoingComment.setVisibility(View.GONE);}
    }

    void StartApp(){
        layoutFinish.setVisibility(View.GONE);
        ongoingComment.setVisibility(View.GONE);
        smsTextBox.setText(getString(R.string.sms_box_text_default));
    }

    void ClearBoxes(){
        troubleFoundedBox.setText("");
        troubleFixBox.setText("");
        troubleNeedToFixBox.setText("");
    }

    void sendToast(String text, int duration){
        Toast.makeText(this, text, duration).show();
    }

    void WhatsAppSend(String sms){
        Intent sendWhatsapp = new Intent();
        sendWhatsapp.putExtra(Intent.EXTRA_TEXT, sms);
        sendWhatsapp.setPackage("com.whatsapp");
        sendWhatsapp.setType("text/plain");
        startActivity(sendWhatsapp);
    }

    boolean checkTeam(){
        boolean teamCheck = false;
        if(teamCodeBox.getText().toString().isEmpty()){
            sendToast(noTeam, longDuration);
        }else{teamCheck = true;}
        return teamCheck;
    }

    boolean checkSiteCode() {
        boolean siteCheck = false;
        if(siteCodeBox.getText().toString().isEmpty()){
            sendToast(noSiteCode, longDuration);
        }else{siteCheck = true;}
        return siteCheck;
    }

}