package kr.co.senko.ansungbarnmon.db;

import org.json.JSONException;
import org.json.JSONObject;

public class RegionInfo {
    private final String groupID;
    private final String groupName;
    private final String currentTime;
    private final String currentValue;

    public RegionInfo(JSONObject resources) {
        try {
            this.groupID = resources.getString("sys_op_group_id");
            this.groupName = resources.getString("group_name");
            this.currentTime = resources.getString("real_timestamp");
            this.currentValue = resources.getString("real_level");
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
}
