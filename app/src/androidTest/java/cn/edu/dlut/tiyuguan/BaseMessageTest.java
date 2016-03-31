package cn.edu.dlut.tiyuguan;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseModel;
import cn.edu.dlut.tiyuguan.model.Venue;

/**
 * Created by asus on 2016/3/31.
 */
public class BaseMessageTest extends ApplicationTest {

    public void testJson2Model() {
        try {
            JSONObject o = new JSONObject("{\n" +
                    "        \"venuesName\": \"篮球馆\",\n" +
                    "        \"closeTime\": \"20:00:00\",\n" +
                    "        \"venuesCharge\": \"20\",\n" +
                    "        \"venuesId\": \"1\",\n" +
                    "        \"locationNum\": \"10\",\n" +
                    "        \"openTime\": \"09:00:00\"\n" +
                    "      }");

            BaseMessage message = new BaseMessage();
            BaseModel model = message.json2Model("cn.edu.dlut.tiyuguan.model.Venue", o);
            assertEquals("篮球馆", ((Venue)model).getVenuesName());
            assertEquals(20f, ((Venue)model).getVenuesCharge());
            assertEquals(1, ((Venue)model).getVenuesId());
            assertEquals("09:00:00", ((Venue)model).getOpenTime());
            System.out.print(model);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
