package grp.util;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import grp.Application;

public class Support {
	
	public static void log(Object x) {
		System.out.println(x.toString());
	}
	
	public static void delay(Runnable runnable) {
		SwingUtilities.invokeLater(runnable);
	}
	
	public static List<Node> nodeList(NodeList list) {
		
		return IntStream.range(0, list.getLength()).mapToObj(list::item).collect(Collectors.toList());
	}
	
	@SafeVarargs
	public static <A> List<A> list(A... values) {
		return Arrays.asList(values);
	}
	
	public static <A, B> List<B> map(Function<A, B> fn, List<A> list) {
		return list.stream().map(fn).collect(Collectors.toList());	
	}
	
	public static <A> List<A> filter(Predicate<? super A> fn, List<A> list) {
		return fn == null ? list : list.stream().filter(fn).collect(Collectors.toList());	
	}
	
	public static <A> A getOr(A object, A alternative) {
		return object != null ? object : alternative;
	}
	
	public static Date dateFromString(String string) {
		
		Map<String, String> patterns = new HashMap<String, String>();
		patterns.put("\\d{2}:\\d{2}", "HH:mm");
		patterns.put("\\d{2}:\\d{2}:\\d{2}", "HH:mm:ss");
		patterns.put("\\d{2}:\\d{2}:\\d{2}.\\d{3}", "HH:mm:ss.SSS");
		patterns.put("\\d{4}", "yyyy");
		patterns.put("\\d{4}-\\d{2}", "yyyy-MM");
		patterns.put("\\d{4}-\\d{2}-\\d{2}", "yyyy-MM-dd");
		patterns.put("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}", "yyyy-MM-dd HH:mm");
		patterns.put("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", "yyyy-MM-dd HH:mm:ss");
		patterns.put("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3}", "yyyy-MM-dd HH:mm:ss.SSS");
		
		String format = patterns.get(patterns.keySet().stream()
				.filter(p -> Pattern.matches(p, string)).findFirst().get());
		
		try {
			return (new SimpleDateFormat(format)).parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String stringFromDate(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		return i != -1 ? fileName.substring(i+1) : "";
	}
	
	public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(.*)" + regex, "$1" + replacement);
    }
	
	public static void addKeyBinding(JComponent comp, String keys, ActionListener action) {
		for(String key : keys.split(" ")) {
			if(key.equals("SPACE")) {
				key = " ";
			}
			String cmd = Application.name + Instant.now().toEpochMilli();
			KeyStroke stroke = key.length() == 1 ? KeyStroke.getKeyStroke(key.charAt(0)) :
				KeyStroke.getKeyStroke(key);
			comp.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, cmd);
			comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, cmd);
			comp.getActionMap().put(cmd, new Dispatcher(cmd, action));
		}
	}

	@SuppressWarnings("serial")
	static private class Dispatcher extends AbstractAction {
		ActionListener dispatcher;
		
		Dispatcher(final String cmd, final ActionListener dispatch) {
			super(cmd);
			this.dispatcher = dispatch;
		}
		public void actionPerformed(ActionEvent evt) {
			this.dispatcher.actionPerformed(evt);
		}
	}
	
	public static void addMouseListener(JComponent comp, String method, ActionListener action) {
		comp.addMouseListener(new MouseListener() {
			
			private boolean isMethod(String candidate) {
				return map(String::toLowerCase, list(method.split(" "))).contains(candidate);
			}
			
			private ActionEvent mouseToAction(MouseEvent event) {
				return new ActionEvent(event.getSource(), event.getID(), event.paramString());
			}
			
			public void mouseReleased(MouseEvent event) {
				if(isMethod("release")) {
					action.actionPerformed(mouseToAction(event));
				}
			}
			
			public void mousePressed(MouseEvent event) {
				if(isMethod("press")) {
					action.actionPerformed(mouseToAction(event));
				}
			}
			
			public void mouseExited(MouseEvent event) {
				if(isMethod("exit")) {
					action.actionPerformed(mouseToAction(event));
				}
			}
			
			public void mouseEntered(MouseEvent event) {
				if(isMethod("enter")) {
					action.actionPerformed(mouseToAction(event));
				}
			}
			
			public void mouseClicked(MouseEvent event) {
				if (isMethod("click") && event.getClickCount() == 1
						|| isMethod("doubleclick") && event.getClickCount() == 2
						|| isMethod("rightclick") && event.getButton() == MouseEvent.BUTTON3) {
					action.actionPerformed(mouseToAction(event));
				}
			}
		});
	}
	
	public static void addKeyListener(JComponent comp, String key, ActionListener action) {
		comp.addKeyListener(new KeyAdapter() {
			
			private boolean isKey(String candidate) {
				return map(String::toLowerCase, list(key.split(" "))).contains(candidate.toLowerCase());
			}
			
			private ActionEvent keyToAction(KeyEvent event) {
				return new ActionEvent(event.getSource(), event.getID(), event.paramString());
			}
			
			public void keyPressed(KeyEvent event) {
				if(key == null || isKey(KeyEvent.getKeyText(event.getKeyCode()))) {
					action.actionPerformed(keyToAction(event));
				}
			}
		});
	}
	
	public static Map<String, String> dictFromString(String string) {
		Map<String, String> dict = new LinkedHashMap<>();
		
		for(String element : string.split(";")) {
			String[] pair = element.split(":");
			String key = pair[0].trim();
			String value = pair[1].trim();
			dict.put(key, value);
		}
		
		return dict;
	}
	
	public static List<String> listFromString(String string) {
		List<String> list = new ArrayList<>();
		
		String separator = string.indexOf(",") != -1 ? "," : ";";
		
		for(String element : string.split(separator)) {
			list.add(element.trim());
		}
		
		return list;
	}
	
	public static String stringFromList(List<Object> list) {
		return "[" + list.stream().reduce((a, b) -> a.toString() + ", " + b.toString()) + "]";
	}
	
	public static Optional<Character> getMnemonic(String text) {
		int i = text.indexOf("_");
		if(i == -1 || i == text.length() - 1) return Optional.empty();
		return Optional.of(text.charAt(i + 1));
	}
	
	public static void addChangeListener(JTextComponent text, ChangeListener changeListener) {
		text.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				delay(() -> {
					changeListener.stateChanged(new ChangeEvent("change"));
				});
			}
		});
	    text.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				changeListener.stateChanged(new ChangeEvent("focus"));
			}
			public void focusLost(FocusEvent e) {
				changeListener.stateChanged(new ChangeEvent("blur"));
			}
	    });
	}

	public static void setTimeout(int delay, Runnable runnable) {
		ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
		s.schedule(runnable, delay, TimeUnit.MILLISECONDS);
	}
	
	public interface TimeTask {
		public void run(ScheduledExecutorService s);
	}
	
	public static void setInterval(int period, TimeTask runnable) {
		ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
		s.scheduleAtFixedRate(() -> runnable.run(s), 0, period, TimeUnit.MILLISECONDS);
	}
	
	public static void displayMessage(String message) {
		String title = "Message";
		int colonIndex = message.indexOf(':');
		if(colonIndex >= 0) {
			title = message.substring(0, colonIndex);
			message = message.substring(colonIndex + 1).trim();
		}
		int type = JOptionPane.INFORMATION_MESSAGE;
		if(title.contains("Error")) {
			type = JOptionPane.ERROR_MESSAGE;
		} else if(title.equals("Warning")) {
			type = JOptionPane.WARNING_MESSAGE;
		}
		JOptionPane.showMessageDialog(null, message, title, type);
		if(title.equals("Critical Error")) {
			System.exit(1);
		}
	}
	
	public static InputStream loadResource(String name) {
		name = replaceTilde(name);
		InputStream resource;
		try {
			resource = new FileInputStream(name);
		} catch (FileNotFoundException e) {
			resource = null;
		}
		if(resource == null) {
			resource = Application.class.getClassLoader().getResourceAsStream(name);
		}
		if(resource == null) {
			displayMessage("Critical Error: Resource " + name + " not found");
		}
		return resource;
	}
	
	public static URL getResourceURL(String name) {
		name = replaceTilde(name);
		URL url = Application.class.getClassLoader().getResource(name);
		if(url == null) {
			displayMessage("Critical Error: Resource " + name + " not found");
		}
		return url;
	}
	
	public static String readFile(String name) {
		name = replaceTilde(name);
		String result = null;
		try {
			result = Files.lines(Paths.get(name)).reduce("", (a, b) -> a + "\n" + b).substring(1);
		} catch (IndexOutOfBoundsException e) {
			result = "";
		} catch (IOException e) {
			displayMessage("Error: " + name + " not found");
		}
		return result;
	}
	
	public static String capitalize(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	
	public static Double doubleFromString(String string) {
		try {
			return Double.valueOf(string);
		} catch(NumberFormatException | NullPointerException ex1) {
			return null;
		}
	}
	
	public static Integer integerFromString(String string) {
		Double number = doubleFromString(string);
		return number != null ? number.intValue() : null;
	}
	
	public static <A> Integer keyFromValue(List<A> list, A value, Integer defaultValue) {
		A elem = find(o -> o.equals(value), list);
		return elem != null ? list.indexOf(elem) : defaultValue;
	}
	
	public static <A> A find(Predicate<? super A> fn, List<A> list) {
		return find(fn, list, null);
	}
	
	public static <A> A find(Predicate<? super A> fn, List<A> list, A defaultValue) {
		Optional<A> firstValue = list.stream().filter(fn).findFirst();
		return firstValue.isPresent() ? firstValue.get() : defaultValue;
	}
	
	public static Image getImageResource(String name) {
		try {
			return ImageIO.read(Support.loadResource(name));
		} catch (IOException e) {
			displayMessage("Critical Error: Image " + name + " could not be loaded");
		}
		return null;
	}
	
	public static File createFile(String path) {
		File file = new File(replaceTilde(path));
		if(!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	public static String replaceTilde(String path) {
		return path.replaceFirst("^~", System.getProperty("user.home"));
	}
	
	public static String escapeHTML(String s) {
	    StringBuilder out = new StringBuilder(Math.max(16, s.length()));
	    for (int i = 0; i < s.length(); i++) {
	        char c = s.charAt(i);
	        if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
	            out.append("&#");
	            out.append((int) c);
	            out.append(';');
	        } else {
	            out.append(c);
	        }
	    }
	    return out.toString();
	}
}
