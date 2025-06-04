package mm;
import java.util.Arrays;

public class Memory {

	public Memory(int numFrames){
		this.numFrames = numFrames;
		this.frames = new Page[this.numFrames];
	}

	public void putPage(Page p, int index){
		frames[index] = p;
	}

	public Page removePage(Page p){
		Page ret;
		for(int i = 0; i < numFrames; i++){
			if(p.equals(frames[i])){
				ret = frames[i];
				frames[i] = null;
			}
		}
		return ret;
	}

	public Page[] getFrames(){
		return Arrays.copyOf(this.frames, numFrames);
	}

	public int getFrame(Page p){
		int frame;
		for(frame = 0; frame < numFrames; frame++){
			if(p.equals(frames[frame]))
				return frame;
		}
		return -1;
	}

	private int numFrames;
	private Page[] frames;
}
