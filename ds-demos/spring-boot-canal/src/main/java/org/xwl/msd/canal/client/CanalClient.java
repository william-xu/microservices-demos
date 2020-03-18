package org.xwl.msd.canal.client;
import java.net.InetSocketAddress;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;

public class CanalClient {
    public static void main(String args[]) {
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("127.0.0.1",
                11111), "example", "canal", "canal");
        int batchSize = 1000;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            while (true) {
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    printEntry(message.getEntries());
                }
                connector.ack(batchId); // 提交确认
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }
        } finally {
            connector.disconnect();
        }
    }

    private static void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
            RowChange rowChage = null;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }
            EventType eventType = rowChage.getEventType();
            System.out.println(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            for (RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    redisDelete(entry.getHeader().getTableName(), rowData.getBeforeColumnsList());
                } else if (eventType == EventType.INSERT) {
                    redisInsert(entry.getHeader().getTableName(), rowData.getAfterColumnsList());
                } else {
                    System.out.println("-------> before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("-------> after");
                    redisUpdate(entry.getHeader().getTableName(), rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn(List<Column> columns) {
        for (Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }

    private static void redisInsert(String tableName, List<Column> columns) {
        JSONObject json = new JSONObject();
        for (Column column : columns) {
            json.put(column.getName(), column.getValue());
        }
        if (columns.size() > 0) {
            System.out.println(columns.get(0).getValue() + "," + json.toJSONString());
            RedisUtil.stringSet(tableName, columns.get(0).getValue(), json.toJSONString());
        }
    }

    private static void redisUpdate(String tableName, List<Column> columns) {
        JSONObject json = new JSONObject();
        for (Column column : columns) {
            json.put(column.getName(), column.getValue());
        }
        if (columns.size() > 0) {
            System.out.println(columns.get(0).getValue() + "," + json.toJSONString());
            RedisUtil.stringSet(tableName, columns.get(0).getValue(), json.toJSONString());
        }
    }

    private static void redisDelete(String tableName, List<Column> columns) {
        JSONObject json = new JSONObject();
        for (Column column : columns) {
        	json.put(column.getName(), column.getValue());
        }
        if (columns.size() > 0) {
            System.out.println(columns.get(0).getValue());
            RedisUtil.delKey(tableName, columns.get(0).getValue());
        }
    }
}  
