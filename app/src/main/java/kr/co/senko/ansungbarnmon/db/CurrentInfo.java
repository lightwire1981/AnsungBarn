package kr.co.senko.ansungbarnmon.db;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CurrentInfo {
    private final String groupID;
    private final String groupName;
    private final String currentTime;
    private final String currentValue;
    private final ArrayList<String> currentLog = new ArrayList<>();

    public CurrentInfo(JSONObject resources) {
        try {
            this.groupID = resources.getString("sys_op_group_id");
            this.groupName = resources.getString("group_name");
            this.currentTime = resources.getString("real_timestamp");
            this.currentValue = resources.getString("real_level");

            for(int index=1; index < 24; index++) {
                this.currentLog.add(resources.getString("real_level_hour_before_"+(index < 10 ? "0"+index:index)));
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public ArrayList<String> getCurrentLog() {
        return currentLog;
    }
}
