# Java JDK 新特性学习笔记

---

## 一、Lambda 表达式

### 1.1 函数式编程思想

| 编程思想 | 核心理念 | 类比 |
|---|---|---|
| 面向对象 | **强调过程**：找到对象，让对象帮我们做事 | 去北京 → 关注怎么去（高铁/飞机/开车）|
| 函数式编程 | **强调结果**：只关心做了没有，不关心怎么做 | 去北京 → 只关注去了还是没去 |

> 函数式编程是 JDK 8 引入的新思想，Lambda 表达式是其核心实现方式。

---

### 1.2 Lambda 表达式语法

**基本格式：**

```
(参数列表) -> { 方法体 }
```

| 组成 | 说明 |
|---|---|
| `()` | 重写方法的参数列表 |
| `->` | 箭头，将参数传递到方法体 |
| `{}` | 重写方法的方法体 |

**示例（匿名内部类 → Lambda 演变）：**

```java
// 传统匿名内部类写法
new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("线程执行了");
    }
}).start();

// Lambda 表达式写法
new Thread(() -> System.out.println("线程执行了")).start();
```

---

### 1.3 Lambda 表达式使用前提 ★

```
使用 Lambda 表达式有且只有一个前提：
  必须以【函数式接口】作为方法参数传递。

函数式接口定义：有且只有一个抽象方法的接口。
检测注解：@FunctionalInterface（编译期校验，不是必须写但推荐加）
```

```java
@FunctionalInterface
public interface USB {
    void open(String s);  // 只有一个抽象方法 ✔
    // void close();      // 再加一个就不是函数式接口了 ✗
}
```

---

### 1.4 Lambda 表达式省略规则 ★★

**新手入门步骤（万能法）：**

1. 观察方法参数是否为函数式接口
2. 如果是，先以**匿名内部类**的形式传入实参
3. 从 `new 接口名` 开始到重写方法的方法名结束，全选删除（注意还要删掉右半个 `}`）
4. 在参数列表后面、方法体 `{` 前面加上 `->`

**四条省略规则：**

| 规则 | 说明 | 示例 |
|---|---|---|
| 规则1 | 重写方法的**参数类型**可以省略 | `(String s)` → `(s)` |
| 规则2 | 如果重写方法**只有一个参数**，小括号可以省略 | `(s)` → `s` |
| 规则3 | 如果方法体**只有一句话**，大括号和分号可以省略 | `{ return x; }` → 见规则4 |
| 规则4 | 方法体只有一句话且**带 return**，大括号、return、分号全部省略 | `{ return x+1; }` → `x+1` |

**完整演变示例：**

```java
// 步骤1：匿名内部类原始写法
Collections.sort(list, new Comparator<Person>() {
    @Override
    public int compare(Person o1, Person o2) {
        return o1.getAge() - o2.getAge();
    }
});

// 步骤2：基础 Lambda（删除接口相关代码，加箭头）
Collections.sort(list, (Person o1, Person o2) -> {
    return o1.getAge() - o2.getAge();
});

// 步骤3：省略参数类型（规则1）
Collections.sort(list, (o1, o2) -> {
    return o1.getAge() - o2.getAge();
});

// 步骤4：省略大括号、return、分号（规则3+4，最终形态）
Collections.sort(list, (o1, o2) -> o1.getAge() - o2.getAge());
```

---

## 二、函数式接口

> `java.util.function` 包下提供了大量内置函数式接口，是 Lambda 表达式和 Stream 流的基础。

### 2.1 四大核心函数式接口 ★

| 接口 | 包 | 核心方法 | 含义 | 记忆口诀 |
|---|---|---|---|---|
| `Supplier<T>` | `java.util.function` | `T get()` | **供给型**：产出数据，有出无入 | 供货商，只出货 |
| `Consumer<T>` | `java.util.function` | `void accept(T t)` | **消费型**：消费数据，有入无出 | 消费者，只花钱 |
| `Function<T,R>` | `java.util.function` | `R apply(T t)` | **函数型**：数据转换，有入有出 | 转换器，有进有出 |
| `Predicate<T>` | `java.util.function` | `boolean test(T t)` | **断言型**：判断数据，返回布尔 | 判断器，给结论 |

---

### 2.2 Supplier\<T\>（供给型接口）

```
作用：我们想要什么就提供什么（无参数，有返回值）
方法：T get()
```

