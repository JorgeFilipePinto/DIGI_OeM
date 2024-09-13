package com.example.digi;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String txtSms, noTeam, noSiteCode, smsCreated, noSMS, okSms, invalidSms,radioTask, radioStatus, siteCode, teamCode;

    Button clTeamCode, clSiteCode, clTroubleFounded, clTroubleFix, clTroubleNeedFix, generateSms, smsCopy;
    ImageButton sendSMSWhatsapp, share;
    RadioGroup tasks, status;
    RadioButton siteDown, integration, warehouse, noGw;
    RadioButton travelling, start, blocked, onGoing, finish;
    LinearLayout ongoingComment, layoutFinish, layoutSms;
    EditText teamCodeBox, siteCodeBox, ongoingTextComment,troubleFoundedBox, troubleFixBox, troubleNeedToFixBox, smsTextBox;

    boolean isSmsCreated = false, smsValid = false, warehouseIsSelected, onGoingIsSelected, finishIsSelected;
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
        share = findViewById(R.id.btn_share);

        ongoingComment = findViewById(R.id.ongoing_comment);
        ongoingTextComment = findViewById(R.id.edit_text_ongoing_comment);
        layoutFinish= findViewById(R.id.layout_finish);
        troubleFoundedBox = findViewById(R.id.text_box_trouble_founded_main);
        troubleFixBox = findViewById(R.id.text_box_trouble_fixed);
        troubleNeedToFixBox = findViewById(R.id.text_box_trouble_need_fixed);
        clTroubleFounded = findViewById(R.id.btn_trouble_founded_clear_main);
        clTroubleFix = findViewById(R.id.btn_trouble_fixed_clear_main);
        clTroubleNeedFix = findViewById(R.id.btn_trouble_need_fixed_clear_main);
        layoutSms = findViewById(R.id.layoutsms);
        generateSms = findViewById(R.id.btn_generate_message);
        smsTextBox = findViewById(R.id.sms_text_box);
        sendSMSWhatsapp = findViewById(R.id.btn_whatsapp_send);
        smsCopy = findViewById(R.id.btn_sms_copy);
        invalidSms = getString(R.string.invalid_sms);

        travelling.setOnClickListener(this);
        start.setOnClickListener(this);
        blocked.setOnClickListener(this);
        onGoing.setOnClickListener(this);
        finish.setOnClickListener(this);
        siteDown.setOnClickListener(this);
        integration.setOnClickListener(this);
        warehouse.setOnClickListener(this);
        noGw.setOnClickListener(this);


        StartApp();

        clTeamCode.setOnClickListener(view -> teamCodeBox.setText(""));

        clSiteCode.setOnClickListener(v -> siteCodeBox.setText(""));

        clTroubleFounded.setOnClickListener(v -> troubleFoundedBox.setText(""));

        clTroubleFix.setOnClickListener(v -> troubleFixBox.setText(""));

        clTroubleNeedFix.setOnClickListener(v -> troubleNeedToFixBox.setText(""));


        generateSms.setOnClickListener(view -> {
            sms();
            if(smsValid)
            {
                layoutSms.setVisibility(view.VISIBLE);
            }
            else
            {
                sendToast(invalidSms, longDuration);
                layoutSms.setVisibility(view.INVISIBLE);
            }
        });

        sendSMSWhatsapp.setOnClickListener(view -> {
            if(smsValid)
            {
                WhatsAppSend(txtSms);
            }
            else
            {
                sendToast(noSMS, shortDuration);
            }
        });

        smsCopy.setOnClickListener(view -> {
            if(smsValid)
            {
                smsCopied(smsTextBox.getText().toString());
            }
            else
            {
                sendToast(noSMS, shortDuration);
            }

        });
    }

    void StartApp()
    {
        DisableElments();
        radioTask = "_SITE_DOWN";
        radioStatus = "_TRAVELLING";
        layoutFinish.setVisibility(View.GONE);
        ongoingComment.setVisibility(View.GONE);
        errorMessages();
    }

    @Override
    public void onClick(View view)
    {
        decodeTasks(view.getId());
        decodeStatus(view.getId());
        if(view.getId() == finish.getId() && !warehouseIsSelected)
        {
            layoutFinish.setVisibility(View.VISIBLE);
        }
        else
        {
            layoutFinish.setVisibility(View.GONE);
        }

        if(view.getId() == onGoing.getId())
        {
            ongoingComment.setVisibility(View.VISIBLE);
        }
        else
        {
            ongoingComment.setVisibility(View.GONE);
        }
    }



    void decodeTasks(int task)
    {
        if(task == R.id.radio_btn_site_down)
        {
            radioTask = "_SITE_DOWN";
            warehouseIsSelected = false;
        }
        else if(task == R.id.radio_btn_integration)
        {
            radioTask = "_INTEGRATION";
            warehouseIsSelected = false;
        }
        else if(task == R.id.radio_btn_warehouse)
        {
            radioTask = "_WAREHOUSE";
            warehouseIsSelected = true;
        }
        else if(task == R.id.radio_btn_non_gw_gw)
        {
            radioTask = "_NON_GW->GW";
            warehouseIsSelected = false;
        }
    }

    void decodeStatus(int status)
    {
        if(status == R.id.radio_btn_travelling)
        {
            radioStatus = "_TRAVELLING";
            onGoingIsSelected = false;
            finishIsSelected = false;
        }
        else if(status == R.id.radio_btn_start)
        {
            radioStatus = "_START";
            onGoingIsSelected = false;
            finishIsSelected = false;
        }
        else if(status == R.id.radio_btn_blocked)
        {
            radioStatus = "_BLOCKED";
            onGoingIsSelected = false;
            finishIsSelected = false;
        }
        else if(status == R.id.radio_btn_ongoing)
        {
            radioStatus = "_ONGOING";
            onGoingIsSelected = true;
            finishIsSelected = false;
        }
        else if(status == R.id.radio_btn_finish)
        {
            radioStatus = "_FINISH";
            onGoingIsSelected = false;
            finishIsSelected = true;
        }

    }

    void sendToast(String text, int duration)
    {

        Toast.makeText(this, text, duration).show();
    }

    boolean setTeam()
    {
        boolean teamCheck = false;
        if(!teamCodeBox.getText().toString().isEmpty())
        {
            teamCheck = true;
            teamCode = teamCodeBox.getText().toString().toUpperCase();
        }
        return teamCheck;
    }

    boolean setSiteCode()
    {
        boolean siteCheck = false;
        if(!siteCodeBox.getText().toString().isEmpty())
        {
            siteCheck = true;
            siteCode = "_" + siteCodeBox.getText().toString().toUpperCase();
        }
        return siteCheck;
    }

    void errorMessages()
    {
        noTeam = getString(R.string.no_team_code_found);
        noSiteCode = getString(R.string.no_site_code_found);
        smsCreated = getString(R.string.sms_created);
        noSMS = getString(R.string.no_sms_found);
        okSms = getString(R.string.sms_founded);
    }


    void sms()
    {
        setTeam();
        setSiteCode();
        String strOnGoing = "";
        String strTroubleFind = "";
        String strTroubleFix = "";
        String strTroubleNeedFix = "";
        String strFinish = "";

        if(!ongoingTextComment.getText().toString().isEmpty())
        {
            strOnGoing = getString(R.string.ongoing_comment) + " " + ongoingTextComment.getText().toString();
        }

        if(!troubleFoundedBox.getText().toString().isEmpty())
        {
            strTroubleFind = getString(R.string.trouble_found) + " " + troubleFoundedBox.getText().toString();
            strFinish += strTroubleFind;
        }

        if(!troubleFixBox.getText().toString().isEmpty())
        {
            strTroubleFix = getString(R.string.trouble_fix) + " " + troubleFixBox.getText().toString();
            strFinish += strTroubleFix;
        }

        if(!troubleNeedToFixBox.getText().toString().isEmpty())
        {
            strTroubleNeedFix = getString(R.string.trouble_to_fix) + " " + troubleNeedToFixBox.getText().toString();
            strFinish += strTroubleNeedFix;
        }

        if(warehouseIsSelected)
        {
            if(setTeam()) {
                txtSms = teamCode + radioTask + "_TRAVELLING";
                smsTextBox.setText(txtSms);
                smsValid = true;
            }
            else
            {
                smsValid = false;
                sendToast(noTeam, longDuration);
            }
        }
        else if(onGoingIsSelected)
        {
            if(!warehouseIsSelected)
            {
                txtSms = teamCode + siteCode + radioTask + radioStatus + strOnGoing;
                smsTextBox.setText(txtSms);
                smsValid = true;
            }
            else {
                sendToast("Selecione outra tarefa", longDuration);
                smsValid = false;
            }
        }
        else if(finishIsSelected)
        {
            if (!warehouseIsSelected)
            {
                txtSms = teamCode + siteCode + radioTask + radioStatus + strFinish;
                ClearBoxes();
                smsTextBox.setText(txtSms);
                smsValid = true;
            }
            else
            {
                sendToast("Selecione outra tarefa", longDuration);
                smsValid = false;
            }
        }
        else
            {
                if(teamCodeBox.getText().toString().isEmpty())
                {
                    sendToast(noTeam, longDuration);
                    smsValid = false;
                }
                else
                {
                    if(siteCodeBox.getText().toString().isEmpty())
                    {
                       sendToast(noSiteCode,longDuration);
                        smsValid = false;
                    }
                    else
                    {
                        txtSms = teamCode + siteCode + radioTask + radioStatus;
                        smsTextBox.setText(txtSms);
                        smsValid = true;
                    }
                }

            }
    }


    void WhatsAppSend(String sms)
    {
        Intent sendWhatsapp = new Intent();
        sendWhatsapp.putExtra(Intent.EXTRA_TEXT, sms);
        sendWhatsapp.setPackage("com.whatsapp");
        sendWhatsapp.setType("text/plain");
        startActivity(sendWhatsapp);
    }

    void smsCopied(String sms)
    {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("SMS", sms);
        clipboardManager.setPrimaryClip(clipData);
        sendToast(okSms, shortDuration);
    }

    void ClearBoxes(){
        troubleFoundedBox.setText("");
        troubleFixBox.setText("");
        troubleNeedToFixBox.setText("");
    }

    void DisableElments()
    {
        share.setVisibility(View.INVISIBLE);
    }
}