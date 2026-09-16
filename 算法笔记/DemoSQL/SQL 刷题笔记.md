
### [1148. 文章浏览 I](https://leetcode.cn/problems/article-views-i/)
```sql
select distinct author_id as id
from Views
where author_id = viewer_id
order by author_id asc
```
- `distinct` 用于从查询结果中**去除重复的行**，返回唯一的值。

### [197. 上升的温度](https://leetcode.cn/problems/rising-temperature/)
```sql
select a.id
from Weather a
join Weather b
on a.recordDate = b.recordDate + interval 1 day
where a.temperature > b.temperature
```
- `interval 1 day` 是 MySQL 中的日期/时间运算语法，表示 **增加一天**
```sql
select a.id
from Weather a
left join Weather b 
-- 返回`a.recordDate - b.recordDate` 的天数差（整数）
on datediff(a.recordDate, b.recordDate) = 1 
where a.temperature > b.temperature 
```
- `DATEDIFF` 是 SQL 中用于**计算两个日期之间天数差**的函数

### [1661. 每台机器的进程平均运行时间](https://leetcode.cn/problems/average-time-of-process-per-machine/)
```sql
SELECT
    machine_id,
    ROUND(
        SUM(CASE activity_type
            WHEN 'end' THEN timestamp
            WHEN 'start' THEN -timestamp
            ELSE 0
        END)/count(distinct process_id) ,
        3
    ) AS processing_time
FROM Activity
GROUP BY machine_id
ORDER BY machine_id;
```
-  CASE 表达式
```sql
CASE 列名
    WHEN 值1 THEN 结果1
    WHEN 值2 THEN 结果2
    ...
    ELSE 默认结果
END
```

