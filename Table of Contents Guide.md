# Ronald-miniSpring Table of Contents Guide

本指南提供项目的目录结构概览，并对主要模块做简要说明，便于快速上手与导航。

## 顶层结构

- LICENSE — 许可证
- pom.xml — Maven 项目配置
- README.md — 项目简介与用法
- Table of Contents Guide.md — 本文档（目录与说明）
- src/ — 源码与测试

## 目录树（核心）

src/
  ├─ main/
  │  ├─ java/
  │  │  └─ com/minispring/
  │  │     ├─ aop/                      # AOP 抽象与代理实现（JDK/CGLIB），切点/通知等
  │  │     │  ├─ aspectj/               # 使用 AspectJ 表达式的切点实现
  │  │     │  └─ framework/             # AOP 基础设施：AdvisedSupport、AopProxy、ProxyFactory 等
  │  │     ├─ beans/                    # Bean 定义/创建/装配与类型转换
  │  │     │  ├─ factory/               # BeanFactory 体系、作用域、生命周期扩展
  │  │     │  │  ├─ config/             # BeanDefinition、BeanPostProcessor、Scope 等契约与实现
  │  │     │  │  ├─ support/            # 具体实现：DefaultListableBeanFactory、实例化策略等
  │  │     │  │  └─ xml/                # XML 解析：XmlBeanDefinitionReader、NamespaceHandler 等
  │  │     │  ├─ BeanWrapper.java       # 属性注入封装
  │  │     │  └─ SimpleTypeConverter.java # 字符串到基本类型/日期等转换
  │  │     ├─ context/                  # 应用上下文与事件机制
  │  │     │  ├─ support/               # ClassPathXmlApplicationContext 等具体上下文实现
  │  │     │  └─ event/                 # 事件发布与监听（多播器、上下文刷新/关闭事件）
  │  │     ├─ core/                     # 核心工具：类型转换、资源加载、环境变量等
  │  │     │  ├─ convert/               # 转换 SPI 与默认转换器注册
  │  │     │  ├─ env/                   # Environment/PropertySource 抽象
  │  │     │  └─ io/                    # 资源加载：ClassPath/File/URL Resource
  │  │     ├─ util/                     # 工具类（ClassUtils 等）
  │  │     └─ web/                      # Web 相关扩展（预留）
  │  └─ resources/
  │     └─ META-INF/                    # Spring namespace 处理器映射
  └─ test/
     ├─ java/
     │  └─ com/minispring/test/         # 单元/集成测试
     │     ├─ AopTest.java              # AOP 功能测试
     │     ├─ ApplicationContextTest.java # 上下文加载与事件测试
     │     ├─ BeanLifecycleTest.java    # Bean 生命周期与回调
     │     ├─ CircularDependencyTest.java # 循环依赖处理
     │     ├─ DependencyInjectionTest.java # 依赖注入
     │     ├─ ResourceLoaderTest.java   # 资源加载
     │     ├─ ScopeTest.java            # 作用域
     │     ├─ TypeConverterTest.java    # 类型转换
     │     └─ XmlBeanDefinitionReaderTest.java # XML 解析
     └─ resources/
        ├─ application.properties
        ├─ bean-definitions.xml
        ├─ context-namespace.xml
        ├─ spring.xml
        └─ META-INF/

## 模块简要说明

- aop
  - 提供类似 Spring AOP 的核心抽象（Pointcut/Advice/Advisor）和两种代理实现：JDK 动态代理与 CGLIB。
  - 通过 `ProxyFactory` 根据配置选择代理方式；`AspectJExpressionPointcut` 支持切点表达式。
- beans
  - 定义 Bean 的创建、属性填充、自动装配、作用域与生命周期扩展点（如 `BeanPostProcessor`）。
  - `DefaultListableBeanFactory` 负责 Bean 注册与实例化；支持多种实例化策略与构造器解析。
  - XML 配置通过 `XmlBeanDefinitionReader` 加载；命名空间由 `NamespaceHandler` 扩展。
- context
  - `ApplicationContext` 作为更高层容器，整合资源加载、事件发布与环境抽象。
  - 提供 `ClassPathXmlApplicationContext`、`FileSystemXmlApplicationContext` 等实现。
- core
  - 包含类型转换 SPI、默认转换器注册、资源抽象与环境属性体系。
- util
  - 常用工具类封装，如 `ClassUtils`。
- web
  - Web 相关接口与占位模块，为后续集成 Web 容器/请求上下文做准备。

## 快速上手

- 阅读 `README.md` 了解背景与基本用法。
- 从 `src/test/java/com/minispring/test` 中的测试用例入手，逐步了解容器启动、Bean 注册、AOP 与类型转换等能力。
- 若需扩展命名空间或自定义作用域，可参考 `beans.factory.xml` 与 `beans.factory.config` 包中的扩展点。

如需补充更多目录说明或英文版指南，请告知。
