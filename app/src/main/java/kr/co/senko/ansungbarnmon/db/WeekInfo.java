package kr.co.senko.ansungbarnmon.db;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class WeekInfo {
    private String groupID;
    private String groupName;
    private ArrayList<ArrayList<String>> weekLog = new ArrayList<>();

    public WeekInfo(JSONArray resources) {
        try {
            this.groupID = resources.getJSONObject(0).getString("sys_op_group_id");
            this.groupName = resources.getJSONObject(0).getString("group_name");
            for(int index=0; index < resources.length(); index++) {
                ArrayList<String> temp = new ArrayList<>();
                for(int subdex=0; subdex < 7; subdex++) {
                    temp.add(resources.getJSONObject(index).getString("week_level_day_before_"+(subdex+1)));
                }
                this.weekLog.add(temp);
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

    public ArrayList<ArrayList<String>> getWeekLog() {
        return weekLog;
    }
}
