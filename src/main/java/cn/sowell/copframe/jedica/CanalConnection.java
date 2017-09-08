package cn.sowell.copframe.jedica;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
/**
 * 
 * <p>Title: CanalConnection</p>
 * <p>Description: </p><p>
 * 
 * </p>
 * @author Copperfield Zhang
 * @date 2017年9月8日 上午9:51:40
 */
public class CanalConnection implements InitializingBean{
	//配置文件
	Resource configFile;
	JedicaConfig config;
	
	private boolean interruptFlag = false;
	private boolean connecting = false;
	
	List<CanalEntryHandler> handlerList = new ArrayList<CanalEntryHandler>();
	
	Logger logger = Logger.getLogger(CanalConnection.class);
	private long lastAvailableBatch;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		config = new JedicaConfig(configFile);
		connect();
	}
	
	public void connect(){
		synchronized (this) {
			if(connecting){
				throw new RuntimeException("当前已经存在连接，不能重复连接");
			}
		}
		interruptFlag = false;
		CanalConnector connector = config.getCanalConnector();
		try {
			connector.connect();
			String filter = config.getCanalFilter();
			connector.subscribe(filter);
			connector.rollback();
			lastAvailableBatch = System.currentTimeMillis();
			while(true){
				if(interruptFlag || isInterrupt()){
					break;
				}
				Message message = connector.getWithoutAck(config.getCanalBatchSize());
				long batchId = message.getId();
				int size = message.getEntries().size();
				if(config.isCanalShowTicktock()){
					System.out.println("从Canal服务器[" + config.getCanalAddress() + "]获取数据，共" + size + "条");
				}
				if (batchId == -1 || size == 0) {
					try {
						Thread.sleep(config.getCanalBatchInterval());
					} catch (InterruptedException e) {
						logger.error("Canal连接线程休眠时发生错误，将断开连接", e);
						connector.rollback();
						break;
					}
				} else {
					lastAvailableBatch = System.currentTimeMillis();
					if(!handleEntry(message.getId(), message.getEntries())){
						//处理时发生错误
						logger.error("处理canal返回的消息时发生错误，获取消息的位置将会回滚");
						connector.rollback();
					}
			     }
			     connector.ack(batchId); // 提交确认
			}
		} catch (CanalClientException e) {
			logger.error("连接canal时发生错误", e);
		}finally{
			connector.disconnect();
		}
	}

	private boolean isInterrupt() {
		if(config.getCanalTimeout() > 0 && System.currentTimeMillis() - lastAvailableBatch > config.getCanalTimeout()){
			logger.debug("没有从canal服务器获得数据超过[" + config.getCanalTimeout() + "]毫秒，断开连接");
			return true;
		}
		return false;
	}
	
	private boolean handleEntry(long messageId, List<Entry> entries){
		boolean ack = true;
		for (CanalEntryHandler handler : handlerList) {
			try {
				handler.handle(messageId, entries);
			} catch (CanalHandleException e) {
				//设定不提交当前位置
				if(!e.isAck() && ack) ack = false;
				//不继续执行其他handler
				if(e.isBreak()) break;
			} catch (Exception e) {
				logger.error(handler.getClass() + "处理Canal消息时发生错误", e);
			}
		}
		return ack;
	}

	public List<CanalEntryHandler> getHandlerList() {
		return handlerList;
	}

	public void setHandlerList(List<CanalEntryHandler> handlerList) {
		this.handlerList = handlerList;
	}

	public Resource getConfigFile() {
		return configFile;
	}

	public void setConfigFile(Resource configFile) {
		this.configFile = configFile;
	}

	
}
