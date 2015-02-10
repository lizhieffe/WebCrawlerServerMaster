package interfaces;

import ServerNode.SlaveNode;

public interface ISlaveManager {
	public boolean addSlave(String ip, int port);
	public boolean removeSlave(SlaveNode slave);
	public boolean removeSlave(String ip, int port);
	public boolean containsSlave(String ip, int port);
	public SlaveNode getSlave(String ip, int port);
	public SlaveNode getSlave(int index);
	public int getNum();
}
