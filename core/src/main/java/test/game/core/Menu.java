package test.game.core;

import static playn.core.PlayN.*;

import playn.core.Font;
import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.Pointer.Listener;
import tripleplay.game.UIScreen;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Interface;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.Stylesheet;
import tripleplay.ui.layout.AxisLayout;

public class Menu extends UIScreen{
	
	private Interface iface;
	private GroupLayer layer;
	static Group buttons;
	private Root root;

	public void createMenu(final Listener delegate)
	{
		test1(delegate);
	}

	
	public void test1(final Listener delegate)
	{
		layer = graphics().createGroupLayer();
	    iface = new Interface();
	
//	    Font MYFONT = PlayN.graphics().createFont("Sans", Font.Style.BOLD_ITALIC, 50);
		Stylesheet sheet = SimpleStyles.newSheet();
		
	    root = iface.createRoot(AxisLayout.vertical().gap(5), sheet);
	      
	    layer.add(root.layer);

	    root.add(buttons = new Group(AxisLayout.horizontal().offStretch()));
	    Button btnOne = new Button("DoNothing");
//	    btnQuit.addStyles(Style.FONT.is(MYFONT));
	    Button btnTwo = new Button("SayNothing");
//	    btnDraw.addStyles(Style.FONT.is(MYFONT));
	    buttons.add(btnOne);
	    buttons.add(btnTwo);
	    
	    packRoot();
	}
	
	public void packRoot()
	{
		root.pack(400, 100) ;
		root.addStyles(Style.VALIGN.bottom);
	}
	
	public void addToScene()
	{
		graphics().rootLayer().add(layer);
	}
	
	public Interface getIface()
	{
		return iface ;
	}

	
	public void addButton(Button button) {			
		buttons.add(button);
		packRoot();		
	}

	public void removeButton(Button button) {
		buttons.remove(button);
		packRoot();
		
	}
}
