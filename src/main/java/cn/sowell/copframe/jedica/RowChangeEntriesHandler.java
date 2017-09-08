package cn.sowell.copframe.jedica;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;

public abstract class RowChangeEntriesHandler implements CanalEntryHandler{

	Logger logger = Logger.getLogger(RowChangeEntriesHandler.class);
	
	@Override
	public void handle(long messageId, List<Entry> entries)
			throws CanalHandleException {
		for (Entry entry : entries) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
            try {
            	RowChange rowChage = RowChange.parseFrom(entry.getStoreValue());
            	try {
					handle(entry, rowChage);
				}catch(CanalHandleException handlerException){
					throw handlerException;
				} catch (Exception e) {
					logger.error("执行方式时发生异常，但将继续处理下一个变化", e);
				}
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }
        }
	}

	public abstract void handle(Entry entry,RowChange rowChage) throws CanalHandleException;


}
