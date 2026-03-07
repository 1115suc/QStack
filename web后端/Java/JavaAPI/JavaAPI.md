# Java 基础 & 常用 API 学习笔记

---

## 一、String 类

### 1.1 String 介绍

```
String 类代表字符串，位于 java.lang 包，无需手动导包。
```

**核心特点：**

- **所有字符串字面量都是 String 对象**：凡是带双引号的，如 `"abc"`，都是 String 的对象。
- **字符串是常量，值创建后不可更改**：`s += "world"` 会产生新对象，原对象不变。
- **String 对象不可变，因此可以共享**：两个字面量相同的字符串变量指向常量池中同一对象。

> 📌 **内存图示说明（对应 img/string_pool.png）**
>
> - 字面量创建的字符串存储在**字符串常量池**（堆内存中）
> - `new String(...)` 创建的对象在堆内存的普通区域

---

### 1.2 String 底层实现原理

| JDK 版本  | 底层数组                           | 说明           |
| --------- | ---------------------------------- | -------------- |
| JDK 8     | `private final char[] value`       | 每个字符占2字节 |
| JDK 9 及以后 | `private final byte[] value`    | 每个字符占1字节，更省内存 |

> 字符串定义完成后，底层 `byte[]` 数组地址被 `final` 修饰锁死，无法改变，这是字符串"不可变"的根本原因。

---

### 1.3 String 的创建方式

```java
// 1. 无参构造（创建空字符串）
String s1 = new String();

// 2. 根据字符串创建
String s2 = new String("abc");

// 3. 根据 char 数组创建
char[] chars = {'a', 'b', 'c'};
String s3 = new String(chars);

// 4. 根据 char 数组的一部分创建（offset: 起始索引, count: 个数）
String s4 = new String(chars, 1, 2); // 结果: "bc"

// 5. 根据 byte 数组创建（默认平台字符集，IDEA 环境为 UTF-8）
byte[] bytes = {97, 98, 99};
String s5 = new String(bytes); // 结果: "abc"

// 6. 根据 byte 数组的一部分创建
String s6 = new String(bytes, 0, 2); // 结果: "ab"

// 7. 简化形式（推荐）★
String s7 = "abc";
```

> ⚠️ 注意：中文字符在 UTF-8 编码下占 3 个字节，在 GBK 下占 2 个字节，字节值通常为负数。

---

### 1.4 String 面试题 ★

```java
String s1 = "abc";
String s2 = "abc";
String s3 = new String("abc");

System.out.println(s1 == s2);  // true  → 同指向常量池
System.out.println(s1 == s3);  // false → s3 在堆内存中
System.out.println(s2 == s3);  // false
```

**经典问题：**

- `String s = new String("abc")` 共有几个对象？**2个**（一个是 new 本身，一个是 `"abc"` 字面量）
- 共创建了几个对象？**1或2个**，若 `"abc"` 已在常量池中，则只创建 1 个（堆上的 new 对象）

---

### 1.5 字符串拼接常见问题 ★

```java
String s1 = "hello";
String s2 = "world";
String s3 = "helloworld";
String s4 = "hello" + "world";  // 编译期优化，直接生成 "helloworld"
String s5 = s1 + "world";       // 含变量，运行期生成新对象
String s6 = s1 + s2;            // 含变量，运行期生成新对象

System.out.println(s3 == s4);  // true  → 编译期合并常量
System.out.println(s3 == s5);  // false → 含变量，产生新对象
System.out.println(s3 == s6);  // false → 含变量，产生新对象
```

**结论：**
- 等号右边全是**字符串字面量拼接** → 不会产生新对象（编译期优化）
- 等号右边有**变量**参与拼接 → 会产生新的字符串对象

---

### 1.6 String 常用方法

#### 判断方法

```java
boolean equals(String s)            // 比较内容（区分大小写）
boolean equalsIgnoreCase(String s)  // 比较内容（忽略大小写）
```

