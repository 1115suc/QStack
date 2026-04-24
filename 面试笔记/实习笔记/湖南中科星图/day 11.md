- 今天使用 PostgreSQL 中的 PostGIS 写了 SQL 之后以后需要注意下面几点：
	- 注意表结构中字段的空间结构类型
		- 4547（单位：米    投影坐标   中国国家大地坐标系投影）
		- 4326（单位：度   经纬度   GPS使用的全球坐标系） 的类型
```sql
	 SELECT ST_SRID('字段名'), ST_GeometryType('字段名')  
		FROM '表名'  
		LIMIT 1; 
```
