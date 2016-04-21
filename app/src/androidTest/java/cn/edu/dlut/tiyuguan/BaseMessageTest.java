package cn.edu.dlut.tiyuguan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseModel;
import cn.edu.dlut.tiyuguan.model.NoticeModel;
import cn.edu.dlut.tiyuguan.model.Venue;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;

/**
 * Created by asus on 2016/3/31.
 */
public class BaseMessageTest extends ApplicationTest {

    public void testJson2Model() {
        try {
            String noticeJsonStr = "{ \"code\":1005, \"data\": { \"NoticeModel\": {\"noticeTitle\": \"篮球馆\",\"noticeTime\": \"22:00:00\",\"noticePublisher\": \"20\",\"noticeContent\": \"周六不开门\"}},\"message\": {\"result\": \"success\"}}";
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(noticeJsonStr);
            String formatJsonStr = m.replaceAll("");
            BaseMessage message = AppUtil.getMessage(formatJsonStr, "NoticeModel");
            NoticeModel model = (NoticeModel) message.getData("NoticeModel");
            assertNotNull(model);
            assertEquals("25", model.getNoticePublisher());

        } catch (JSONException e) {
            System.err.print(e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.print(e.toString());
            e.printStackTrace();
        }
    }
}
