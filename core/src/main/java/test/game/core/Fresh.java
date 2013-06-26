package test.game.core;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.ImmediateLayer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import playn.core.util.Clock;
import playn.core.Surface;

public class Fresh extends Game.Default implements Listener 
{

	private final static int UPDATE_RATE = 50;
	private final Clock.Source clock = new Clock.Source(UPDATE_RATE);
	public Menu menu;

	public Fresh() {
		super(UPDATE_RATE); // call update every 50ms
	}

	@Override
	public void init() 
	{
		// create and add background image layer
		Image bgImage = assets().getImage("images/bg.png");
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);
		graphics().rootLayer().add(bgLayer);

		final CanvasShader canvasShader = new CanvasShader(graphics().ctx());
		graphics().rootLayer().add(
				graphics().createImmediateLayer(new ImmediateLayer.Renderer(){
					@Override
					public void render(Surface surface) {
						canvasShader.prepareColor(0) ;
					}})) ;

		menu = new Menu();
		//this implements the listener which we're not using
		menu.createMenu(this);
		//add menu above everything.
		menu.addToScene() ;
	}

	@Override
	public void update(int delta) 
	{
		clock.update(delta) ;
		menu.getIface().update(delta);
	}

	@Override
	public void paint(float alpha) {
		clock.paint(alpha) ;
		menu.getIface().paint(clock);		
		// the background automatically paints itself
	}

	@Override
	public void onPointerStart(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPointerEnd(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPointerDrag(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPointerCancel(Event event) {
		// TODO Auto-generated method stub

	}
}