```java
// 需求：用 Supplier 求 int 数组的最大值
public static void main(String[] args) {
    // 匿名内部类写法
    method(new Supplier<Integer>() {
        @Override
        public Integer get() {
            int[] arr = {4, 3, 6, 7, 5};
            Arrays.sort(arr);
            return arr[arr.length - 1]; // 返回最大值
        }
    });

    // Lambda 简化写法
    method(() -> {
        int[] arr = {4, 3, 6, 7, 5};
        Arrays.sort(arr);
        return arr[arr.length - 1];
    });
}

public static void method(Supplier<Integer> supplier) {
    Integer max = supplier.get();
    System.out.println("max = " + max); // 7
}
```


---

### 2.3 Consumer\<T\>（消费型接口）

```
作用：消费/操作一个数据（有参数，无返回值）
方法：void accept(T t)
```

```java
// 需求：消费一个字符串，打印其长度
public static void main(String[] args) {
    // 匿名内部类
    method(new Consumer<String>() {
        @Override
        public void accept(String s) {
            System.out.println(s.length());
        }
    }, "abcdefg");

    // Lambda 简化
    method(s -> System.out.println(s.length()), "abcdefg"); // 7
}

public static void method(Consumer<String> consumer, String s) {
    consumer.accept(s);
}
```


---

### 2.4 Function\<T, R\>（函数型接口）

```
作用：根据 T 类型的输入，转换得到 R 类型的输出（类型转换）
方法：R apply(T t)
```

```java
// 需求：将 Integer 转换为 String
public static void main(String[] args) {
    // 匿名内部类
    method(new Function<Integer, String>() {
        @Override
        public String apply(Integer integer) {
            return integer + "";
        }
    }, 100);

    // Lambda 简化
    method(integer -> integer + "", 200); // 输出: 2001（字符串拼接）
}

public static void method(Function<Integer, String> function, Integer number) {
    String s = function.apply(number);
    System.out.println("s + 1 = " + (s + 1)); // 字符串拼接，非加法
}
```

---

### 2.5 Predicate\<T\>（断言型接口）

```
作用：对数据进行判断，返回 boolean 结果
方法：boolean test(T t)
```

```java
// 需求：判断字符串长度是否等于 7
public static void main(String[] args) {
    // 匿名内部类
    method(new Predicate<String>() {
        @Override
        public boolean test(String s) {
            return s.length() == 7;
        }
    }, "abcdefg");

    // Lambda 简化
    method(s -> s.length() == 7, "abcd"); // false
}

public static void method(Predicate<String> predicate, String s) {
    boolean test = predicate.test(s);
    System.out.println("test = " + test);
}
```

---

### 2.6 补充：其他常用函数式接口

| 接口 | 方法 | 说明 |
|---|---|---|
| `BiConsumer<T, U>` | `void accept(T t, U u)` | 接受两个参数的消费型接口 |
| `BiFunction<T, U, R>` | `R apply(T t, U u)` | 接受两个参数的函数型接口 |
| `UnaryOperator<T>` | `T apply(T t)` | 一元运算（输入输出同类型），继承 Function |
| `BinaryOperator<T>` | `T apply(T t1, T t2)` | 二元运算（两个同类型参数），继承 BiFunction |
| `IntSupplier` | `int getAsInt()` | 专门针对 int 的供给型（避免装箱拆箱） |

---

## 三、Stream 流

### 3.1 Stream 流概述

```
Stream 流中的"流"不是 IO 流，而是一种"流式编程"方式，类似工厂的流水线。

核心优势：
  传统方式：多次循环 + 临时集合 → 代码冗长
  Stream方式：链式调用 → 代码简洁、语义清晰
```


**直观对比：**

```java
// 传统方式：筛选姓张的、三个字、再遍历（需要多个循环+中间集合）
ArrayList<String> listZhang = new ArrayList<>();
for (String s : list) {
    if (s.startsWith("张")) listZhang.add(s);
}
ArrayList<String> listThree = new ArrayList<>();
for (String s : listZhang) {
    if (s.length() == 3) listThree.add(s);
}
for (String s : listThree) System.out.println(s);

// Stream 方式：一行搞定
list.stream()
    .filter(s -> s.startsWith("张"))
    .filter(s -> s.length() == 3)
    .forEach(System.out::println);
```

---

### 3.2 Stream 流的获取

```java
// 方式1：针对集合 —— Collection 中的 stream() 方法
ArrayList<String> list = new ArrayList<>();
Stream<String> stream1 = list.stream();

// 方式2：针对数组 —— Stream 接口中的静态方法 of()
Stream<String> stream2 = Stream.of("张三", "李四", "王五");

// 补充方式3：针对基本类型数组（避免装箱）
IntStream intStream = IntStream.of(1, 2, 3, 4, 5);
IntStream intStream2 = Arrays.stream(new int[]{1, 2, 3});
```

---

### 3.3 Stream 流的方法分类

Stream 方法分为两类：

