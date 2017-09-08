package test.sowell.copframe.jedica;

import java.util.List;




import redis.clients.jedis.Jedis;
import cn.sowell.copframe.jedica.CanalHandleException;
import cn.sowell.copframe.jedica.RowChangeEntriesHandler;

import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.Header;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;

public class CanalRedisDataHandler extends RowChangeEntriesHandler{

	@Override
	public void handle(Entry entry, RowChange rowChage)
			throws CanalHandleException {
		Header header = entry.getHeader();
		String schemaName = header.getSchemaName();
		String tableName = header.getTableName();
		
		if(checkUpdatedTable(schemaName, tableName)){
			//TODO: 从数据库加载对象
			Object data;
			
			
			
		}
		
		List<RowData> rdList = rowChage.getRowDatasList();
		for (RowData rowData : rdList) {
			rowData.getAfterColumnsList().forEach(c->{
				c.hasIsKey();
			});
		}
		
	}

	private boolean checkUpdatedTable(String schemaName, String tableName) {
		// TODO Auto-generated method stub
		return false;
	}

}