> ⚠️ **防止空指针异常的正确写法：**
> ```java
> // 错误写法（s 可能为 null）
> s.equals("abc");
>
> // 正确写法1：用字面量去点
> "abc".equals(s);
>
> // 正确写法2：使用 Objects 工具类（推荐）★
> Objects.equals(s, "abc");  // 内部已处理 null 情况
> ```

---

#### 获取方法

| 方法签名 | 说明 |
| --- | --- |
| `int length()` | 获取字符串长度 |
| `String concat(String s)` | 字符串拼接，返回新串 |
| `char charAt(int index)` | 根据索引获取字符 |
| `int indexOf(String s)` | 获取子串第一次出现的索引，不存在返回 `-1` |
| `String substring(int beginIndex)` | 从指定索引截取到末尾 |
| `String substring(int beginIndex, int endIndex)` | 截取子串，**含头不含尾** |

```java
String s = "abcdefg";
System.out.println(s.length());         // 7
System.out.println(s.concat("haha"));   // abcdefghaha
System.out.println(s.charAt(0));        // a
System.out.println(s.indexOf("c"));     // 2
System.out.println(s.substring(3));     // defg
System.out.println(s.substring(1, 4));  // bcd（含头不含尾）
```

> ✏️ **补充说明：** 原文档中写作 `subString`，实际 Java API 方法名为 `substring`（全小写），使用时注意。

---

#### 转换方法

```java
char[] toCharArray()                    // 字符串 → char 数组
byte[] getBytes()                       // 字符串 → byte 数组（平台默认编码）
byte[] getBytes(String charsetName)     // 字符串 → byte 数组（指定编码）
String replace(CharSequence c1, CharSequence c2)  // 替换字符/子串
```

```java
String s = "abcdefg";
char[] chars = s.toCharArray();         // ['a','b','c','d','e','f','g']
byte[] bytes = s.getBytes();            // [97,98,99,100,101,102,103]
System.out.println(s.replace("a","z")); // zbcdefg

// 按 GBK 编码转换（一个中文占 2 字节）
byte[] gbkBytes = "你好".getBytes("GBK");
```

---

#### 其他常用方法

```java
boolean contains(String s)   // 判断是否包含指定子串
boolean startsWith(String s) // 判断是否以指定字符串开头
boolean endsWith(String s)   // 判断是否以指定字符串结尾
String toLowerCase()         // 转小写
String toUpperCase()         // 转大写
String trim()                // 去掉两端空格（不去除中间空格）
String[] split(String regex) // 按正则规则分割字符串
```

> ⚠️ **split 注意事项：**
> - `.` 在正则中代表"任意字符"，分割时需转义：`split("\\.")`
> - `|`、`*`、`+` 等正则特殊字符同理需转义

```java
"abc,txt".split(",");     // ["abc", "txt"] ✔
"haha.hehe".split(".");   // 错误！. 会匹配任意字符
"haha.hehe".split("\\.");  // ["haha", "hehe"] ✔
```

---

### 1.7 遍历字符串

```java
String s = "abcdefg";
for (int i = 0; i < s.length(); i++) {
    System.out.println(s.charAt(i));
}
```

---

## 二、StringBuilder 类

### 2.1 StringBuilder 介绍

**为什么不用 String 直接拼接？**

- String 每拼接一次，就在堆内存产生一个新对象 → **内存浪费，效率低**

**StringBuilder 的优势：**

- 底层维护一个**未被 final 修饰的 `byte[]` 缓冲区**，默认长度 **16**
- 拼接过程中数据都保存在缓冲区中，**不随意产生新对象**
- 缓冲区不够时**自动扩容**：新长度 = 旧长度 × 2 + 2（若实际数据更多则以实际为准）

---

### 2.2 StringBuilder 使用

**构造方法：**

```java
StringBuilder sb1 = new StringBuilder();        // 空缓冲区
StringBuilder sb2 = new StringBuilder("abc");   // 初始内容
```

**常用方法：**

```java
StringBuilder append(任意类型)  // 追加内容，返回 this（支持链式调用）
StringBuilder reverse()        // 翻转内容，返回 this
int length()                   // 获取长度
String toString()              // 转为 String
```