| 类型 | 说明 | 返回值 | 代表方法 |
|---|---|---|---|
| **中间操作** | 返回新的 Stream，可以链式调用 | `Stream<T>` | `filter`、`limit`、`skip`、`map`、`distinct`、`sorted` |
| **终结操作** | 结束流，流用完后不能再用 | 非 Stream | `forEach`、`count`、`collect`、`reduce`、`findFirst` |

> ⚠️ **终结方法执行后，该 Stream 流对象不可再使用！**

---

### 3.4 Stream 常用方法详解

#### forEach — 遍历（终结方法）

```java
// void forEach(Consumer<? super T> action)
Stream.of("金莲", "三上", "松下")
      .forEach(s -> System.out.println(s));

// 使用方法引用进一步简化
Stream.of("金莲", "三上", "松下")
      .forEach(System.out::println);
```

#### count — 统计元素个数（终结方法）

```java
// long count()
long count = Stream.of("金莲", "三上", "松下", "柳岩").count();
System.out.println("count = " + count); // 4
```

#### filter — 按条件过滤（中间操作）

```java
// Stream<T> filter(Predicate<? super T> predicate)
Stream.of("金莲", "三上", "松下", "柳岩", "张无忌")
      .filter(s -> s.length() == 2)   // 只留两个字的
      .forEach(System.out::println);   // 金莲 三上 松下
```

#### limit — 获取前 n 个元素（中间操作）

```java
// Stream<T> limit(long maxSize)
Stream.of("金莲", "三上", "松下", "柳岩", "张无忌")
      .limit(3)
      .forEach(System.out::println); // 金莲 三上 松下
```

#### skip — 跳过前 n 个元素（中间操作）

```java
// Stream<T> skip(long n)
Stream.of("金莲", "三上", "松下", "柳岩", "张无忌")
      .skip(2)
      .forEach(System.out::println); // 松下 柳岩 张无忌
```

#### concat — 合并两个流（静态方法，中间操作）

```java
// static <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b)
Stream<String> s1 = Stream.of("金莲", "三上", "松下");
Stream<String> s2 = Stream.of("涛哥1", "涛哥2", "涛哥3");
Stream.concat(s1, s2).forEach(System.out::println);
```

#### collect — 流转集合（终结方法）★

```java
// <R, A> R collect(Collector<? super T, A, R> collector)
Stream<String> stream = Stream.of("金莲", "三上", "松下", "柳岩");

// 转 List
List<String> list = stream.collect(Collectors.toList());

// 转 Set（自动去重）
Set<String> set = Stream.of("a", "b", "a").collect(Collectors.toSet());

// 转 Map（补充）
Map<String, Integer> map = Stream.of("abc", "de", "f")
    .collect(Collectors.toMap(s -> s, String::length));
```

#### distinct — 去重（中间操作）

```java
// Stream<T> distinct()  底层依赖 hashCode() 和 equals()
Stream.of("张三", "李四", "张三")
      .distinct()
      .forEach(System.out::println); // 张三 李四

// 自定义对象去重：必须重写 hashCode() 和 equals()
Stream.of(new Person("张三", 10), new Person("李四", 12), new Person("张三", 10))
      .distinct()
      .forEach(System.out::println); // 需 Person 重写了 hashCode/equals
```

#### map — 转换流中的数据类型（中间操作）★

```java
// <R> Stream<R> map(Function<? super T, ? extends R> mapper)
// Integer → String
Stream.of(1, 2, 3, 4, 5)
      .map(i -> i + "")         // 每个 Integer 转成 String
      .forEach(s -> System.out.println(s + "1")); // "11" "21" ...
```

#### sorted — 排序（中间操作，补充）

```java
// 自然排序
Stream.of(3, 1, 4, 1, 5, 9).sorted().forEach(System.out::println);

// 自定义排序（Comparator）
Stream.of("banana", "apple", "cherry")
      .sorted((a, b) -> a.length() - b.length()) // 按字符串长度升序
      .forEach(System.out::println);
```

---

### 3.5 Stream 综合练习

```java
/**
 * 需求：
 * 队伍A：只要名字3个字的，取前3人
 * 队伍B：只要姓张的，跳过前2人
 * 合并两队伍，打印所有姓名
 */
public static void main(String[] args) {
    List<String> teamA = Arrays.asList("迪丽热巴","宋远桥","苏星河","老子","庄子","孙子","洪七公");
    List<String> teamB = Arrays.asList("古力娜扎","张无忌","张三丰","赵丽颖","张二狗","张天爱","张三");

    Stream.concat(
        teamA.stream().filter(s -> s.length() == 3).limit(3),
        teamB.stream().filter(s -> s.startsWith("张")).skip(2)
    ).forEach(System.out::println);
    // 输出: 迪丽热巴 宋远桥 苏星河 张二狗 张天爱 张三
}
```

