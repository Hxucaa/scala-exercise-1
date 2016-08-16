### Scala Exercise 1

This repo solves the following exercise.

定义数据类型

```scala
type Definitions = Seq[(String, Seq[(String, String)])]
```

形如：
```scala
val defs: Definitions = Seq("User" -> Seq("name" -> "oldpig", "gender" -> "male", "mixin" -> "Address",
                            "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang"))
```

其中 `mixin` 是一个特殊指令，以它为 key 的值对 `"mixin" -> v` 将被同一个 Definitions 中以 `v` 为 key 的值对 `v -> (s: Seq)` 中 s 的内容所替代。 例如上例中的定义，经过替代后的形式为：

```scala
def expand(d: Definitions): Definitions

assert(expand(defs) == Seq("User" -> Seq("name" -> "oldpig", "gender" -> "male", "Province" -> "Shanghai", "District" -> "Minhang"), 
                           "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang"))
```

条件：

1. 可能存在嵌套mixin的情况，即 A mixin B, B mixin C, C mixin D
2. 可能存在多个mixin，同一个v也可能被mixin多次，即 A mixin B, A mixin C, B mixin C
3. 不存在循环mixin的情况，即 A mixin B, B mixin A, 或 A mixin B, B mixin C, C mixin A
4. 除了 mixin 外，不存在两个相同的 ke

本月的试题就是实现上述方法 `expand` ，输出数据中要求不存在任何 mixin，不要求保持输入数据的出现次序

要求：
```
请在github上（或使用任何一个开放的代码托管服务如bitbucket.org, git.oschina.net, coding.net等）创建完整的sbt项目，使用Scala语言解决本题。将项目链接作为入群问题的答案。
尽量不使用 var
用ScalaTest, Specs2或ScalaCheck编写测试（我们也接受使用JUnit或其它工具编写的测试）。
本群也欢迎猎头及HR人员入群，此类请将公司招聘网页的链接或招聘文档百度网盘链接作为入群问题的答案。
```

如果觉得题目太简单，可以考虑：

1. 在代码中探测循环mixin，发现则抛出异常
2. 在结果中保持输入数据次序
3. 存在 2 个或多个 key 相同的情况