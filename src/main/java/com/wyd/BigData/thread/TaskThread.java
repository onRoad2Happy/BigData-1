package com.wyd.BigData.thread;

import com.wyd.BigData.RDD.LoginRDD;

public class TaskThread implements Runnable{

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(60000);
				LoginRDD.distinct();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
