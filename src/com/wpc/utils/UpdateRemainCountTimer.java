package com.wpc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.wpc.service.ServiceLocator;

public class UpdateRemainCountTimer {
	Timer timer;
    
    public UpdateRemainCountTimer(){
        Date time = getTime();
        timer = new Timer();
        timer.schedule(new UpdateTimerTask(), time);
    }
    
    public Date getTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date time = calendar.getTime();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
        return time;
    }
    
    public static void main(String[] args) {
        new UpdateRemainCountTimer();
    }
    
    public class UpdateTimerTask extends TimerTask{

        @Override
        public void run() {
        	System.out.print("定时任务开始执行");
            /*try {
				ServiceLocator.getVideoService().updateRemainCount();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
        }
    }
}
