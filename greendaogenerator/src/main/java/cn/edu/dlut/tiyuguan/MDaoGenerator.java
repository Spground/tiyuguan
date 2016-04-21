package cn.edu.dlut.tiyuguan;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MDaoGenerator {
    public static void main(String[] args) throws IOException {
        Schema schema = new Schema(1, "cn.edu.dlut.tiyuguan.bean");
        schema.setDefaultJavaPackageDao("cn.edu.dlut.tiyuguan.dao");
        addRemind(schema);
        addNotice(schema);
        try {
            new DaoGenerator().generateAll(schema,
                    "../tiyuguan/app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Entity addRemind(Schema schema) {
        Entity remind = schema.addEntity("RemindBean");
        remind.addIdProperty().autoincrement();
        remind.addLongProperty("advanced");//预约提醒提前量
        Property orderIdProperty = remind.addStringProperty("orderId").notNull().getProperty();
        Entity order = addOrder(schema);
        remind.addToOne(order, orderIdProperty);
        return remind;
    }

    public static Entity addOrder(Schema schema) {
        Entity order = schema.addEntity("OrderBean");
        order.addStringProperty("orderId").notNull().primaryKey();
        order.addLongProperty("startTime");
        order.addLongProperty("endTime");
        Entity user = addUser(schema);
        Property userId = order.addStringProperty("userId").notNull().getProperty();
        ToMany user2Orders = user.addToMany(order, userId);
        user2Orders.setName("orders");
        return order;
    }

    public static Entity addUser(Schema schema) {
        Entity user = schema.addEntity("UserBean");
        user.addStringProperty("userName");
        user.addStringProperty("userId").notNull().primaryKey();
        user.addStringProperty("userRole");
        user.addStringProperty("creditWorthiness");
        return user;
    }

    public static Entity addNotice(Schema schema) {
        Entity notice = schema.addEntity("NoticeBean");
        notice.addIdProperty().autoincrement();
        notice.addStringProperty("noticeTitle");
        notice.addStringProperty("noticeContent");
        notice.addStringProperty("noticeTime");
        notice.addStringProperty("noticePublisher");
        return notice;
    }
}