```java
StringBuilder sb = new StringBuilder();
// 链式调用
sb.append("张无忌").append("赵敏").append("周芷若");
System.out.println(sb);  // 张无忌赵敏周芷若

sb.reverse();
System.out.println(sb);  // 若芷周敏赵忌无张

String result = sb.toString();  // 转为 String，后续可调用 String 的方法
```

**回文判断练习：**

```java
Scanner sc = new Scanner(System.in);
String data = sc.next();
String reversed = new StringBuilder(data).reverse().toString();
System.out.println(data.equals(reversed) ? "是回文" : "不是回文");
```

---

### 2.3 String / StringBuilder / StringBuffer 对比 ★

| 类 | 可变性 | 线程安全 | 拼接效率 |
| --- | --- | --- | --- |
| `String` | 不可变 | 安全（不可变对象天然安全） | 低（每次产生新对象）|
| `StringBuilder` | 可变 | **不安全** | **最高** |
| `StringBuffer` | 可变 | **安全**（方法加 synchronized） | 低于 StringBuilder |

**效率排序：** `StringBuilder` > `StringBuffer` > `String`

> 📌 **使用建议：**
> - 单线程字符串拼接 → 优先使用 `StringBuilder`
> - 多线程共享字符串拼接 → 使用 `StringBuffer`

---

## 三、Math 类

### 3.1 Math 类介绍

```
数学工具类，构造私有，所有方法均为 static，直接用类名调用。
```

### 3.2 常用方法

```java
Math.abs(-10)       // 10     绝对值
Math.ceil(3.2)      // 4.0    向上取整
Math.floor(3.9)     // 3.0    向下取整
Math.round(3.5)     // 4      四舍五入（返回 long）
Math.round(-2.5)    // -2     注意负数四舍五入规则
Math.max(10, 20)    // 20     较大值
Math.min(10, 20)    // 10     较小值
Math.pow(2, 10)     // 1024.0 幂运算（补充）
Math.sqrt(16)       // 4.0    平方根（补充）
Math.random()       // [0.0, 1.0) 随机数（补充）
Math.PI             // 3.14159...（补充，圆周率常量）
```

> ✏️ **补充：** 原文档未提及 `pow`、`sqrt`、`random`、`Math.PI`，这些在实际开发中同样常用。

---

## 四、BigInteger 类

### 4.1 BigInteger 介绍

```
处理超大整数（超过 long 范围）的类，位于 java.math 包。
理论上限：42亿的21亿次方，内存有限时可视为无上限。
```

**构造：**

```java
BigInteger b = new BigInteger("121212121212121212121212121212");
// 参数必须是数字字符串形式
```

### 4.2 常用方法

```java
BigInteger add(BigInteger val)       // 加法 this + val
BigInteger subtract(BigInteger val)  // 减法 this - val
BigInteger multiply(BigInteger val)  // 乘法 this * val
BigInteger divide(BigInteger val)    // 整除 this / val（截断小数）
BigInteger mod(BigInteger val)       // 取模（补充）
int compareTo(BigInteger val)        // 比较大小，返回 -1/0/1（补充）

// 转换回基本类型
int intValue()
long longValue()
double doubleValue()  // 补充
```

```java
BigInteger b1 = new BigInteger("999999999999999999999999");
BigInteger b2 = new BigInteger("1");
System.out.println(b1.add(b2)); // 1000000000000000000000000
```

---

## 五、BigDecimal 类

### 5.1 BigDecimal 介绍

**问题背景：** `float` / `double` 做运算会出现精度丢失：

```java
float a = 3.55F, b = 2.12F;
System.out.println(a - b); // 1.4300001（精度丢失！）
```

**作用：** 解决浮点数运算精度丢失问题，用于**金融/财务**等高精度场景。

### 5.2 构造与创建

```java
// 方式1：字符串构造（推荐）
BigDecimal b1 = new BigDecimal("3.55");

// 方式2：静态方法（可传 double，内部会转换，比直接 new BigDecimal(double) 更安全）
BigDecimal b2 = BigDecimal.valueOf(2.12);
```

