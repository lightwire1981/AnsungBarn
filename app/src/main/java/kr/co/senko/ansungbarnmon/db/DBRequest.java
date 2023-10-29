package kr.co.senko.ansungbarnmon.db;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import kr.co.senko.ansungbarnmon.R;

public class DBRequest {

    public static enum REQUEST_TYPE {
        CURRENT,
        WEEK
    }

    private Context context;

    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private final Handler resultHandler;


    public DBRequest(Context context, Handler resultHandler) {
        this.context = context;
        this.resultHandler = resultHandler;
    }

    public interface OnCompleteListener {
        void onComplete(String result);
    }

    public void executeAsync(REQUEST_TYPE type, String groupID, OnCompleteListener onCompleteListener) {
        service.execute(() -> {
            try {
                final String result = new RequestData(type, groupID).call();
                resultHandler.post(() -> onCompleteListener.onComplete(result));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    class RequestData implements Callable<String> {
        REQUEST_TYPE type;
        String groupID;
        final String TAG = "DBRequest";

        public RequestData(REQUEST_TYPE type, String groupID) {
            this.type = type;
            this.groupID = groupID;
        }

        @Override
        public String call() throws Exception {
            String response;
            String URL;
            String currentUrl = context.getString(R.string.current_list_url);
            String weekUrl = context.getString(R.string.week_list_url);
            String addID = context.getString(R.string.group_query);

            switch (type) {
                case CURRENT:
                    URL = currentUrl+(groupID.isEmpty() ? "":addID+groupID);
                    break;
                case WEEK:
                    URL = weekUrl+(groupID.isEmpty() ? "":addID+groupID);
                    break;
                default:
                    Log.e("<<<<<<<<<< URL Error by ID : ", groupID);
                    return "";
            }
            Log.i("<<<<<<<<<<< URL : ", URL);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection)(new URL(URL)).openConnection();
            httpsURLConnection.setDoInput(true);
///           httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setReadTimeout(10000);
            httpsURLConnection.setConnectTimeout(15000);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            if (httpsURLConnection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                Log.e(TAG, "Http Connection Fail");
                Log.d(TAG, httpsURLConnection.getResponseMessage());
                Log.d(TAG, httpsURLConnection.getResponseCode()+"");
                return null;
            }
            response = (new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream(), StandardCharsets.UTF_8))).readLine();
            Log.d(TAG, "Response Value :: "+response);

            return response;
        }
    }
}
