package com.example.hairreservationsystem;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * 싱글톤 클래스로 구현
 * 반드시 getIntance를 통해서 클래스 내에 만들어진 객체를 가리키게 해야 한다.
 */
public class HttpConnectionManager {
    public static final String baseURL = "http://10.1.188.54:3000/";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static HttpConnectionManager instance;

    public static HttpConnectionManager getInstance() {
        if (instance == null) {
            instance = new HttpConnectionManager();
        }
        return instance;
    }


    /**
     * 로그인을 시도하는 메소드
     * 받아온 파라미터를 json에 담아서 서버로 보낸다. (키 값을 반드시 통일화시킬 것)
     * @param CID
     * @param CPassword
     * @param responseHandler
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public void login(String CID, String CPassword, JsonHttpResponseHandler responseHandler) throws JSONException, UnsupportedEncodingException {
        String url = baseURL + "/login";// "http://10.1.188.54:3000/login"
        JSONObject jsonParams = new JSONObject();
        //json형태로 데이터를 전환한다. 첫 파라미터엔 키 값, 두번째에 데이터가 들어간다. (키값이 같아야 함)
        jsonParams.put("CID", CID);
        jsonParams.put("CPassword", CPassword);
        //json에 들어간 데이터를 다시 stinrg으로 전환한다.
        StringEntity entity = new StringEntity(jsonParams.toString());
        client.post(null, url, entity, "application/json", responseHandler);
    }

    /**
     * 일반회원이 회원가입 완료 버튼을 눌렀을 떄 동작하는 메소드
     * 순서대로 CID, CPassword, CName, CPhoneNum을 파라미터로 받는다.
     * 받아온 파라미터를 json에 담아서 서버로 보낸다. (키 값을 반드시 통일화시킬 것)
     * @param CID
     * @param CPassword
     * @param CName
     * @param CPhoneNum
     * @param responseHandler
     *
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public void makeAccount(String CID, String CPassword, String CName, String CPhoneNum, JsonHttpResponseHandler responseHandler)throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/makeaccount";// "http://10.1.188.54:3000/makeaccount"
        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("CID", CID);
            jsonParams.put("CPassword", CPassword);
            jsonParams.put("CName", CName);
            jsonParams.put("CPhoneNum", CPhoneNum);
            //StringEntity entity = new StringEntity(jsonParams.toString());
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            Log.d("Result", jsonParams.toString());

            client.post(null, url, entity, "application/json", responseHandler);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 미용사로 회원가입을 시도
     * CID ,CPassword, CName, CPhoneNum, license를 파라미터로 받아
     * 회원 테이블에 기본 정보를 저장하고
     * license는 CID와 함께 미용사 테이블에 저장됨
     */
    public void makeHairDresser(String CID, String CPassword, String CName, String CPhoneNum, String license, JsonHttpResponseHandler responseHandler)throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/makehairdresser"; // "http://10.1.188.54:3000/makehairdresser"
        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("CID", CID);
            jsonParams.put("CPassword", CPassword);
            jsonParams.put("CName", CName);
            jsonParams.put("CPhoneNum", CPhoneNum);
            jsonParams.put("license", license);
            Log.d("Result", jsonParams.toString());
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        }catch(Exception e){e.printStackTrace();}
    }

    /**
     * ID찾기를 누를 시, DB 내에 있는 CName, CPhoneNum 값을 비교하여 조회를 한다
     * 일치하면 DB 내에 있는 CID값을 response로 넣어 클라이언트로 다시 던져준다. (반드시 키 값을 통일화시킬 것)
     * @param CName
     * @param CPhoneNum
     * @param responseHandler
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public void findID(String CName, String CPhoneNum, JsonHttpResponseHandler responseHandler)throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/findid";
        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("CName", CName);
            jsonParams.put("CPhoneNum", CPhoneNum);
            StringEntity entity = new StringEntity(jsonParams.toString());
            Log.d("Result", jsonParams.toString());

            client.post(null, url, entity, "application/json", responseHandler);
        }catch(Exception e){e.printStackTrace();}
    }

}