> ⚠️ **不推荐** `new BigDecimal(3.55)` 直接传 double，因为 double 本身就有精度问题。

### 5.3 常用方法

```java
BigDecimal add(BigDecimal val)       // 加
BigDecimal subtract(BigDecimal val)  // 减
BigDecimal multiply(BigDecimal val)  // 乘
BigDecimal divide(BigDecimal val)    // 除（除不尽会抛 ArithmeticException）

// 除法（指定保留位数和取舍方式，推荐使用 RoundingMode 枚举）★
BigDecimal divide(BigDecimal divisor, int scale, RoundingMode roundingMode)

// 转换回基本类型
double doubleValue()
int intValue()
```

**RoundingMode 取舍方式：**

| 枚举值 | 说明 |
| --- | --- |
| `RoundingMode.UP` | 向上加1（远离零方向） |
| `RoundingMode.DOWN` | 直接截断 |
| `RoundingMode.HALF_UP` | 四舍五入（常用） |
| `RoundingMode.HALF_DOWN` | 五舍六入（补充） |

```java
BigDecimal b1 = new BigDecimal("3.55");
BigDecimal b2 = new BigDecimal("2.12");

// 保留2位小数，四舍五入
BigDecimal result = b1.divide(b2, 2, RoundingMode.HALF_UP);
System.out.println(result); // 1.67

double d = result.doubleValue();
```

> ✏️ **补充说明：** 原文档使用的 `BigDecimal.ROUND_UP`（整型常量）方式已在 JDK9 标记为 **@Deprecated**，推荐使用 `RoundingMode` 枚举版本。

---

## 六、Date 类

### 6.1 Date 介绍

```
表示特定时间瞬间，精确到毫秒，位于 java.util 包。
时间原点：1970年1月1日 00:00:00（格林威治标准时间，UTC+0）
北京时间（UTC+8）= 时间原点 + 8小时
```

### 6.2 构造方法

```java
Date d1 = new Date();        // 获取当前系统时间
Date d2 = new Date(1000L);   // 从时间原点起第 1000 毫秒（即 1970-01-01 00:00:01）
```

### 6.3 常用方法

```java
void setTime(long time)  // 设置时间（传入毫秒值）
long getTime()           // 获取时间（返回毫秒值）
```

```java
Date date = new Date();
System.out.println(date.getTime());  // 输出当前时间的毫秒值
date.setTime(0L);
System.out.println(date);  // Thu Jan 01 08:00:00 CST 1970（北京时间+8h）
```

---

## 七、Calendar 类

### 7.1 Calendar 介绍

```
日历类，抽象类，不能直接 new，通过静态方法获取子类对象。
位于 java.util 包。
```

**获取对象：**

```java
Calendar calendar = Calendar.getInstance(); // 返回当前系统时区的日历对象（多态）
```

**⚠️ 月份注意（国内外差异）：**

| 国外（Calendar 常量） | 0 | 1 | 2 | ... | 11 |
| --- | --- | --- | --- | --- | --- |
| 对应国内月份 | 1月 | 2月 | 3月 | ... | 12月 |

### 7.2 常用方法

```java
int get(int field)              // 获取指定日历字段的值
void set(int field, int value)  // 设置指定日历字段的值
void add(int field, int amount) // 日历字段偏移（正数向后，负数向前）
Date getTime()                  // 将 Calendar 转为 Date 对象
void set(int year, int month, int date) // 一次性设置年月日（扩展）
```

**常用字段常量：**

```java
Calendar.YEAR         // 年
Calendar.MONTH        // 月（0-11）
Calendar.DATE         // 日
Calendar.DAY_OF_WEEK  // 星期（1=周日, 2=周一 ... 7=周六）
Calendar.HOUR_OF_DAY  // 24小时制的小时
Calendar.MINUTE       // 分
Calendar.SECOND       // 秒
```

