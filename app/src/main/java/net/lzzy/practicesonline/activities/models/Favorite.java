package net.lzzy.practicesonline.activities.models;

import net.lzzy.sqllib.AsPrimaryKey;
import net.lzzy.sqllib.Sqlitable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author lzzy_gxy on 2019/4/16.
 * Description:
 */
public  class Favorite extends BaseEntity implements Sqlitable {
   public static final String COL_QUESTION_ID = "questionId";
    private UUID questionId;

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean needUpdate() {
        return false;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        return null;
    }

    @Override
    public void fromJson(JSONObject json) throws JSONException {

    }
}