---

## 四、方法引用

### 4.1 方法引用概述

```
方法引用是 Lambda 表达式的进一步简化。
当 Lambda 体中只是调用一个已存在的方法，且该方法的参数和返回值
与重写方法完全一致时，可以使用方法引用替代 Lambda。

格式符号：::（双冒号）
```

**使用条件：**

1. 被引用的方法要写在重写方法的方法体里面
2. 被引用方法的**参数类型**和**返回值类型**要与所在重写方法一致
3. 被引用的方法最好是直接操作重写方法的参数值的

---

### 4.2 四种方法引用形式

#### 形式1：对象名 :: 实例方法名

```java
// 格式：对象名::成员方法名
// 场景：被引用方法属于某个对象实例

// Lambda 写法
method(() -> " abc ".trim());

// 方法引用写法（trim() 操作的就是 " abc " 这个对象本身）
method(" abc "::trim);

// 方法定义
public static void method(Supplier<String> supplier) {
    System.out.println(supplier.get()); // "abc"
}
```

#### 形式2：类名 :: 静态方法名

```java
// 格式：类名::静态方法名
// 场景：被引用方法是某个类的静态方法

// Lambda 写法
method(() -> Math.random());

// 方法引用写法
method(Math::random);

// 方法定义
public static void method(Supplier<Double> supplier) {
    System.out.println(supplier.get());
}
```

**常见示例：**

```java
// System.out 是 PrintStream 对象实例，println 是成员方法
stream.forEach(System.out::println);

// Integer::parseInt 是类名引用静态方法
Stream.of("1", "2", "3").map(Integer::parseInt).forEach(System.out::println);
```

#### 形式3：类名 :: 构造方法（构造引用）

```java
// 格式：类名::new
// 场景：Lambda 体中是 new 一个对象

// Lambda 写法
method(s -> new Person(s), "张三");

// 构造引用写法
method(Person::new, "李四");

// 方法定义
public static void method(Function<String, Person> function, String name) {
    Person person = function.apply(name);
    System.out.println(person);
}
```

#### 形式4：数组类型 :: new（数组引用）

```java
// 格式：数组类型[]::new
// 场景：Lambda 体中是创建一个新数组

// Lambda 写法
method(len -> new int[len], 10);

// 数组引用写法
method(int[]::new, 10);

// 方法定义
public static void method(Function<Integer, int[]> function, Integer len) {
    int[] arr = function.apply(len);
    System.out.println("数组长度：" + arr.length); // 10
}
```

---

### 4.3 方法引用四种形式汇总

| 形式 | 语法 | 示例 |
|---|---|---|
| 实例方法引用 | `对象名::实例方法名` | `"abc"::toUpperCase` |
| 静态方法引用 | `类名::静态方法名` | `Math::random`、`Integer::parseInt` |
| 构造方法引用 | `类名::new` | `Person::new`、`ArrayList::new` |
| 数组构造引用 | `类型[]::new` | `int[]::new`、`String[]::new` |

---

## 五、Java 9~17 新特性

### 5.0 JDK 版本选择建议


| JDK 版本 | 类型 | 说明 |
|---|---|---|
| JDK 8 | **LTS**（长期支持）| 目前仍是企业存量最大的版本 |
| JDK 11 | **LTS** | Spring Boot 2.x 兼容的主要版本 |
| JDK 17 | **LTS** | Spring Boot 3.x / Spring 6 最低要求版本 ★ |
| JDK 21 | **LTS** | 2023年发布，虚拟线程正式稳定 |

**JDK 17 性能提升亮点：**

- **吞吐量**：G1 GC 相比 JDK 8 提升约 **18%**，ZGC 相比 JDK 11 提升超 **20%**
- **GC延迟**：G1 GC 相比 JDK 8 提升接近 **60%**，ZGC 相比 JDK 11 提升超 **40%**

> Spring Boot 3.0+ 要求最低 Java 17，Jakarta EE 9+，是当前新项目选型的主流方向。

---

### 5.1 接口私有方法（JDK 9）

**背景：**

| JDK 版本 | 接口新增成员 |
|---|---|
| JDK 7 及以前 | 只能定义：抽象方法、常量 |
| JDK 8 | 新增：`public default` 默认方法、`public static` 静态方法 |
| JDK 9 | 新增：**`private` 私有方法**、`private static` 私有静态方法 |

**为什么需要私有方法？**

当多个 `default` 方法或 `static` 方法中存在重复逻辑，需要抽取公共代码，但又不希望对外暴露时，就使用私有方法。