```java
Calendar cal = Calendar.getInstance();
System.out.println(cal.get(Calendar.YEAR));   // 获取当前年份
cal.set(Calendar.YEAR, 2028);                 // 设置年份为 2028
cal.add(Calendar.YEAR, -1);                   // 年份减 1（→ 2027）
Date date = cal.getTime();                    // 转为 Date 对象
```

**实战：判断闰年**

```java
Calendar cal = Calendar.getInstance();
cal.set(year, 2, 1);         // 设为 3月1日（Month=2 即3月）
cal.add(Calendar.DATE, -1);  // 减1天 → 2月最后一天
int day = cal.get(Calendar.DATE);
System.out.println(day == 29 ? "闰年" : "平年");
```

---

## 八、SimpleDateFormat 类

### 8.1 介绍

```
日期格式化类，在 Date 与 String 之间互相转换，位于 java.text 包。
```

**构造：**

```java
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
```

**时间格式字母说明：**

| 字母 | 含义 | 示例 |
| --- | --- | --- |
| `y` | 年 | `yyyy` → 2024 |
| `M` | 月 | `MM` → 01~12 |
| `d` | 日 | `dd` → 01~31 |
| `H` | 时（24小时） | `HH` → 00~23 |
| `h` | 时（12小时） | `hh` → 01~12（补充）|
| `m` | 分 | `mm` → 00~59 |
| `s` | 秒 | `ss` → 00~59 |
| `S` | 毫秒 | `SSS` → 000~999（补充）|
| `E` | 星期 | 周一~周日（补充）|

### 8.2 常用方法

```java
String format(Date date)     // Date → String（格式化）
Date parse(String source)    // String → Date（解析，需处理 ParseException）
```

```java
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

// Date → String
String formatted = sdf.format(new Date());
System.out.println(formatted); // 2024-01-15 10:30:00

// String → Date
String timeStr = "2000-10-10 10:10:10";
Date date = sdf.parse(timeStr); // 注意抛出 ParseException
System.out.println(date);
```

---

## 九、JDK8 新日期类

> JDK8 对日期时间 API 进行了全面重构，新 API 位于 `java.time` 包，**线程安全、不可变、更易用**。

### 9.1 LocalDate（本地日期）

```java
// 获取对象
LocalDate today = LocalDate.now();                     // 当前日期
LocalDate ld = LocalDate.of(2000, 10, 10);             // 指定年月日

// get 系列（获取字段）
int year  = today.getYear();          // 年
int month = today.getMonthValue();    // 月（1-12，修正了 Calendar 的月份问题）
int day   = today.getDayOfMonth();    // 日

// with 系列（设置字段，返回新对象，不修改原对象）
LocalDate newDate = today.withYear(2000).withMonth(10).withDayOfMonth(10);

// plus/minus 系列（偏移）
LocalDate nextYear  = today.plusYears(1);
LocalDate lastMonth = today.minusMonths(1);
```

---

### 9.2 LocalDateTime（本地日期时间）

```java
LocalDateTime now = LocalDateTime.now();
LocalDateTime ldt = LocalDateTime.of(2000, 10, 10, 10, 10, 10);

// 同样支持 get / with / plus / minus 系列方法
System.out.println(ldt.getHour());    // 获取小时
System.out.println(ldt.getMinute());  // 获取分钟
```

---

### 9.3 Period（日期间隔）

```java
// 计算两个 LocalDate 之间相差的年/月/日
LocalDate d1 = LocalDate.of(2021, 11, 11);
LocalDate d2 = LocalDate.of(2022, 12, 12);
Period p = Period.between(d1, d2);

System.out.println(p.getYears());   // 1（相差的年）
System.out.println(p.getMonths());  // 1（相差的月）
System.out.println(p.getDays());    // 1（相差的日）
```

---

### 9.4 Duration（时间间隔）

