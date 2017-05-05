package com.wyd.BigData.RDD;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.flume.SparkFlumeEvent;

public class ItemLogRDD implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5320691333307682669L;
	static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
	private static final Pattern SPACE = Pattern.compile("\t");

	@SuppressWarnings("serial")
	public static void call(JavaRDD<SparkFlumeEvent> rdd, SparkSession spark) {
		ItemLogRDD ldd = new ItemLogRDD();
		JavaRDD<SparkFlumeEvent> filterRdd = ldd.filter(rdd);
		JavaRDD<Row> rowRDD = filterRdd.map(new Function<SparkFlumeEvent, Row>() {
			@Override
			public Row call(SparkFlumeEvent flume) throws Exception {
				String line = new String(flume.event().getBody().array());
				String[] parts = SPACE.split(line);
				return RowFactory.create(Long.valueOf(parts[1]), Integer.valueOf(parts[2]), Integer.valueOf(parts[3]),
						Integer.valueOf(parts[4]), Integer.valueOf(parts[5]), Integer.valueOf(parts[6]),
						Integer.valueOf(parts[7]), Integer.valueOf(parts[8]), Integer.valueOf(parts[9]), parts[10],
						parts[11], Integer.valueOf(parts[12]), Integer.valueOf(parts[13]), Integer.valueOf(parts[14]));
			}
		});
		StructType schema = ldd.createStruct();
		// 创建一个DataFrame
		Dataset<Row> dataFrame = spark.createDataFrame(rowRDD, schema);
		if (dataFrame.count() > 0) {
			dataFrame.createOrReplaceTempView("tmp_itemLog");
			String tableName = "tab_itemLog";
			ldd.createTable(spark, tableName);
			spark.sql("INSERT INTO " + tableName + " select * from tmp_itemLog");
		}
	}

	private void createTable(SparkSession spark, String tableName) {
		spark.sql("CREATE TABLE IF NOT EXISTS " + tableName
				+ " (time bigint,playerId int,itemId int , changeOrigin int, changeType int, changeNum int, mainType int, subType int, useType int,remark string,name string, accountId int, beforeNum int, afterNum int)");
	}

	@SuppressWarnings("serial")
	public JavaRDD<SparkFlumeEvent> filter(JavaRDD<SparkFlumeEvent> rdd) {
		return rdd.filter(new Function<SparkFlumeEvent, Boolean>() {
			@Override
			public Boolean call(SparkFlumeEvent flume) throws Exception {
				String line = new String(flume.event().getBody().array());
				String[] parts = SPACE.split(line);
				return (parts.length >= 2 && "19".equals(parts[0]));
			}
		});
	}

	private StructType createStruct() {
		List<StructField> structFields = new ArrayList<StructField>();
		structFields.add(DataTypes.createStructField("time", DataTypes.LongType, true));
		structFields.add(DataTypes.createStructField("playerId", DataTypes.IntegerType, true));
		structFields.add(DataTypes.createStructField("itemId", DataTypes.IntegerType, true));
		structFields.add(DataTypes.createStructField("changeOrigin", DataTypes.IntegerType, true));
		structFields.add(DataTypes.createStructField("changeType", DataTypes.IntegerType, true));
		structFields.add(DataTypes.createStructField("changeNum", DataTypes.IntegerType, true));
		structFields.add(DataTypes.createStructField("mainType", DataTypes.IntegerType, true));
		structFields.add(DataTypes.createStructField("subType", DataTypes.IntegerType, true));
		structFields.add(DataTypes.createStructField("useType", DataTypes.IntegerType, true));
		structFields.add(DataTypes.createStructField("remark", DataTypes.StringType, true));
		structFields.add(DataTypes.createStructField("name", DataTypes.StringType, true));
		structFields.add(DataTypes.createStructField("accountId", DataTypes.IntegerType, true));
		structFields.add(DataTypes.createStructField("beforeNum", DataTypes.IntegerType, true));
		structFields.add(DataTypes.createStructField("afterNum", DataTypes.IntegerType, true));
		return DataTypes.createStructType(structFields);
	}
}