```java
public interface USB {

    // JDK 9：私有实例方法（只能被 default 方法调用）
    private void open() {
        System.out.println("USB私有方法：打开");
    }

    // JDK 9：私有静态方法（可被 default 和 static 方法调用）
    private static void close() {
        System.out.println("USB私有静态方法：关闭");
    }

    // default 方法调用私有方法（私有方法外部不可直接调用）
    public default void methodDefault() {
        open();   // 调用私有实例方法
        close();  // 调用私有静态方法
    }
}
```

```java
public class UsbImpl implements USB { } // 实现类无需重写私有方法

// 使用
new UsbImpl().methodDefault(); // 打开 关闭
```

---

### 5.2 钻石操作符与匿名内部类结合（JDK 9）

**JDK 8 的限制：**

```java
// JDK 8 中以下代码编译报错：
// '<>' cannot be used with anonymous classes
Comparator<Person> comp = new Comparator<>() { ... }; // ❌ JDK 8 不支持
```

**JDK 9 之后正常支持：**

```java
// JDK 9+ 匿名内部类可以使用 <> 进行类型自动推断
ArrayList<Person> list = new ArrayList<>();
list.add(new Person("张三", 10));
list.add(new Person("李四", 8));

// <> 钻石操作符在匿名内部类中正常工作（JDK 9+）
Collections.sort(list, new Comparator<>() {
    @Override
    public int compare(Person o1, Person o2) {
        return o1.getAge() - o2.getAge();
    }
});
```

---

### 5.3 try-with-resources 升级（JDK 9）

**JDK 7 的写法：** 必须在 `try(...)` 中声明并初始化资源对象

```java
// JDK 7：声明和初始化必须在 try() 内
try (FileWriter fw = new FileWriter("io.txt")) {
    fw.write("你好");
} catch (Exception e) {
    e.printStackTrace();
}
```

**JDK 9 的升级：** 资源对象可以在外部提前声明，`try(...)` 中直接引用变量名即可

```java
// JDK 9：资源对象在外部声明，try() 中直接用变量名引用
FileWriter fw = new FileWriter("io.txt");
try (fw) {   // 直接写变量名，不用再声明类型
    fw.write("你好");
} catch (Exception e) {
    e.printStackTrace();
}
// 执行完 try 块后，fw 依然会自动关闭（自动调用 close()）
```

**前提条件：**

- 资源对象必须实现 `java.io.Closeable` 接口
- 资源对象必须是 `final` 的（隐式，不能在 try 块中再修改引用）

---

### 5.4 局部变量类型推断 var（JDK 10）

**语法：**

```java
var 变量名 = 值;  // 编译器根据右侧值自动推断类型
```

```java
// JDK 10 之前：必须明确写出类型
int i = 10;
String j = "helloworld";
ArrayList<String> list = new ArrayList<String>();

// JDK 10+ ：使用 var 自动推断
var i = 10;                        // 推断为 int
var j = "helloworld";              // 推断为 String
var list = new ArrayList<String>();// 推断为 ArrayList<String>
var arr = new int[]{1, 2, 3};      // 推断为 int[]

// 在增强 for 循环中使用
for (var element : arr) {
    System.out.println(element);
}
```

**var 的限制（重要 ★）：**

```
✅ 可以使用的场景：
  - 局部变量声明（方法体、代码块内）
  - for 循环的循环变量
  - try-with-resources 中的资源变量

❌ 不能使用的场景：
  - 类的成员变量
  - 方法的参数
  - 方法的返回值类型
  - 不能用 var 声明而不赋值（var x; ← 编译错误）
  - Lambda 表达式参数（JDK 11 开始 Lambda 参数可用，但无实际意义）
```

> ⚠️ `var` 是一个**保留字**，而非关键字（即可以作为变量名/方法名，但强烈不推荐）。`var` 类型在**编译期**就已确定，运行时没有任何额外开销。

---

### 5.5 switch 表达式增强（JDK 12~14）

**传统 switch 语句的四大问题：**

1. 忘写 `break` 导致 **case 穿透**
2. 不同 case 内不能定义同名局部变量（共享同一块作用域）
3. 一个 case 只能写一个值，不能合并相同逻辑的分支
4. switch 整体不能作为**表达式**返回值

---

#### JDK 12：switch 表达式（预览）—— 箭头语法