```java
// 计算两个 LocalDateTime 之间精确时间差
LocalDateTime ldt1 = LocalDateTime.of(2021, 11, 11, 11, 11, 11);
LocalDateTime ldt2 = LocalDateTime.of(2022, 12, 12, 12, 12, 12);
Duration d = Duration.between(ldt1, ldt2);

System.out.println(d.toDays());    // 相差总天数
System.out.println(d.toHours());   // 相差总小时数
System.out.println(d.toMinutes()); // 相差总分钟数
System.out.println(d.toMillis());  // 相差总毫秒数
```

> 📌 **选择建议：**
> - 计算年/月/日差 → 用 **Period**
> - 计算时/分/秒/毫秒差 → 用 **Duration**

---

### 9.5 DateTimeFormatter（新日期格式化）

```java
// 获取格式化器
DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

// LocalDateTime → String（格式化）
String str = dtf.format(LocalDateTime.now());

// String → LocalDateTime（解析）
TemporalAccessor ta = dtf.parse("2000-10-10 10:10:10");
LocalDateTime ldt = LocalDateTime.from(ta);
System.out.println(ldt); // 2000-10-10T10:10:10
```

---

### 9.6 旧日期 API 与新日期 API 对比

| 特性 | 旧（Date / Calendar） | 新（LocalDate / LocalDateTime 等） |
| --- | --- | --- |
| 线程安全 | ❌ 不安全 | ✅ 安全（不可变对象）|
| 月份 | 0-11（坑）| 1-12（直观）|
| 可读性 | 较差 | 优秀 |
| 是否可变 | 可变 | 不可变 |
| 推荐程度 | 旧代码维护 | ✅ 新代码优先 |

---

## 十、System 类

```
系统相关工具类，构造私有，方法全为 static，直接用类名调用。
位于 java.lang 包，无需导包。
```

### 10.1 常用方法

```java
// 获取当前时间的毫秒值（常用于测量代码执行效率）
long t1 = System.currentTimeMillis();
// ... 执行代码 ...
long t2 = System.currentTimeMillis();
System.out.println("耗时：" + (t2 - t1) + "ms");

// 终止 JVM（status=0 正常退出，非0 异常退出）
System.exit(0);

// 数组复制
// src: 源数组, srcPos: 源起始索引
// dest: 目标数组, destPos: 目标起始索引
// length: 复制元素个数
System.arraycopy(src, srcPos, dest, destPos, length);
```

**arraycopy 示例：**

```java
int[] src  = {1, 2, 3, 4, 5};
int[] dest = new int[10];
System.arraycopy(src, 0, dest, 0, 5);
// dest: [1, 2, 3, 4, 5, 0, 0, 0, 0, 0]
```

---

## 十一、Arrays 类

```
数组工具类，构造私有，方法全为 static，直接用类名调用。
位于 java.util 包。
```

### 11.1 常用方法

| 方法 | 说明 |
| --- | --- |
| `static String toString(int[] a)` | 按格式打印数组元素 `[1, 2, 3]` |
| `static void sort(int[] a)` | 升序排序（基本类型） |
| `static <T> void sort(T[] a, Comparator<T> c)` | 按指定规则排序（补充） |
| `static int binarySearch(int[] a, int key)` | 二分查找（**前提：已升序排序**） |
| `static int[] copyOf(int[] original, int newLength)` | 数组扩容/截断 |
| `static int[] copyOfRange(int[] original, int from, int to)` | 范围复制（补充） |
| `static boolean equals(int[] a, int[] b)` | 比较两数组内容是否相同（补充）|

```java
int[] arr = {5, 3, 4, 6, 1};

System.out.println(Arrays.toString(arr));       // [5, 3, 4, 6, 1]
Arrays.sort(arr);
System.out.println(Arrays.toString(arr));       // [1, 3, 4, 5, 6]

int idx = Arrays.binarySearch(arr, 4);
System.out.println("索引: " + idx);            // 2

int[] newArr = Arrays.copyOf(arr, 10);
System.out.println(Arrays.toString(newArr));    // [1, 3, 4, 5, 6, 0, 0, 0, 0, 0]
```

> ⚠️ **binarySearch 注意：** 使用前**必须先排序**，否则结果不可预期。

---

