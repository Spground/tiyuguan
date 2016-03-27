package cn.edu.dlut.tiyuguan.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by asus on 2015/10/6.
 */
public class BaseTaskPool {

    //TODO:
    private static BaseTaskPool taskPool = new BaseTaskPool();

    private ExecutorService es;
    private BaseTaskPool(){
        es = Executors.newFixedThreadPool(5);
    }

    public static BaseTaskPool getInstance(){
        if(taskPool == null)
            taskPool = new BaseTaskPool();
        return taskPool;
    }

    /**asyn execute task**/
    public void addTask(final BaseTask task){
        es.execute(new Runnable() {
            @Override
            public void run() {
                task.start();
            }
        });
    }
    public ExecutorService getExecutorService(){
        return this.es;
    }
}
