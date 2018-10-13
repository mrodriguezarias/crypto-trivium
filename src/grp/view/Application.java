package grp.view;

import grp.controller.MainController;
import grp.util.Support;

public class Application {
	
	public static final String name = "Trivium Cipher";
	public static final String version = "1.0";
	
	public static MainController controller;
	public static MainView view;
	
    public static void main(String[] args) {
    	Support.delay(() -> {
    		controller = new MainController();
    		view = new MainView(controller); 
    		view.setVisible(true);
    	});
    }
}