package cn.sowell.copframe.jedica;

import java.util.function.Consumer;
/**
 * 
 * <p>Title: Jedica</p>
 * <p>Description: </p><p>
 * 
 * </p>
 * @author Copperfield Zhang
 * @date 2017年9月7日 下午4:40:00
 */
public interface Jedica {
	/**
	 * 根据key获得Redis中保存的对应对象
	 * @param key
	 * @param clazz
	 * @return
	 */
	<T> T get(String key, Class<T> clazz);
	/**
	 * 根据key设置Redis中的对象
	 * @param key
	 * @param value
	 * @return
	 */
	<T> Jedica put(String key, T value);
	/**
	 * 根据key获得Redis中的对象，处理后更新
	 * @param key
	 * @param consumer
	 * @return
	 */
	<T> Jedica update(String key, Consumer<T> consumer);
	
	/**
	 * 移除Redis中对应的对象
	 * @param key
	 * @return
	 */
	Jedica remove(String key);
}
