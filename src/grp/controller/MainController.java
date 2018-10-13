package grp.controller;

import grp.util.Support;

public class MainController {
	
	public boolean isReady() {
		return true;
	}
	
	public void whenReady(Runnable runnable) {
		Support.setInterval(1000, s -> {
			if(isReady()) {
				runnable.run();	
				s.shutdown();
			}
		});
	}

}
