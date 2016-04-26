
#闲暇
本APP是一个阅读类的APP，提供一些趣闻轶事，百科知识，以及一些电影相关信息。
##为什么开发这样的一个项目
因为自己在吃饭或排队或坐公交等其它闲暇的时候，总喜欢看一些小能吸引眼球的小文章，而我又喜欢果壳和知乎中的内容，于是就想自己也开发一个
集这些信息于一身功能的客户端，没有让人皱眉的启动页广告，没有应用推荐，没有后台的消息推送，而且样式自己定制，挺爽的。至于电影那个module，
因为我是参照网上简书上的一个介绍猫眼API的文章做的，有些API他没有介绍，我也没自己去分析了，只做了首页。

##目前已有功能
- 主界面四个Tab，第一个tab调用果壳的API根据channel查询数据然后显示，第二个tab根据知乎日报的两个api查询数据（具体见项目代码），第三个tab调用猫眼
电影API显示最近上映电影的相关介绍
- 详情页采用webview+SimpleDraweeView显示，包括收藏和分享功能

##目前效果
- 首页列表展示，采用cardview
- 文章详情页，webview加载html
- 自定义图片选择器

##项目描述
- 本项目采用MVP架构模式，rxandroid+retrofit2+dagger2+greendao，图片显示框架用的是fresco。
- 首页底部tab采用自定义控件实现微信的tab颜色渐变样式（根据hyman的视频而作）。
- 用户反馈模块使用自定义控件实现图片选择器。
- 采用开源库SwipeBackActivity实现滑动返回。
- butterknife注解实现快速开发，smarttablayout轻易实现美观的tablayout。
- 下拉刷新和上拉加载更多使用SwipeToLoadLayout开源库。
- greendao操作数据库，简单快速。
- dagger2组织项目的API，DB，Helper，presenter,activity，实现松耦合，项目结构一目了然。

##版权声明
本项目为开源项目，项目接入了果壳网、知乎日报、猫眼电影的API，所有最终解释权归API的提供方所有。
