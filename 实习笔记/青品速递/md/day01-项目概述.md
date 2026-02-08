# 课程安排
- 了解神领物流
- 了解物流行业
- 了解项目的技术架构
- 了解项目的业务功能
- 项目功能演示
- 搭建开发环境
- 基于现有代码进行bug修复
- 阅读已有的代码
# 1、场景说明

现在的你，已经学习了目前最主流的系统架构技术《微服务技术栈》，并且呢也拿到了满意的offer，入职了一家物流公司，公司名叫：神领物流公司。

现在你的心情还是比较复杂的，既开心又担心，开心是这个offer你很满意，担心的是，听朋友说物流行业的项目业务非常复杂，技术涉及的也比较多，而自己从来没有接触过物流项目，就担心自己能不能Hold得住？万一……

不用过于担心，本套课程就是带着你一点点的了解项目，站到一个新人的角度来看待这个项目，代码从哪里拉取？开发规范是什么？有哪些环境？项目业务是什么样的？ ……

![image-20220725211508122.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1665994390645-292d9d08-3927-4a96-8d4a-0c8a653d0b45.png#averageHue=%23626f62&clientId=u27ebf908-2b86-4&errorMessage=unknown%20error&from=paste&height=278&id=u60260370&name=image-20220725211508122.png&originHeight=458&originWidth=640&originalType=binary&ratio=1&rotation=0&showTitle=false&size=231265&status=error&style=shadow&taskId=u0cc5a6b7-daa0-49a5-b358-592327cd70c&title=&width=387.878765459999)

# 2、神领物流是什么？

神领物流是一个基于微服务架构体系的**【生产级】**物流项目系统，这可能是目前你能学习到的最接近企业真实场景的项目课程，其业务完整度、真实度、复杂度会让你感到惊讶，在这里你会学习到最核心的物流调度系统，也可以学习到在复杂的微服务架构体系下开发以及相关问题的解决。学完后你的收获会很“哇塞”。
## 2.1、公司介绍

公司从2019年开始业务快速扩张，网点数量从138家扩展至540家，车辆从170台增长到800台。同时，原有的系统非常简单，比如车辆的调度靠人工操作、所有的货物分拣依靠人员，核心订单数据手动录入，人效非常低。

随着业务不断演进，技术的不断提升，原有运输管理系统已无法满足现有快速扩展下的业务需求，但针对现有系统评估后发现，系统升级成本远高于重新研发。

因此，公司决定基于现有业务体系进行重新构建，打造一套新的TMS运输系统，直接更替原有系统。业务侧重于展示车辆调研、线路规划等核心业务流程，操作智能化，大幅度提升人效及管控效率。

## 2.2、组织架构
![](https://cdn.nlark.com/yuque/0/2022/jpeg/27683667/1665996673705-67f5f038-4e74-4205-8cfc-ee8dd1b4ee66.jpeg)

Java开发人员所在的一级部门为信息中心，主要负责集团新系统的研发、维护、更新迭代。信息中心下设3个2级部门，产品部、运维部以及开发部门，开发部门总计42人，按照以业务线划分为4个组、TMS项目组之外、WMS项目组、OMS项目、CRM组。

TMS(Transportation Management System 运输管理系统) 项目组目前共8人，其中前端3人，后端5人。后端人员根据以下功能模块拆分进行任务分配，实际业务中也不可能是一人包打天下，分工合作才是常态化操作。

## 2.3、产品说明

神领物流系统类似顺丰速运，是向C端用户提供快递服务的系统。竞品有：顺丰、中通、圆通、京东快递等。
项目产品主要有4端产品：

- 用户端：基于微信小程序开发，外部客户使用，可以寄件、查询物流信息等。
- 快递员端：基于安卓开发的手机APP，公司内部的快递员使用，可以接收取派件任务等。
- 司机端：基于安卓开发的手机APP，公司内部的司机使用，可以接收运输任务、上报位置信息等。
- 后台系统管理：基于vue开发，PC端使用，公司内部管理员用户使用，可以进行基础数据维护、订单管理、运单管理等。
# 3、物流行业系统
从广度上来说，物流系统可以理解为由多个子系统组成，这里我们以一般综合型物流系统举例，在整体框架上可以分为仓储系统WMS、运配系统TMS、单据系统OMS和计费系统BMS。

这四大系统本质上解决了物流行业的四大核心问题：怎么存放、怎么运送、怎么跟进、怎么结算。

神领物流系统，是TMS运配系统，本质上解决的是怎样运送的问题。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666843467766-309e2f8f-1fc0-4f05-ba11-3142f542929c.png#averageHue=%23e1deb0&clientId=u5ccd6243-bb68-4&from=paste&height=1303&id=uf6361701&name=image.png&originHeight=1955&originWidth=2700&originalType=binary&ratio=1&rotation=0&showTitle=false&size=2507860&status=done&style=shadow&taskId=u55fe548e-896b-41e8-9420-51863c19e41&title=&width=1800)
# 4、系统架构和技术架构
## 4.1、系统架构

![](https://cdn.nlark.com/yuque/0/2022/jpeg/27683667/1665997407045-5b9ee5fe-d30b-4cec-a5a9-599f5a8ee6bd.jpeg)
## 4.2、技术架构

下图展现了神领物流项目使用的主要的技术：

![](https://cdn.nlark.com/yuque/0/2022/jpeg/27683667/1665997873162-a4c9ea61-f71e-4111-862e-efa01fd35e7b.jpeg)
# 5、功能演示
## 5.1、需求文档
下面将演示四端的主要功能，更多的功能具体查看各端的需求文档。

| 用户端 | [https://share.lanhuapp.com/#/invite?sid=qx01hbI7](https://share.lanhuapp.com/#/invite?sid=qx01hbI7)      密码: UxGE |
| --- | --- |
| 快递员端 | [https://share.lanhuapp.com/#/invite?sid=qxe42Dya](https://share.lanhuapp.com/#/invite?sid=qxe42Dya)     密码: Nomz |
| 司机端 | [https://share.lanhuapp.com/#/invite?sid=qX0NEmro](https://share.lanhuapp.com/#/invite?sid=qX0NEmro)   密码: yrzZ |
| 管理端 | [https://share.lanhuapp.com/#/invite?sid=qX0axVem](https://share.lanhuapp.com/#/invite?sid=qX0axVem)    密码: fh3i |

## 5.2、功能架构
![](https://cdn.nlark.com/yuque/0/2022/jpeg/27683667/1672389615739-d11e9b27-5241-49e4-bd52-2374f32a38be.jpeg)
## 5.3、业务功能流程

![](https://cdn.nlark.com/yuque/0/2022/jpeg/27683667/1667813584952-24320691-7837-4c72-97c1-2d99b7da71fe.jpeg)

:::info
流程说明：

- 用户在**【用户端】**下单后，生成订单
- 系统会根据订单生成**【取件任务】**，快递员上门取件后成功后生成**【运单】**
- 用户对订单进行支付，会产生**【交易单】**
- 快件开始运输，会经历起始营业部、分拣中心、转运中心、分拣中心、终点营业部之间的转运运输，在此期间会有多个**【运输任务】**
- 到达终点网点后，系统会生成**【派件任务】**，快递员进行派件作业
- 最后，用户将进行签收或拒收操作
:::
## 5.4、用户端
功能演示操作视频列表：

| 下单操作 | [点击查看](https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/0c8fc60a-2cf5-4140-9592-124cb3352fd0.mp4) |
| --- | --- |
| 取消订单 | [点击查看](https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/efd2553b-69ab-4ec1-ad71-f0fd27c84165.mp4) |
| 地址簿 | [点击查看](https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/1fcbdd1e-70bc-461c-9b0e-60ec75edbabb.mp4) |


![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666145111077-c453ef4e-3165-4085-8b70-f38c82be3d1a.png#averageHue=%23fffdfc&clientId=u20e031de-c504-4&errorMessage=unknown%20error&from=paste&height=554&id=u47bf6dc7&name=image.png&originHeight=914&originWidth=422&originalType=binary&ratio=1&rotation=0&showTitle=true&size=65584&status=error&style=shadow&taskId=u1b0ff756-a55e-439f-b7f0-c33bbaee655&title=%E7%94%A8%E6%88%B7%E4%B8%8B%E5%8D%95&width=255.75756097518683 "用户下单") 
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666145242663-e0a704ba-dcfa-4f99-a25f-1b7a3ec5f119.png#averageHue=%23bcbbbb&clientId=u20e031de-c504-4&errorMessage=unknown%20error&from=paste&height=554&id=u8fda641e&name=image.png&originHeight=914&originWidth=422&originalType=binary&ratio=1&rotation=0&showTitle=true&size=73294&status=error&style=shadow&taskId=u46922d9f-554f-42dd-a6ff-3207fab138a&title=%E4%BC%B0%E7%AE%97%E8%BF%90%E8%B4%B9&width=255.75756097518683 "估算运费") 
 ![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666145268551-72b01391-9f65-4a87-90c5-23cb3ffecaa6.png#averageHue=%23f4f4f5&clientId=u20e031de-c504-4&errorMessage=unknown%20error&from=paste&height=554&id=u6c53f021&name=image.png&originHeight=914&originWidth=422&originalType=binary&ratio=1&rotation=0&showTitle=true&size=72828&status=error&style=shadow&taskId=uf6899e53-240b-405b-8861-5841a9d082e&title=%E4%B8%8B%E5%8D%95%E6%88%90%E5%8A%9F&width=255.75756097518683 "下单成功")
## 5.5、快递员端

功能演示操作视频列表：

| 派件操作流程 | [点击查看](https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/7bb3000d-69b8-473f-9d6b-d391b8c28a9f.mp4) |
| --- | --- |
| 取件操作流程 | [点击查看](https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/7767cda8-8e83-4c5c-a976-634815ec0a10.mp4) |
| 全部取派操作流程 | [点击查看](https://outin-ffd84744973f11eb806300163e038793.oss-cn-beijing.aliyuncs.com/sv/605f258-1844feb861d/605f258-1844feb861d.mp4) |
| 搜索操作流程 | [点击查看](https://outin-ffd84744973f11eb806300163e038793.oss-cn-beijing.aliyuncs.com/sv/60a0b1bf-1845000a4d0/60a0b1bf-1845000a4d0.mp4) |
| 消息操作流程 | [点击查看](https://outin-ffd84744973f11eb806300163e038793.oss-cn-beijing.aliyuncs.com/sv/38c12638-18450c563db/38c12638-18450c563db.mp4) |

![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666147847509-b0623617-3ea2-4293-b9a0-7ba0dc51e076.png#averageHue=%23f6f4f4&clientId=u75175024-45c1-4&errorMessage=unknown%20error&from=paste&height=552&id=uc1e98e30&name=image.png&originHeight=1624&originWidth=750&originalType=url&ratio=1&rotation=0&showTitle=true&size=179302&status=error&style=shadow&taskId=u7bfb3afe-8511-47ff-969f-9af839c481e&title=%E5%8F%96%E4%BB%B6%E4%BB%BB%E5%8A%A1%E5%88%97%E8%A1%A8&width=255 "取件任务列表")
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666147783779-ced836d1-62c2-4fac-aa26-9cf6e5180138.png#averageHue=%23f7f3f3&clientId=u75175024-45c1-4&errorMessage=unknown%20error&from=paste&height=773&id=uab55be5a&name=image.png&originHeight=2274&originWidth=750&originalType=url&ratio=1&rotation=0&showTitle=true&size=171292&status=error&style=shadow&taskId=uc986bcae-248b-461d-9573-315d5d79d5c&title=%E5%8E%BB%E5%8F%96%E4%BB%B6&width=255 "去取件")
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666148205970-20236142-d3aa-455a-8b18-0b5438d6b4e5.png#averageHue=%23eae9e9&clientId=u75175024-45c1-4&errorMessage=unknown%20error&from=paste&height=552&id=u72db6544&name=image.png&originHeight=1624&originWidth=750&originalType=url&ratio=1&rotation=0&showTitle=true&size=132740&status=error&style=shadow&taskId=u9f79792c-f3a3-45c0-ba55-6a34aeef709&title=%E6%89%AB%E6%8F%8F%E6%94%AF%E4%BB%98&width=255 "扫描支付")
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666148251950-460e944c-26a7-47d2-819f-6bb03022d98b.png#averageHue=%23f3f3f3&clientId=u75175024-45c1-4&errorMessage=unknown%20error&from=paste&height=552&id=u5644a74f&name=image.png&originHeight=1624&originWidth=750&originalType=url&ratio=1&rotation=0&showTitle=true&size=51074&status=error&style=shadow&taskId=u5d19cb93-7812-4d20-b6fe-267addd175e&title=%E5%8F%96%E4%BB%B6%E6%88%90%E5%8A%9F&width=255 "取件成功")
## 5.6、司机端

[点击查看演示视频](https://outin-ffd84744973f11eb806300163e038793.oss-cn-beijing.aliyuncs.com/sv/4ffdd092-184501a12ff/4ffdd092-184501a12ff.mp4)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666148843502-dbfb5e33-0aec-43be-992a-1005cf84d63d.png#averageHue=%23f6f4f3&clientId=u75175024-45c1-4&errorMessage=unknown%20error&from=paste&height=552&id=u881fa848&name=image.png&originHeight=1624&originWidth=750&originalType=url&ratio=1&rotation=0&showTitle=true&size=166160&status=error&style=shadow&taskId=ue9c50999-5066-4605-ae28-ffcfe21a3d0&title=%E5%8F%B8%E6%9C%BA%E8%BF%90%E8%BE%93%E4%BB%BB%E5%8A%A1&width=255 "司机运输任务")
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666148927871-7a8bfa02-3a6f-4c9a-86f8-ae2355750ee1.png#averageHue=%23f7f6f5&clientId=u75175024-45c1-4&errorMessage=unknown%20error&from=paste&height=579&id=u27edc02a&name=image.png&originHeight=1702&originWidth=750&originalType=url&ratio=1&rotation=0&showTitle=true&size=135650&status=error&style=shadow&taskId=ud2af17ef-d255-45ae-bce5-ff942ef9026&title=%E4%BB%BB%E5%8A%A1%E8%AF%A6%E6%83%85&width=255 "任务详情")
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666149092451-d7eb65a3-f0dc-4a9e-b1b3-147a265d126f.png#averageHue=%23f0e9e7&clientId=u75175024-45c1-4&errorMessage=unknown%20error&from=paste&height=560&id=u9bc1359c&name=image.png&originHeight=1624&originWidth=740&originalType=url&ratio=1&rotation=0&showTitle=true&size=235207&status=error&style=shadow&taskId=ub2c27ca7-b533-492d-b89b-be154210f4e&title=%E6%8F%90%E8%B4%A7%E6%88%90%E5%8A%9F%EF%BC%88%E8%BF%90%E8%BE%93%E5%BC%80%E5%A7%8B%EF%BC%89&width=255 "提货成功（运输开始）")
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666149148556-d9866b4c-746e-470d-a536-ef4e8298a9d8.png#averageHue=%23f7f1f1&clientId=u75175024-45c1-4&errorMessage=unknown%20error&from=paste&height=552&id=u7b37a577&name=image.png&originHeight=1624&originWidth=750&originalType=url&ratio=1&rotation=0&showTitle=true&size=73287&status=error&style=shadow&taskId=u7273f8ff-3deb-42bb-8c28-a50be982a13&title=%E5%88%B0%E8%BE%BE%E7%9B%AE%E7%9A%84%E5%9C%B0&width=255 "到达目的地")
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666149017437-ff6a070b-8562-4daa-8876-67e72b0f8554.png#averageHue=%23f8f8f7&clientId=u75175024-45c1-4&errorMessage=unknown%20error&from=paste&height=552&id=uea82a8a5&name=image.png&originHeight=1624&originWidth=750&originalType=url&ratio=1&rotation=0&showTitle=true&size=76515&status=error&style=shadow&taskId=u230efa33-9f41-4d76-895d-a914367c161&title=%E5%A6%82%E6%9E%9C%E6%9C%89%E5%BC%82%E5%B8%B8%E5%8F%AF%E4%BB%A5%E8%BF%9B%E8%A1%8C%E4%B8%8A%E6%8A%A5&width=255 "如果有异常可以进行上报")
## 5.7、后台管理系统
功能演示操作视频列表：

| 建立机构 | [点击查看](https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/44443260-b57f-41f8-a1f2-22c44b1c16c1.mp4) |
| --- | --- |
| 新建员工 | [点击查看](https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/ab24e727-9c1f-458c-a8c3-b2d3cbfce46d.mp4) |
| 绘制作业范围 | [点击查看](https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/9a1e3679-38eb-4585-b41b-7d9277dc1da0.mp4) |
| 新建线路 | [点击查看](https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/cd62d82c-7910-4df0-835b-08854ecb0e79.mp4) |
| 启用车辆 | [点击查看](https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/5d8adc94-201f-43ee-8ef1-7906e5d8f272.mp4) |

![QQ图片20230114151916.png](https://cdn.nlark.com/yuque/0/2023/png/28217986/1673680992033-4e0d2021-7318-4667-8243-b980711e2cdf.png#averageHue=%23fdf8f4&clientId=u0b008fb9-82fc-4&from=paste&height=1479&id=u593de249&name=QQ%E5%9B%BE%E7%89%8720230114151916.png&originHeight=1972&originWidth=1899&originalType=binary&ratio=1&rotation=0&showTitle=true&size=348248&status=done&style=shadow&taskId=u9e60a9a6-8cd7-41d5-b1fa-6a3aa140b15&title=%E5%B7%A5%E4%BD%9C%E5%8F%B0&width=1424 "工作台")![image.png](https://cdn.nlark.com/yuque/0/2023/png/28217986/1673681344577-4f59ebcc-4b21-4076-ba64-ef1bee8bb22c.png#averageHue=%23fdfcfb&clientId=u5467c59d-305d-4&from=paste&height=619&id=u98bf5c5a&name=image.png&originHeight=825&originWidth=1898&originalType=binary&ratio=1&rotation=0&showTitle=true&size=100076&status=done&style=shadow&taskId=ue1626a0e-ae64-4e02-89ff-f1e6071ec26&title=%E8%BD%A6%E8%BE%86%E7%AE%A1%E7%90%86&width=1424 "车辆管理")
![image.png](https://cdn.nlark.com/yuque/0/2023/png/28217986/1673681441709-cd4f06cb-b65f-45c0-a9e4-da18b0da2a0d.png#averageHue=%23fdfcfb&clientId=u5467c59d-305d-4&from=paste&height=911&id=uacf9df64&name=image.png&originHeight=1214&originWidth=1890&originalType=binary&ratio=1&rotation=0&showTitle=true&size=181877&status=done&style=shadow&taskId=u4bcde4ca-2ef2-49f2-8c66-2f44ee7899f&title=%E8%AE%A2%E5%8D%95%E7%AE%A1%E7%90%86&width=1418 "订单管理")
![image.png](https://cdn.nlark.com/yuque/0/2023/png/28217986/1673681517364-16df33a4-9a48-405d-be13-2c9214ee8028.png#averageHue=%23fdfcfb&clientId=u5467c59d-305d-4&from=paste&height=573&id=ue03a6758&name=image.png&originHeight=764&originWidth=1879&originalType=binary&ratio=1&rotation=0&showTitle=true&size=99026&status=done&style=shadow&taskId=ue56fece5-ed2e-43a5-b1b4-4b203d1fa4a&title=%E5%8F%B8%E6%9C%BA%E7%AE%A1%E7%90%86&width=1409 "司机管理")
![image.png](https://cdn.nlark.com/yuque/0/2023/png/28217986/1673681584167-010c4de4-a89a-4db1-8913-c77b7bfb302c.png#averageHue=%23fdfaf9&clientId=u5467c59d-305d-4&from=paste&height=639&id=u51c29d53&name=image.png&originHeight=852&originWidth=1878&originalType=binary&ratio=1&rotation=0&showTitle=true&size=114462&status=done&style=shadow&taskId=u2b9845f8-ca99-4f52-af48-4e542ceb079&title=%E6%8E%92%E7%8F%AD%E7%AE%A1%E7%90%86&width=1409 "排班管理")
![image.png](https://cdn.nlark.com/yuque/0/2023/png/28217986/1673681630034-08d98831-7274-4cab-a0e8-826ca34e62bf.png#averageHue=%23fdfcfb&clientId=u5467c59d-305d-4&from=paste&height=662&id=u6392d187&name=image.png&originHeight=883&originWidth=1880&originalType=binary&ratio=1&rotation=0&showTitle=true&size=144461&status=done&style=shadow&taskId=u94d03047-1ff2-4f90-b6c3-6aa48155b22&title=%E8%BF%90%E8%BE%93%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86&width=1410 "运输任务管理")
![image.png](https://cdn.nlark.com/yuque/0/2023/png/28217986/1673681710302-70138a00-4dc0-44ba-90d4-fcb2d7226ed4.png#averageHue=%23fdfbfb&clientId=u5467c59d-305d-4&from=paste&height=691&id=uf3e2975c&name=image.png&originHeight=922&originWidth=1891&originalType=binary&ratio=1&rotation=0&showTitle=true&size=137537&status=done&style=shadow&taskId=ude666272-5dc4-4ad3-89c1-77249b710df&title=%E7%BA%BF%E8%B7%AF%E7%AE%A1%E7%90%86&width=1418 "线路管理")
![image.png](https://cdn.nlark.com/yuque/0/2023/png/28217986/1673681799739-d8cccc7a-2d0f-4d99-9ed5-4a2e5eadeffc.png#averageHue=%23ecb68e&clientId=u5467c59d-305d-4&from=paste&height=755&id=u1adb8b6b&name=image.png&originHeight=1007&originWidth=1876&originalType=binary&ratio=1&rotation=0&showTitle=true&size=135586&status=done&style=shadow&taskId=u9067930d-fe64-4eaa-bb81-69fdb22bd30&title=%E8%BF%90%E5%8D%95%E7%AE%A1%E7%90%86&width=1407 "运单管理")
![image.png](https://cdn.nlark.com/yuque/0/2023/png/28217986/1673681882850-4e26eb0c-532c-499e-8d70-56b1b05c4fe4.png#averageHue=%23e9c391&clientId=u5467c59d-305d-4&from=paste&height=610&id=ub3b6b4e8&name=image.png&originHeight=813&originWidth=1883&originalType=binary&ratio=1&rotation=0&showTitle=true&size=98741&status=done&style=shadow&taskId=ub02572a2-fb9c-4aac-84b6-fd4987c2e52&title=%E6%9C%BA%E6%9E%84%E7%AE%A1%E7%90%86&width=1412 "机构管理")
![image.png](https://cdn.nlark.com/yuque/0/2023/png/28217986/1673681922716-5a9002a3-4cf4-4048-b9ae-85d76445bf68.png#averageHue=%23ea9972&clientId=u5467c59d-305d-4&from=paste&height=416&id=u19319be1&name=image.png&originHeight=554&originWidth=1882&originalType=binary&ratio=1&rotation=0&showTitle=true&size=60460&status=done&style=shadow&taskId=u09f7e073-d4ae-4f1a-8e7f-295f355a012&title=%E8%BF%90%E8%B4%B9%E7%AE%A1%E7%90%86&width=1412 "运费管理")
# 6、开发环境
## 6.1、开发模式

在神领物流开发团队中，采用了分组协作开发的模式，整个开发团队分为5个小组，每个小组4~5人，不同的分组负责不同的微服务。

开发环境分为本地开发环境、测试环境、生成环境：

- **本地开发环境：**自己的电脑环境
- **测试环境：**在内网中搭建的一套大家都可以访问使用的环境
- **生成环境：**最终给用户使用的环境
## 6.2、团队分工

目前神领物流项目拥有**19**个微服务，**1**个网关，**1**个parent工程，**2**个公共依赖工程，这些工程由上述的**5**个小组共同维护开发。

**新入职的你，加入到了开发一组。**

| 开发组/负责模块 | 开发一组 | 开发二组 | 开发三组 | 开发四组 | 开发五组 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| [sl-express-parent](http://git.sl-express.com/sl/sl-express-parent.git) | ● |  |  |  |  | 父工程 |
| [sl-express-common](http://git.sl-express.com/sl/sl-express-common.git) | ● |  |  |  |  | 通用工程 |
| [sl-express-mq](http://git.sl-express.com/sl/sl-express-mq.git) | ● |  |  |  |  | 统一消息代码 |
| [sl-express-gateway](http://git.sl-express.com/sl/sl-express-gateway.git) | ● |  |  |  |  | 网关 |
| [sl-express-ms-base](http://git.sl-express.com/sl/sl-express-ms-base-service.git) | ● |  |  |  |  | 基础微服务 |
| [sl-express-ms-carriage](http://git.sl-express.com/sl/sl-express-ms-carriage-service.git) |  | ● |  |  |  | 运费微服务 |
| [sl-express-ms-courier](http://git.sl-express.com/sl/sl-express-ms-courier-service.git) |  | ● |  |  |  | 快递员微服务 |
| [sl-express-ms-dispatch](http://git.sl-express.com/sl/sl-express-ms-dispatch-service.git) |  |  | ● |  |  | 调度微服务 |
| [sl-express-ms-driver](http://git.sl-express.com/sl/sl-express-ms-driver-service.git) |  |  |  | ● |  | 司机微服务 |
| [sl-express-ms-oms](http://git.sl-express.com/sl/sl-express-ms-oms-service.git) |  | ● |  |  |  | 订单微服务 |
| [sl-express-ms-service-scope](http://git.sl-express.com/sl/sl-express-ms-service-scope-service.git) |  |  |  | ● |  | 服务范围微服务 |
| [sl-express-ms-sms](http://git.sl-express.com/sl/sl-express-ms-sms-service.git) |  |  |  | ● |  | 短信微服务 |
| [sl-express-ms-track](http://git.sl-express.com/sl/sl-express-ms-track-service.git) |  | ● |  |  |  | 轨迹微服务 |
| [sl-express-ms-trade](http://git.sl-express.com/sl/sl-express-ms-trade-service.git) |  |  | ● |  |  | 支付微服务 |
| [sl-express-ms-transport](http://git.sl-express.com/sl/sl-express-ms-transport-service.git) |  |  | ● |  |  | 路线微服务 |
| [sl-express-ms-transport-info](http://git.sl-express.com/sl/sl-express-ms-transport-info-service.git) |  |  | ● |  |  | 物流信息微服务 |
| [sl-express-ms-user](http://git.sl-express.com/sl/sl-express-ms-user-service.git) |  |  |  |  | ● | 用户微服务 |
| [sl-express-ms-web-courier](http://git.sl-express.com/sl/sl-express-ms-web-courier.git) |  | ● |  |  |  | 快递员web服务 |
| [sl-express-ms-web-customer](http://git.sl-express.com/sl/sl-express-ms-web-customer.git) |  |  |  |  | ● | 用户web服务 |
| [sl-express-ms-web-driver](http://git.sl-express.com/sl/sl-express-ms-web-driver.git) |  |  |  | ● |  | 司机web服务 |
| [sl-express-ms-web-manager](http://git.sl-express.com/sl/sl-express-ms-web-manager.git) |  |  |  |  | ● | 后台web服务 |
| [sl-express-ms-work](http://git.sl-express.com/sl/sl-express-ms-work-service.git) |  |  | ● |  |  | 运单微服务 |
| [sl-express-ms-search](http://git.sl-express.com/sl/sl-express-ms-search-service.git) |  |  |  |  | ● | 搜索微服务 |

:::info
**思考：**是否需要把所有的工程代码都拉取到本地进行编译运行？
 
不需要的。你只需要将自己将要负责的开发任务相关的代码拉取到本地进行开发即可，其他的服务都可以调用测试环境正在运行的服务。

 另外，你有可能是没有权限拉取到其他开发组的代码的。
:::

微服务间调用关系如下：
![](https://cdn.nlark.com/yuque/0/2022/jpeg/27683667/1668339834117-bce9f84f-3716-4807-8b9d-fb05c8f57479.jpeg)
> 可以看到，四个端的请求都会统一进入网关，再分发到对应的四个web微服务，再由web微服务请求其他微服务完成业务。


[查看微服务间的依赖关系](https://sl-express.itheima.net/#/zh-cn/modules/%E7%A5%9E%E9%A2%86%E7%89%A9%E6%B5%81%E5%BE%AE%E6%9C%8D%E5%8A%A1%E4%BE%9D%E8%B5%96%E5%85%B3%E7%B3%BB)
## 6.3、软件环境

为了模拟企业中的开发环境，所以我们需要通过VMware导入linux虚拟机，该虚拟机中已经安装了课程中所需要的各种环境，包括，git、maven私服、Jenkins、MySQL、RabbitMQ等。

:::info
关于JDK版本的说明：神领物流项目使用的JDK版本为11，需要同学们统一下环境，JDK11的安装包在资料中有提供。
目录：资料\软件包\jdk-11.0.15.1_windows-x64_bin.exe
:::
> 🔔关闭本地开发环境的防火墙（很重要！）

### 6.3.1、导入虚拟机

具体参考文档：[虚拟机导入手册](https://www.yuque.com/zhangzhijun-91vgw/xze2gr/gav9r8x8kvd2eyxg)
:::danger
注意：只要按照文档导入虚拟机即可，其他软件无需自己安装，都已经安装了，并且开机自启。
:::
![image-20220728162541120.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666004497851-777b646d-7019-45ec-bf88-6add87d7ac95.png#averageHue=%230f121c&clientId=u1bd8b6a9-44e9-4&errorMessage=unknown%20error&from=paste&height=477&id=uc77b4c2a&name=image-20220728162541120.png&originHeight=787&originWidth=1902&originalType=binary&ratio=1&rotation=0&showTitle=false&size=110155&status=error&style=shadow&taskId=u7892ae94-fe46-4310-9861-42c5bd95a25&title=&width=1152.7272061014346)
:::info
通过dps命令可以查询上述列表，dps是自定义命令。
自定义命令方法如下：
:::
```shell
vim ~/.bashrc

#增加如下内容
alias dps='docker ps --format "table{{.ID}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}"'
#保存退出

#生效
source ~/.bashrc
```
### 6.3.2、配置本机hosts
在本机hosts文件中设置如下配置：

```shell
192.168.150.101 git.sl-express.com
192.168.150.101 maven.sl-express.com
192.168.150.101 jenkins.sl-express.com
192.168.150.101 auth.sl-express.com
192.168.150.101 rabbitmq.sl-express.com
192.168.150.101 nacos.sl-express.com
192.168.150.101 neo4j.sl-express.com
192.168.150.101 xxl-job.sl-express.com
192.168.150.101 eaglemap.sl-express.com
192.168.150.101 seata.sl-express.com
192.168.150.101 skywalking.sl-express.com
192.168.150.101 api.sl-express.com
192.168.150.101 admin.sl-express.com
```

打开浏览器测试：[http://git.sl-express.com/](http://git.sl-express.com/)
![image-20220728164743695.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666004677962-4ae83c79-8dde-409c-b87a-321113980a57.png#averageHue=%23f4efee&clientId=u1bd8b6a9-44e9-4&errorMessage=unknown%20error&from=paste&height=579&id=u922e86bd&name=image-20220728164743695.png&originHeight=955&originWidth=1646&originalType=binary&ratio=1&rotation=0&showTitle=false&size=182792&status=error&style=shadow&taskId=u1c9a6913-96d9-4f3c-b068-9b9802008f1&title=&width=997.5756999174349)

看到这个页面就说明hosts已经生效。（也可以再试试其他的，比如：maven私服、jenkins等）
### 6.3.3、服务列表

:::danger
说明：如果通过域名访问，无需设置端口。
:::
| 名称 | 地址 | 用户名/密码 | 端口 |
| --- | --- | --- | --- |
| git | [http://git.sl-express.com/](http://git.sl-express.com/) | sl/sl123 | 10880 |
| maven | [http://maven.sl-express.com/nexus/](http://maven.sl-express.com/nexus/) | admin/admin123 | 8081 |
| jenkins | [http://jenkins.sl-express.com/](http://jenkins.sl-express.com/) | root/123 | 8090 |
| 权限管家 | [http://auth.sl-express.com/api/authority/static/index.html](http://auth.sl-express.com/api/authority/static/index.html) | admin/123456 | 8764 |
| RabbitMQ | [http://rabbitmq.sl-express.com/](http://rabbitmq.sl-express.com/) | sl/sl321 | 15672 |
| MySQL | - | root/123 | 3306 |
| nacos | [http://nacos.sl-express.com/nacos/](http://nacos.sl-express.com/nacos/) | nacos/nacos | 8848 |
| neo4j | [http://neo4j.sl-express.com/browser/](http://neo4j.sl-express.com/browser/) | neo4j/neo4j123 | 7474 |
| xxl-job | [http://xxl-job.sl-express.com/xxl-job-admin](http://xxl-job.sl-express.com/xxl-job-admin) | admin/123456 | 28080 |
| EagleMap | [http://eaglemap.sl-express.com/](http://eaglemap.sl-express.com/) | eagle/eagle | 8484 |
| seata | [http://seata.sl-express.com/](http://seata.sl-express.com/) | seata/seata | 7091 |
| Gateway | [http://api.sl-express.com/](http://api.sl-express.com/) | - | 9527 |
| admin | [http://admin.sl-express.com/](http://admin.sl-express.com/) | - | 80 |
| skywalking | [http://skywalking.sl-express.com/](http://skywalking.sl-express.com/) | - | 48080 |
| Redis | - | 123321 | 6379 |
| MongoDB | - | sl/123321 | 27017 |

### 6.3.4、配置Maven私服
在本地的maven（建议版本为3.6.x）配置中配置上述的私服，配置文件参考如下：
settings.xml文件：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings
        xmlns="http://maven.apache.org/SETTINGS/1.0.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

    <!-- 本地仓库 -->
    <localRepository>F:\maven\repository</localRepository>

    <!-- 配置私服中deploy的账号 -->
    <servers>
        <server>
            <id>sl-releases</id>
            <username>deployment</username>
            <password>deployment123</password>
        </server>
        <server>
            <id>sl-snapshots</id>
            <username>deployment</username>
            <password>deployment123</password>
        </server>
    </servers>

    <!-- 使用阿里云maven镜像，排除私服资源库 -->
    <mirrors>
        <mirror>
            <id>mirror</id>
            <mirrorOf>central,jcenter,!sl-releases,!sl-snapshots</mirrorOf>
            <name>mirror</name>
            <url>https://maven.aliyun.com/nexus/content/groups/public</url>
        </mirror>
    </mirrors>

    <profiles>
        <profile>
            <id>sl</id>
            <!-- 配置项目deploy的地址 -->
            <properties>
                <altReleaseDeploymentRepository>
                    sl-releases::default::http://maven.sl-express.com/nexus/content/repositories/releases/
                </altReleaseDeploymentRepository>
                <altSnapshotDeploymentRepository>
                    sl-snapshots::default::http://maven.sl-express.com/nexus/content/repositories/snapshots/
                </altSnapshotDeploymentRepository>
            </properties>
            <!-- 配置项目下载依赖的私服地址 -->
            <repositories>
                <repository>
                    <id>sl-releases</id>
                    <url>http://maven.sl-express.com/nexus/content/repositories/releases/</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
                <repository>
                    <id>sl-snapshots</id>
                    <url>http://maven.sl-express.com/nexus/content/repositories/snapshots/</url>
                    <releases>
                        <enabled>false</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>
            </repositories>
        </profile>
    </profiles>

    <activeProfiles>
         <!-- 激活配置 -->
        <activeProfile>sl</activeProfile>
    </activeProfiles>

</settings>
```

### 6.3.5、服务版本
| **服务名** | **版本号** |
| --- | --- |
| sl-express-parent | 1.4 |
| sl-express-common | 1.2-SNAPSHOT |
| 其他微服务 | 1.1-SNAPSHOT |

# 7、开发任务
## 7.1、任务描述

接下来是你加入到开发一组后接到的第一个任务，具体内容是：
后台管理系统只允许管理员登录，非管理员（司机或快递员）是没有权限登录的，现在的情况是，任何角色的人都能登录到后台管理系统，应该是当非管理员登录时需要提示没有权限。
这个可以算是一个bug修复的工作。接下来，你需要思考下，该如何解决这个问题。

解决步骤：

- 先确定鉴权工作是在哪里完成的 
   - 通过前面的系统架构，可以得知是在网关中完成的
- 拉取到网关的代码
- 阅读鉴权的业务逻辑
- 了解权限系统
- 动手编码解决问题
- 部署，功能测试

### 7.1.1、部署后台管理系统
后台管理系统的部署是使用101机器的Jenkins部署的，具体参考[前端部署文档](https://www.yuque.com/zhangzhijun-91vgw/xze2gr/rhyie35xipdqk9dg)。部署完成后，就可以看到登录页面。
地址：[http://admin.sl-express.com/](http://admin.sl-express.com/)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666251505172-046baf07-c207-4bbd-b8cc-a9bb5191605f.png#averageHue=%2363707a&clientId=uc8580d77-409b-4&errorMessage=unknown%20error&from=paste&height=491&id=ue28f3cf3&name=image.png&originHeight=942&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=827773&status=error&style=shadow&taskId=ue24b599e-5bde-4c43-8344-5cb02cc2d5d&title=&width=1000)
可以看到这个页面是可以正常访问，只是没有获取到验证码，是因为验证码的获取是需要后端服务支撑的，目前并没有启动后端服务。
### 7.1.2、部署后端服务
后端服务需要启动如下几个服务：
![](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666099328254-3ef3aaea-3625-4746-9bce-4de51e18d11e.png#averageHue=%230f131d&from=url&id=hP6Pk&originHeight=500&originWidth=1665&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=shadow&title=)
目前，只启动了`itcast-auth-server`，其他均未启动：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666252164143-a74f35fc-6c41-41fe-832f-d6366a92d20d.png#averageHue=%230f131d&clientId=uca6ec329-9de3-4&errorMessage=unknown%20error&from=paste&height=473&id=u466a77d2&name=image.png&originHeight=709&originWidth=1678&originalType=binary&ratio=1&rotation=0&showTitle=false&size=96264&status=error&style=shadow&taskId=uca6eb4a2-8e29-4130-abb7-581d3426041&title=&width=1118.6666666666667)
所以需要在Jenkins中，依次启动这几个服务，类似如下构建（需要找到对应的构建任务进行构建）：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666252520245-0c60cae3-0033-4ba0-9f4b-0a85b784b396.png#averageHue=%23fefefe&clientId=uca6ec329-9de3-4&errorMessage=unknown%20error&from=paste&height=535&id=udb06cde9&name=image.png&originHeight=802&originWidth=1876&originalType=binary&ratio=1&rotation=0&showTitle=false&size=84837&status=error&style=shadow&taskId=u3a9b3593-9bd7-424f-b76e-44c0ea6764b&title=&width=1250.6666666666667)

启动完成：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666252464036-4912fcb9-df39-4b29-8c84-8ff6cef27272.png#averageHue=%230f131d&clientId=uca6ec329-9de3-4&errorMessage=unknown%20error&from=paste&height=475&id=u022fc67b&name=image.png&originHeight=712&originWidth=1658&originalType=binary&ratio=1&rotation=0&showTitle=false&size=98131&status=error&style=shadow&taskId=ud7bfefab-ec84-4e40-8ef7-fb04a465a07&title=&width=1105.3333333333333)
在nacos中已经完成了服务的注册：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666252575158-2aaebfc1-0039-46a4-ac3d-1e1161543253.png#averageHue=%23f9f9f9&clientId=uca6ec329-9de3-4&errorMessage=unknown%20error&from=paste&height=429&id=u16e1a4c1&name=image.png&originHeight=643&originWidth=1609&originalType=binary&ratio=1&rotation=0&showTitle=false&size=88224&status=error&style=shadow&taskId=ue66425af-6f6d-4828-a3dd-c45952f154f&title=&width=1072.6666666666667)
### 7.1.3、测试
刷新后台管理系统页面，即可成功看到验证码，说明所需要的服务已经启动完成了。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666252651737-41297cfc-bbf2-4151-9d23-d0a309f17fe5.png#averageHue=%23e25d3d&clientId=uca6ec329-9de3-4&errorMessage=unknown%20error&from=paste&height=522&id=rJbTQ&name=image.png&originHeight=783&originWidth=769&originalType=binary&ratio=1&rotation=0&showTitle=false&size=157679&status=error&style=shadow&taskId=u834c86aa-5350-4b69-aec1-12bd58073a7&title=&width=512.6666666666666)
使用默认账号，shenlingadmin/123456 即可完成登录：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666252699199-a5b921f6-3edd-4a5a-a188-928b3b9dbac8.png#averageHue=%23fbe1b4&clientId=uca6ec329-9de3-4&errorMessage=unknown%20error&from=paste&height=609&id=u794215a8&name=image.png&originHeight=914&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=221360&status=error&style=shadow&taskId=u4bb635a1-ff31-4642-8dc9-e74481067d3&title=&width=1280)

使用非管理员账号进行测试，例如：gzsj/123456 （司机账号） 或  hdkdy001/123456 （快递员账号） 进行测试，发现依然是可以登录的。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666253052325-87925e01-138c-4633-8594-fda6c564327a.png#averageHue=%23fbe1b5&clientId=uca6ec329-9de3-4&errorMessage=unknown%20error&from=paste&height=608&id=ua1997973&name=image.png&originHeight=912&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=212383&status=error&style=shadow&taskId=u9c3fe7b0-e092-4adb-ba09-2da62729f9c&title=&width=1280)
这样，这个问题就重现了。
## 7.2、拉取代码

拉取代码步骤：

- 在本地创建 sl-express 文件夹，该目录存放项目课程期间所有的代码
- 启动idea，打开该目录
![image-20220728213318073.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666007637689-dd449a6a-4dab-4b24-9908-f4bc5b4aad6b.png#averageHue=%23f0efee&clientId=u1bd8b6a9-44e9-4&errorMessage=unknown%20error&from=paste&height=183&id=u6a13abd7&name=image-20220728213318073.png&originHeight=302&originWidth=615&originalType=binary&ratio=1&rotation=0&showTitle=false&size=18335&status=error&style=shadow&taskId=u9570c859-93cf-426e-8bda-4462dc3ec9f&title=&width=372.7272511842178)
- 登录 [git](http://git.sl-express.com/) 服务，找到 sl-express-gateway 工程，拷贝地址，在idea中拉取代码（注意存储路径）
![image-20220728213450406.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666007654827-955e1cba-cfef-41c1-a2e2-3ca73232f24a.png#averageHue=%23f2f1f0&clientId=u1bd8b6a9-44e9-4&errorMessage=unknown%20error&from=paste&height=135&id=u0f17f2c5&name=image-20220728213450406.png&originHeight=222&originWidth=901&originalType=binary&ratio=1&rotation=0&showTitle=false&size=10676&status=error&style=shadow&taskId=ua1a93fbe-3460-4ce4-90d0-85061008ade&title=&width=546.0605744991549)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1667827609764-eba4105e-341d-4866-8cdd-dc54a59251b7.png#averageHue=%23eeedeb&clientId=u19bd6eb7-b922-4&from=paste&height=115&id=ub7c21c3b&name=image.png&originHeight=172&originWidth=695&originalType=binary&ratio=1&rotation=0&showTitle=false&size=12955&status=done&style=shadow&taskId=u6c479e32-f829-443d-8b3a-8a8aee1efd4&title=&width=463.3333333333333)
- 拉取到代码后，设置jdk版本为11
![image-20220728213637258.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666007665915-2c652140-6472-4a9e-ac3f-c1cf77aefc7e.png#averageHue=%23efeeee&clientId=u1bd8b6a9-44e9-4&errorMessage=unknown%20error&from=paste&height=612&id=uec99917a&name=image-20220728213637258.png&originHeight=1009&originWidth=1535&originalType=binary&ratio=1&rotation=0&showTitle=false&size=71634&status=error&style=shadow&taskId=u9d5c4673-fe57-45e9-b53c-a8835656272&title=&width=930.3029765329663)
- 添加modules，将sl-express-gateway加入进来
![image-20220728213945708.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666007677419-776caa3e-6378-434c-acbf-49f0a8c58614.png#averageHue=%23e1bb7a&clientId=u1bd8b6a9-44e9-4&errorMessage=unknown%20error&from=paste&height=245&id=ucd53d710&name=image-20220728213945708.png&originHeight=405&originWidth=687&originalType=binary&ratio=1&rotation=0&showTitle=false&size=23189&status=error&style=shadow&taskId=uaa2aab6a-d02f-4783-b2ff-8760f59542a&title=&width=416.3636122984677)
- 成功拉取代码
![image-20220728214019842.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666007688480-f46fcc43-e911-4fa7-aa92-01b063e62948.png#averageHue=%23faf9f9&clientId=u1bd8b6a9-44e9-4&errorMessage=unknown%20error&from=paste&height=176&id=u315dbf17&name=image-20220728214019842.png&originHeight=291&originWidth=578&originalType=binary&ratio=1&rotation=0&showTitle=false&size=16798&status=error&style=shadow&taskId=u7ac012c0-74d1-4c3e-891f-df4e1065f36&title=&width=350.3030100560616)
- 说明：该工程会依赖 sl-express-parent，此maven依赖是通过[私服](http://maven.sl-express.com/nexus/)拉取到的。
## 7.3、权限管家

在神领物流项目中，快递员、司机、管理人员都是在权限管家中进行管理的，所以他们的登录都是需要对接权限管家完成的。

具体权限管家的介绍说明参见：[权限管家使用说明](https://www.yuque.com/zhangzhijun-91vgw/xze2gr/pseyizoo073plvox)
## 7.4、测试用户登录

前面已经了解了权限管家，接下来我们将基于权限管家在`sl-express-gateway`中进行测试用户的登录以及对于token的校验。
### 7.4.1、依赖说明
对接权限管家需要引入依赖：

```xml
<dependency>
    <groupId>com.itheima.em.auth</groupId>
    <artifactId>itcast-auth-spring-boot-starter</artifactId>
</dependency>
```

:::info
该依赖已经导入，并且在parent中指定了版本号。
 
该依赖已经上传到maven中央仓库，可以直接下载，地址：[https://mvnrepository.com/artifact/com.itheima.em.auth/itcast-auth-spring-boot-starter](https://mvnrepository.com/artifact/com.itheima.em.auth/itcast-auth-spring-boot-starter)
:::
### 7.4.2、解读配置
在bootstrap-local.yml配置文件中配置了nacos配置中心，一些参数存放到了nacos中，这些参数一般都是不同环境不一样配置的。

sl-express-gateway.properties如下：
```properties
#权限系统的配置
authority.host = 192.168.150.101
authority.port = 8764
authority.timeout = 10000
#应用id
authority.applicationId = 981194468570960001

#角色id
role.manager = 986227712144197857,989278284569131905,996045142395786081,996045927523359809
#快递员角色
role.courier = 989559057641637825
#司机角色
role.driver = 989559028277315009

#RSA公钥
sl.jwt.public-key = MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDC6of/EqnM2008gRpFAJJd3iGF5o6P6SuJOcKq4iJQ+62EF4WKGIGQunJjPwVNQFqDuT7ko9bRFZNnMba9A5GrFELtAK7tzO9l19JgFcCBQoU3J6ehPCCunRKz52qJuzS0yiJp0dbB2i6cb7mSCftwZavmcpzhsBaOGQd23AnAmQIDAQAB
```

其中applicationId、角色id都是需要在权限系统中找到。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666057155954-b5f2346f-1ce9-47d3-a953-1030e55bd743.png#averageHue=%23fefbfa&clientId=u1bd8b6a9-44e9-4&errorMessage=unknown%20error&from=paste&height=547&id=u48e64faa&name=image.png&originHeight=902&originWidth=856&originalType=binary&ratio=1&rotation=0&showTitle=false&size=44704&status=error&style=shadow&taskId=ud163f95b-618c-4896-8a78-7ef84572588&title=&width=518.7878488027486)

角色id需要在数据库表中查询，表为：itcast_auth.itcast_auth_role
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666271250310-08a996fb-1b5c-450c-9a8f-5ec1425e7745.png#averageHue=%23fdfdfc&clientId=u224c9508-8741-4&errorMessage=unknown%20error&from=paste&height=175&id=u3d7bcce3&name=image.png&originHeight=262&originWidth=382&originalType=binary&ratio=1&rotation=0&showTitle=false&size=14944&status=error&style=shadow&taskId=u10bba48f-3924-40e0-9ff1-5d1bfd0db12&title=&width=254.66666666666666)
### 7.4.3、测试
测试用例在AuthTemplateTest中：

```java
    @Test
    public void testLogin() {
        //登录
        Result<LoginDTO> result = this.authTemplate.opsForLogin()
                .token("shenlingadmin", "123456");

        String token = result.getData().getToken().getToken();
        System.out.println("token为：" + token);

        UserDTO user = result.getData().getUser();
        System.out.println("user信息：" + user);

        //查询角色
        Result<List<Long>> resultRole = AuthTemplateFactory.get(token).opsForRole()
                .findRoleByUserId(user.getId());
        System.out.println(resultRole);
    }
```

token校验测试：

```java
    @Test
    public void checkToken() {
        String token = "xxx.xx.xxx"; //上面方法中生成的token
        AuthUserInfoDTO authUserInfo = this.tokenCheckService.parserToken(token);
        System.out.println(authUserInfo);

        System.out.println(JSONUtil.toJsonStr(authUserInfo));
    }
```

:::danger
**说明：**权限管家生成的token采用的是RSA非对称加密方式，项目中配置的公钥一定要与权限系统中使用的公钥一致，否则会出现无法校验token的情况。
:::
![image-20220729185656492.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666057311886-c7516890-51dd-4cfb-a3d5-f211a6715942.png#averageHue=%230f131c&clientId=u1bd8b6a9-44e9-4&errorMessage=unknown%20error&from=paste&height=207&id=uff14fc96&name=image-20220729185656492.png&originHeight=342&originWidth=1103&originalType=binary&ratio=1&rotation=0&showTitle=false&size=36471&status=error&style=shadow&taskId=uf7d45a53-3ffc-4a20-92d3-d3ff50a196a&title=&width=668.484809847467)
项目中的公钥文件：
![image-20220729185825534.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666057329815-5a88c5f3-e8e4-49b3-b9e3-8391548a4464.png#averageHue=%23f9f8f7&clientId=u1bd8b6a9-44e9-4&errorMessage=unknown%20error&from=paste&height=293&id=u021155eb&name=image-20220729185825534.png&originHeight=484&originWidth=536&originalType=binary&ratio=1&rotation=0&showTitle=false&size=24012&status=error&style=shadow&taskId=u08b3caad-0f4b-498d-9b0b-36d14353213&title=&width=324.84846607274915)
## 7.5、阅读鉴权代码
### 7.5.1、整体流程
首先需要明确的一点是四个终端都是通过`sl-express-gateway`进行验证与鉴权的，下面以管理员请求流程为例，其他的流程类似。
![](https://cdn.nlark.com/yuque/__puml/8a57e6729921b0ddcb659a114ed9d633.svg#lake_card_v2=eyJ0eXBlIjoicHVtbCIsImNvZGUiOiJAc3RhcnR1bWxcblxuYXV0b251bWJlclxuXG5hY3RvciBcIueuoeeQhuWRmFwiIGFzIFVzZXJcbnBhcnRpY2lwYW50IFwiTWFuYWdlclRva2VuR2F0ZXdheUZpbHRlckZhY3RvcnlcIiBhcyBGYWN0b3J5XG5wYXJ0aWNpcGFudCBcIlRva2VuR2F0ZXdheUZpbHRlclwiIGFzIEZpbHRlclxucGFydGljaXBhbnQgXCJzbC1leHByZXNzLW1zLXdlYi1tYW5hZ2VyXCIgYXMgTWFuYWdlclxuXG5hY3RpdmF0ZSBVc2VyXG5cblVzZXIgLT4gRmFjdG9yeTogL21hbmFnZXIvKipcbmFjdGl2YXRlIEZhY3RvcnlcblxuRmFjdG9yeSAtPiBGaWx0ZXI6IOaehOmAoFRva2VuR2F0ZXdheUZpbHRlcuWvueixoVxuYWN0aXZhdGUgRmlsdGVyXG5cbkZpbHRlciAtPiBGaWx0ZXI6IOaJp-ihjGZpbHRlcigp5pa55rOVXG5cbkZpbHRlciAtPiBGaWx0ZXI6IOagoemqjOaYr-WQpuS4uuaUvuihjHBhdGhcblVzZXIgLy8tLSBGaWx0ZXI6IOaXoOmcgOagoemqjOeahOaUvuihjFxuXG5GaWx0ZXIgLT4gRmFjdG9yeTog5omn6KGMQXV0aEZpbHRlcuaOpeWPo-eahGNoZWNrKCnmlrnms5VcblxuRmFjdG9yeSAtPiBGYWN0b3J5OiDpgJrov4fmnYPpmZDns7vnu5_moKHpqox0b2tlblxuXG5GYWN0b3J5IC0-IEZpbHRlcjog6L-U5Zue5qCh6aqM57uT5p6cXG5cbkZpbHRlciAtPiBGaWx0ZXI6IOWIpOaWree7k-aenOaYr-WQpuS4uuepulxuVXNlciAvLy0tIEZpbHRlcjog5Li656m65ZON5bqUNDAxXG5GaWx0ZXIgLT4gRmFjdG9yeTog5omn6KGMQXV0aEZpbHRlcuaOpeWPo-eahGF1dGgoKeaWueazlVxuXG5GYWN0b3J5IC0-IEZhY3Rvcnk6IOafpeivouinkuiJsui_m-ihjOagoemqjFxuRmFjdG9yeSAtPiBGaWx0ZXI6IOi_lOWbnkJvb2xlYW7lgLxcblVzZXIgLy8tLSBGaWx0ZXI6IGZhbHNl77ya5ZON5bqUNDAwXG5GaWx0ZXIgLT4gTWFuYWdlcjogdHJ1ZTog6L2s5Y-R6K-35rGCXG5hY3RpdmF0ZSBNYW5hZ2VyXG5NYW5hZ2VyIC0-IE1hbmFnZXI6IOS4muWKoeWkhOeQhlxuTWFuYWdlciAtPiBVc2VyOiDlk43lupTnu5PmnpxcblxuQGVuZHVtbCIsInVybCI6Imh0dHBzOi8vY2RuLm5sYXJrLmNvbS95dXF1ZS9fX3B1bWwvOGE1N2U2NzI5OTIxYjBkZGNiNjU5YTExNGVkOWQ2MzMuc3ZnIiwiaWQiOiJQUjMxUSIsIm1hcmdpbiI6eyJ0b3AiOnRydWUsImJvdHRvbSI6dHJ1ZX0sImhlaWdodCI6MzQzLCJ3aWR0aE1vZGUiOiJjb250YWluIiwiY2FyZCI6ImRpYWdyYW0ifQ==)### 7.5.2、自定义过滤器
不同终端进入Gateway的请求路径是不一样的，并且不同的终端对于token的校验和鉴权逻辑是不一样的，所以需要在网关中对于各个终端创建不同的过滤器来实现。
请求路径如下：

- 快递员端：`/courier/**`
- 用户端：`/customer/**`
- 司机端：`/driver/**`
- 管理端：`/manager/**`

具体的配置文件内容如下：
```yaml
server:
  port: 9527
  tomcat:
    uri-encoding: UTF-8
    threads:
      max: 1000
      min-spare: 30
spring:
  cloud:
    nacos:
      username: nacos
      password: nacos
      server-addr: 192.168.150.101:8848
      discovery:
        namespace: ecae68ba-7b43-4473-a980-4ddeb6157bdc
        ip: 192.168.150.1
      config:
        namespace: ecae68ba-7b43-4473-a980-4ddeb6157bdc
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowed-origin-patterns: "*"
            allowed-headers: "*"
            allow-credentials: true
            allowed-methods:
              - GET
              - POST
              - DELETE
              - PUT
              - OPTION
      discovery:
        locator:
          enabled: true #表明gateway开启服务注册和发现的功能，并且spring cloud gateway自动根据服务发现为每一个服务创建了一个router，这个router将以服务名开头的请求路径转发到对应的服务
      routes:
        - id: sl-express-ms-web-courier
          uri: lb://sl-express-ms-web-courier
          predicates:
            - Path=/courier/**
          filters:
            - StripPrefix=1
            - CourierToken
            - AddRequestHeader=X-Request-From, sl-express-gateway
        - id: sl-express-ms-web-customer
          uri: lb://sl-express-ms-web-customer
          predicates:
            - Path=/customer/**
          filters:
            - StripPrefix=1
            - CustomerToken
            - AddRequestHeader=X-Request-From, sl-express-gateway
        - id: sl-express-ms-web-driver
          uri: lb://sl-express-ms-web-driver
          predicates:
            - Path=/driver/**
          filters:
            - StripPrefix=1
            - DriverToken
            - AddRequestHeader=X-Request-From, sl-express-gateway
        - id: sl-express-ms-web-manager
          uri: lb://sl-express-ms-web-manager
          predicates:
            - Path=/manager/**
          filters:
            - StripPrefix=1
            - ManagerToken
            - AddRequestHeader=X-Request-From, sl-express-gateway
        - id: sl-express-ms-trade
          uri: lb://sl-express-ms-trade
          predicates:
            - Path=/trade/notify/**
          filters:
            - StripPrefix=1
            - AddRequestHeader=X-Request-From, sl-express-gateway
itcast:
  authority:
    host: ${authority.host} #authority服务地址,根据实际情况更改
    port: ${authority.port} #authority服务端口
    timeout: ${authority.timeout} #http请求的超时时间
    public-key-file: auth/pub.key
    applicationId: ${authority.applicationId}

#角色id
role:
  manager: ${role.manager}
  courier: ${role.courier}
  driver: ${role.driver}

sl:
  noAuthPaths:
    - /courier/login/account
    - /courier/swagger-ui.html
    - /courier/webjars/
    - /courier/swagger-resources
    - /courier/v2/api-docs
    - /courier/doc.html
    - /customer/user/login
    - /customer/user/refresh
    - /customer/swagger-ui.html
    - /customer/webjars/
    - /customer/swagger-resources
    - /customer/v2/api-docs
    - /customer/doc.html
    - /driver/login/account
    - /driver/swagger-ui.html
    - /driver/webjars/
    - /driver/swagger-resources
    - /driver/v2/api-docs
    - /driver/doc.html
    - /manager/login
    - /manager/webjars/
    - /manager/swagger-resources
    - /manager/v2/api-docs
    - /manager/doc.html
    - /manager/captcha
  jwt:
    public-key: ${sl.jwt.user-secret-key}
```
可以看到，在配置文件中配置了注册中心、cors跨域、自定义过滤器、自定义配置、白名单路径等信息。
其中，自定义过滤器配置了4个，与处理类对应关系如下：

- CourierToken **->** com.sl.gateway.filter.CourierTokenGatewayFilterFactory
- CustomerToken **->** com.sl.gateway.filter.CustomerTokenGatewayFilterFactory
- DriverToken **-> **com.sl.gateway.filter.DriverTokenGatewayFilterFactory
- ManagerToken **-> **com.sl.gateway.filter.ManagerTokenGatewayFilterFactory

在GatewayFilterFactory中，继承AbstractGatewayFilterFactory类，实现GatewayFilterFactory接口中的apply()方法，返回GatewayFilter对象，即可在filter()方法中实现具体的业务逻辑。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1671072914178-081939c0-98fc-419c-b091-60cae7e2bd5e.png#averageHue=%23fdfdfc&clientId=u7fb52c93-0df2-4&from=paste&height=453&id=u3c344a38&name=image.png&originHeight=680&originWidth=1733&originalType=binary&ratio=1&rotation=0&showTitle=false&size=58041&status=done&style=shadow&taskId=u06e88cbf-dcbb-47f0-808f-26a5133b80e&title=&width=1155.3333333333333)
具体的业务逻辑，在自定义`TokenGatewayFilter`类中完成。
:::info
**❓思考：**
四个终端都共用`TokenGatewayFilter`类，而各个终端的校验逻辑是不一样的，该怎么做呢？
:::
### 7.5.3、TokenGatewayFilter

`TokenGatewayFilter`过滤器是整个项目中的校验/ 鉴权流程的具体实现，由于存在不同的终端，导致具体的校验和鉴权逻辑不一样，所以需要通过自定义接口`AuthFilter`实现，也就是4个端的`TokenGatewayFilterFactory`同时也需要实现`AuthFilter`接口。

在向下游服务转发请求时，会携带2个头信息，分别是userInfo和token，也就是会将用户信息和token传递下去。

```java
package com.sl.gateway.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.itheima.auth.sdk.dto.AuthUserInfoDTO;
import com.sl.gateway.config.MyConfig;
import com.sl.transport.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class TokenGatewayFilter implements GatewayFilter, Ordered {

    private MyConfig myConfig;
    private AuthFilter authFilter;

    public TokenGatewayFilter(MyConfig myConfig, AuthFilter authFilter) {
        this.myConfig = myConfig;
        this.authFilter = authFilter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求路径
        String path = exchange.getRequest().getPath().toString();
        //查看请求路径是否在白名单中
        if (StrUtil.startWithAny(path, myConfig.getNoAuthPaths())) {
            //无需校验，直接放行
            return chain.filter(exchange);
        }

        //获取header的参数
        String token = exchange.getRequest().getHeaders().getFirst(this.authFilter.tokenHeaderName());
        if (StrUtil.isEmpty(token)) {
            //没有权限
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        //校验token
        AuthUserInfoDTO authUserInfoDTO = null;
        try { //捕获token校验异常
            authUserInfoDTO = this.authFilter.check(token);
        } catch (Exception e) {
            log.error("令牌校验失败，token = {}, path= {}", token, path, e);
        }
        if (ObjectUtil.isEmpty(authUserInfoDTO)) {
            //token失效 或 伪造，响应401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        //鉴权
        Boolean result = false;
        try { //捕获鉴权异常
            result = this.authFilter.auth(token, authUserInfoDTO, path);
        } catch (Exception e) {
            log.error("权限校验失败，token = {}, path= {}", token, path, e);
        }
        if (!result) {
            //没有权限，响应400
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        //增加参数，向下游微服务传递参数
        exchange.getRequest().mutate().header(Constants.GATEWAY.USERINFO, JSONUtil.toJsonStr(authUserInfoDTO));
        exchange.getRequest().mutate().header(Constants.GATEWAY.TOKEN, token);

        //校验通过放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        //指定了拦截器的顺序，设置最小值确定第一个被执行
        return Integer.MIN_VALUE;
    }

}

```
### 7.5.4、AuthFilter

AuthFilter是自定义接口，用于不同客户端（用户端、司机端、快递员端、管理端）校验/鉴权逻辑的实现，该接口定义了3个方法：

- `check()`方法用于校验token
- `auth()`方法用于鉴权
- `tokenHeaderName()`方法是默认实现，默认请求头中token参数的名为：Authorization
- 执行流程是先校验token的有效性，再进行鉴权。

```java
package com.sl.gateway.filter;

import com.itheima.auth.sdk.dto.AuthUserInfoDTO;
import com.sl.transport.common.constant.Constants;

/**
 * 鉴权业务的回调，具体逻辑由 GatewayFilterFactory 具体完成
 */
public interface AuthFilter {

    /**
     * 校验token
     *
     * @param token 请求中的token
     * @return token中携带的数据
     */
    AuthUserInfoDTO check(String token);

    /**
     * 鉴权
     *
     * @param token        请求中的token
     * @param authUserInfo token中携带的数据
     * @param path         当前请求的路径
     * @return 是否通过
     */
    Boolean auth(String token, AuthUserInfoDTO authUserInfo, String path);

    /**
     * 请求中携带token的名称
     *
     * @return 头名称
     */
    default String tokenHeaderName() {
        return Constants.GATEWAY.AUTHORIZATION;
    }

}

```
### 7.5.5、管理员校验实现

```java
package com.sl.gateway.filter;

import com.itheima.auth.sdk.dto.AuthUserInfoDTO;
import com.itheima.auth.sdk.service.TokenCheckService;
import com.sl.gateway.config.MyConfig;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 后台管理员token拦截处理
 */
@Component
public class ManagerTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> implements AuthFilter {

    @Resource
    private MyConfig myConfig;
    @Resource
    private TokenCheckService tokenCheckService;

    @Override
    public GatewayFilter apply(Object config) {
        //由于实现了AuthFilter接口，所以可以传递this对象到TokenGatewayFilter中
        return new TokenGatewayFilter(this.myConfig, this);
    }

    @Override
    public AuthUserInfoDTO check(String token) {
        //校验token
        return tokenCheckService.parserToken(token);
    }

    @Override
    public Boolean auth(String token, AuthUserInfoDTO authUserInfoDTO, String path) {
        return true;
    }
}

```

:::info
**🔔分析：**
由于`auth()`方法直接返回true，导致所有角色都能通过校验，也就是所有角色的用户都能登录到后台管理系统，这里就是bug原因的根本所在。
:::
## 7.6、解决bug
### 7.6.1、实现
:::info
**思路：**
想让管理员角色的用户通过，而非管理员角色不能通过，这里就需要对auth()方法进行实现了，现在的实现是都返回true，那当然是所有的都通过了。
所以，需要查询出当前用户的角色，查看是否具备管理员角色，如果有就放行，否则拒绝。
:::

具体代码实现：
```java
package com.sl.gateway.filter;

import cn.hutool.core.collection.CollUtil;
import com.itheima.auth.factory.AuthTemplateFactory;
import com.itheima.auth.sdk.AuthTemplate;
import com.itheima.auth.sdk.dto.AuthUserInfoDTO;
import com.itheima.auth.sdk.service.TokenCheckService;
import com.sl.gateway.config.MyConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 后台管理员token拦截处理
 */
@Component
public class ManagerTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> implements AuthFilter {

    @Resource
    private MyConfig myConfig;
    @Resource
    private TokenCheckService tokenCheckService;
    //获取配置文件中的管理员角色id
    @Value("${role.manager}")
    private List<Long> managerRoleIds;

    @Override
    public GatewayFilter apply(Object config) {
        //由于实现了AuthFilter接口，所以可以传递this对象到TokenGatewayFilter中
        return new TokenGatewayFilter(this.myConfig, this);
    }

    @Override
    public AuthUserInfoDTO check(String token) {
        //校验token
        return tokenCheckService.parserToken(token);
    }

    @Override
    public Boolean auth(String token, AuthUserInfoDTO authUserInfoDTO, String path) {
        //获取AuthTemplate对象
        AuthTemplate authTemplate = AuthTemplateFactory.get(token);
        //查询该用户的角色
        List<Long> roleIds = authTemplate.opsForRole().findRoleByUserId(authUserInfoDTO.getUserId()).getData();

        //取交集，判断是否有交集即可判断出是否有权限
        Collection<Long> intersection = CollUtil.intersection(roleIds, this.managerRoleIds);
        return CollUtil.isNotEmpty(intersection);
    }
}

```

### 7.6.2、测试
测试分两种方法，分别是接口测试和功能测试，我们首先进行功能测试，然后再进行接口测试（swagger接口）。
测试无误后，可以将代码提交到git中。
#### 7.6.2.1、功能测试
由于本地启动服务后，会在nacos中注册了2个服务：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1673000387771-a3fdf43e-a487-4696-b281-98a623724d4a.png#averageHue=%23e1fad7&clientId=u2404c223-f0da-4&from=paste&height=163&id=u510de01e&name=image.png&originHeight=245&originWidth=1542&originalType=binary&ratio=1&rotation=0&showTitle=false&size=33909&status=done&style=shadow&taskId=u75a908cc-0f8b-464c-83b0-d039ea9ede5&title=&width=1028)
所以需要将101服务器上的网关停止掉再进行测试。`docker stop sl-express-gateway`
另外，需要修改101服务器上的nginx配置，让 api.sl-express.com 对应的服务指向到本地的9527端口服务（测试完成后再改回来）。
修改nginx配置：
```shell
cd /usr/local/src/nginx/conf
vim nginx.conf
#由于目前nginx正在运行中，nginx.conf是只读的，所以需要通过 wq! 命令强制保存

#配置生效
nginx -s reload
```
修改内容如下：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1673005349696-52501b13-a69c-4ae8-8594-ef42657a877d.png#averageHue=%230e111a&clientId=u0281ffb0-0259-4&from=paste&height=467&id=uee748a43&name=image.png&originHeight=700&originWidth=1159&originalType=binary&ratio=1&rotation=0&showTitle=false&size=50025&status=done&style=shadow&taskId=u0e50badd-242c-4c10-8056-46cd04953ca&title=&width=772.6666666666666)
使用司机账号进行测试：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666277077045-79bd2d07-3ea1-4e4a-9db5-b4107ee293b6.png#averageHue=%23e0e9d4&clientId=u224c9508-8741-4&errorMessage=unknown%20error&from=paste&height=570&id=lH03i&name=image.png&originHeight=855&originWidth=806&originalType=binary&ratio=1&rotation=0&showTitle=false&size=148924&status=error&style=shadow&taskId=u17c93b40-875e-4645-bfd7-734f465b958&title=&width=537.3333333333334)
可以看到，司机账号无法登录。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666277369985-40b129ad-b269-4bf3-bcf0-64e2367811c2.png#averageHue=%23eac998&clientId=u224c9508-8741-4&errorMessage=unknown%20error&from=paste&height=341&id=mQEBe&name=image.png&originHeight=512&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=84573&status=error&style=shadow&taskId=ue12f4e94-0f94-4775-977c-c9411e0b2fa&title=&width=1280)
#### 7.6.2.2、接口测试
测试步骤：

- 首先，测试管理员的登录，获取到token
- 接着测试管理员请求接口资源（期望结果：正常获取到数据）
- 更换成司机用户进行登录，并且测试请求接口资源（期望结果：响应400，没有权限）

将本地Gateway服务启动起来，访问 [http://127.0.0.1:9527/manager/doc.html](http://127.0.0.1:9527/manager/doc.html) 即可看到【管理后台微服务接口文档】
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666276029943-8150b0c3-7a00-4725-8da2-ba68400e839d.png#averageHue=%23c7b593&clientId=u224c9508-8741-4&errorMessage=unknown%20error&from=paste&height=609&id=u97351d36&name=image.png&originHeight=914&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=147631&status=error&style=shadow&taskId=u911a0e53-49a1-482b-90b6-dd6e2feee0f&title=&width=1280)
随便测试个接口，会发现响应401：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666276115483-983cb826-348a-44fe-9b9b-ac175f6e5905.png#averageHue=%23c8b795&clientId=u224c9508-8741-4&errorMessage=unknown%20error&from=paste&height=609&id=lxulY&name=image.png&originHeight=914&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=108791&status=error&style=shadow&taskId=u0585d885-a972-4c2c-8db4-970078c8a54&title=&width=1280)
测试登录接口，需要先获取验证码再进行登录：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666276219995-cbf6e779-4bb3-4256-ad1b-d603f9aa68a0.png#averageHue=%23d0bd98&clientId=u224c9508-8741-4&errorMessage=unknown%20error&from=paste&height=609&id=u4183282a&name=image.png&originHeight=914&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=142895&status=error&style=shadow&taskId=u4f96387d-1343-425f-bc55-75a98760d28&title=&width=1280)
登录成功：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666276282928-ee4a4c72-9587-4b2a-a8f2-712449a3222f.png#averageHue=%23d1bd98&clientId=u224c9508-8741-4&errorMessage=unknown%20error&from=paste&height=609&id=ue0237ab0&name=image.png&originHeight=914&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=143125&status=error&style=shadow&taskId=u64011794-f803-4ffd-a571-210d81f5cba&title=&width=1280)
获取到token：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666276303877-9b46a698-6684-40ac-b6b1-e34d8f387488.png#averageHue=%23fdfdfd&clientId=u224c9508-8741-4&errorMessage=unknown%20error&from=paste&height=169&id=uc0df00ac&name=image.png&originHeight=254&originWidth=1210&originalType=binary&ratio=1&rotation=0&showTitle=false&size=33908&status=error&style=shadow&taskId=u110d78fa-d4d0-4f45-b4e7-91ca4b6ce6b&title=&width=806.6666666666666)
设置请求头：Authorization
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666276525767-767f1096-f85c-46bb-bd96-33cf16404a71.png#averageHue=%23e0d8b9&clientId=u224c9508-8741-4&errorMessage=unknown%20error&from=paste&height=609&id=ubd4616ab&name=image.png&originHeight=914&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=195982&status=error&style=shadow&taskId=uaedec1e1-acc8-4cbc-9440-df6f5d867aa&title=&width=1280)
进行功能测试：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666276504615-b4bcb37b-0e5c-4744-b49d-a28d74aea880.png#averageHue=%23c8b795&clientId=u224c9508-8741-4&errorMessage=unknown%20error&from=paste&height=609&id=u015889db&name=image.png&originHeight=914&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=152944&status=error&style=shadow&taskId=u31761581-07b8-4e2e-82fd-0f607d2f853&title=&width=1280)
更换成司机账户测试：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666276749051-87770590-a7b7-4fe7-b07b-0be22694fea6.png#averageHue=%23cab895&clientId=u224c9508-8741-4&errorMessage=unknown%20error&from=paste&height=609&id=u3cb031e2&name=image.png&originHeight=914&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=112511&status=error&style=shadow&taskId=u351de8b8-d935-430f-b40e-b30387df902&title=&width=1280)
会发现，更换成司机账户后会响应400，符合我们的预期。
### 7.6.3、部署

项目的发布，我们采用Jenkins持续集成的方式，在提供的虚拟机中已经部署好了Jenkins，我们只需要进行简单的操作即可完成部署。
第一步，浏览器打开：[http://jenkins.sl-express.com/](http://jenkins.sl-express.com/)  （账号：root/123）
第二步，按照如下数字标识进行操作
![image-20220806102651465.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666060443196-1c23a711-5ff8-45d5-8d58-8293818876ed.png#averageHue=%23f4f5f7&clientId=u1bde193b-1ead-4&errorMessage=unknown%20error&from=paste&height=416&id=u527808a4&name=image-20220806102651465.png&originHeight=686&originWidth=1350&originalType=binary&ratio=1&rotation=0&showTitle=false&size=101239&status=error&style=shadow&taskId=uc4a43cf8-7d65-4060-95a8-58f7c56277b&title=&width=818.1817708921853)
选择默认参数：
![image-20220806102721738.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666060453073-bc03811a-b0eb-461d-9135-008f4cd56eec.png#averageHue=%23fdfdfd&clientId=u1bde193b-1ead-4&errorMessage=unknown%20error&from=paste&height=438&id=uf66b63e3&name=image-20220806102721738.png&originHeight=722&originWidth=1069&originalType=binary&ratio=1&rotation=0&showTitle=false&size=40173&status=error&style=shadow&taskId=uff3c7932-7298-41ca-9955-43a1d8c3ea3&title=&width=647.8787504324046)
第三步，查看部署控制台，点击【sl-express-gateway】进入任务详情，左侧下方查看构建历史，点击最近的一个构建图标：
![image-20220801091004278.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666060479867-168b48e4-2076-4ab4-a334-a0c6e988aa40.png#averageHue=%23f2f2f1&clientId=u1bde193b-1ead-4&errorMessage=unknown%20error&from=paste&height=211&id=uc21cf5d9&name=image-20220801091004278.png&originHeight=348&originWidth=531&originalType=binary&ratio=1&rotation=0&showTitle=false&size=33146&status=error&style=shadow&taskId=u08fde4b7-c81d-4ad8-88c4-e815362fcac&title=&width=321.81816321759294)
看到如下内容，说明以及部署成功。
![image-20220801091047892.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666060555697-30fc6e10-92fc-40c6-8b01-b44e5d6f74e2.png#averageHue=%23fcfbfa&clientId=u1bde193b-1ead-4&errorMessage=unknown%20error&from=paste&height=225&id=u3c579652&name=image-20220801091047892.png&originHeight=371&originWidth=1370&originalType=binary&ratio=1&rotation=0&showTitle=false&size=61481&status=error&style=shadow&taskId=u9c64bbc2-59f2-432e-b54c-5617184b4e8&title=&width=830.3029823128104)
部署成功后，可以进行正常功能测试。
# 8、课后练习
## 8.1、练习一：快递员的鉴权
难度系数：★☆☆☆☆

提示：快递员端的鉴权与管理端的鉴权类似，只是角色id不同。如果想要通过App进行登录测试，请参考[前端部署文档](https://www.yuque.com/zhangzhijun-91vgw/xze2gr/rhyie35xipdqk9dg)。
## 8.2、练习二：司机端的鉴权
难度系数：★☆☆☆☆

提示：司机端的鉴权与管理端的鉴权类似，只是角色id不同。如果想要通过App进行登录测试，请参考[前端部署文档](https://www.yuque.com/zhangzhijun-91vgw/xze2gr/rhyie35xipdqk9dg)。
# 9、面试连环问
:::info
面试官问：

- 简单介绍下你做的物流项目。
- 微服务项目团队如何协作？你们多少个小组开发？
- 项目中是如何进行持续集成的？提交git后如何自动进行构建？
- 说说统一网关中是如何进行认证与鉴权工作的？在网关中如何自定义过滤器？
- 项目中的用户权限是如何管理的？如何与权限管家对接？
:::
