package grp.component;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;

import grp.util.Support;

@SuppressWarnings("serial")
public class FilePicker extends Control {

	private JTextField pathField = new JTextField();
	private Button browseButton = new Button("Browse");
	private JFileChooser fileChooser = new JFileChooser();
	private boolean saveModeEnabled = false;
	private File model;
	private ActionListener deleteAction = e -> pathField.setText("");
	
	public FilePicker() {
		this(null, false);
	}
	
	public FilePicker(List<File> model) {
		this(null, false);
	}
	
	public FilePicker(boolean saveModeEnabled) {
		this(null, saveModeEnabled);
	}
	
	public FilePicker(File model, boolean saveModeEnabled) {
		setModel(model);
		this.saveModeEnabled = saveModeEnabled;
		
		pathField.setFont(defaultFont);
		pathField.setEditable(true);
		pathField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) { browseButton.setMnemonic('B'); }
			public void focusLost(FocusEvent e) { browseButton.setMnemonic(0); }
		});
		pathField.setTransferHandler(new FileDroppableField());
		Support.addKeyListener(pathField, "DELETE", deleteAction);
		browseButton.addActionListener(e -> addFilesWithChooser());
		
		add(pathField);
		add(createSeparator());
		add(browseButton);
	}
	
	public void setModel(File file) {
		if(file == null) return;
		
		if(file.getPath().indexOf('/') == -1) {
			file = new File(fileChooser.getCurrentDirectory().getPath() + "/" + file.getPath());
		}
		
		this.model = file;
		
		setPathField();
	}
	
	private void setPathField() {
		if(model == null) {
			pathField.setText("");
			return;
		}
		
		pathField.setText(model.getPath());
	}
	
	public void setSaveModeEnabled(boolean newValue) {
		saveModeEnabled = newValue;
	}
	
	public File getSelectedFile() {
		return fileChooser.getSelectedFile();
	}
	
	private boolean isFileValid(File file) {
		return saveModeEnabled || file.exists();
	}
	
	private boolean showFileChooser() {
		int r = saveModeEnabled ? fileChooser.showSaveDialog(null) : fileChooser.showOpenDialog(null);
		return r == JFileChooser.APPROVE_OPTION;
	}
	
	private void addFilesWithChooser() {
		while(showFileChooser()) {
			File file = getSelectedFile();
			if(isFileValid(file)) {
				setModel(file);
				break;
			}
			
			Support.displayMessage("Warning: Invalid file");
		}
	}
	
	private class FileDroppableField extends TransferHandler {
		
		@SuppressWarnings("unchecked")
		private List<File> getDroppedFiles(TransferSupport info) {
			List<File> droppedFiles = null;
			if(canImport(info)) try {
				Transferable transferable = info.getTransferable();
				droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
			} catch (UnsupportedFlavorException | IOException e) {
				e.printStackTrace();
			}
			return droppedFiles;
		}
		
		public boolean canImport(TransferSupport info) {
			if(info.getComponent() instanceof JTextComponent) return true;
			return info.isDrop() && info.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
		}
		
		public boolean importData(TransferSupport info) {
			if(info.getDataFlavors()[0].getRepresentationClass().equals(String.class)) {
				try {
					((JTextComponent) info.getComponent()).setText((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor));
					return true;
				} catch (UnsupportedFlavorException e) {
					return false;
				} catch (IOException e) {
					return false;
				}
			}
			
			List<File> files = getDroppedFiles(info);
			if(files == null) return false;
			files = Support.filter(file -> isFileValid(file), files);
			if(!files.isEmpty()) {
				setModel(files.get(0));
			}
			return true;
		}
		
		public int getSourceActions(JComponent c) {
	        return COPY_OR_MOVE;
	    }
		
		public Transferable createTransferable(JComponent c) {
	        return new StringSelection(((JTextComponent) c).getSelectedText());
	    }
		
		public void exportDone(JComponent c, Transferable t, int action) {
	        if(action == MOVE) {
	            ((JTextComponent) c).replaceSelection("");
	        }
	    }
	}
	
	public void setValue(String value) {
		if(value != null) {
			setModel(new File(value));
		}
	}
	
	public String getValue() {
		return pathField.getText();
	}
}