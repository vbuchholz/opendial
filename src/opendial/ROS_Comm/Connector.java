package opendial.ROS_Comm;

import org.ros.RosCore;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

/**
 * Created by vbuchholz on 25.05.17.
 */
public class Connector {

    public void connect() {
        RosCore rosCore = RosCore.newPublic(11311);
        rosCore.start();

        try {
            rosCore.awaitStart();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        NodeMainExecutor nodeExecutor = DefaultNodeMainExecutor.newDefault();

        NodeConfiguration talkerConfig = NodeConfiguration.newPrivate();
        talkerConfig.setMasterUri(rosCore.getUri());
        talkerConfig.setNodeName("Talker");
        Talker talker = new Talker();
        nodeExecutor.execute(talker, talkerConfig);

        NodeConfiguration listenerConfig = NodeConfiguration.newPrivate();
        listenerConfig.setMasterUri(rosCore.getUri());
        listenerConfig.setNodeName("Listener");
        Listener listener = new Listener();
        //nodeExecutor.execute(listener, listenerConfig);
    }
}
