package solo.multiprotocol;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class Main extends PluginBase implements Listener{
	
	public HashSet<Integer> allow = new HashSet<Integer>();
	
	@SuppressWarnings({ "unchecked", "serial", "deprecation" })
	@Override
	public void onEnable(){
		this.getDataFolder().mkdirs();
		Config config = new Config(new File(this.getDataFolder(), "setting.yml"), Config.YAML, new LinkedHashMap<String, Object>(){{
			put("accept-protocols", new HashSet<Integer>(){{
				add(100);
				add(101);
			}});
		}});
		this.allow = (HashSet<Integer>) config.get("accept-protocols");
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onDataPacketReceive(DataPacketReceiveEvent event){
		if(event.getPacket().pid() == ProtocolInfo.LOGIN_PACKET){
			LoginPacket pk = (LoginPacket) event.getPacket();
			if(this.allow.contains(pk.protocol)){
				pk.protocol = ProtocolInfo.CURRENT_PROTOCOL;
			}
		}
	}
	
}