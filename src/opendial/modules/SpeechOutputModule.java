package opendial.modules;

import opendial.DialogueState;
import opendial.DialogueSystem;
import opendial.ROS_Comm.Talker;
import opendial.bn.values.StringVal;
import opendial.bn.values.Value;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Module which lets ROS publish system speech.
 * Created by vbuchholz on 11.06.17.
 */
public class SpeechOutputModule implements Module {

    // logger
    final static Logger log = Logger.getLogger("OpenDial");

    // dialogue system
    private DialogueSystem system;

    // ROS interface
    private Talker rosTalker;

    // whether the engine is paused or not
    private boolean isPaused = true;

    /**
     * Creates a new module connecting the dialogue system to the ROS talker.
     *
     * @param system the dialogue system
     */
    public SpeechOutputModule(DialogueSystem system) {
        this.system = system;
        rosTalker = this.system.getROSconnector().getTalker();
    }

    /**
     * Starts the Module.
     */
    @Override
    public void start() {
        isPaused = false;
    }

    /**
     * If the updated variables contains the system output (and the system is not
     * paused), publishes the utterance.
     *
     * @param state the current dialogue state
     * @param updatedVars the set of updated variables
     */
    @Override
    public void trigger(DialogueState state, Collection<String> updatedVars) {
        String systemOutput = system.getSettings().systemOutput;
        if (updatedVars.contains(systemOutput) && state.hasChanceNode(systemOutput) && !isPaused) {
            Value utteranceVal = state.queryProb(systemOutput).toDiscrete().getBest();
            if (utteranceVal instanceof StringVal) {
                Thread t = new Thread(()-> rosTalker.publishSystemSpeech(utteranceVal.toString()));
                t.start();
            }
        }
    }

    /**
     * Pauses or unpauses the engine.
     *
     * @param toPause true if the engine should be paused, false if it should be
     *            unpaused.
     */
    @Override
    public void pause(boolean toPause) {
        isPaused = toPause;
    }

    /**
     * Returns true if the engine is currently active (not paused), and false
     * otherwise.
     *
     * @return true if active, false if inactive.
     */
    @Override
    public boolean isRunning() {
        return !isPaused;
    }
}
