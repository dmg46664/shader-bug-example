package test.game.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import test.game.core.Fresh;

public class FreshActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new Fresh());
  }
}
