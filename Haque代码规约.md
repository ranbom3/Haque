# Haque代码规约

## 命名规范

1. 类名：使用 UpperCamelCase 风格，但以下情形例外：DO / BO / DTO / VO / AO / PO / UID 等。
2. 方法名：方法名、参数名、成员变量、局部变量都统一使用 lowerCamelCase 风格
3.  常量：常量命名全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长
4. **POJO 类中的任何布尔类型的变量，都不要加 is 前缀，否则部分框架解析会引起序列 化错误。 说明：在本文 MySQL 规约中的建表约定第一条，表达是与否的值采用 is_xxx 的命名方式，所以，需要在 设置从 is_xxx 到 xxx 的映射关系。**
5. 应用工具类包名为 com.alibaba.ei.kunlun.aap.util、类名为 MessageUtils（此规则参考 spring 的 框架结构）
6. 避免在子父类的成员变量之间、或者不同代码块的局部变量之间采用完全相同的命名， 使可读性降低。

## 常量定义

1. 在 long 或者 Long 赋值时，数值后使用大写的 L，不能是小写的 l，小写容易跟数字 混淆，造成误解。
2. 不要使用一个常量类维护所有常量，要按常量功能进行归类，分开维护。
3. 如果变量值仅在一个固定范围内变化用 enum 类型来定义。
4. 关于基本数据类型与包装数据类型的使用标准如下：
   + 所有的 POJO 类属性必须使用包装数据类型。
   + 所有的局部变量使用基本数据类型。
5. 禁止在 POJO 类中，同时存在对应属性 xxx 的 isXxx()和 getXxx()方法。
6. final 可以声明类、成员变量、方法、以及本地变量，下列情况使用 final 关键字：
   + 不允许被继承的类，如：String 类。
   +  不允许修改引用的域对象，如：POJO 类的域变量。
   + 不允许被覆写的方法，如：POJO 类的 setter 方法。
   + 不允许运行过程中重新赋值的局部变量
   + 避免上下文重复使用一个变量，使用 final 可以强制重新定义一个变量，方便更好地进行重构。
7. 工具类不允许有 public 或 default 构造方法。

## 日期与事件

1. 日期格式化时，传入 pattern 中表示年份统一使用小写的 y。
   + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
2. 在日期格式中分清楚大写的 M 和小写的 m，大写的 H 和小写的 h 分别指代的意义。
   + 表示月份是大写的 M；
   + 表示分钟则是小写的 m；
   + 24 小时制的是大写的 H；
   + 12 小时制的则是小写的 h
3. 获取当前毫秒数：System.currentTimeMillis(); 而不是 new Date().getTime()。

## 集合处理

1. 关于 hashCode 和 equals 的处理，遵循如下规则：

   +  只要重写 equals，就必须重写 hashCode。
   +  因为 Set 存储的是不重复的对象，依据 hashCode 和 equals 进行判断，所以 Set 存储的对象必须重写 这两个方法。
   +  如果自定义对象作为 Map 的键，那么必须覆写 hashCode 和 equals。

2. 判断所有集合内部的元素是否为空，使用 isEmpty()方法，而不是 size()==0 的方式。

3. 使用 Map 的方法 keySet()/values()/entrySet()返回集合对象时，不可以对其进行添 加元素操作，否则会抛出 UnsupportedOperationException 异常。

4. 使用集合转数组的方法，必须使用集合的 toArray(T[] array)，传入的是类型完全一 致、长度为 0 的空数组。

5. 使用工具类 Arrays.asList()把数组转换成集合时，不能使用其修改集合相关的方法， 它的 add/remove/clear 方法会抛出 UnsupportedOperationException 异常。

6. 不要在 foreach 循环里进行元素的 remove/add 操作。remove 元素请使用 Iterator 方式，如果并发操作，需要对 Iterator 对象加锁。

   ```java
   // 正例：
   List<String> list = new ArrayList<>();
   list.add("1");
   list.add("2");
   Iterator<String> iterator = list.iterator();
   while (iterator.hasNext()) {
    String item = iterator.next();
    if (删除元素的条件) {
    iterator.remove();
    }
   }
   // 反例：
   for (String item : list) {
    if ("1".equals(item)) {
    list.remove(item);
    }
   }
   
   ```

7. 在 JDK7 版本及以上，Comparator 实现类要满足如下三个条件，不然 Arrays.sort， Collections.sort 会抛 IllegalArgumentException 异常。

8. 集合初始化时，指定集合初始值大小

   + HashMap 使用 HashMap(int initialCapacity) 初始化，如果暂时无法确定集合大小，那么指定默 认值（16）即可。
   + 反例：HashMap 需要放置 1024 个元素，由于没有设置容量初始大小，随着元素不断增加，容量 7 次被迫 扩大，resize 需要重建 hash 表。当放置的集合元素个数达千万级别时，不断扩容会严重影响性能。

9. 利用 Set 元素唯一的特性，可以快速对一个集合进行去重操作，避免使用 List 的 contains()进行遍历去重或者判断包含操作。



## 控制语句

1. 当 switch 括号内的变量类型为 String 并且此变量为外部参数时，必须先进行 null 判断。
2. 表达异常的分支时，少用 if-else 方式，这种方式可以改写成：
3. 类、类属性、类方法的注释必须使用 Javadoc 规范，使用/**内容*/格式，不得使用 // xxx 方式。
4. 对于暂时被注释掉，后续可能恢复使用的代码片断，在注释代码上方，统一规定使用三个斜杠(///) 来说明注释掉代码的理由。如：

## 代码格式

我们做得很好

## OOP规约

1. Object 的 equals 方法容易抛空指针异常，应使用常量或确定有值的对象来调用 equals。
2. **所有整型包装类对象之间值的比较，全部使用 equals 方法比较。 说明：对于 Integer var = ? 在-128 至 127 之间的赋值，Integer 对象是在 IntegerCache.cache 产生， 会复用已有对象，这个区间内的 Integer 值可以直接使用==进行判断，但是这个区间之外的所有数据，都 会在堆上产生，并不会复用已有对象，这是一个大坑，推荐使用 equals 方法进行判断。** 
3. 浮点数之间的等值判断，基本数据类型不能用==来比较，包装数据类型不能用 equals 来判断。

## mapper和service层方法命名规范（自定义）

1. mapper接口里方法命名方式：
   + `selectxxxByYYY()`：条件查询（返回单条数据）
   + `selectxxxsByYYY()`：条件查询（返回多条数据）
   + `selectAll()`：查询所有
   + `updatexxxByYYY()`：更新数据
   + `deletexxxByYYY()`：删除数据
   + `insertxxx(Entity entity)`：插入数据
2. service层方法命名方式：
   + `findxxxByYYY()`：条件查询（返回单条数据）
   + `findxxxsByYYY()`：条件查询（返回多条数据）
   + `findAll()`：查询所有
   + `modifyxxxByYYY()`：更新数据
   + `removexxxByYYY()`：删除数据
   + `savexxx(Entity entity)`：插入数据
3. 