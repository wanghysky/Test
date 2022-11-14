# Test
完整APK

## 介绍
- 使用kotlin+协程+liveData+viewModel构造的MVVM架构的项目

##架构设计
- 增加config.gradle，收纳全局配置，依赖
- 抽取module_base 库作为APP基础库，基础控件，工具类，第三方引用等都在此模块

##代码设计
- 网络框架使用retrofit+okhttp+协程，简单封装，支持多种返回类型。
- 使用MVVM模式，相比MVC和MVP， MVVM有着更简洁的代码，更便于处理数据和展示，而且项目中对Livedata进行二次封装，通过打印日志的方式，更便于追朔业务流程和崩溃率，
  未使用Databingd是因为个人觉得dataBing在排查问题时不便于排查，并且不便于改造。
- 图片框架使用fresco，简单封装，只是对图片size做了控制
- webView层引入第三方组件，时间较紧使用第三方可以快速看到效果

##UI设计
- 视觉采用简约风，嵌套滑动+吸顶效果
- 视觉比较粗糙，应该还有很多细节可以打磨，哈哈

##其他
- 本想用自己的组件话框架，但是考虑到项目可能上Googplay对编译版本有要求，尝试升级发现工作量比较大，所以现写了简单的框架

## 功能展示
- 文章列表页
![3a06dafe17d921331ad5f660d5bc47e](https://user-images.githubusercontent.com/29591960/201714682-29e8c4dc-a37b-45f2-8df9-88b102507d43.jpg)

- 文章详情页
![2250c841de72b6819fdf824406ee952](https://user-images.githubusercontent.com/29591960/201714813-7a1e9864-f449-417a-836c-c2a585681032.jpg)

## APK下载
- https://raw.githubusercontent.com/wanghysky/Test/tree/main/app/release

## 项目地址
- [Github](https://github.com/wanghysky/Test)