```java
// JDK 12 之前（传统写法）
int month = 5;
switch (month) {
    case 3:
    case 4:
    case 5:
        System.out.println("春季"); break;
    case 6:
    case 7:
    case 8:
        System.out.println("夏季"); break;
    // ...
}

// JDK 12+ 箭头语法（-> 写法，自动省略 break，无穿透问题）
switch (month) {
    case 12, 1, 2  -> System.out.println("冬季"); // 多值合并 ✔
    case 3, 4, 5   -> System.out.println("春季"); // 自动 break ✔
    case 6, 7, 8   -> System.out.println("夏季");
    case 9, 10, 11 -> System.out.println("秋季");
    default        -> System.out.println("月份有误");
}
```

> ⚠️ `->` 和 `:` 两种写法**不能混用**，否则编译报错。

---

#### JDK 13：yield 语句 —— switch 表达式返回值

```java
// JDK 13 之前：想获取 switch 结果，需要外部变量辅助
String season = "";
switch (month) {
    case 12, 1, 2: season = "冬季"; break;
    // ...
}

// JDK 13+ 使用 yield 关键字：switch 直接作为表达式返回值
var season = switch (month) {
    case 12, 1, 2  -> { yield "冬季"; }  // yield 代替 return
    case 3, 4, 5   -> { yield "春季"; }
    case 6, 7, 8   -> { yield "夏季"; }
    case 9, 10, 11 -> { yield "秋季"; }
    default        -> { yield "月份有误"; }
};
System.out.println("season = " + season); // 春季
```

> 📌 **yield vs return：**
> - `yield` 用于从 switch 表达式中**返回一个值**
> - `return` 用于从**方法**中返回值
> - 如果 `->` 右侧只有一个表达式（不是代码块），可以直接省略 `yield`：
    >   ```java
>   case 3, 4, 5 -> "春季";  // 不需要 yield
>   ```

---

#### JDK 14：switch 表达式正式确定（转正）

```java
// JDK 14 起 switch 表达式正式成为语言特性（无需预览模式）
String result = switch (month) {
    case 3, 4, 5   -> "春季";
    case 6, 7, 8   -> "夏季";
    case 9, 10, 11 -> "秋季";
    case 12, 1, 2  -> "冬季";
    default        -> throw new IllegalArgumentException("无效月份: " + month);
};
```

---

### 5.6 文本块（JDK 13 预览 → JDK 15 正式）

**背景：** 在 Java 中嵌入多行字符串（HTML/JSON/SQL）需要大量转义和拼接，难以阅读。

**语法：**

```java
String 变量名 = """
    多行文本内容
    支持换行、缩进
    """;
```

**示例对比：**

```java
// JDK 15 之前：复杂的转义和拼接
String html = "<html>\n" +
              "  <body>\n" +
              "    <p>Hello, World</p>\n" +
              "  </body>\n" +
              "</html>\n";

// JDK 15+ 文本块：清晰直观
String html = """
    <html>
      <body>
        <p>Hello, World</p>
      </body>
    </html>
    """;
```

**文本块使用规则：**

```java
// ✅ 正确写法：开始分隔符后必须换行
String s = """
    内容
    """;

// ❌ 错误写法：开始符后没有换行
String err1 = """""";         // 编译错误
String err2 = """  """;       // 编译错误

// 文本块中可以自由使用双引号
String story = """
    He said, "Hello!"
    She said, "Hi!"
    """;

// 空字符串推荐写法
String empty = "";  // 推荐，比文本块写法简单
```

**文本块的实际应用：**

```java
// JSON 字符串
String json = """
    {
        "name": "张三",
        "age": 20,
        "city": "北京"
    }
    """;

// SQL 语句
String sql = """
    SELECT id, name, age
    FROM users
    WHERE age > 18
    ORDER BY age DESC
    """;
```

---

### 5.7 instanceof 模式匹配（JDK 14 预览 → JDK 16 正式）

**传统写法（JDK 16 之前）：**

```java
// 需要先 instanceof 判断，再强制转型，代码冗余
Animal animal = new Dog();
if (animal instanceof Dog) {
    Dog dog = (Dog) animal; // 多余的强转
    dog.lookDoor();
}
```

**JDK 16+ 新写法（模式匹配）：**

```java
// instanceof 判断和强转合二为一，直接绑定变量名
Animal animal = new Dog();
if (animal instanceof Dog dog) {  // 判断成立后，dog 直接可用
    dog.lookDoor(); // 无需额外强转
}

// 还可以在条件中使用绑定的变量
if (animal instanceof Dog dog && dog.getName() != null) {
    System.out.println("狗的名字：" + dog.getName());
}
```

---

### 5.8 Record 类（JDK 14 预览 → JDK 16 正式）

**概述：**

```
Record 是一种全新的类型，本质是一个 final 类。
会自动生成：全参构造、getter（无 set）、equals（比较所有字段）、hashCode、toString。
适用于：不可变数据载体（DTO/VO 等场景）。
```

**语法：**

