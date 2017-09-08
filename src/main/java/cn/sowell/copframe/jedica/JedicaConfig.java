package cn.sowell.copframe.jedica;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Properties;

import org.springframework.core.io.Resource;

import cn.sowell.copframe.utils.FormatUtils;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;

public class JedicaConfig {
	private String canalAddress;
	private int canalPort = 11111;
	private String canalDestination;
	private int canalBatchSize = 1000;
	private int canalBatchInterval = 1000;
	private String canalFilter = ".*\\..*";
	private boolean canalShowTicktock = true;
	private long canalTimeout = 0;
	
	
	private String redisHost;
	private int redisPort = 6379;
	private String redisAuth;
	private int redisPoolMaxActive = 500;
	private int redisPoolMaxIdle = 5;
	private long redisPoolMaxWait = 10000; 
	
	
	
	private CanalConnector canalConnector;
	private InetSocketAddress canalSocketAddress;
	private Properties properties;
	
	
	public JedicaConfig(Resource resource) throws IOException {
		Properties properties = new Properties();
		properties.load(resource.getInputStream());
		this.loadPropertie(properties);
	}
	
	public JedicaConfig(Properties properties) {
		this.loadPropertie(properties);
	}
	
	private void loadPropertie(Properties properties){
		this.properties = properties;
		
		
		this.canalAddress = properties.getProperty("canal.adress");
		this.canalPort = nnVal("canal.port", canalPort);
		this.canalSocketAddress = new InetSocketAddress(canalAddress, canalPort);
		this.canalDestination = properties.getProperty("canal.destination");
		this.canalConnector = CanalConnectors.newSingleConnector(canalSocketAddress, canalDestination, "", "");
		this.canalBatchSize = nnVal("canal.batchSize", canalBatchSize);
		this.canalBatchInterval = nnVal("cacanal.batchIntervalna", canalBatchInterval);
		this.canalFilter = nnVal("canal.filter", ".*\\..*");
		this.canalShowTicktock = nnVal("canal.showTicktock", canalShowTicktock);
		this.canalTimeout = nnVal("canal.timeout", canalTimeout) * 60000;
		
		this.redisHost = properties.getProperty("redis.host");
		this.redisPort = nnVal("redis.port", redisPort);
		this.redisAuth = properties.getProperty("redis.auth");
		this.redisPoolMaxActive = nnVal("redis.pool.maxActive", redisPoolMaxActive);
		this.redisPoolMaxIdle = nnVal("redis.pool.maxIdle", redisPoolMaxIdle);
		this.redisPoolMaxWait = nnVal("redis.pool.maxWait", redisPoolMaxWait);
		
		
	}
	
	@SuppressWarnings("unchecked")
	private <T> T nnVal(String key, T def) {
		String val = properties.getProperty(key);
		if(val == null){
			return def;
		}else{
			return (T) FormatUtils.toClass(def.getClass(), val);
		}
	}

	public SocketAddress getCanalSocketAddress(){
		return canalSocketAddress;
	}
	
	public CanalConnector getCanalConnector() {
		return canalConnector;
	}

	public String getCanalAddress() {
		return canalAddress;
	}

	public int getCanalPort() {
		return canalPort;
	}

	public String getCanalDestination() {
		return canalDestination;
	}

	public int getCanalBatchSize() {
		return canalBatchSize;
	}

	public int getCanalBatchInterval() {
		return canalBatchInterval;
	}

	public String getCanalFilter() {
		return canalFilter;
	}

	public boolean isCanalShowTicktock() {
		return canalShowTicktock;
	}

	public long getCanalTimeout() {
		return canalTimeout;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public String getRedisAuth() {
		return redisAuth;
	}

	public int getRedisPoolMaxActive() {
		return redisPoolMaxActive;
	}

	public int getRedisPoolMaxIdle() {
		return redisPoolMaxIdle;
	}

	public long getRedisPoolMaxWait() {
		return redisPoolMaxWait;
	}

}