## 十二、包装类

### 12.1 基本类型与包装类对应表

| 基本类型 | 包装类 | 说明 |
| --- | --- | --- |
| `byte` | `Byte` | |
| `short` | `Short` | |
| `int` | `Integer` | 最常用 ★ |
| `long` | `Long` | |
| `float` | `Float` | |
| `double` | `Double` | |
| `char` | `Character` | ⚠️ 原文档写作 `Charactor`，正确拼写为 **Character** |
| `boolean` | `Boolean` | |

**为什么需要包装类？**

- 某些场景只能接受引用类型（如集合类 `ArrayList<Integer>`）
- 包装类中提供了许多操作基本类型数据的工具方法
- JavaBean 中字段推荐使用包装类（默认值为 `null`，便于与数据库 NULL 值对应）

---

### 12.2 Integer 的使用 ★

**装箱（基本类型 → 包装类）：**

```java
// 方式1：构造（已过时，不推荐）
Integer i1 = new Integer(10);

// 方式2：valueOf（推荐）
Integer i2 = Integer.valueOf(10);
Integer i3 = Integer.valueOf("100");  // 字符串形式

// 方式3：自动装箱（JDK5+，推荐）
Integer i4 = 10;
```

**拆箱（包装类 → 基本类型）：**

```java
Integer i = Integer.valueOf(10);

// 方式1：intValue()
int a = i.intValue();

// 方式2：自动拆箱（JDK5+，推荐）
int b = i;
int c = i + 10;  // 自动拆箱后运算，结果自动装箱
```

---

### 12.3 Integer 缓存机制 ★（面试考点）

```java
Integer i1 = 100;
Integer i2 = 100;
System.out.println(i1 == i2);  // true

Integer i3 = 128;
Integer i4 = 128;
System.out.println(i3 == i4);  // false
```

**原理：** `Integer` 内部维护了一个**整数缓存池（IntegerCache）**，范围 **-128 ~ 127**。
- 在此范围内的整数，`valueOf()` 返回缓存中的同一对象 → `==` 为 `true`
- 超出范围，每次创建新对象 → `==` 为 `false`
- **结论：比较包装类对象的值，一定要用 `equals()`，不要用 `==`**

---

### 12.4 基本类型 ↔ String 互转

**基本类型 → String：**

```java
int i = 10;

// 方式1：拼接空字符串（简单，但性能略低）
String s1 = i + "";

// 方式2：String.valueOf()（推荐）
String s2 = String.valueOf(i);
```

**String → 基本类型（parseXXX 系列）：**

| 包装类 | 方法 | 说明 |
| --- | --- | --- |
| `Integer` | `static int parseInt(String s)` | String → int |
| `Long` | `static long parseLong(String s)` | String → long |
| `Double` | `static double parseDouble(String s)` | String → double |
| `Boolean` | `static boolean parseBoolean(String s)` | String → boolean |
| `Byte` | `static byte parseByte(String s)` | String → byte |
| `Short` | `static short parseShort(String s)` | String → short |
| `Float` | `static float parseFloat(String s)` | String → float |

```java
int num = Integer.parseInt("1111");
System.out.println(num + 1); // 1112（字符串转成了真正的整数）

double d = Double.parseDouble("3.14");
boolean b = Boolean.parseBoolean("true");
```

---

### 12.5 JavaBean 中使用包装类（规范）

```java
public class User {
    private Integer uid;       // 推荐使用包装类，默认值 null，便于与数据库 NULL 对应
    private String username;
    private String password;

    public User() {}

    public User(Integer uid, String username, String password) {
        this.uid = uid;
        this.username = username;
        this.password = password;
    }

    // getter / setter 省略
}
```

> 若 uid 为数据库主键自增，则 JavaBean 中 uid 可为 null，SQL 直接写 `INSERT INTO user VALUES(NULL, ?, ?)`，无需手动赋值。

---

## 知识总结

### 一、String 核心要点

