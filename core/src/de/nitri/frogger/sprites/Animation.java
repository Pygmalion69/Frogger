package de.nitri.frogger.sprites;


import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
	
	final TextureRegion[] keyFrames;
	final float frameDuration;
	private List<AnimationEventListener> _listeners;
	
	
	//THE EVENT >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public class AnimationEvent extends EventObject {
		
		public AnimationEvent(Object source) {
              super(source);
        }
	}
	
	public interface AnimationEventListener {
	   public void onAnimationEnded(AnimationEvent e);
	}
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Animation (float frameDuration, TextureRegion... keyFrames) {
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
		_listeners = new ArrayList<AnimationEventListener>();
	}

	public TextureRegion getKeyFrame (float stateTime, boolean loop) {
		int frameNumber = (int)(stateTime / frameDuration);

		if (!loop) {
			frameNumber = Math.min(keyFrames.length - 1, frameNumber);
			if (frameNumber == keyFrames.length - 1) {
				sendEvent();
			}
		} else {
			frameNumber = frameNumber % keyFrames.length;
		}
		
		return keyFrames[frameNumber];
	}
	
	public void addEventListener(AnimationEventListener listener)  {
		_listeners.add(listener);
	}
	public void removeEventListener(AnimationEventListener listener)   {
		_listeners.remove(listener);
	}
	
	private void sendEvent() {
		AnimationEvent event = new AnimationEvent(this);
		int len = _listeners.size();
		for (int i = 0; i < len; i++) {
			_listeners.get(i).onAnimationEnded(event);
			
		}
	}
	 
}
