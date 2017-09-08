package cn.sowell.copframe.jedica;

import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry.Entry;

public interface CanalEntryHandler {

	void handle(long messageId, List<Entry> entries) throws CanalHandleException;

}
