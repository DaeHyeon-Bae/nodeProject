package com.example.hairreservationsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class MakeAccount extends AppCompatActivity {
    private HttpConnectionManager hcManager;
    private EditText CID, CPassword, checkCPassword, CName, CPhoneNum;
    private EditText license;
    private Button btnMakeClient, btnMakeDresser, btnMakeManager;
    private TabHost tabHost;
    private TabHost.TabSpec ts1, ts2, ts3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_account);
        setTabHost();
        setID();
        onClickMakeClient();
        onClickMakeHairDresser();
        onClickMakeManager();
    }

    void setID(){
        btnMakeClient = (Button)findViewById(R.id.completeClient);
        btnMakeDresser = (Button)findViewById(R.id.completeHairDresser);
        btnMakeManager = (Button)findViewById(R.id.completeManager);
    }
    /**
     * 사업자 CID, CPassword, CName, CPhoneNum, BussinessNum
     */
    void onClickMakeManager(){
        btnMakeManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 미용사 CID, CPassword, CName, CPhoneNum, HairDresserLicenseNum
     */
    void onClickMakeHairDresser(){
        btnMakeDresser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CID = (EditText)findViewById(R.id.makeHID);
                CPassword = (EditText)findViewById(R.id.makeHPassword);
                CName = (EditText)findViewById(R.id.makeHName);
                CPhoneNum = (EditText)findViewById(R.id.makeHPhoneNum);
                checkCPassword = (EditText)findViewById(R.id.makeCheckHPassword);
                license = (EditText)findViewById(R.id.makeLicense);

                final String myID = CID.getText().toString();
                final String myPassword = CPassword.getText().toString();
                final String myName = CName.getText().toString();
                final String myPhoneNum = CPhoneNum.getText().toString();
                final String checkPassword = checkCPassword.getText().toString();
                final String myLicense = license.getText().toString();

                if(myPassword.equals(checkPassword)){
                    Log.d("Success", "비밀번호 확인 통과");
                    hcManager = HttpConnectionManager.getInstance(); //싱글톤
                    try{
                        hcManager.makeHairDresser(myID, myPassword, myName, myPhoneNum, myLicense, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Log.d("R ", "응답이 잘 받아와집니까?");
                                try{
                                    if(response.getString("CID").equals(myID)){
                                        Toast.makeText(getApplicationContext(), "이미 존재하는 ID입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(getApplicationContext(), "CName : " + response.getString("CName"), Toast.LENGTH_SHORT).show();
                                }
                                catch(JSONException e){e.printStackTrace();}
                                super.onSuccess(statusCode, headers, response);
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.d("F ", "응답에 실패했습니까?");
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    }catch(JSONException e){e.printStackTrace();}
                    catch(UnsupportedEncodingException e){e.printStackTrace();}
                }
                else{
                    Log.d("Failure", "비밀번호 확인이 필요합니다.");
                }
            }
        });
    }

    /**
     * 일반회원 CID, CPassword, CName, CPhoneNum
     */
    void onClickMakeClient(){
        btnMakeClient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CID = (EditText)findViewById(R.id.makeCID);
                CPassword = (EditText)findViewById(R.id.makeCPassword);
                CName = (EditText)findViewById(R.id.makeCName);
                CPhoneNum = (EditText)findViewById(R.id.makeCPhoneNum);
                checkCPassword = (EditText)findViewById(R.id.makeCheckCPassword);
                /*
                * 조건을 확인하기 위한 순서
                * 1. 비밀번호 일치유무를 확인한다.
                * 2. ID 중복유무를 확인한다
                * 3. 1,2번 조건이 모두 만족되면 회원가입이 끝난다.
                * */
                final String myID = CID.getText().toString();
                final String myPassword = CPassword.getText().toString();
                final String myName = CName.getText().toString();
                final String myPhoneNum = CPhoneNum.getText().toString();
                final String checkPassword = checkCPassword.getText().toString();

                if(myPassword.equals(checkPassword)){
                    //비밀번호 일치 여부 통화
                    Log.d("Success", "비밀번호 확인 통과");
                    hcManager = HttpConnectionManager.getInstance(); //싱글톤
                    try{
                        hcManager.makeAccount(myID, myPassword, myName, myPhoneNum, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Log.d("R ", "응답이 잘 받아와집니까?");
                                try{
                                    if(response.getString("CID").equals(myID)){
                                        Toast.makeText(getApplicationContext(), "이미 존재하는 ID입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(getApplicationContext(), "CName : " + response.getString("CName"), Toast.LENGTH_SHORT).show();
                                }
                                catch(JSONException e){e.printStackTrace();}

                                super.onSuccess(statusCode, headers, response);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.d("F ", "응답에 실패했습니까?");
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    }catch(JSONException e){e.printStackTrace();}
                    catch(UnsupportedEncodingException e){e.printStackTrace();}
                }else{
                    Log.d("Error", "비밀번호 확인이 필요합니다.");
                }
            }
        });

    }
    /**
     * 탭호스트 정의
     */
    void setTabHost(){
        tabHost = (TabHost) findViewById(R.id.selectClient);
        tabHost.setup();
        // First tab
        ts1 = tabHost.newTabSpec("Tab1");
        ts1.setContent(R.id.makeClient);
        ts1.setIndicator("일반회원");
        tabHost.addTab(ts1);

        ts2 = tabHost.newTabSpec("Tab2");
        ts2.setContent(R.id.makeHairDresser);
        ts2.setIndicator("미용사");
        tabHost.addTab(ts2);

        ts3 = tabHost.newTabSpec("Tab3");
        ts3.setContent(R.id.makeManager);
        ts3.setIndicator("사업자");
        tabHost.addTab(ts3);
    }
}