```java
public record 类名(字段类型 字段名, ...) {
    // 可选：静态字段、静态方法、实例方法
}
```

**示例：**

```java
// 定义 Record 类
public record Person(String name, Integer age) {
    // ✅ 可以声明静态字段
    static int count = 0;

    // ✅ 可以声明静态方法
    public static void staticMethod() {
        System.out.println("静态方法");
    }

    // ✅ 可以声明实例方法（非静态成员方法）
    public String info() {
        return name + " - " + age;
    }

    // ❌ 不能声明实例字段（非静态成员变量）
    // int id;  ← 编译错误

    // ❌ 不能声明无参构造
    // public Person() {} ← 编译错误
}
```

```java
// 使用 Record 类
Person p1 = new Person("张三", 20);  // 只有全参构造
Person p2 = new Person("张三", 20);

System.out.println(p1.name());       // 张三（getter 方法名即字段名）
System.out.println(p1.age());        // 20
System.out.println(p1);              // Person[name=张三, age=20]（自动 toString）
System.out.println(p1.equals(p2));   // true（比较所有字段值）
```

**Record 类的限制总结：**

| 限制 | 说明 |
|---|---|
| 不能声明实例字段 | 只能通过构造参数定义字段 |
| 只有全参构造 | 无法定义无参构造 |
| 不能被继承 | `final` 类 |
| 不能显式继承其他类 | 隐式继承 `java.lang.Record` |
| 不能声明为 `abstract` | — |
| 字段都是 `final` 的 | 不可变（没有 setter）|

---

### 5.9 密封类（JDK 15 预览 → JDK 17 正式）

**概述：**

```
密封类（Sealed Class）允许类或接口限制哪些类可以继承/实现它。
比 final 更灵活：final 完全禁止继承，Sealed 允许特定子类继承。
```

**语法：**

```java
// 密封类定义：permits 指定允许的子类
public sealed class Animal permits Dog, Cat { }

// 密封接口定义
public sealed interface Flyable permits Bird, Plane { }
```

**子类必须是以下三种之一：**

| 修饰符 | 含义 |
|---|---|
| `final` | 该子类不可再被继承 |
| `sealed` | 该子类也是密封类，继续用 permits 限制 |
| `non-sealed` | 开放继承，任何类都可以继承该子类 |

```java
// 密封类
public sealed class Animal permits Dog, Cat { }

// 子类1：non-sealed，开放继承
public non-sealed class Dog extends Animal {
    public void lookDoor() { System.out.println("狗会看门"); }
}

// 子类2：non-sealed，开放继承
public non-sealed class Cat extends Animal {
    public void catchMouse() { System.out.println("猫会抓鼠"); }
}

// 密封接口示例
sealed interface Flyable permits Bird { }
non-sealed class Bird implements Flyable { }
```

**使用场景：** 与 switch 模式匹配结合，实现穷举判断，类似代数数据类型（ADT）。

---

### 5.10 Java 9 模块化系统（了解）

**背景问题：**

1. 简单应用也需要庞大的 JRE（rt.jar 60.5MB+）
2. 大型项目中 package 之间依赖混乱，"意大利面条式代码"
3. JAR Hell：同一 JAR 不同版本冲突
4. 公共 API 难以真正封装

**模块化优势：**

- 减少内存开销，按需加载模块
- 更清晰的依赖关系（编译期/运行期均校验）
- 更好的安全性（可控制哪些 package 对外暴露）

```
Java 8 API 结构：包（package）是顶级封装
Java 9+ API 结构：模块（module）是顶级封装，模块下才是包
```

---

### 5.11 JDK 新特性版本速查表

| 特性 | 首次出现 | 正式版本 | 关键字/语法 |
|---|---|---|---|
| Lambda 表达式 | JDK 8 | JDK 8 | `->` |
| Stream 流 | JDK 8 | JDK 8 | `.stream()` |
| 接口默认/静态方法 | JDK 8 | JDK 8 | `default`、`static` |
| Optional 类 | JDK 8 | JDK 8 | `Optional.of()` |
| 接口私有方法 | JDK 9 | JDK 9 | `private` in interface |
| 模块化系统 | JDK 9 | JDK 9 | `module-info.java` |
| try-with-resources 升级 | JDK 9 | JDK 9 | 外部声明变量直接引用 |
| 钻石操作符+匿名内部类 | JDK 9 | JDK 9 | `new Xxx<>() {}` |
| var 局部变量类型推断 | JDK 10 | JDK 10 | `var` |
| switch 箭头语法 | JDK 12（预览）| JDK 14 | `case x -> ...` |
| 多值 case | JDK 12（预览）| JDK 14 | `case 1,2,3 ->` |
| 文本块 | JDK 13（预览）| JDK 15 | `"""..."""` |
| yield 语句 | JDK 13（预览）| JDK 14 | `yield 值;` |
| instanceof 模式匹配 | JDK 14（预览）| JDK 16 | `instanceof Dog dog` |
| Record 类 | JDK 14（预览）| JDK 16 | `record` |
| 密封类 | JDK 15（预览）| JDK 17 | `sealed`、`permits`、`non-sealed` |
| 虚拟线程 | JDK 19（预览）| JDK 21 | `Thread.ofVirtual()` |

