# 课程安排
- 路线规划需求分析
- 熟悉路线规划项目工程
- 实现机构数据同步
- 实现路线管理
- 综合功能测试
# 1、背景说明
通过前面的Neo4j的学习，你已经基本掌握了Neo4j的使用，接下来就需要应用Neo4j来实现路线规划微服务了。
目前路线规划微服务中的代码框架基本上已经写好了，但是具体的实现并没有编写，所以就需要你来填充这些关键核心的代码。
![lg.gif](https://cdn.nlark.com/yuque/0/2022/gif/27683667/1661393495230-5ed7dd64-d9ed-4911-af64-6ac745e62281.gif#averageHue=%23f2eddf&clientId=ufeadf2ce-7102-4&errorMessage=unknown%20error&from=paste&height=145&id=ufdf67f2b&name=lg.gif&originHeight=240&originWidth=240&originalType=binary&ratio=1&rotation=0&showTitle=false&size=25250&status=error&style=none&taskId=u6ffe84dc-97a5-4342-a53c-be75fb9f891&title=&width=145.45453704749963)
# 2、需求分析
对于运输路线规划，总的需求有两个，一个是机构管理，一个是路线管理。
其中，机构的管理是需要与【权限管家】系统中的【组织管理】中的数据进行同步，该同步是需要通过MQ完成的。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1668684314048-72fd52ba-8472-4648-a83c-8d0f00070e14.png#averageHue=%23fefefe&clientId=u3603cbee-3ab6-4&from=paste&height=346&id=u7e6bf3a6&name=image.png&originHeight=519&originWidth=1387&originalType=binary&ratio=1&rotation=0&showTitle=false&size=34969&status=done&style=shadow&taskId=u09f81570-b4af-4a62-b35d-e80169b642e&title=&width=924.6666666666666)
路线管理，主要提供路线的维护，最核心的服务是提供路线规划查询服务。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/28217986/1673668095843-69f17b95-e142-4717-bd06-8d2bf15a4ee4.png#averageHue=%23fefcfb&clientId=u0af63341-747c-4&from=paste&height=498&id=u8edc9057&name=image.png&originHeight=498&originWidth=598&originalType=binary&ratio=1&rotation=0&showTitle=true&size=23821&status=done&style=shadow&taskId=u086090c5-4303-4cc2-86c9-499f7824445&title=%E6%96%B0%E5%A2%9E%E7%BA%BF%E8%B7%AF&width=598 "新增线路")
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661395537827-42d0c5fe-ba6e-4455-8714-cde074d13dc2.png#averageHue=%23e6c28e&clientId=u93f722b6-ba7f-4&errorMessage=unknown%20error&from=paste&height=476&id=u9ffd0723&name=image.png&originHeight=786&originWidth=1601&originalType=binary&ratio=1&rotation=0&showTitle=true&size=118643&status=error&style=shadow&taskId=u8bf1641b-2542-4dd9-ad60-7cbb3e67dd7&title=%E8%B7%AF%E7%BA%BF%E5%88%97%E8%A1%A8&width=970.3029742210288 "路线列表")
:::danger
需要说明的是，一个完整的运输路线是由多个转运节点组合完成的，并且每一个转运路线都是双向往返的，也就是A与B节点直接的路线必须是成对创建的。
:::
# 3、项目工程
## 3.1、拉取代码
拉取`sl-express-ms-transport`相关的代码：

| 工程名 | git地址 |
| --- | --- |
| sl-express-ms-transport-api | [http://git.sl-express.com/sl/sl-express-ms-transport-api](http://git.sl-express.com/sl/sl-express-ms-transport-api) |
| sl-express-ms-transport-domain | [http://git.sl-express.com/sl/sl-express-ms-transport-domain.git](http://git.sl-express.com/sl/sl-express-ms-transport-domain.git) |
| sl-express-ms-transport-service | [http://git.sl-express.com/sl/sl-express-ms-transport-service.git](http://git.sl-express.com/sl/sl-express-ms-transport-service.git) |

![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661409604471-a3d7b14b-3618-4b82-83c6-eec91a90cedf.png#averageHue=%23f1efed&clientId=uf78e1dbf-4cc4-4&errorMessage=unknown%20error&from=paste&height=81&id=ud22689cb&name=image.png&originHeight=133&originWidth=492&originalType=binary&ratio=1&rotation=0&showTitle=false&size=8740&status=error&style=shadow&taskId=u13498189-fbc1-4cfb-92f9-0628d400a91&title=&width=298.18180094737426)
## 3.2、配置文件
在配置文件中引入了如下共享配置：

| 文件名                        | 说明                                |
| -------------------------- | --------------------------------- |
| shared-spring-rabbitmq.yml | 关于rabbitmq的统一配置，其中有对于消息消费失败处理的配置项 |
| shared-spring-eaglemap.yml | 自研对接地图服务商的中台服务EagleMap的配置         |
| shared-spring-neo4j.yml    | neo4j的相关配置                        |

### 3.2.1、shared-spring-rabbitmq.yml
```yaml
#rabbitmq的基础配置
spring:
  rabbitmq: #mq的配置
    host: 192.168.150.101
    port: 5672
    username: sl
    password: sl321
    virtual-host: /dispatch
    publisher-confirm-type: correlated #发送消息的异步回调，记录消息是否发送成功
    publisher-returns: true #开启publish-return功能，消息到达交换机，但是没有到达对列表
    template:
      mandatory: true #消息路由失败时的策略, true: 调用ReturnCallback, false：丢弃消息
    listener:
      simple:
        acknowledge-mode: auto #，出现异常时返回nack，消息回滚到mq；没有异常，返回ack
        retry:
          enabled: true # 开启消费者失败重试
          initial-interval: 1000 # 初识的失败等待时长为1秒
          multiplier: 1 # 失败的等待时长倍数，下次等待时长 = multiplier * last-interval
          max-attempts: 3 # 最大重试次数
          stateless: true # true无状态；false有状态。如果业务中包含事务，这里改为false
```
### 3.2.2、shared-spring-eaglemap.yml
:::info
关于EagleMap使用，在后面讲解。
:::
```yaml
eagle:
  host: 192.168.150.101 #EagleMap服务地址
  port: 8484 #EagleMap服务端口
  timeout: 20000 #http请求的超时时间
```
### 3.2.3、shared-spring-neo4j.yml
```yaml
spring:
  data:
    neo4j:
      database: ${neo4j.database}
  neo4j:
    authentication:
      username: ${neo4j.username}
      password: ${neo4j.password}
    uri: ${neo4j.uri}
```
具体的参数值在`sl-express-transport.properties`文件中：
```properties
neo4j.uri=neo4j://192.168.150.101:7687
neo4j.username=neo4j
neo4j.password=neo4j123
neo4j.database=neo4j

jdbc.url = jdbc:mysql://192.168.150.101:3306/sl_transport?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&useSSL=false
jdbc.username = root
jdbc.password = 123

#权限系统对接的交换机
rabbitmq.exchange = itcast-auth
```
## 3.3、代码结构
下面是路线规划微服务代码结构，主要是实现下面选中的部分：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661410871116-6f4ce17e-cef8-48fc-8b70-272beda91f4f.png#averageHue=%23faf9f8&clientId=uf78e1dbf-4cc4-4&errorMessage=unknown%20error&from=paste&height=503&id=u1b9face6&name=image.png&originHeight=830&originWidth=622&originalType=binary&ratio=1&rotation=0&showTitle=false&size=44051&status=error&style=shadow&taskId=u51a75f2e-f39c-4a91-9618-d07cba5335d&title=&width=376.9696751814365)
:::danger
关于Entity，与sl-express-sdn工程的类似，只是属性多了一些，按照项目的业务需求制定的。
:::
Feign接口定义：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661602795290-b212b85e-df2d-4537-8817-b8de86115e3f.png#averageHue=%23fbfaf9&clientId=u4d76cb3d-b589-4&errorMessage=unknown%20error&from=paste&height=170&id=u500f73f6&name=image.png&originHeight=280&originWidth=635&originalType=binary&ratio=1&rotation=0&showTitle=false&size=12840&status=error&style=shadow&taskId=ueb447e7e-5932-4010-9823-00355675109&title=&width=384.8484626048428)
domain定义：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661602815385-111e2014-acf5-4d26-ab07-5db679b86488.png#averageHue=%23faf9f7&clientId=u4d76cb3d-b589-4&errorMessage=unknown%20error&from=paste&height=201&id=u7ebf2c74&name=image.png&originHeight=332&originWidth=591&originalType=binary&ratio=1&rotation=0&showTitle=false&size=17882&status=error&style=shadow&taskId=u44f63c0d-29b9-4c73-bec2-6ae6650c336&title=&width=358.18179747946783)
## 3.4、sl-express-mq
在项目中，为了统一使用RabbitMQ，所以将MQ的使用进行了封装，使用方法参考文档《[sl-express-mq使用手册](https://sl-express.itheima.net/#/zh-cn/modules/sl-express-mq)》
### 3.4.1、发送消息
对于发送消息的场景，正常情况没有问题，直接发送即可：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1677747565104-1479809b-38a7-4ee7-ab85-70e36e717016.png#averageHue=%23fcfcfb&clientId=u1be9e8bc-373d-4&from=paste&height=159&id=ud32b9dee&name=image.png&originHeight=239&originWidth=1197&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=22847&status=done&style=shadow&taskId=u95a12cd2-50a1-4f99-88ef-a1c2545fca1&title=&width=798)
如果是非正常情况就需要特殊处理了，一般会有三种非正常情况需要处理：

- 第一种情况，消息发送到交换机（exchange），但是没有队列与交换机绑定，消息会丢失。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1677747542317-e461628f-3269-44b5-b5e2-5ab7bd7e8a68.png#averageHue=%23fcfbfa&clientId=u1be9e8bc-373d-4&from=paste&height=171&id=u1d79372f&name=image.png&originHeight=257&originWidth=1222&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=26503&status=done&style=shadow&taskId=u304e0b28-f99a-459d-93c6-2040090801a&title=&width=814.6666666666666)
- 第二种情况，在消息的发送后进行确认，如果发送失败需要将消息持久化，例如：发送的交换机不存在的情况。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1677747891930-b527fd38-5616-4a6a-9ee3-bf4bb4dacee4.png#averageHue=%23fcf8f6&clientId=u1be9e8bc-373d-4&from=paste&height=149&id=u453dea89&name=image.png&originHeight=223&originWidth=1013&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=23533&status=done&style=shadow&taskId=ubccb3dc5-29fb-4427-b5b9-a443c58fc68&title=&width=675.3333333333334)
- 第三种情况，由于网络、MQ服务宕机等原因导致消息没有发送到MQ服务器。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1677748019068-47a2b11d-ca88-48eb-99aa-f956d901cee5.png#averageHue=%23fdf5f1&clientId=u1be9e8bc-373d-4&from=paste&height=127&id=u293aeaac&name=image.png&originHeight=191&originWidth=670&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=13055&status=done&style=shadow&taskId=u08cda024-cb1b-4632-b7af-077970ead2a&title=&width=446.6666666666667)

**第一种情况：**
对于消息只是到了交换机，并没有到达队列，这种情况记录日志即可，因为我们也不确定哪个队列需要这个消息。
配置如下（nacos中的`shared-spring-rabbitmq.yml`文件）：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1676427302100-873277d3-435c-4f1d-8911-f4306b027028.png#averageHue=%23222020&clientId=u6f0f8ddb-318f-4&from=paste&height=379&id=ufef3248a&name=image.png&originHeight=568&originWidth=1375&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=150968&status=done&style=shadow&taskId=u6995b5dc-479a-44dd-86dc-aa0c6a7906b&title=&width=916.6666666666666)
```java
package com.sl.mq.config;

import cn.hutool.core.util.StrUtil;
import com.sl.transport.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MessageConfig implements ApplicationContextAware {

    /**
     * 发送者回执 没有路由到队列的情况
     *
     * @param applicationContext 应用上下文
     * @throws BeansException 异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取RabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 设置ReturnCallback
        rabbitTemplate.setReturnsCallback(message -> {
            if (StrUtil.contains(message.getExchange(), Constants.MQ.DELAYED_KEYWORD)) {
                //延迟消息没有发到队列是正常情况，无需记录日志
                return;
            }
            // 投递失败，记录日志
            log.error("消息没有投递到队列，应答码：{}，原因：{}，交换机：{}，路由键：{},消息：{}",
                    message.getReplyCode(), message.getReplyText(), message.getExchange(), message.getRoutingKey(), message.getMessage());
        });
    }

}

```
**第二种情况：**
在配文件中开启配置`publisher-confirm-type`，即可在发送消息时添加回调方法：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1676427331425-de958f75-5448-4385-ad3a-35868b1c1eb0.png#averageHue=%23222020&clientId=u6f0f8ddb-318f-4&from=paste&height=381&id=u498a6541&name=image.png&originHeight=571&originWidth=1388&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=151290&status=done&style=shadow&taskId=uc0f44209-29b6-4b2c-ba8c-bdbfd798528&title=&width=925.3333333333334)
在代码中进行处理，将消息数据持久化到数据库中，后续通过xxl-job进行处理，将消息进行重新发送。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1672304142276-ef564ea6-dbfd-4f7d-ad23-77f0e0e85983.png#averageHue=%23fdfcfb&clientId=ub939c037-ffdd-4&from=paste&height=445&id=u23ee5b9f&name=image.png&originHeight=668&originWidth=1585&originalType=binary&ratio=1&rotation=0&showTitle=false&size=66901&status=done&style=shadow&taskId=udd2ab883-3c74-4f4d-90af-367b6bf3abe&title=&width=1056.6666666666667)
同样，如果出现异常情况也是将消息持久化：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1677727282231-d0f433b6-5466-4e06-b894-576f3d9bb584.png#averageHue=%23fdfcfb&clientId=uabbce504-f769-4&from=paste&height=294&id=u1e4534ca&name=image.png&originHeight=441&originWidth=1480&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=53023&status=done&style=shadow&taskId=udd86a478-f8bb-4fb1-b0e2-79013451a88&title=&width=986.6666666666666)
**第三种情况：**
将发送消息的代码进行try{}catch{}处理，如果出现异常会通过Spring-retry机制进重试，最多重试3次，如果依然失败就将消息数据进行持久化：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1677727332009-841bf175-d82e-491a-ad87-2c2baa676ec9.png#averageHue=%23fdfcfc&clientId=uabbce504-f769-4&from=paste&height=504&id=ude0fbf75&name=image.png&originHeight=756&originWidth=1571&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=71101&status=done&style=shadow&taskId=uaaa1eb79-3850-4e5f-be6e-0bc32bd5bde&title=&width=1047.3333333333333)
设置重试：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1677727362761-4ab68fae-f89d-43c5-bc3c-74325dc85672.png#averageHue=%23fcfbfa&clientId=uabbce504-f769-4&from=paste&height=189&id=uced0fd8f&name=image.png&originHeight=283&originWidth=1671&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=40401&status=done&style=shadow&taskId=u366409e1-410c-421f-be6a-6f9bda6b50d&title=&width=1114)
最终的落库操作：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1672304978965-69e906b5-fd6c-4b8b-9cbf-022a2382f2a7.png#averageHue=%23fdfcfc&clientId=ub939c037-ffdd-4&from=paste&height=232&id=ubdaa8996&name=image.png&originHeight=348&originWidth=1231&originalType=binary&ratio=1&rotation=0&showTitle=false&size=36698&status=done&style=shadow&taskId=u2e9d5254-ca1f-44aa-bc4c-0537831b8f2&title=&width=820.6666666666666)
xxl-job任务，主要负责从数据库中查询出错误消息数据然后进行重试：
```java
package com.sl.mq.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sl.mq.entity.FailMsgEntity;
import com.sl.mq.service.FailMsgService;
import com.sl.mq.service.MQService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 失败消息的处理任务
 */
@Slf4j
@Component
@ConditionalOnBean({MQService.class, FailMsgService.class})
public class FailMsgJob {

    @Resource
    private FailMsgService failMsgService;
    @Resource
    private MQService mqService;

    @XxlJob("failMsgJob")
    public void execute() {
        //查询失败的数据，每次最多处理100条错误消息
        LambdaQueryWrapper<FailMsgEntity> queryWrapper = new LambdaQueryWrapper<FailMsgEntity>()
                .orderByAsc(FailMsgEntity::getCreated)
                .last("limit 100");
        List<FailMsgEntity> failMsgEntityList = this.failMsgService.list(queryWrapper);
        if (CollUtil.isEmpty(failMsgEntityList)) {
            return;
        }

        for (FailMsgEntity failMsgEntity : failMsgEntityList) {
            try {
                //发送消息
                this.mqService.sendMsg(failMsgEntity.getExchange(), failMsgEntity.getRoutingKey(), failMsgEntity.getMsg());
                //删除数据
                this.failMsgService.removeById(failMsgEntity.getId());
            } catch (Exception e) {
                log.error("处理错误消息失败, failMsgEntity = {}", failMsgEntity);
            }
        }
    }
}

```
xxl-job中的任务调度：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1672305271514-512e097a-fb7a-4d84-981a-f22b2fbecae5.png#averageHue=%23e0c498&clientId=ub939c037-ffdd-4&from=paste&height=304&id=u4a2bc32e&name=image.png&originHeight=456&originWidth=1564&originalType=binary&ratio=1&rotation=0&showTitle=false&size=51664&status=done&style=shadow&taskId=ucf0c3047-a0b1-46be-a45b-2ca52c626fd&title=&width=1042.6666666666667)
### 3.4.2、消费消息
对于消息的消费，首先采用的自动确认策略：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1672306644098-25b4bd13-3309-4ff0-98b6-ac4450ff3d4c.png#averageHue=%23212020&clientId=ub939c037-ffdd-4&from=paste&height=77&id=uc082f893&name=image.png&originHeight=116&originWidth=1037&originalType=binary&ratio=1&rotation=0&showTitle=false&size=20829&status=done&style=shadow&taskId=u3cbb9f15-2b76-4920-a7d4-81627510d14&title=&width=691.3333333333334)
如果出现消费错误，会进行重试，最多重试3次：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1672306709957-86675680-af17-493f-835e-728e4fc6e05c.png#averageHue=%23242221&clientId=ub939c037-ffdd-4&from=paste&height=196&id=u798babde&name=image.png&originHeight=294&originWidth=1012&originalType=binary&ratio=1&rotation=0&showTitle=false&size=75808&status=done&style=shadow&taskId=u4c9a1035-4a51-4b2a-ad6b-f9feb4a6678&title=&width=674.6666666666666)
如果3次后依然失败，需要将消息发送到指定的队列，为了区分不同的微服务，所以会针对不同微服务创建不同的队列，但是交换机是同一个：
```java
package com.sl.mq.config;

import com.sl.transport.common.constant.Constants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorMessageConfig {

    @Value("${spring.application.name}") //获取微服务的名称
    private String appName;

    @Bean
    public TopicExchange errorMessageExchange() {
        //定义错误消息的交换机，类型为：topic
        return new TopicExchange(Constants.MQ.Exchanges.ERROR, true, false);
    }

    @Bean
    public Queue errorQueue() {
        //【前缀+微服务】名作为错误消息存放的队列名称，并且开启了持久化
        return new Queue(Constants.MQ.Queues.ERROR_PREFIX + appName, true);
    }

    @Bean
    public Binding errorBinding(Queue errorQueue, TopicExchange errorMessageExchange) {
        //完成绑定关系
        return BindingBuilder.bind(errorQueue).to(errorMessageExchange).with(appName);
    }

    @Bean
    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate) {
        //设置全部重试失败后进行重新发送消息，指定了交换机以及路由key
        //需要注意的是，路由key是应用名称，与上述的绑定关系中的路由key一致
        return new RepublishMessageRecoverer(rabbitTemplate, Constants.MQ.Exchanges.ERROR, appName);
    }
}

```
最终会以微服务名称创建队列：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1672307521272-1f1aeb65-8c4d-48c1-affc-3981359143fe.png#averageHue=%23ebe9e8&clientId=ub939c037-ffdd-4&from=paste&height=184&id=u44d199a4&name=image.png&originHeight=276&originWidth=1606&originalType=binary&ratio=1&rotation=0&showTitle=false&size=83220&status=done&style=shadow&taskId=u4e897646-8938-4b64-9830-c745c6d5a91&title=&width=1070.6666666666667)
其绑定关系如下：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1672307540927-59ab439e-ed6a-403e-ae73-12f1f7221fe7.png#averageHue=%23f9f9f9&clientId=ub939c037-ffdd-4&from=paste&height=264&id=u66db07dc&name=image.png&originHeight=396&originWidth=991&originalType=binary&ratio=1&rotation=0&showTitle=false&size=28463&status=done&style=shadow&taskId=u2551c9b4-9546-4289-bd2f-180e5a7a8ae&title=&width=660.6666666666666)
### 3.4.3、统一封装
为了在各个微服务中方便发送消息，所以在`sl-express-ms-base`微服务中进行了封装，使用时`com.sl.ms.base.api.common.MQFeign`调用即可。
在base微服务中添加了配置以及启用Spring-retry机制：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1677728445152-1e022e5f-806f-4842-9284-c51a12ed4ab5.png#averageHue=%23fbfaf5&clientId=u5dece25d-787d-4&from=paste&height=297&id=u866103d4&name=image.png&originHeight=446&originWidth=1325&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=56111&status=done&style=shadow&taskId=uef8112ea-5cba-4b6d-9d2b-9b741e9150b&title=&width=883.3333333333334)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1677728466984-0f8ea414-9cb4-4e6a-89e5-9e0f5c7acda8.png#averageHue=%23fcfcfa&clientId=u5dece25d-787d-4&from=paste&height=290&id=ub7992c2a&name=image.png&originHeight=435&originWidth=1081&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=30078&status=done&style=shadow&taskId=u5de5a23f-641d-42c3-a758-838b05a2734&title=&width=720.6666666666666)
使用示例如下：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1677728596414-94681e10-89d4-4307-9a7f-c45f07ed2912.png#averageHue=%23fbfafa&clientId=u5dece25d-787d-4&from=paste&height=317&id=u916cea4d&name=image.png&originHeight=476&originWidth=1618&originalType=binary&ratio=1.5&rotation=0&showTitle=false&size=54444&status=done&style=shadow&taskId=ufaeda5aa-35e8-4bf3-807a-075a33de4b3&title=&width=1078.6666666666667)
> **发送时指定交换机、路由key、消息内容、延时时间（毫秒）即可。**

# 4、机构同步
机构的新增、更新、删除是在权限管家中完成的，需要是操作后同步到路线规划微服务中，这里采用的是MQ消息通知的方式。
## 4.1、业务流程
![](https://cdn.nlark.com/yuque/__puml/e87b243b2b36f12c01374c888fb0d622.svg#lake_card_v2=eyJ0eXBlIjoicHVtbCIsImNvZGUiOiJAc3RhcnR1bWxcblxuYXV0b251bWJlclxuXG5hY3RvciBcIueUqOaIt1wiIGFzIHVzZXJcbnBhcnRpY2lwYW50IFwi5p2D6ZmQ566h5a62XCIgYXMgYXV0aFxucGFydGljaXBhbnQgXCLot6_nur_op4TliJLlvq7mnI3liqFcIiBhcyB0cmFuc3BvcnRcbnBhcnRpY2lwYW50IFwiTmVvNGpcIiBhcyBuZW80alxuXG51c2VyIC0-IGF1dGggLS0rKzog5paw5aKe5py65p6EXG5hdXRoIC0-IGF1dGg6IOS_neWtmOaVsOaNrlxuYXV0aCAtLT4gdXNlcjog6L-U5ZueXG5hdXRoIC0tPiB0cmFuc3BvcnQgLS0rKzog5Y-R6YCB5raI5oGvXG50cmFuc3BvcnQgLT4gdHJhbnNwb3J0IDog5Lia5Yqh5aSE55CGXG50cmFuc3BvcnQgLT4gbmVvNGogKys6IOS_neWtmOaVsOaNrlxubmVvNGogLT4gbmVvNGogOiDkv53lrZjmlbDmja5cbm5lbzRqIC0-IHRyYW5zcG9ydCAtLTog6L-U5ZueXG5AZW5kdW1sIiwidXJsIjoiaHR0cHM6Ly9jZG4ubmxhcmsuY29tL3l1cXVlL19fcHVtbC9lODdiMjQzYjJiMzZmMTJjMDEzNzRjODg4ZmIwZDYyMi5zdmciLCJpZCI6ImRGRmNqIiwibWFyZ2luIjp7InRvcCI6dHJ1ZSwiYm90dG9tIjp0cnVlfSwiY2FyZCI6ImRpYWdyYW0ifQ==)## 4.2、权限管家配置
权限管家的MQ配置是在 `/itcast/itcast-auth-server/application-test.properties`文件中，如下：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661414367036-b19987ad-7794-47c8-92ad-a2956210d3c2.png#averageHue=%230d141e&clientId=uf78e1dbf-4cc4-4&errorMessage=unknown%20error&from=paste&height=216&id=uc987dcca&name=image.png&originHeight=357&originWidth=1230&originalType=binary&ratio=1&rotation=0&showTitle=false&size=37168&status=error&style=shadow&taskId=u3badaf30-4a59-41fa-924f-4b6ed4ec63c&title=&width=745.4545023684356)
可以看出，消息发往的交换机为：itcast-auth，交换机的类型为：topic
发送消息的规则如下：
:::info

- 消息为json字符串
   - 如：{"type":"ORG","content":[{"managerId":"1","parentId":"0","name":"测试组织","id":"973902113476182273","status":true}],"operation":"UPDATE"}
- type表示变更的对象，比如组织：ORG 
- content为更改对象列表
- operation类型列表
   - 新增-ADD
   - 修改-UPDATE
   - 删除-DEL
:::
所以，对应的在`sl-express-transport.properties`中配置相同的交换机。
## 4.3、业务规范
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661418858904-20af71d3-24c9-4b12-8902-0ea95643a9e8.png#averageHue=%23fefefe&clientId=uf78e1dbf-4cc4-4&errorMessage=unknown%20error&from=paste&height=435&id=u0ecaf4a0&name=image.png&originHeight=717&originWidth=859&originalType=binary&ratio=1&rotation=0&showTitle=false&size=26996&status=error&style=shadow&taskId=uc031847f-b190-4ab9-b2a9-633c44ab01a&title=&width=520.6060305158425)
上图是在权限管家中新增组织的界面，可以从界面中看出，添加的组织并没有标识是【网点】还是【转运中心】，所以，在这里我们做一下约定，按照机构名称的后缀进行区分，具体规则如下：

- xxx转运中心  →  一级转运中心（OLT）
- xxx分拣中心 →  二级转运中心 （TLT）
- xxx营业部  →  网点（AGENCY）
## 4.4、具体实现
```java
package com.sl.transport.mq;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sl.transport.common.constant.Constants;
import com.sl.transport.entity.node.AgencyEntity;
import com.sl.transport.entity.node.BaseEntity;
import com.sl.transport.entity.node.OLTEntity;
import com.sl.transport.entity.node.TLTEntity;
import com.sl.transport.enums.OrganTypeEnum;
import com.sl.transport.service.IService;
import com.sl.transport.utils.OrganServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 对于权限管家系统消息的处理
 */
@Slf4j
@Component
public class AuthMQListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = Constants.MQ.Queues.AUTH_TRANSPORT),
            exchange = @Exchange(name = "${rabbitmq.exchange}", type = ExchangeTypes.TOPIC),
            key = "#"
    ))
    public void listenAgencyMsg(String msg) {
        //{"type":"ORG","operation":"ADD","content":[{"id":"977263044792942657","name":"55","parentId":"0","managerId":null,"status":true}]}
        log.info("接收到消息 -> {}", msg);
        JSONObject jsonObject = JSONUtil.parseObj(msg);
        String type = jsonObject.getStr("type");
        if (!StrUtil.equalsIgnoreCase(type, "ORG")) {
            //非机构消息
            return;
        }
        String operation = jsonObject.getStr("operation");
        JSONObject content = (JSONObject) jsonObject.getJSONArray("content").getObj(0);
        String name = content.getStr("name");
        Long parentId = content.getLong("parentId");

        IService iService;
        BaseEntity entity;
        if (StrUtil.endWith(name, "转运中心")) {
            //一级转运中心
            iService = OrganServiceFactory.getBean(OrganTypeEnum.OLT.getCode());
            entity = new OLTEntity();
            entity.setParentId(0L);
        } else if (StrUtil.endWith(name, "分拣中心")) {
            //二级转运中心
            iService = OrganServiceFactory.getBean(OrganTypeEnum.TLT.getCode());
            entity = new TLTEntity();
            entity.setParentId(parentId);
        } else if (StrUtil.endWith(name, "营业部")) {
            //网点
            iService = OrganServiceFactory.getBean(OrganTypeEnum.AGENCY.getCode());
            entity = new AgencyEntity();
            entity.setParentId(parentId);
        } else {
            return;
        }

        //设置参数
        entity.setBid(content.getLong("id"));
        entity.setName(name);
        entity.setStatus(content.getBool("status"));

        switch (operation) {
            case "ADD": {
                iService.create(entity);
                break;
            }
            case "UPDATE": {
                iService.update(entity);
                break;
            }
            case "DEL": {
                iService.deleteByBid(entity.getBid());
                break;
            }
        }

    }

}

```
:::danger
由于Service还没有具体实现，暂时不对代码测试，后面实现后进行测试。
:::
# 5、IService
在Service中一些方法是通用的，比如新增、更新、删除等，这个通用的方法可以写到一个Service中，其他的Service继承该Service即可。
## 5.1、IService
接口定义：
```java
package com.sl.transport.service;

import com.sl.transport.entity.node.BaseEntity;

/**
 * 基础服务实现
 */
public interface IService<T extends BaseEntity> {

    /**
     * 根据业务id查询数据
     *
     * @param bid 业务id
     * @return 节点数据
     */
    T queryByBid(Long bid);

    /**
     * 新增节点
     *
     * @param t 节点数据
     * @return 新增的节点数据
     */
    T create(T t);

    /**
     * 更新节点
     *
     * @param t 节点数据
     * @return 更新的节点数据
     */
    T update(T t);

    /**
     * 根据业务id删除数据
     *
     * @param bid 业务id
     * @return 是否删除成功
     */
    Boolean deleteByBid(Long bid);

}

```
## 5.2、ServiceImpl
下面编写具体的实现类：
```java
package com.sl.transport.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.sl.transport.common.util.ObjectUtil;
import com.sl.transport.entity.node.BaseEntity;
import com.sl.transport.repository.BaseRepository;
import com.sl.transport.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础服务的实现
 */
public class ServiceImpl<R extends BaseRepository, T extends BaseEntity> implements IService<T> {

    @Autowired
    private R repository;

    @Override
    public T queryByBid(Long bid) {
        return (T) this.repository.findByBid(bid).orElse(null);
    }

    @Override
    public T create(T t) {
        t.setId(null);//id由neo4j自动生成
        return (T) this.repository.save(t);
    }

    @Override
    public T update(T t) {
        //先查询，再更新
        T tData = this.queryByBid(t.getBid());
        if (ObjectUtil.isEmpty(tData)) {
            return null;
        }
        BeanUtil.copyProperties(t, tData, CopyOptions.create().ignoreNullValue().setIgnoreProperties("id", "bid"));
        return (T) this.repository.save(tData);
    }

    @Override
    public Boolean deleteByBid(Long bid) {
        return this.repository.deleteByBid(bid) > 0;
    }
}

```
## 5.3、AgencyServiceImpl
网点服务实现类：
```java
package com.sl.transport.service.impl;

import com.sl.transport.entity.node.AgencyEntity;
import com.sl.transport.repository.AgencyRepository;
import com.sl.transport.service.AgencyService;
import org.springframework.stereotype.Service;

@Service
public class AgencyServiceImpl extends ServiceImpl<AgencyRepository, AgencyEntity> implements AgencyService {

}

```
## 5.4、OLTServiceImpl
一级转运中心服务实现类：
```java
package com.sl.transport.service.impl;

import com.sl.transport.entity.node.OLTEntity;
import com.sl.transport.repository.OLTRepository;
import com.sl.transport.service.OLTService;
import org.springframework.stereotype.Service;

@Service
public class OLTServiceImpl extends ServiceImpl<OLTRepository, OLTEntity>
        implements OLTService {
}

```
## 5.5、TLTServiceImpl
二级转运中心服务实现类：
```java
package com.sl.transport.service.impl;

import com.sl.transport.entity.node.TLTEntity;
import com.sl.transport.repository.TLTRepository;
import com.sl.transport.service.TLTService;
import org.springframework.stereotype.Service;

@Service
public class TLTServiceImpl extends ServiceImpl<TLTRepository, TLTEntity>
        implements TLTService {

}

```
## 5.6、单元测试
编写测试用例：
```java
package com.sl.transport.service;

import com.sl.transport.entity.node.AgencyEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AgencyServiceTest {

    @Resource
    private AgencyService agencyService;

    @Test
    public void testQueryByBid(){
        AgencyEntity agencyEntity = this.agencyService.queryByBid(25073L);
        System.out.println(agencyEntity);
		//AgencyEntity(super=BaseEntity(id=18, parentId=null, bid=25073, name=江苏省南京市玄武区紫金墨香苑, managerName=null, phone=025-58765331,025-83241955,025-83241881, address=栖霞区燕尧路100号, location=Point [x=32.117016, y=118.863193], status=null, extra=null))
    }

}
```
> 🚨注意：需要将OrganController、TransportLineController中的@RestController注释掉才能测试，否则会抛出异常。

## 5.7、测试机构同步
将Neo4j中的数据全部删除：`MATCH (n) DETACH DELETE n`
创建机构：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661426006150-2a008fe5-4744-4217-a6f5-934794d693b5.png#averageHue=%23fefefe&clientId=u876d64eb-b77d-4&errorMessage=unknown%20error&from=paste&height=432&id=ub93109cb&name=image.png&originHeight=713&originWidth=856&originalType=binary&ratio=1&rotation=0&showTitle=false&size=27078&status=error&style=shadow&taskId=u62c1100d-f4d3-416b-9644-3781ccd9d8d&title=&width=518.7878488027486)
可以看到对应的Neo4j中已经有数据：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661425946094-dbda9e63-7fa9-47d6-a3e6-ba4f0921bfc7.png#averageHue=%23d4e3d4&clientId=u876d64eb-b77d-4&errorMessage=unknown%20error&from=paste&height=254&id=u140fb50b&name=image.png&originHeight=419&originWidth=809&originalType=binary&ratio=1&rotation=0&showTitle=false&size=39874&status=error&style=shadow&taskId=u09171e1c-92c4-42bf-aa4d-db87485f58a&title=&width=490.30300196428)
同理可以测试更新、删除操作。
# 6、机构管理
按照业务系统的需求，会通过bid查询机构，无需指定type，也就是说，我们需要将网点和转运中心都看作是机构，需要实现两个查询方法：

- 根据bid查询
- 查询机构列表
## 6.1、接口定义
```java
package com.sl.transport.service;

import com.sl.transport.domain.OrganDTO;

import java.util.List;

/**
 * @author zzj
 * @version 1.0
 */
public interface OrganService {

    /**
     * 无需指定type，根据id查询
     *
     * @param bid 机构id
     * @return 机构信息
     */
    OrganDTO findByBid(Long bid);

    /**
     * 无需指定type，根据ids查询
     *
     * @param bids 机构ids
     * @return 机构信息
     */
    List<OrganDTO> findByBids(List<Long> bids);

    /**
     * 查询所有的机构，如果name不为空的按照name模糊查询
     *
     * @param name 机构名称
     * @return 机构列表
     */
    List<OrganDTO> findAll(String name);

    /**
     * 查询机构树
     * @return 机构树
     */
    String findAllTree();
}

```
## 6.2、具体实现
```java
package com.sl.transport.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.transport.common.exception.SLException;
import com.sl.transport.domain.OrganDTO;
import com.sl.transport.enums.ExceptionEnum;
import com.sl.transport.repository.OrganRepository;
import com.sl.transport.service.OrganService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrganServiceImpl implements OrganService {
    @Resource
    private OrganRepository organRepository;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public OrganDTO findByBid(Long bid) {
        OrganDTO organDTO = this.organRepository.findByBid(bid);
        if (ObjectUtil.isNotEmpty(organDTO)) {
            return organDTO;
        }
        throw new SLException(ExceptionEnum.ORGAN_NOT_FOUND);
    }

    @Override
    public List<OrganDTO> findByBids(List<Long> bids) {
        List<OrganDTO> organDTOS = this.organRepository.findByBids(bids);
        if (ObjectUtil.isNotEmpty(organDTOS)) {
            return organDTOS;
        }
        throw new SLException(ExceptionEnum.ORGAN_NOT_FOUND);
    }

    @Override
    public List<OrganDTO> findAll(String name) {
        return this.organRepository.findAll(name);
    }

    @Override
    public String findAllTree() {
        return "";
    }
}

```
## 6.3、OrganRepositoryImpl
下面对于OrganRepository接口进行实现：
```java
package com.sl.transport.repository.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.sl.transport.domain.OrganDTO;
import com.sl.transport.enums.OrganTypeEnum;
import com.sl.transport.repository.OrganRepository;
import org.neo4j.driver.internal.InternalPoint2D;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class OrganRepositoryImpl implements OrganRepository {

    @Resource
    private Neo4jClient neo4jClient;

    @Override
    public OrganDTO findByBid(Long bid) {
        String cypherQuery = StrUtil.format("MATCH (n)\n" +
                "WHERE n.bid = {}\n" +
                "RETURN n", bid);
        return CollUtil.getFirst(executeQuery(cypherQuery));
    }

    @Override
    public List<OrganDTO> findByBids(List<Long> bids) {
        String cypherQuery = StrUtil.format("MATCH (n)\n" +
                "WHERE n.bid in {}\n" +
                "RETURN n", bids);
        return executeQuery(cypherQuery);
    }

    @Override
    public List<OrganDTO> findAll(String name) {
        name = StrUtil.removeAll(name, '\'', '"');
        String cypherQuery = StrUtil.isEmpty(name) ?
                "MATCH (n) RETURN n" :
                StrUtil.format("MATCH (n) WHERE n.name CONTAINS '{}' RETURN n", name);
        return executeQuery(cypherQuery);
    }

    private List<OrganDTO> executeQuery(String cypherQuery) {
        return ListUtil.toList(this.neo4jClient.query(cypherQuery)
                .fetchAs(OrganDTO.class) //设置响应的类型
                .mappedBy((typeSystem, record) -> { //对结果进行封装处理
                    Map<String, Object> map = record.get("n").asMap();
                    OrganDTO organDTO = BeanUtil.toBean(map, OrganDTO.class);
                    InternalPoint2D location = (InternalPoint2D) map.get("location");
                    if (ObjectUtil.isNotEmpty(location)) {
                        organDTO.setLongitude(location.x());
                        organDTO.setLatitude(location.y());
                    }
                    //获取类型
                    String type = CollUtil.getFirst(record.get("n").asNode().labels());
                    organDTO.setType(OrganTypeEnum.valueOf(type).getCode());
                    return organDTO;
                }).all());
    }
}

```
## 6.4、测试用例
```java
package com.sl.transport.service;

import com.sl.transport.domain.OrganDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrganServiceTest {

    @Resource
    private OrganService organService;

    @Test
    void findByBid() {
		//bid值要改成自己neo4j中的值
        OrganDTO organDTO = this.organService.findByBid(1012479939628238305L);
        System.out.println(organDTO);
    }

    @Test
    void findAll() {
		//查询包含“上海”关键字的机构
        List<OrganDTO> list = this.organService.findAll("上海");
        list.forEach(System.out::println);
    }
}
```
## 6.5、整合测试
基于swagger接口进行测试：[http://127.0.0.1:18083/doc.html](http://127.0.0.1:18083/doc.html)
:::danger
测试时，由于部分Service还没有实现，会导致启动报错，所以需要将报错Controller中的@RestController注释掉。
:::
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661436832675-b01d0c60-6453-44e4-9cd0-244d58f29adb.png#averageHue=%23fcfcfc&clientId=u8e19e744-02a1-4&errorMessage=unknown%20error&from=paste&height=487&id=ud1ee79f1&name=image.png&originHeight=803&originWidth=1429&originalType=binary&ratio=1&rotation=0&showTitle=false&size=109499&status=error&style=shadow&taskId=u74150fe8-6f63-4956-86a8-815dc97f1a7&title=&width=866.060556003654)
## 6.6、树形结构
在后台系统中，对于机构数据的展现需要通过树形结构展现，如下：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1673419872381-74654f1d-9521-4606-a422-1d462e1aebcd.png#averageHue=%23fdfbfa&clientId=u5a15bc20-4154-4&from=paste&height=437&id=u007ea3c7&name=image.png&originHeight=655&originWidth=468&originalType=binary&ratio=1&rotation=0&showTitle=false&size=41244&status=done&style=shadow&taskId=u3e4ed56e-f86e-422f-8282-1b6f4f63fcb&title=&width=312)
所以在`com.sl.transport.service.OrganService`中`findAllTree()`方法中封装了树形结构。
具体的封装逻辑采用hutool工具包中的TreeUtil，参考文档：[点击查看](https://www.hutool.cn/docs/#/core/%E8%AF%AD%E8%A8%80%E7%89%B9%E6%80%A7/%E6%A0%91%E7%BB%93%E6%9E%84/%E6%A0%91%E7%BB%93%E6%9E%84%E5%B7%A5%E5%85%B7-TreeUtil)
代码实现如下：
```java
    @Override
    public String findAllTree() {
        List<OrganDTO> organList = this.findAll(null);
        if (CollUtil.isEmpty(organList)) {
            return "";
        }

        //构造树结构
        List<Tree<Long>> treeNodes = TreeUtil.build(organList, 0L,
                (organDTO, tree) -> {
                    tree.setId(organDTO.getId());
                    tree.setParentId(organDTO.getParentId());
                    tree.putAll(BeanUtil.beanToMap(organDTO));
                    tree.remove("bid");
                });

        try {
            return this.objectMapper.writeValueAsString(treeNodes);
        } catch (JsonProcessingException e) {
            throw new SLException("序列化json出错！", e);
        }
    }
```
数据类似这样：
```json
[
    {
        "id": "1012438698496623009",
        "parentId": "0",
        "name": "上海市转运中心",
        "type": 1,
        "phone": null,
        "address": null,
        "latitude": null,
        "longitude": null,
        "managerName": null,
        "extra": null,
        "status": true,
        "children": [
            {
                "id": "1012479939628238305",
                "parentId": "1012438698496623009",
                "name": "浦东区分拣中心",
                "type": 2,
                "phone": null,
                "address": null,
                "latitude": null,
                "longitude": null,
                "managerName": null,
                "extra": null,
                "status": true
            }
        ]
    },
    {
        "id": "1012479716659037537",
        "parentId": "0",
        "name": "北京市转运中心",
        "type": 1,
        "phone": null,
        "address": null,
        "latitude": null,
        "longitude": null,
        "managerName": null,
        "extra": null,
        "status": true
    }
]
```
功能测试：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661436796038-a4700a64-d9f6-49f7-935c-ead08dce6c84.png#averageHue=%23f8f8f8&clientId=u8e19e744-02a1-4&errorMessage=unknown%20error&from=paste&height=326&id=Gat60&name=image.png&originHeight=538&originWidth=1412&originalType=binary&ratio=1&rotation=0&showTitle=false&size=80788&status=error&style=shadow&taskId=u55b1777d-917b-444d-927c-0ac0e8e8b71&title=&width=855.7575262961228)
> 🔔如果测试没有数据，需要自行在权限管家中创建相对应的网点、转运中心等数据，进行测试。

## 6.7、编辑机构
在后台系统中可以对机构数据进行编辑，主要是填充一些属性数据，例如：经纬度、详细地址。如下：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661565905827-d35c4b85-4929-4ab0-aaeb-87a8392493f3.png#averageHue=%23fdfcfb&clientId=udb28364f-6735-4&errorMessage=unknown%20error&from=paste&height=333&id=u1e871295&name=image.png&originHeight=550&originWidth=1612&originalType=binary&ratio=1&rotation=0&showTitle=false&size=49753&status=error&style=shadow&taskId=u8cd705ac-79c6-48c8-802d-cf8f2054af3&title=&width=976.9696405023725)
经纬度是如何计算出来的呢？这里使用的是高德地图的API进行查询的，将中文字段转化为经纬度值，我们是直接调用的高德地图的API吗，不是的，我们是通过EagleMap调用的。
### 6.7.1、EagleMap介绍
EagleMap是黑马程序员研究院自研的地图中台服务，它可以对接多个地图服务商，目前已经完成百度地图和高德地图的对接。
目前EagleMap已经部署安装在101机器中，配置文件所在的位置：`/itcast/eaglemap/app/application.yml`
:::danger
在这里强烈建议将高德地图的ak改成自己的，不要使用默认的，因为使用人多了可能会被封号，将不能正常使用。更改了ak后，要记得重启EagleMap服务`docker restart eagle-map-server`
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661566455518-f9b4b85b-f55b-489e-9ea6-7418c111c0bd.png#averageHue=%230e1623&clientId=udb28364f-6735-4&errorMessage=unknown%20error&from=paste&height=281&id=u71e84cf4&name=image.png&originHeight=463&originWidth=741&originalType=binary&ratio=1&rotation=0&showTitle=false&size=51484&status=error&style=shadow&taskId=u944a2167-778b-403c-94bb-a1a09999339&title=&width=449.0908831341551)
**web API的申请：**[https://lbs.amap.com/dev/key](https://lbs.amap.com/dev/key)
**申请时注意【服务平台】选项，需要申请【Web端(JS API)】和【Web服务】的key。**
:::
具体的使用，参考sdk使用手册：
# 7、路线管理
路线管理是在路线规划中核心的功能，用户在下单时、订单转运单时会进行调用路线规划，后台系统对路线进行维护管理。路线类型如下：

- **干线**
   - 一级转运中心到一级转运中心
- **支线**
   - 一级转运中心与二级转运中心之间线路
- **接驳路线**
   - 二级转运中心到网点
- **专线（暂时不支持）**
   - 任务城市到任意城市
- **临时线路（暂时不支持）**
   - 任意转运中心到任意转运中心
:::danger
 新增路线业务规则：干线：起点终点无顺序，支线：起点必须是二级转运中心，接驳路线：起点必须是网点  
:::
## 7.1、业务流程
![](https://cdn.nlark.com/yuque/__puml/f54961d9d4e7a6b0caa0c07382e54e1c.svg#lake_card_v2=eyJ0eXBlIjoicHVtbCIsImNvZGUiOiJAc3RhcnR1bWxcblxuYXV0b251bWJlclxuXG5hY3RvciBcIueUqOaIt1wiIGFzIHVzZXJcbnBhcnRpY2lwYW50IFwi6K6i5Y2V5b6u5pyN5YqhXCIgYXMgb21zXG5wYXJ0aWNpcGFudCBcIuWQjuWPsOeuoeeQhuezu-e7n1wiIGFzIGFkbWluXG5wYXJ0aWNpcGFudCBcIndvcmvlvq7mnI3liqFcIiBhcyB3b3JrXG5wYXJ0aWNpcGFudCBcIui3r-e6v-inhOWIkuW-ruacjeWKoVwiIGFzIHRyYW5zcG9ydFxucGFydGljaXBhbnQgXCJOZW80alwiIGFzIG5lbzRqXG5cbnVzZXIgLT4gb21zICsrOiDkuIvljZVcbm9tcyAtPiB0cmFuc3BvcnQgKys6IOi3r-e6v-inhOWIklxudHJhbnNwb3J0IC0-IG5lbzRqICsrOiDmn6Xor6Lot6_nur9cbnJldHVybiDov5Tlm55cbnRyYW5zcG9ydCAtPiBvbXMgLS06IOi_lOWbnui3r-e6v1xub21zIC0-IHVzZXIgOiDkuIvljZXmiJDlip9cbm9tcyAtPiB3b3JrICsrOuiuouWNlei9rOi_kOWNlVxud29yayAtPiB0cmFuc3BvcnQgKys6IOi3r-e6v-inhOWIklxudHJhbnNwb3J0IC0-IHdvcmsgLS06IOi_lOWbnui3r-e6v1xud29yayAtPiB3b3JrIDog5a2Y5YKo5pWw5o2uXG5cbmdyb3VwIOi3r-e6v-S4jeWtmOWcqFxudHJhbnNwb3J0IC0-IG9tcyAtLTog6Lev57q_5LiN5a2Y5ZyoXG5kZWFjdGl2YXRlIHdvcmtcbm9tcyAtPiB1c2VyIC0tOiDkuIvljZXlpLHotKVcbmVuZFxuXG5ncm91cCDot6_nur_nrqHnkIZcbmFkbWluIC0-IHRyYW5zcG9ydCArKzog6Lev57q_566h55CGXG50cmFuc3BvcnQgLT4gbmVvNGogKys6IENSVURcbnJldHVybiDov5Tlm55cbnJldHVybiDov5Tlm55cbmVuZFxuQGVuZHVtbCIsInVybCI6Imh0dHBzOi8vY2RuLm5sYXJrLmNvbS95dXF1ZS9fX3B1bWwvZjU0OTYxZDlkNGU3YTZiMGNhYTBjMDczODJlNTRlMWMuc3ZnIiwiaWQiOiJabWtOOSIsIm1hcmdpbiI6eyJ0b3AiOnRydWUsImJvdHRvbSI6dHJ1ZX0sImNhcmQiOiJkaWFncmFtIn0=)## 7.2、Repository
### 7.2.1、接口定义
```java
package com.sl.transport.repository;

import com.sl.transport.common.util.PageResponse;
import com.sl.transport.domain.TransportLineNodeDTO;
import com.sl.transport.domain.TransportLineSearchDTO;
import com.sl.transport.entity.line.TransportLine;
import com.sl.transport.entity.node.AgencyEntity;
import com.sl.transport.entity.node.BaseEntity;

import java.util.List;

/**
 * 运输路线查询
 */
public interface TransportLineRepository {

    /**
     * 查询两个网点之间最短的路线，查询深度为：10
     *
     * @param start 开始网点
     * @param end   结束网点
     * @return 路线
     */
    TransportLineNodeDTO findShortestPath(AgencyEntity start, AgencyEntity end);

    /**
     * 查询两个网点之间最短的路线，最大查询深度为：10
     *
     * @param start 开始网点
     * @param end   结束网点
     * @param depth 查询深度，最大为：10
     * @return 路线
     */
    TransportLineNodeDTO findShortestPath(AgencyEntity start, AgencyEntity end, int depth);

    /**
     * 查询两个网点之间的路线列表，成本优先 > 转运节点优先
     *
     * @param start 开始网点
     * @param end   结束网点
     * @param depth 查询深度
     * @param limit 返回路线的数量
     * @return 路线
     */
    List<TransportLineNodeDTO> findPathList(AgencyEntity start, AgencyEntity end, int depth, int limit);

    /**
     * 查询数据节点之间的关系数量
     *
     * @param firstNode  第一个节点
     * @param secondNode 第二个节点
     * @return 数量
     */
    Long queryCount(BaseEntity firstNode, BaseEntity secondNode);

    /**
     * 新增路线
     *
     * @param firstNode     第一个节点
     * @param secondNode    第二个节点
     * @param transportLine 路线数据
     * @return 新增关系的数量
     */
    Long create(BaseEntity firstNode, BaseEntity secondNode, TransportLine transportLine);

    /**
     * 更新路线
     *
     * @param transportLine 路线数据
     * @return 更新的数量
     */
    Long update(TransportLine transportLine);

    /**
     * 删除路线
     *
     * @param lineId 关系id
     * @return 删除关系的数量
     */
    Long remove(Long lineId);

    /**
     * 分页查询路线
     *
     * @param transportLineSearchDTO 搜索参数
     * @return 路线列表
     */
    PageResponse<TransportLine> queryPageList(TransportLineSearchDTO transportLineSearchDTO);


    /**
     * 根据ids批量查询路线
     *
     * @param ids id列表
     * @return 路线列表
     */
    List<TransportLine> queryByIds(Long... ids);

    /**
     * 根据id查询路线
     *
     * @param id 路线id
     * @return 路线数据
     */
    TransportLine queryById(Long id);
}

```
### 7.2.2、接口实现
```java
package com.sl.transport.repository.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import com.sl.transport.common.util.PageResponse;
import com.sl.transport.domain.TransportLineNodeDTO;
import com.sl.transport.domain.TransportLineSearchDTO;
import com.sl.transport.entity.line.TransportLine;
import com.sl.transport.entity.node.AgencyEntity;
import com.sl.transport.entity.node.BaseEntity;
import com.sl.transport.repository.TransportLineRepository;
import com.sl.transport.utils.TransportLineUtils;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.value.PathValue;
import org.neo4j.driver.types.Relationship;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 对于路线的各种操作
 */
@Component
public class TransportLineRepositoryImpl implements TransportLineRepository {

    @Resource
    private Neo4jClient neo4jClient;

    @Override
    public TransportLineNodeDTO findShortestPath(AgencyEntity start, AgencyEntity end) {
        return this.findShortestPath(start, end, 10);
    }

    @Override
    public TransportLineNodeDTO findShortestPath(AgencyEntity start, AgencyEntity end, int depth) {
        //获取网点数据在Neo4j中的类型
        String type = AgencyEntity.class.getAnnotation(Node.class).value()[0];
        //构造查询语句
        String cypherQuery = StrUtil.format(
                "MATCH path = shortestPath((start:{}) -[*..{}]-> (end:{}))\n" +
                        "WHERE start.bid = $startId AND end.bid = $endId AND start.status = true AND end.status = true\n" +
                        "RETURN path", type, depth, type);
        Collection<TransportLineNodeDTO> transportLineNodeDTOS = this.executeQueryPath(cypherQuery, start, end);
        if (CollUtil.isEmpty(transportLineNodeDTOS)) {
            return null;
        }
        for (TransportLineNodeDTO transportLineNodeDTO : transportLineNodeDTOS) {
            return transportLineNodeDTO;
        }
        return null;
    }

    private List<TransportLineNodeDTO> executeQueryPath(String cypherQuery, AgencyEntity start, AgencyEntity end) {
        return ListUtil.toList(this.neo4jClient.query(cypherQuery)
                .bind(start.getBid()).to("startId") //设置参数
                .bind(end.getBid()).to("endId") //设置参数
                .fetchAs(TransportLineNodeDTO.class) //设置响应的类型
                .mappedBy((typeSystem, record) -> { //对结果进行封装处理
                    PathValue pathValue = (PathValue) record.get(0);
                    return TransportLineUtils.convert(pathValue);
                }).all());
    }

    @Override
    public List<TransportLineNodeDTO> findPathList(AgencyEntity start, AgencyEntity end, int depth, int limit) {
        //获取网点数据在Neo4j中的类型
        String type = AgencyEntity.class.getAnnotation(Node.class).value()[0];
        //构造查询语句
        String cypherQuery = StrUtil.format(
                "MATCH path = (start:{}) -[*..{}]-> (end:{})\n" +
                        "WHERE start.bid = $startId AND end.bid = $endId AND start.status = true AND end.status = true\n" +
                        "UNWIND relationships(path) AS r\n" +
                        "WITH sum(r.cost) AS cost, path\n" +
                        "RETURN path ORDER BY cost ASC, LENGTH(path) ASC LIMIT {}", type, depth, type, limit);
        return this.executeQueryPath(cypherQuery, start, end);
    }

    @Override
    public Long queryCount(BaseEntity firstNode, BaseEntity secondNode) {
        String firstNodeType = firstNode.getClass().getAnnotation(Node.class).value()[0];
        String secondNodeType = secondNode.getClass().getAnnotation(Node.class).value()[0];
        String cypherQuery = StrUtil.format(
                "MATCH (m:{}) -[r]- (n:{})\n" +
                        "WHERE m.bid = $firstBid AND n.bid = $secondBid\n" +
                        "RETURN count(r) AS c", firstNodeType, secondNodeType);
        Optional<Long> optional = this.neo4jClient.query(cypherQuery)
                .bind(firstNode.getBid()).to("firstBid")
                .bind(secondNode.getBid()).to("secondBid")
                .fetchAs(Long.class)
                .mappedBy((typeSystem, record) -> Convert.toLong(record.get("c")))
                .one();
        return optional.orElse(0L);
    }

    @Override
    public Long create(BaseEntity firstNode, BaseEntity secondNode, TransportLine transportLine) {
        String firstNodeType = firstNode.getClass().getAnnotation(Node.class).value()[0];
        String secondNodeType = secondNode.getClass().getAnnotation(Node.class).value()[0];
        String cypherQuery = StrUtil.format(
                "MATCH (m:{} {bid : $firstBid})\n" +
                        "WITH m\n" +
                        "MATCH (n:{} {bid : $secondBid})\n" +
                        "WITH m,n\n" +
                        "CREATE\n" +
                        " (m) -[r:IN_LINE {cost:$cost, number:$number, type:$type, name:$name, distance:$distance, time:$time, extra:$extra, startOrganId:$startOrganId, endOrganId:$endOrganId,created:$created, updated:$updated}]-> (n),\n" +
                        " (m) <-[:OUT_LINE {cost:$cost, number:$number, type:$type, name:$name, distance:$distance, time:$time, extra:$extra, startOrganId:$endOrganId, endOrganId:$startOrganId, created:$created, updated:$updated}]- (n)\n" +
                        "RETURN count(r) AS c", firstNodeType, secondNodeType);
        Optional<Long> optional = this.neo4jClient.query(cypherQuery)
                .bindAll(BeanUtil.beanToMap(transportLine))
                .bind(firstNode.getBid()).to("firstBid")
                .bind(secondNode.getBid()).to("secondBid")
                .fetchAs(Long.class)
                .mappedBy((typeSystem, record) -> Convert.toLong(record.get("c")))
                .one();
        return optional.orElse(0L);
    }

    @Override
    public Long update(TransportLine transportLine) {
        String cypherQuery = "MATCH () -[r]-> ()\n" +
                "WHERE id(r) = $id\n" +
                "SET r.cost = $cost , r.number = $number, r.name = $name ,r.distance = $distance ,r.time = $time, r.startOrganId = $startOrganId, r.endOrganId = $endOrganId, r.updated = $updated , r.extra = $extra \n" +
                "RETURN count(r) AS c";
        Optional<Long> optional = this.neo4jClient.query(cypherQuery)
                .bindAll(BeanUtil.beanToMap(transportLine))
                .fetchAs(Long.class)
                .mappedBy((typeSystem, record) -> Convert.toLong(record.get("c")))
                .one();
        return optional.orElse(0L);
    }

    @Override
    public Long remove(Long lineId) {
        String cypherQuery = "MATCH () -[r]-> ()\n" +
                "WHERE id(r) = $lineId\n" +
                "DETACH DELETE r\n" +
                "RETURN count(r) AS c";
        Optional<Long> optional = this.neo4jClient.query(cypherQuery)
                .bind(lineId).to("lineId")
                .fetchAs(Long.class)
                .mappedBy((typeSystem, record) -> Convert.toLong(record.get("c")))
                .one();
        return optional.orElse(0L);
    }

    @Override
    public PageResponse<TransportLine> queryPageList(TransportLineSearchDTO transportLineSearchDTO) {
        int page = Math.max(transportLineSearchDTO.getPage(), 1);
        int pageSize = transportLineSearchDTO.getPageSize();
        int skip = (page - 1) * pageSize;
        Map<String, Object> searchParam = BeanUtil.beanToMap(transportLineSearchDTO, false, true);
        MapUtil.removeAny(searchParam, "page", "pageSize");
        //构建查询语句，第一个是查询数据，第二个是查询数量
        String[] cyphers = this.buildPageQueryCypher(searchParam);
        String cypherQuery = cyphers[0];

        //数据
        List<TransportLine> list = ListUtil.toList(this.neo4jClient.query(cypherQuery)
                .bind(skip).to("skip")
                .bind(pageSize).to("limit")
                .bindAll(searchParam)
                .fetchAs(TransportLine.class)
                .mappedBy((typeSystem, record) -> {
                    //封装数据
                    return this.toTransportLine(record);
                }).all());

        // 数据总数
        String countCypher = cyphers[1];
        Long total = this.neo4jClient.query(countCypher)
                .bindAll(searchParam)
                .fetchAs(Long.class)
                .mappedBy((typeSystem, record) -> Convert.toLong(record.get("c")))
                .one().orElse(0L);

        PageResponse<TransportLine> pageResponse = new PageResponse<>();
        pageResponse.setPage(page);
        pageResponse.setPageSize(pageSize);
        pageResponse.setItems(list);
        pageResponse.setCounts(total);
        Long pages = Convert.toLong(PageUtil.totalPage(Convert.toInt(total), pageSize));
        pageResponse.setPages(pages);

        return pageResponse;
    }

    private String[] buildPageQueryCypher(Map<String, Object> searchParam) {
        String queryCypher;
        String countCypher;
        if (CollUtil.isEmpty(searchParam)) {
            //无参数
            queryCypher = "MATCH (m) -[r]-> (n) RETURN m,r,n ORDER BY id(r) DESC SKIP $skip LIMIT $limit";
            countCypher = "MATCH () -[r]-> () RETURN count(r) AS c";
        } else {
            //有参数
            String cypherPrefix = "MATCH (m) -[r]-> (n)";
            StringBuilder sb = new StringBuilder();
            sb.append(cypherPrefix).append(" WHERE 1=1 ");
            for (String key : searchParam.keySet()) {
                Object value = searchParam.get(key);
                if (value instanceof String) {
                    if (StrUtil.isNotBlank(Convert.toStr(value))) {
                        sb.append(StrUtil.format("AND r.{} CONTAINS ${} \n", key, key));
                    }
                } else {
                    sb.append(StrUtil.format("AND r.{} = ${} \n", key, key));
                }
            }
            String cypher = sb.toString();
            queryCypher = cypher + "RETURN m,r,n ORDER BY id(r) DESC SKIP $skip LIMIT $limit";
            countCypher = cypher + "RETURN count(r) AS c";
        }
        return new String[]{queryCypher, countCypher};
    }

    @Override
    public List<TransportLine> queryByIds(Long... ids) {
        String cypherQuery = "MATCH (m) -[r]-> (n)\n" +
                "WHERE id(r) in $ids\n" +
                "RETURN m,r,n";
        return ListUtil.toList(this.neo4jClient.query(cypherQuery)
                .bind(ids).to("ids")
                .fetchAs(TransportLine.class)
                .mappedBy((typeSystem, record) -> {
                    //封装数据
                    return this.toTransportLine(record);
                }).all());
    }

    private TransportLine toTransportLine(Record record) {
        org.neo4j.driver.types.Node startNode = record.get("m").asNode();
        org.neo4j.driver.types.Node endNode = record.get("n").asNode();
        Relationship relationship = record.get("r").asRelationship();
        Map<String, Object> map = relationship.asMap();

        TransportLine transportLine = BeanUtil.toBeanIgnoreError(map, TransportLine.class);
        transportLine.setStartOrganName(startNode.get("name").asString());
        transportLine.setStartOrganId(startNode.get("bid").asLong());
        transportLine.setEndOrganName(endNode.get("name").asString());
        transportLine.setEndOrganId(endNode.get("bid").asLong());
        transportLine.setId(relationship.id());
        return transportLine;
    }

    @Override
    public TransportLine queryById(Long id) {
        List<TransportLine> transportLines = this.queryByIds(id);
        if (CollUtil.isNotEmpty(transportLines)) {
            return transportLines.get(0);
        }
        return null;
    }

}

```
## 7.3、路线Service
### 7.3.1、接口定义
```java
package com.sl.transport.service;

import com.sl.transport.common.util.PageResponse;
import com.sl.transport.domain.TransportLineNodeDTO;
import com.sl.transport.domain.TransportLineSearchDTO;
import com.sl.transport.entity.line.TransportLine;

import java.util.List;

/**
 * 计算路线相关业务
 */
public interface TransportLineService {

    /**
     * 新增路线
     *
     * @param transportLine 路线数据
     * @return 是否成功
     */
    Boolean createLine(TransportLine transportLine);

    /**
     * 更新路线
     *
     * @param transportLine 路线数据
     * @return 是否成功
     */
    Boolean updateLine(TransportLine transportLine);

    /**
     * 删除路线
     *
     * @param id 路线id
     * @return 是否成功
     */
    Boolean deleteLine(Long id);

    /**
     * 分页查询路线
     *
     * @param transportLineSearchDTO 搜索参数
     * @return 路线列表
     */
    PageResponse<TransportLine> queryPageList(TransportLineSearchDTO transportLineSearchDTO);

    /**
     * 查询两个网点之间最短的路线，最大查询深度为：10
     *
     * @param startId 开始网点id
     * @param endId   结束网点id
     * @return 路线
     */
    TransportLineNodeDTO queryShortestPath(Long startId, Long endId);

    /**
     * 查询两个网点之间成本最低的路线，最大查询深度为：10
     *
     * @param startId 开始网点id
     * @param endId   结束网点id
     * @return 路线集合
     */
    TransportLineNodeDTO findLowestPath(Long startId, Long endId);

    /**
     * 根据调度策略查询路线
     *
     * @param startId 开始网点id
     * @param endId   结束网点id
     * @return 路线
     */
    TransportLineNodeDTO queryPathByDispatchMethod(Long startId, Long endId);

    /**
     * 根据ids批量查询路线
     *
     * @param ids id列表
     * @return 路线列表
     */
    List<TransportLine> queryByIds(Long... ids);

    /**
     * 根据id查询路线
     *
     * @param id 路线id
     * @return 路线数据
     */
    TransportLine queryById(Long id);

}

```
### 7.3.2、接口实现
```java
package com.sl.transport.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.itheima.em.sdk.EagleMapTemplate;
import com.itheima.em.sdk.enums.ProviderEnum;
import com.itheima.em.sdk.vo.Coordinate;
import com.sl.transport.common.exception.SLException;
import com.sl.transport.common.util.PageResponse;
import com.sl.transport.domain.*;
import com.sl.transport.entity.line.TransportLine;
import com.sl.transport.entity.node.AgencyEntity;
import com.sl.transport.entity.node.BaseEntity;
import com.sl.transport.entity.node.OLTEntity;
import com.sl.transport.entity.node.TLTEntity;
import com.sl.transport.enums.DispatchMethodEnum;
import com.sl.transport.enums.ExceptionEnum;
import com.sl.transport.enums.TransportLineEnum;
import com.sl.transport.repository.TransportLineRepository;
import com.sl.transport.service.CostConfigurationService;
import com.sl.transport.service.DispatchConfigurationService;
import com.sl.transport.service.OrganService;
import com.sl.transport.service.TransportLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 路线相关业务
 *
 * @author zzj
 * @version 1.0
 */
@Service
public class TransportLineServiceImpl implements TransportLineService {

    @Resource
    private TransportLineRepository transportLineRepository;
    @Resource
    private EagleMapTemplate eagleMapTemplate;
    @Resource
    private OrganService organService;
    @Resource
    private DispatchConfigurationService dispatchConfigurationService;
    @Resource
    private CostConfigurationService costConfigurationService;

    // 新增路线业务规则：干线：起点终点无顺序，支线：起点必须是二级转运中心，接驳路线：起点必须是网点
    @Override
    public Boolean createLine(TransportLine transportLine) {
        TransportLineEnum transportLineEnum = TransportLineEnum.codeOf(transportLine.getType());
        if (null == transportLineEnum) {
            throw new SLException(ExceptionEnum.TRANSPORT_LINE_TYPE_ERROR);
        }

        if (ObjectUtil.equal(transportLine.getStartOrganId(), transportLine.getEndOrganId())) {
            //起点终点不能相同
            throw new SLException(ExceptionEnum.TRANSPORT_LINE_ORGAN_CANNOT_SAME);
        }

        BaseEntity firstNode;
        BaseEntity secondNode;
        switch (transportLineEnum) {
            case TRUNK_LINE: {
                // 干线
                firstNode = OLTEntity.builder().bid(transportLine.getStartOrganId()).build();
                secondNode = OLTEntity.builder().bid(transportLine.getEndOrganId()).build();
                break;
            }
            case BRANCH_LINE: {
                // 支线，起点必须是 二级转运中心
                firstNode = TLTEntity.builder().bid(transportLine.getStartOrganId()).build();
                secondNode = OLTEntity.builder().bid(transportLine.getEndOrganId()).build();
                break;
            }
            case CONNECT_LINE: {
                // 接驳路线，起点必须是 网点
                firstNode = AgencyEntity.builder().bid(transportLine.getStartOrganId()).build();
                secondNode = TLTEntity.builder().bid(transportLine.getEndOrganId()).build();
                break;
            }
            default: {
                throw new SLException(ExceptionEnum.TRANSPORT_LINE_TYPE_ERROR);
            }
        }

        if (ObjectUtil.hasEmpty(firstNode, secondNode)) {
            throw new SLException(ExceptionEnum.START_END_ORGAN_NOT_FOUND);
        }

        //判断路线是否已经存在
        Long count = this.transportLineRepository.queryCount(firstNode, secondNode);
        if (count > 0) {
            throw new SLException(ExceptionEnum.TRANSPORT_LINE_ALREADY_EXISTS);
        }

        transportLine.setId(null);
        transportLine.setCreated(System.currentTimeMillis());
        transportLine.setUpdated(transportLine.getCreated());
        //补充信息
        this.infoFromMap(firstNode, secondNode, transportLine);

        count = this.transportLineRepository.create(firstNode, secondNode, transportLine);
        return count > 0;
    }

    /**
     * 通过地图查询距离、时间，计算成本
     *
     * @param firstNode     开始节点
     * @param secondNode    结束节点
     * @param transportLine 路线对象
     */
    private void infoFromMap(BaseEntity firstNode, BaseEntity secondNode, TransportLine transportLine) {
        //查询节点数据
        OrganDTO startOrgan = this.organService.findByBid(firstNode.getBid());
        if (ObjectUtil.hasEmpty(startOrgan, startOrgan.getLongitude(), startOrgan.getLatitude())) {
            throw new SLException("请先完善机构信息");
        }
        OrganDTO endOrgan = this.organService.findByBid(secondNode.getBid());
        if (ObjectUtil.hasEmpty(endOrgan, endOrgan.getLongitude(), endOrgan.getLatitude())) {
            throw new SLException("请先完善机构信息");
        }

        //查询地图服务商
        Coordinate origin = new Coordinate(startOrgan.getLongitude(), startOrgan.getLatitude());
        Coordinate destination = new Coordinate(endOrgan.getLongitude(), endOrgan.getLatitude());
        //设置高德地图参数，默认是不返回预计耗时的，需要额外设置参数
        Map<String, Object> param = MapUtil.<String, Object>builder().put("show_fields", "cost").build();
        String driving = this.eagleMapTemplate.opsForDirection().driving(ProviderEnum.AMAP, origin, destination, param);
        if (StrUtil.isEmpty(driving)) {
            return;
        }
        JSONObject jsonObject = JSONUtil.parseObj(driving);
        //时间，单位：秒
        Long duration = Convert.toLong(jsonObject.getByPath("route.paths[0].cost.duration"), -1L);
        transportLine.setTime(duration);
        //距离，单位：米
        Double distance = Convert.toDouble(jsonObject.getByPath("route.paths[0].distance"), -1d);
        transportLine.setDistance(NumberUtil.round(distance, 0).doubleValue());

        // 总成本 = 每公里平均成本 * 距离（单位：米） / 1000
        Double cost = costConfigurationService.findCostByType(transportLine.getType());
        transportLine.setCost(NumberUtil.round(cost * distance / 1000, 2).doubleValue());
    }

    @Override
    public Boolean updateLine(TransportLine transportLine) {
        // 先查后改
        TransportLine transportLineData = this.queryById(transportLine.getId());
        if (null == transportLineData) {
            throw new SLException(ExceptionEnum.TRANSPORT_LINE_NOT_FOUND);
        }

        //拷贝数据，忽略null值以及不能修改的字段
        BeanUtil.copyProperties(transportLine, transportLineData, CopyOptions.create().setIgnoreNullValue(true)
                .setIgnoreProperties("type", "startOrganId", "startOrganName", "endOrganId", "endOrganName"));

        transportLineData.setUpdated(System.currentTimeMillis());
        Long count = this.transportLineRepository.update(transportLineData);
        return count > 0;
    }

    @Override
    public Boolean deleteLine(Long id) {
        Long count = this.transportLineRepository.remove(id);
        return count > 0;
    }

    @Override
    public PageResponse<TransportLine> queryPageList(TransportLineSearchDTO transportLineSearchDTO) {
        return this.transportLineRepository.queryPageList(transportLineSearchDTO);
    }

    @Override
    public TransportLineNodeDTO queryShortestPath(Long startId, Long endId) {
        AgencyEntity start = AgencyEntity.builder().bid(startId).build();
        AgencyEntity end = AgencyEntity.builder().bid(endId).build();
        if (ObjectUtil.hasEmpty(start, end)) {
            throw new SLException(ExceptionEnum.START_END_ORGAN_NOT_FOUND);
        }
        return this.transportLineRepository.findShortestPath(start, end);
    }

    @Override
    public TransportLineNodeDTO findLowestPath(Long startId, Long endId) {
        AgencyEntity start = AgencyEntity.builder().bid(startId).build();
        AgencyEntity end = AgencyEntity.builder().bid(endId).build();

        if (ObjectUtil.hasEmpty(start, end)) {
            throw new SLException(ExceptionEnum.START_END_ORGAN_NOT_FOUND);
        }

        List<TransportLineNodeDTO> pathList = this.transportLineRepository.findPathList(start, end, 10, 1);
        if (CollUtil.isNotEmpty(pathList)) {
            return pathList.get(0);
        }
        return null;
    }

    /**
     * 根据调度策略查询路线
     *
     * @param startId 开始网点id
     * @param endId   结束网点id
     * @return 路线
     */
    @Override
    public TransportLineNodeDTO queryPathByDispatchMethod(Long startId, Long endId) {
        //调度方式配置
        DispatchConfigurationDTO configuration = this.dispatchConfigurationService.findConfiguration();
        int method = configuration.getDispatchMethod();

        //调度方式，1转运次数最少，2成本最低
        if (ObjectUtil.equal(DispatchMethodEnum.SHORTEST_PATH.getCode(), method)) {
            return this.queryShortestPath(startId, endId);
        } else {
            return this.findLowestPath(startId, endId);
        }
    }

    @Override
    public List<TransportLine> queryByIds(Long... ids) {
        return this.transportLineRepository.queryByIds(ids);
    }

    @Override
    public TransportLine queryById(Long id) {
        return this.transportLineRepository.queryById(id);
    }
}

```
## 7.4、路线成本
### 7.4.1、需求
在后台系统中，可以针对路线成本进行设置：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1672739245626-9374c750-b539-4a28-9577-c504eba8a817.png#averageHue=%23fefbfb&clientId=ud4740818-830b-4&from=paste&height=394&id=u29ee4b71&name=image.png&originHeight=591&originWidth=810&originalType=binary&ratio=1&rotation=0&showTitle=false&size=23672&status=done&style=shadow&taskId=u612bd544-b621-4587-9924-df94b7e232b&title=&width=540)
计算路线成本：距离 * 每公里平均成本
### 7.4.2、Controller
```java
package com.sl.transport.controller;

import com.sl.transport.domain.CostConfigurationDTO;
import com.sl.transport.service.CostConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 成本配置相关业务对外提供接口服务
 */
@Api(tags = "成本配置")
@RequestMapping("cost-configuration")
@Validated
@RestController
public class CostConfigurationController {
    @Resource
    private CostConfigurationService costConfigurationService;

    @ApiOperation(value = "查询成本配置")
    @GetMapping
    public List<CostConfigurationDTO> findConfiguration() {
        return costConfigurationService.findConfiguration();
    }

    @ApiOperation(value = "保存成本配置")
    @PostMapping
    public void saveConfiguration(@RequestBody List<CostConfigurationDTO> dto) {
        costConfigurationService.saveConfiguration(dto);
    }
}

```
### 7.4.3、Service
```java
package com.sl.transport.service;

import com.sl.transport.domain.CostConfigurationDTO;

import java.util.List;

/**
 * 成本配置相关业务
 */
public interface CostConfigurationService {
    /**
     * 查询成本配置
     *
     * @return 成本配置
     */
    List<CostConfigurationDTO> findConfiguration();

    /**
     * 保存成本配置
     * @param dto 成本配置
     */
    void saveConfiguration(List<CostConfigurationDTO> dto);

    /**
     * 查询成本根据类型
     * @param type 类型
     * @return 成本
     */
    Double findCostByType(Integer type);
}

```
### 7.4.4、ServiceImpl
```java
package com.sl.transport.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.sl.transport.common.exception.SLException;
import com.sl.transport.domain.CostConfigurationDTO;
import com.sl.transport.enums.ExceptionEnum;
import com.sl.transport.enums.TransportLineEnum;
import com.sl.transport.service.CostConfigurationService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 成本配置相关业务
 */
@Service
public class CostConfigurationServiceImpl implements CostConfigurationService {

    /**
     * 成本配置 redis key
     */
    private static final String SL_TRANSPORT_COST_REDIS_KEY = "SL_TRANSPORT_COST_CONFIGURATION";

    /**
     * 默认成本配置
     */
    private static final Map<Object, Object> DEFAULT_COST = Map.of(
            TransportLineEnum.TRUNK_LINE.getCode(), 0.8,
            TransportLineEnum.BRANCH_LINE.getCode(), 1.2,
            TransportLineEnum.CONNECT_LINE.getCode(), 1.5);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询成本配置
     *
     * @return 成本配置
     */
    @Override
    public List<CostConfigurationDTO> findConfiguration() {
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(SL_TRANSPORT_COST_REDIS_KEY);
        if (ObjectUtil.isEmpty(entries)) {
            // 使用默认值
            entries = DEFAULT_COST;
        }
        // 返回
        return entries.entrySet().stream()
                .map(v -> new CostConfigurationDTO(Convert.toInt(v.getKey()), Convert.toDouble(v.getValue())))
                .collect(Collectors.toList());
    }

    /**
     * 保存成本配置
     *
     * @param dto 成本配置
     */
    @Override
    public void saveConfiguration(List<CostConfigurationDTO> dto) {
        Map<Object, Object> map = dto.stream().collect(Collectors.toMap(v -> v.getTransportLineType().toString(), v -> v.getCost().toString()));
        stringRedisTemplate.opsForHash().putAll(SL_TRANSPORT_COST_REDIS_KEY, map);
    }

    /**
     * 查询成本根据类型
     *
     * @param type 类型
     * @return 成本
     */
    @Override
    public Double findCostByType(Integer type) {
        if (ObjectUtil.isEmpty(type)) {
            throw new SLException(ExceptionEnum.TRANSPORT_LINE_TYPE_ERROR);
        }
        // 查询redis
        Object o = stringRedisTemplate.opsForHash().get(SL_TRANSPORT_COST_REDIS_KEY, type.toString());
        if (ObjectUtil.isNotEmpty(o)) {
            return Convert.toDouble(o);
        }
        // 返回默认值
        return Convert.toDouble(DEFAULT_COST.get(type));
    }
}

```
## 
## 7.5、测试
新增路线：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661563052056-34141345-7bc5-41f2-a34d-a3b9beae46d5.png#averageHue=%23fdfdfd&clientId=udb28364f-6735-4&errorMessage=unknown%20error&from=paste&height=312&id=u3fb6a21c&name=image.png&originHeight=515&originWidth=1416&originalType=binary&ratio=1&rotation=0&showTitle=false&size=49702&status=error&style=shadow&taskId=u75facf2f-54c9-4a16-8476-75f217ab7d0&title=&width=858.1817685802478)
新增成功：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661563089299-0d3cc317-2c7d-4854-9b2a-a250a9cb942d.png#averageHue=%23f3f9fd&clientId=udb28364f-6735-4&errorMessage=unknown%20error&from=paste&height=293&id=u544fce89&name=image.png&originHeight=483&originWidth=718&originalType=binary&ratio=1&rotation=0&showTitle=false&size=23729&status=error&style=shadow&taskId=u863170e9-d353-4e4d-b720-ffe463467c5&title=&width=435.15149000043635)
同理可以测试其他类型路线。
查询路线列表：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661563794201-c7b75693-9647-42d4-be58-9ecba56c617d.png#averageHue=%23fdfdfd&clientId=udb28364f-6735-4&errorMessage=unknown%20error&from=paste&height=419&id=uec442c85&name=image.png&originHeight=691&originWidth=1411&originalType=binary&ratio=1&rotation=0&showTitle=false&size=49703&status=error&style=shadow&taskId=u364dfcc4-b31f-4af8-9884-66640514563&title=&width=855.1514657250915)
查询到数据：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661563882190-ec560f70-da82-42bf-9a1a-ca00757f810a.png#averageHue=%23fbfbfb&clientId=udb28364f-6735-4&errorMessage=unknown%20error&from=paste&height=433&id=u4d51d732&name=image.png&originHeight=714&originWidth=1242&originalType=binary&ratio=1&rotation=0&showTitle=false&size=133835&status=error&style=shadow&taskId=ud737ac06-6c74-4c8a-b93a-5af01142089&title=&width=752.7272292208106)
# 8、综合测试
## 8.1、功能测试
下面我们可以整合到后台管理系统中进行测试。
查询路线：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661567816771-4d0b6e4e-a281-47f7-8498-c0b6e1258e09.png#averageHue=%23e37858&clientId=udb28364f-6735-4&errorMessage=unknown%20error&from=paste&height=498&id=u852f2d0f&name=image.png&originHeight=821&originWidth=1617&originalType=binary&ratio=1&rotation=0&showTitle=false&size=68566&status=error&style=shadow&taskId=ucee044c8-efdf-4878-870b-f2620c460a7&title=&width=979.9999433575288)
新增路线：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27683667/1673420573169-6f4dad60-3b47-4908-ab3a-3cad22fe83c7.png#averageHue=%238c4832&clientId=ufea784ab-bee4-4&from=paste&height=471&id=u9908e336&name=image.png&originHeight=706&originWidth=1214&originalType=binary&ratio=1&rotation=0&showTitle=false&size=59584&status=done&style=shadow&taskId=u463b67fb-6b6e-44ee-a10e-908cd476500&title=&width=809.3333333333334)
:::danger
新增路线时路线的距离和成本系统会自动进行计算，距离是通过高德地图服务查询的实际距离，成本按照所设置的成本进行计算（同一标准在计算路线时是可行的，但是不能作为真实的成本进行利润计算），在编辑路线时可以修改距离和成本。
:::
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661568488182-5d633bbc-c8ab-4c4b-8689-c5b45d773e33.png#averageHue=%23fdfbfa&clientId=udb28364f-6735-4&errorMessage=unknown%20error&from=paste&height=290&id=uddd76d10&name=image.png&originHeight=478&originWidth=1594&originalType=binary&ratio=1&rotation=0&showTitle=false&size=60281&status=error&style=shadow&taskId=ue84e740f-1f23-4509-9a36-26fcf3c4f0d&title=&width=966.06055022381)
完善下数据：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661571304268-b79030a8-ac45-4f06-a679-71ffb05d9669.png#averageHue=%23fdfbfb&clientId=udb28364f-6735-4&errorMessage=unknown%20error&from=paste&height=416&id=u1bca9f09&name=image.png&originHeight=687&originWidth=1558&originalType=binary&ratio=1&rotation=0&showTitle=false&size=90879&status=error&style=shadow&taskId=u619283e6-f5db-4883-82eb-7d8c376b374&title=&width=944.242369666685)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661571317227-0fe2f0b7-6eb6-40c7-9b64-9445e9100bf0.png#averageHue=%23f1f8fc&clientId=udb28364f-6735-4&errorMessage=unknown%20error&from=paste&height=237&id=ue964d422&name=image.png&originHeight=391&originWidth=698&originalType=binary&ratio=1&rotation=0&showTitle=false&size=30946&status=error&style=shadow&taskId=u77ff48c3-8ffd-4095-9b99-bbff6668ce6&title=&width=423.0302785798114)
## 8.2、Jenkins构建任务
如果在路线下没有service的构建任务，就需要创建一个构建任务：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661742167871-1c9d6179-14c1-4b64-96c1-26d53c01fcc6.png#averageHue=%234eb971&clientId=ufb7a4f96-8471-4&errorMessage=unknown%20error&from=paste&height=270&id=u41b0c6f5&name=image.png&originHeight=445&originWidth=1333&originalType=binary&ratio=1&rotation=0&showTitle=false&size=63500&status=error&style=shadow&taskId=u9a065527-1432-475d-92cd-31f5fbea29a&title=&width=807.8787411846541)
点击新建任务：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661742269482-69f4ad57-f803-4328-9cd1-63fc0a6b9ab9.png#averageHue=%23fefefe&clientId=ufb7a4f96-8471-4&errorMessage=unknown%20error&from=paste&height=429&id=uf03cdc8e&name=image.png&originHeight=708&originWidth=353&originalType=binary&ratio=1&rotation=0&showTitle=false&size=45089&status=error&style=shadow&taskId=ubc6b6799-5776-4bca-8c8b-8d3edc9e1ed&title=&width=213.9393815740307)
输入任务名称，名称与工程名一致：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661742353224-2f050d2c-7d38-4d41-b11f-395e1d57ded3.png#averageHue=%23edeef2&clientId=ufb7a4f96-8471-4&errorMessage=unknown%20error&from=paste&height=146&id=u8a96be72&name=image.png&originHeight=241&originWidth=855&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15425&status=error&style=shadow&taskId=ue96b5539-4831-406e-aa86-1dfc6418afb&title=&width=518.1817882317174)
选择复制一个已有的任务：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661742375380-3885225a-e385-41a2-b0ce-3a862261f3a6.png#averageHue=%23e8eaef&clientId=ufb7a4f96-8471-4&errorMessage=unknown%20error&from=paste&height=240&id=u30c37070&name=image.png&originHeight=396&originWidth=1109&originalType=binary&ratio=1&rotation=0&showTitle=false&size=36869&status=error&style=shadow&taskId=ua1b8d6b6-4862-4465-a22d-c8b02dbe7cb&title=&width=672.1211732736546)
设置描述：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661743413625-decbbaaa-232c-4a3f-a1b7-7ff37dc24a59.png#averageHue=%23fefefe&clientId=ufb7a4f96-8471-4&errorMessage=unknown%20error&from=paste&height=233&id=u42ea34c4&name=image.png&originHeight=384&originWidth=1342&originalType=binary&ratio=1&rotation=0&showTitle=false&size=19042&status=error&style=shadow&taskId=u9f884d44-aaaf-42d1-b897-891ff3b868f&title=&width=813.3332863239355)
端口设置为：18083：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661743471714-42a66932-1409-4349-8bea-f143f4b97cfb.png#averageHue=%23fefefe&clientId=ufb7a4f96-8471-4&errorMessage=unknown%20error&from=paste&height=382&id=udf72d9ef&name=image.png&originHeight=630&originWidth=1220&originalType=binary&ratio=1&rotation=0&showTitle=false&size=30079&status=error&style=shadow&taskId=uc8123ba3-6b10-48d3-9abd-f68298cc568&title=&width=739.3938966581231)
设置名称：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661743509446-91f2dc94-de6e-447c-aadd-10735c9b558c.png#averageHue=%23fefefe&clientId=ufb7a4f96-8471-4&errorMessage=unknown%20error&from=paste&height=445&id=u2fc8bc1f&name=image.png&originHeight=735&originWidth=1200&originalType=binary&ratio=1&rotation=0&showTitle=false&size=39505&status=error&style=shadow&taskId=u26e338c7-381d-462e-b9c5-8046832c865&title=&width=727.2726852374981)
设置git地址：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661743547637-8c8f0415-81af-4450-86ab-9e3d53febf9d.png#averageHue=%23fefefe&clientId=ufb7a4f96-8471-4&errorMessage=unknown%20error&from=paste&height=388&id=uaa4203e5&name=image.png&originHeight=641&originWidth=1253&originalType=binary&ratio=1&rotation=0&showTitle=false&size=36381&status=error&style=shadow&taskId=u264402ca-dd04-4e3f-b745-fdc532b6c42&title=&width=759.3938955021542)
点击保存：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661743579353-4db2b383-38ea-41fd-b5bf-8076107686eb.png#averageHue=%23fefefe&clientId=ufb7a4f96-8471-4&errorMessage=unknown%20error&from=paste&height=204&id=u05f485a7&name=image.png&originHeight=337&originWidth=665&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15429&status=error&style=shadow&taskId=u9c6671fb-ed42-4232-9cd6-13d7532f275&title=&width=403.0302797357802)
测试构建：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661744152310-f87b8eec-e8ae-4299-aafb-390eb8a94065.png#averageHue=%23f1f1f1&clientId=ufb7a4f96-8471-4&errorMessage=unknown%20error&from=paste&height=284&id=u47109b2f&name=image.png&originHeight=469&originWidth=1179&originalType=binary&ratio=1&rotation=0&showTitle=false&size=86729&status=error&style=shadow&taskId=u0d520419-44a1-49eb-8195-042b59eb282&title=&width=714.5454132458419)
测试：[http://192.168.150.101:18083/doc.html](http://192.168.150.101:18083/doc.html)
可以正常查询到数据：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1661744272620-42de1f69-f50e-45f0-bc5a-3be3e4f8e5cf.png#averageHue=%23fbfbfb&clientId=ufb7a4f96-8471-4&errorMessage=unknown%20error&from=paste&height=415&id=u2ec17425&name=image.png&originHeight=685&originWidth=1247&originalType=binary&ratio=1&rotation=0&showTitle=false&size=133523&status=error&style=shadow&taskId=u01232123-fe85-4c41-a9fb-77a49ce4547&title=&width=755.7575320759668)
## 8.3、导入数据
前面都是基于测试数据进行测试的，实际上我们已经构造了一些初始数据，可以直接导入使用，具体命令如下：
```shell
#停止neo4j微服务
docker stop neo4j

#将数据文件neo4j.dump上传到挂载目录下：/var/lib/docker/volumes/neo4j/_data

#执行如下命令进行导入
#参数：--from：指定dump文件路径，--force：强制替换现有库
docker run -it --rm -v neo4j:/data neo4j:4.4.5 neo4j-admin load --from=/data/neo4j.dump --force

#启动neo4j微服务
docker start neo4j

#在管理工具中查询：match (n) return n

#如果需要备份数据，可以用dump命令进行导出数据（导出也是需要先停止服务），示例如下：
docker run -it --rm -v neo4j:/data neo4j:4.4.5 neo4j-admin dump --to=/data/neo4j2.dump --database=neo4j
```
导入的数据如下：
![graph.png](https://cdn.nlark.com/yuque/0/2022/png/27683667/1666612951609-0fd6ba6a-803a-450a-a2a1-077e069350c1.png#averageHue=%23090504&clientId=ub06bfded-37c8-4&errorMessage=unknown%20error&from=ui&id=uf773c47a&name=graph.png&originHeight=1074&originWidth=906&originalType=binary&ratio=1&rotation=0&showTitle=false&size=220448&status=error&style=shadow&taskId=u006a2865-0d66-44a9-a1cf-9017a07adfc&title=)
> 由于导入数据会覆盖之前插入的测试数据，会导致数据只存在权限系统中，出现脏数据，需要手动删除权限系统中对应的测试数据。

# 9、练习
## 9.1、练习1
今日代码量较大，动手编写代码实现相关的业务功能。
## 9.2、练习2
自己构造数据，进一步的完善数据和熟悉业务流程。
# 10、面试连环问
:::info
面试官问：

- 你们物流项目中的路线规划是怎么做的？
- 如何确定路线的成本和距离？成本计算规则是什么？该成本会计算到公司利润核算中吗？
- 对于路线的往返你们是怎么设计的？为什么成对创建的？
- 路线支持修改起点或终点机构吗？请说明理由。
:::