1. **不可变性**：底层 `byte[]` 被 `final` 修饰，每次"修改"实际上都会创建新对象。
2. **常量池机制**：字面量创建的字符串存入常量池并复用；`new String()` 在堆中创建新对象。
3. **`==` vs `equals()`**：`==` 比较引用地址，`equals()` 比较内容，字符串比较内容必须用 `equals()`。
4. **防空指针**：优先用 `"常量".equals(变量)` 或 `Objects.equals(a, b)` 来避免空指针。
5. **方法名纠正**：`substring`（全小写），`startsWith`（非 `statsWith`）。

### 二、StringBuilder 核心要点

1. 单线程字符串频繁拼接首选 `StringBuilder`，多线程用 `StringBuffer`。
2. 底层缓冲区默认16位，**超出自动扩容为 2倍+2**。
3. `append()` 和 `reverse()` 都返回 `this`，支持**链式调用**。
4. 最终需要调用 `toString()` 转为 String 对象。

### 三、数值计算类

| 类 | 适用场景 | 关键方法 |
| --- | --- | --- |
| `Math` | 基础数学运算 | `abs/ceil/floor/round/max/min/pow/sqrt` |
| `BigInteger` | 超大整数 | `add/subtract/multiply/divide` |
| `BigDecimal` | 高精度小数/金融计算 | `add/subtract/multiply/divide(带 RoundingMode)` |

- `float`/`double` 精度不可靠，**涉及金钱务必用 `BigDecimal`**。
- `BigDecimal` 除法如能除尽才可用单参数 `divide()`，否则必须指定 `scale` 和 `RoundingMode`。

### 四、日期类

| 类 | 用途 | 备注 |
| --- | --- | --- |
| `Date` | 表示时间点 | `getTime()`/`setTime()` 操作毫秒值 |
| `Calendar` | 日历字段操作 | 月份从 0 开始，注意 +1 |
| `SimpleDateFormat` | Date/String 互转 | `format()` / `parse()` |
| `LocalDate` | JDK8 日期（年月日） | 不可变，线程安全，月份从 1 开始 |
| `LocalDateTime` | JDK8 日期时间 | 同上 |
| `Period` | 年/月/日间隔 | `between(LocalDate, LocalDate)` |
| `Duration` | 时/分/秒/毫秒间隔 | `between(LocalDateTime, LocalDateTime)` |
| `DateTimeFormatter` | JDK8 格式化 | `format()` / `parse()` |

- **新代码推荐使用 JDK8 新日期 API**，月份直观（1-12），线程安全，API 更合理。

### 五、工具类

| 类 | 用途 | 典型方法 |
| --- | --- | --- |
| `System` | 系统操作 | `currentTimeMillis()` / `arraycopy()` / `exit()` |
| `Arrays` | 数组操作 | `toString()` / `sort()` / `binarySearch()` / `copyOf()` |

- `Arrays.binarySearch()` 前提：**数组已升序排序**。
- `System.arraycopy()` 是 native 方法，性能高于手写循环复制。

### 六、包装类核心要点

1. **自动装箱/拆箱（JDK5+）**：基本类型和包装类之间自动转换，底层仍是 `valueOf()` / `intValue()`。
2. **Integer 缓存 -128~127**：范围内 `==` 为 true，超出范围 `==` 为 false。**任何时候比较包装类值都用 `equals()`**。
3. **String 与基本类型互转**：`String.valueOf(基本类型)` 或 `+""` 转 String；`Integer.parseInt()` 等 `parseXXX` 方法转基本类型。
4. **JavaBean 字段推荐使用包装类**：默认值 `null` 与数据库 NULL 语义一致。
5. `char` 对应的包装类是 **`Character`**（注意拼写，不是 `Charactor`）。

---

> 📝 **学习建议：**
> - String 方法要熟练记忆，是日常开发最高频使用的 API。
> - 日期类优先学习并使用 JDK8 的 `java.time` 包。
> - BigDecimal 是财务项目的必备知识点，除法的 `RoundingMode` 要重点掌握。
> - 包装类的缓存机制是面试高频考点，理解原理比死记答案更重要。