---

## 知识总结

### 一、Lambda 表达式核心要点

| 要点 | 说明 |
|---|---|
| 使用前提 | 必须以**函数式接口**作为方法参数 |
| 函数式接口 | 有且只有**一个抽象方法**，可用 `@FunctionalInterface` 检测 |
| 基本格式 | `(参数) -> { 方法体 }` |
| 省略核心 | 参数类型可省；单参数括号可省；单语句大括号、分号、return 可省 |
| 本质 | 是对**匿名内部类**的简化，底层仍是接口的实现 |

---

### 二、四大函数式接口核心记忆

```
Supplier<T>      get()               → 无入有出（供货）
Consumer<T>      accept(T t)         → 有入无出（消费）
Function<T,R>    apply(T t)→R        → 有入有出（转换）
Predicate<T>     test(T t)→boolean   → 有入布尔（判断）
```

---

### 三、Stream 流核心要点 ★★

```
获取流：
  集合.stream()           → 集合转流
  Stream.of(T... values)  → 数组/元素转流

中间操作（可链式调用，返回新 Stream）：
  filter(Predicate)  → 过滤
  map(Function)      → 转换类型
  distinct()         → 去重（需重写 hashCode/equals）
  sorted()           → 排序
  limit(n)           → 取前n个
  skip(n)            → 跳过前n个
  concat(s1, s2)     → 合并两流（静态方法）

终结操作（流结束，不可再用）：
  forEach(Consumer)  → 遍历
  count()            → 统计数量
  collect(Collectors.toList/toSet/toMap) → 收集为集合
```

> ⚠️ **终结方法执行后 Stream 不可再使用；distinct() 去重自定义对象必须重写 hashCode 和 equals。**

---

### 四、方法引用核心要点

| 形式 | 语法 | 使用条件 |
|---|---|---|
| 实例方法引用 | `对象::方法名` | 方法参数/返回值与重写方法一致 |
| 静态方法引用 | `类名::静态方法名` | 同上 |
| 构造方法引用 | `类名::new` | 构造参数和 apply 参数类型一致 |
| 数组引用 | `类型[]::new` | Function<Integer, T[]> 场景 |

---

### 五、JDK 9~17 新特性速记

| 版本 | 核心特性 | 一句话记忆 |
|---|---|---|
| JDK 9 | 接口私有方法、try 升级、模块化 | 接口更完善，try 更简洁 |
| JDK 10 | `var` 类型推断 | 局部变量不用写类型了 |
| JDK 12-14 | switch 箭头语法、多值 case | switch 更强大，不怕忘 break |
| JDK 13-15 | 文本块 `"""..."""` | 多行字符串再也不用转义 |
| JDK 13-14 | `yield` 返回值 | switch 可以直接当表达式用 |
| JDK 14-16 | `instanceof` 模式匹配 | 判断和强转合二为一 |
| JDK 14-16 | `record` 类 | 不可变数据类自动生成所有方法 |
| JDK 15-17 | `sealed` 密封类 | 精准控制继承权限 |

---

### 六、原文档不足与补充修正

| 原文问题 | 修正/补充 |
|---|---|
| Stream 方法未区分中间操作与终结操作 | 明确分类，终结操作后流不可再使用 |
| distinct() 去重没有说明前提 | 自定义对象必须重写 `hashCode` 和 `equals` |
| sorted() 方法未涉及 | 补充了 `sorted()` 中间操作 |
| collect() 只有 toList | 补充 `toSet()`、`toMap()` 用法 |
| 函数式接口只有四个基础接口 | 补充 `BiConsumer`、`UnaryOperator` 等扩展接口 |
| var 的限制未说明 | 详细说明 var 不能用于成员变量、方法参数、返回值 |
| JDK 版本表不完整 | 补充了 JDK 21 虚拟线程等新特性 |
| yield 与 return 区别未说明 | 明确说明 yield 是 switch 表达式专用 |
| Record 类 getter 方法名说明不清 | 明确说明 getter 方法名就是字段名（无 get 前缀）|
| instanceof 模式匹配代码块混入了 Record 定义 | 已分离两个特性，独立展示 |
