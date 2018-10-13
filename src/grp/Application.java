package grp;

import grp.util.Support;
import grp.view.MainView;

public class Application {
	
	public static final String name = "Trivium Cipher";
	public static final String version = "1.0";
	
	public static MainView view;
	
    public static void main(String[] args) {
    	Support.delay(() -> {
    		view = new MainView();
    		view.setVisible(true);
    	});
    }
}