package grp.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import grp.component.Button;
import grp.component.Section;
import grp.controller.MainController;
import grp.util.Support;

@SuppressWarnings("serial")
public class MainView extends JFrame implements ActionListener {
	
//	private MainController controller;
	
	public Page page;
	public Section footer;
	public Button runButton;
	
	public MainView(MainController controller) {
//		this.controller = controller;
		setProperties();
		initUI();
	}

	public MainView() {
		this(new MainController());
	}

	private void setProperties() {
		setTitle(Application.name);
		setMinimumSize(new Dimension(360, 240));
		setSize(500, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(new ImageIcon(Support.getResourceURL("Icon.png")).getImage());
	}
	
	private void initUI() {
		page = new Page(this);
		add(page);
		
		Support.setTimeout(100, () -> {
//			header = makeHeader();
//			add(header, BorderLayout.NORTH);
			
			footer = makeFooter();
			add(footer, BorderLayout.SOUTH);
			
			refresh();
		});
	}
	
	private Section makeFooter() {
		
		Section footer = new Section();
		footer.setVisible(true);
		footer.setBorder(1, 0, 0, 0);
		
		runButton = new Button("_Run");
		runButton.setEnabled(false);
		
		Button aboutButton = new Button("_About");
		
		footer.add(runButton, BorderLayout.EAST);
		footer.add(aboutButton, BorderLayout.WEST);
		
		return footer;
	}
	
	public void actionPerformed(ActionEvent event) {
		
		switch(event.getActionCommand()) {
		case "Run":
			execute();
			break;
		case "About":
			showAboutDialog();
			break;
		}
	}
	
	public void refresh() {
		Support.delay(() -> {
			repaint();
			revalidate();
		});
	}
	
	public void execute() {
//		new Result(controller.getCommandForCurrentProfile(page.form.getWithIDs()));
	}
	
	public void showAboutDialog() {
		String msg = "<html><h1>" + Application.name + " v" + Application.version + "</h1>";
		msg += "<h3>Licensed under GNU General Public License v3.0</h3>";
		msg += "<p>Made by:</p>";
		msg += "<ul><li>&nbsp;Bruno, Martín</li>";
		msg += "<li>&nbsp;Garcia, Santiago</li>";
		msg += "<li>&nbsp;Nosenzo, Alejandro</li>";
		msg += "<li>&nbsp;Rodríguez Arias, Mariano</li></ul>";
		msg += "<p>Made for:</p>";
		msg += "<ul><li>&nbsp;Marcelo Cipriano</li>";
		msg += "<li>&nbsp;Criptografía</li>";
		msg += "<li>&nbsp;Facultad Regional Buenos Aires</li>";
		msg += "<li>&nbsp;Universidad Tecnológica Nacional</li></ul>";
		msg += "</html>";
		Support.displayMessage("About: " + msg);
	}
	
}
