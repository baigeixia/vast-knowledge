package com.vk.article;

import com.mongodb.client.MongoDatabase;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.ApArticleContent;
import com.vk.article.domain.vo.ArticleInfoVo;
import com.vk.article.service.ApArticleContentService;
import com.vk.article.service.ApArticleService;
import com.vk.common.core.utils.uuid.UUID;
import com.vk.db.repository.article.ArticleMgRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class ArticleApplicationTests  {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ArticleMgRepository articleMgRepository;

    @Autowired
    private ApArticleContentService apArticleContentService;

    @Autowired
    private ApArticleService apArticleService;


    @Test
    void  testArticleContent(){
    }

    @Test
    void  testArticleChannles(){
        ApArticle article = new ApArticle();
        article.setTitle("11111");
        Long l = apArticleService.saveArticle(article);
        System.out.println(l);
    }

    @Test
    void  testGetArticle(){
        ApArticleContent infoContent = apArticleContentService.getInfoContent(22L);
        System.out.println(infoContent);
    }
    @Test
    void  contextLoads(){
        System.out.println(articleMgRepository.findAll());
        // ArticleMg articleMg = new ArticleMg();
        // articleMg.setId(22L);
        // articleMg.setArticleId(2222L);
        // articleMg.setContent("<p><span style=\"color: rgb(112, 207, 248)\">1.微信小程序开发者工具打开正常，上传到微信开发者平台扫码后，发现找不到，</span></p><h3>解决方式：打开调试工具，提示找不到page/index/index目录，模版项目只有page/index,进行修改多一层文件夹就可以了。或者微信后台修改地址</h3><p><span style=\"color: rgb(112, 207, 248)\"><strong>2. 代码质量报按需导入报错，在uniapp项目的manifest.json中源码试图里进行配置</strong></span></p><h2><span style=\"color: rgb(112, 207, 248)\"><strong><em class=\"the-text-italic\">3. message：Error: 上传失败：网络请求错误 非法的文件，错误信息：invalid fil？</em></strong></span></h2><p><s>没有将es6转成es5 在开发者工具勾选1</s></p><h2><code>5.进行打包后发布h5，无法访问</code></h2><blockquote><p>解决方式：将web打包路径设置成./(会默认走hash模式) <a target=\"_blank\" rel=\"noopener noreferrer nofollow\" class=\"the-text-link\" href=\"http://ask.dcloud.net.cn/article/374…\">ask.dcloud.net.cn/article/374…</a></p></blockquote><pre><code class=\"language-css\">.tiptap {\n" +
        //         "    &gt;*+* {\n" +
        //         "        margin-top: 0.75em;\n" +
        //         "    }\n" +
        //         "\n" +
        //         "    ul,\n" +
        //         "    ol {\n" +
        //         "        padding: 0 1rem;\n" +
        //         "    }\n" +
        //         "}</code></pre><h2></h2><hr class=\"the-text-horizontalRule\" contenteditable=\"true\"><ul class=\"the-text-bulletList\"><li><p><code>个人账号无法给他人使用，需要公司账号，进行认证后。</code></p></li></ul><pre><code></code></pre><p></p><p>啊啊啊啊啊啊啊啊啊啊啊</p><table style=\"minWidth: 75px\"><colgroup><col><col><col></colgroup><tbody><tr><th colspan=\"1\" rowspan=\"1\"><p style=\"text-align: center\"><strong><em class=\"the-text-italic\">张三</em></strong></p></th><th colspan=\"1\" rowspan=\"1\"><p>张三2</p></th><th colspan=\"1\" rowspan=\"1\"><p>张三</p></th></tr><tr><td colspan=\"1\" rowspan=\"1\"><p>2</p></td><td colspan=\"1\" rowspan=\"1\"><p style=\"text-align: center\"><span style=\"font-family: monospace\">张三2</span></p></td><th colspan=\"1\" rowspan=\"1\"><p style=\"text-align: center\"><mark data-color=\"#8ce99a\" style=\"background-color: #8ce99a; color: inherit\">张三3</mark></p></th></tr><tr><td colspan=\"1\" rowspan=\"1\"><blockquote><p>张三6</p></blockquote></td><td colspan=\"1\" rowspan=\"1\"><p style=\"text-align: center\"><s>张三5</s></p></td><td colspan=\"1\" rowspan=\"1\"><p><span style=\"color: rgb(149, 141, 241)\">张三4</span></p></td></tr></tbody></table><h2><strong>7. h5跨域</strong></h2><h2 style=\"text-align: center\"><strong>7. input双向绑定失效</strong></h2><p style=\"text-align: right\">基础库选择最高<br>8. uniapp解决 vendor.js 体积过大的问题</p><p><span style=\"color: rgb(185, 241, 141)\">排除引入比较大的插件、静态资源（js、img），运行时 勾选进行压缩 、分包</span></p><h4><mark data-color=\"#8ce99a\" style=\"background-color: #8ce99a; color: inherit\">9.页面参数兼容性</mark></h4><h3>route在h5可以获取，微信小程序获取失败切换成this.route在h5可以获取，微信小程序获取失败 切换成<sup>this.route</sup>在h5可以获取，<a target=\"_blank\" rel=\"noopener noreferrer nofollow\" class=\"the-text-link\" href=\"http://微信小程序获取失败切换成this.mp\">微信小程序获取失败切换成this.mp</a></h3><h4><sup>10.ucharts-tooltip</sup></h4><ol class=\"the-text-orderedList\"><li><p><span style=\"font-family: serif\">ucharts图表 h5 tooltip显示无问题，但是微信小程序tooltip不展示 原因：使用了scroll-view，在qiun-data-charts组件加上inScrollView即可</span></p></li></ol><p><span style=\"font-family: cursive\">2.无法修改tooltip样式，原因： ucharts官方表示不支持插入html文本（写入标签样式代码，不会进行解析编译，当成纯文本展示出来，返回String） ，</span><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" class=\"the-text-link\" href=\"http://详情可查看www.ucharts.cn/v2/#/ask/qu…\"><span style=\"font-family: cursive\">详情可查看www.ucharts.cn/v2/#/ask/qu…</span></a></p><h4>11.uchart 图卡、样式错乱</h4><p>开启最新基础库，启用canvas2d</p><h4>12. uview无法组件内修改原生样式</h4><p>尝试过样式穿透也是无效</p><p>解决方法：common.wxss里修改，注意选择器命名规范，避免冲突。</p><h4>14. ucharts 图表点击事件</h4><p>需求：点击图表进行下钻数据</p><ol class=\"the-text-orderedList\"><li><p>点击X轴数据点：点击后，只能获取坐标信息，无法准确获取点击的坐标轴信息（@getIndex事件）</p></li><li><p>点击柱子：点击后，会与tooltip事件冲突，并且uniapp里移动端无法在tooltip中进行点击，也无法进行修改样式（html不会被解析）。</p></li><li><p>双击柱子：直接用组件引入ucharts,点击后不知道事件类型，但是我们可以模拟，连续点击一定事件时间范围内，认为是双击，再结合@getIndex事件，就可以不影响tooltip的情况下进行点击下钻数据。</p></li></ol><h4>15. 如何进行分包？设置分包了，如何验证自己的分包配置成功？</h4><p>解决方式：<a target=\"_blank\" rel=\"noopener noreferrer nofollow\" class=\"the-text-link\" href=\"https://link.juejin.cn?target=https%3A%2F%2Fdevelopers.weixin.qq.com%2Fminiprogram%2Fdev%2Fframework%2Fsubpackages%2Findependent.html\">官方分包方法</a></p><p>通俗来讲，就是创建一个文件夹，里边存储一些你认为可以独立运行的页面、组件、静态资源，接着在pages.json 按照微信分包的目录格式进行配置，在打包的时候，就会生成一个主包和一个分包。若不配置或者配置不正确，就只有主包生成，分包方式如下：</p><pre><code>@Setter&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "@Getter&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "public&nbsp;class&nbsp;ResultResponse&lt;T&gt;&nbsp;implements&nbsp;Serializable&nbsp;{&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;private&nbsp;static&nbsp;final&nbsp;long&nbsp;serialVersionUID&nbsp;=&nbsp;-1133637474601003587L;&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;/**&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;接口响应状态码&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*/&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;private&nbsp;Integer&nbsp;code;&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;/**&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;接口响应信息&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*/&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;private&nbsp;String&nbsp;msg;&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;/**&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;接口响应的数据&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;private&nbsp;T&nbsp;data;\n" +
        //         "\n" +
        //         "}&nbsp;&nbsp;&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "</code></pre><p><code>在pages.json中配置</code></p><p><code>到这里分包配置已完成，分包后访问路径，前缀是分包的包名</code></p><p><code>运行到小程序模拟器微信开发者工具后，查看分包：</code></p><p></p><p><code>就可以看到具体的分包、主包大小</code></p><p>查看分析报告，可以看到代码中导致体积过大的问题在哪里，开发者可以依据这个进行删除不需要的文件、更换合适大小的插件、静态资源的引入方式、分包进行处理，处理后并验证自己的处理是否生效，如以下问题:</p><p><code>uni_modules引入的插件过大，导致打包一直很慢，一直卡,提醒某个包过大，跳过es6转化压缩。</code></p><p>主要是因为uni_modules会打包进主包，主包超过一定的体积，会自动跳过es6转化和压缩，这有时候会导致项目无法运行、甚至无法发布。</p><p>解决方式：</p><ol class=\"the-text-orderedList\"><li><p>按需引入。 寻找插件的定制化化包（如echart定制化），只引入需要的，减小它的体积，若不能再减了，又需要使用，就使用以下方式处理。</p></li><li><p>分包处理。把该插件当成分包里的组件进行引入,不放在uni_modules里，这样处理就不会打包在主包里，主包体积就会相应减小，就能顺利打包发布了。</p></li></ol><h4>16. 页面滚动问题</h4><p>遇到的问题：</p><p></p><p></p><p></p><p></p><p></p><p></p><ol class=\"the-text-orderedList\"><li><p>ios橡皮筋效果，导致页面滑动卡顿、不流畅，用户甚至会误触，细节上体验不佳</p></li><li><p>期望：顶部tab不动且无橡皮筋效果，仅中间内容能滑动、提高滑动流畅度，避免误触</p></li></ol><p>解决方式：</p><ol class=\"the-text-orderedList\"><li><p>关掉橡皮筋：在page.json,将\"disableScroll\"设置为false</p></li></ol><p></p> Writeediting.vue:843:20\n" +
        //         "Object { callbacks: {…}, isFocused: true, extensionStorage: {…}, options: {…}, isCapturingTransaction: false, capturedTransaction: null, extensionManager: {…}, commandManager: {…}, schema: {…}, view: {…}, … }\n" +
        //         "Writeediting.vue:839:20\n" +
        //         "Object { type: \"doc\", content: (58) […] }\n" +
        //         "Writeediting.vue:842:20\n" +
        //         "<p><span style=\"color: rgb(112, 207, 248)\">1.微信小程序开发者工具打开正常，上传到微信开发者平台扫码后，发现找不到，</span></p><h3>解决方式：打开调试工具，提示找不到page/index/index目录，模版项目只有page/index,进行修改多一层文件夹就可以了。或者微信后台修改地址</h3><p><span style=\"color: rgb(112, 207, 248)\"><strong>2. 代码质量报按需导入报错，在uniapp项目的manifest.json中源码试图里进行配置</strong></span></p><h2><span style=\"color: rgb(112, 207, 248)\"><strong><em class=\"the-text-italic\">3. message：Error: 上传失败：网络请求错误 非法的文件，错误信息：invalid fil？</em></strong></span></h2><p><s>没有将es6转成es5 在开发者工具勾选11</s></p><h2><code>5.进行打包后发布h5，无法访问</code></h2><blockquote><p>解决方式：将web打包路径设置成./(会默认走hash模式) <a target=\"_blank\" rel=\"noopener noreferrer nofollow\" class=\"the-text-link\" href=\"http://ask.dcloud.net.cn/article/374…\">ask.dcloud.net.cn/article/374…</a></p></blockquote><pre><code class=\"language-css\">.tiptap {\n" +
        //         "    &gt;*+* {\n" +
        //         "        margin-top: 0.75em;\n" +
        //         "    }\n" +
        //         "\n" +
        //         "    ul,\n" +
        //         "    ol {\n" +
        //         "        padding: 0 1rem;\n" +
        //         "    }\n" +
        //         "}</code></pre><h2></h2><hr class=\"the-text-horizontalRule\" contenteditable=\"true\"><ul class=\"the-text-bulletList\"><li><p><code>个人账号无法给他人使用，需要公司账号，进行认证后。</code></p></li></ul><pre><code></code></pre><p></p><p>啊啊啊啊啊啊啊啊啊啊啊</p><table style=\"minWidth: 75px\"><colgroup><col><col><col></colgroup><tbody><tr><th colspan=\"1\" rowspan=\"1\"><p style=\"text-align: center\"><strong><em class=\"the-text-italic\">张三</em></strong></p></th><th colspan=\"1\" rowspan=\"1\"><p>张三2</p></th><th colspan=\"1\" rowspan=\"1\"><p>张三</p></th></tr><tr><td colspan=\"1\" rowspan=\"1\"><p>2</p></td><td colspan=\"1\" rowspan=\"1\"><p style=\"text-align: center\"><span style=\"font-family: monospace\">张三2</span></p></td><th colspan=\"1\" rowspan=\"1\"><p style=\"text-align: center\"><mark data-color=\"#8ce99a\" style=\"background-color: #8ce99a; color: inherit\">张三3</mark></p></th></tr><tr><td colspan=\"1\" rowspan=\"1\"><blockquote><p>张三6</p></blockquote></td><td colspan=\"1\" rowspan=\"1\"><p style=\"text-align: center\"><s>张三5</s></p></td><td colspan=\"1\" rowspan=\"1\"><p><span style=\"color: rgb(149, 141, 241)\">张三4</span></p></td></tr></tbody></table><h2><strong>7. h5跨域</strong></h2><h2 style=\"text-align: center\"><strong>7. input双向绑定失效</strong></h2><p style=\"text-align: right\">基础库选择最高<br>8. uniapp解决 vendor.js 体积过大的问题</p><p><span style=\"color: rgb(185, 241, 141)\">排除引入比较大的插件、静态资源（js、img），运行时 勾选进行压缩 、分包</span></p><h4><mark data-color=\"#8ce99a\" style=\"background-color: #8ce99a; color: inherit\">9.页面参数兼容性</mark></h4><h3>route在h5可以获取，微信小程序获取失败切换成this.route在h5可以获取，微信小程序获取失败 切换成<sup>this.route</sup>在h5可以获取，<a target=\"_blank\" rel=\"noopener noreferrer nofollow\" class=\"the-text-link\" href=\"http://微信小程序获取失败切换成this.mp\">微信小程序获取失败切换成this.mp</a></h3><h4><sup>10.ucharts-tooltip</sup></h4><ol class=\"the-text-orderedList\"><li><p><span style=\"font-family: serif\">ucharts图表 h5 tooltip显示无问题，但是微信小程序tooltip不展示 原因：使用了scroll-view，在qiun-data-charts组件加上inScrollView即可</span></p></li></ol><p><span style=\"font-family: cursive\">2.无法修改tooltip样式，原因： ucharts官方表示不支持插入html文本（写入标签样式代码，不会进行解析编译，当成纯文本展示出来，返回String） ，</span><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" class=\"the-text-link\" href=\"http://详情可查看www.ucharts.cn/v2/#/ask/qu…\"><span style=\"font-family: cursive\">详情可查看www.ucharts.cn/v2/#/ask/qu…</span></a></p><h4>11.uchart 图卡、样式错乱</h4><p>开启最新基础库，启用canvas2d</p><h4>12. uview无法组件内修改原生样式</h4><p>尝试过样式穿透也是无效</p><p>解决方法：common.wxss里修改，注意选择器命名规范，避免冲突。</p><h4>14. ucharts 图表点击事件</h4><p>需求：点击图表进行下钻数据</p><ol class=\"the-text-orderedList\"><li><p>点击X轴数据点：点击后，只能获取坐标信息，无法准确获取点击的坐标轴信息（@getIndex事件）</p></li><li><p>点击柱子：点击后，会与tooltip事件冲突，并且uniapp里移动端无法在tooltip中进行点击，也无法进行修改样式（html不会被解析）。</p></li><li><p>双击柱子：直接用组件引入ucharts,点击后不知道事件类型，但是我们可以模拟，连续点击一定事件时间范围内，认为是双击，再结合@getIndex事件，就可以不影响tooltip的情况下进行点击下钻数据。</p></li></ol><h4>15. 如何进行分包？设置分包了，如何验证自己的分包配置成功？</h4><p>解决方式：<a target=\"_blank\" rel=\"noopener noreferrer nofollow\" class=\"the-text-link\" href=\"https://link.juejin.cn?target=https%3A%2F%2Fdevelopers.weixin.qq.com%2Fminiprogram%2Fdev%2Fframework%2Fsubpackages%2Findependent.html\">官方分包方法</a></p><p>通俗来讲，就是创建一个文件夹，里边存储一些你认为可以独立运行的页面、组件、静态资源，接着在pages.json 按照微信分包的目录格式进行配置，在打包的时候，就会生成一个主包和一个分包。若不配置或者配置不正确，就只有主包生成，分包方式如下：</p><pre><code>@Setter&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "@Getter&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "public&nbsp;class&nbsp;ResultResponse&lt;T&gt;&nbsp;implements&nbsp;Serializable&nbsp;{&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;private&nbsp;static&nbsp;final&nbsp;long&nbsp;serialVersionUID&nbsp;=&nbsp;-1133637474601003587L;&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;/**&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;接口响应状态码&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*/&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;private&nbsp;Integer&nbsp;code;&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;/**&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;接口响应信息&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*/&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;private&nbsp;String&nbsp;msg;&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;/**&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;接口响应的数据&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "&nbsp;&nbsp;&nbsp;&nbsp;private&nbsp;T&nbsp;data;\n" +
        //         "\n" +
        //         "}&nbsp;&nbsp;&nbsp;&nbsp;\n" +
        //         "\n" +
        //         "</code></pre><p><code>在pages.json中配置</code></p><p><code>到这里分包配置已完成，分包后访问路径，前缀是分包的包名</code></p><p><code>运行到小程序模拟器微信开发者工具后，查看分包：</code></p><p></p><p><code>就可以看到具体的分包、主包大小</code></p><p>查看分析报告，可以看到代码中导致体积过大的问题在哪里，开发者可以依据这个进行删除不需要的文件、更换合适大小的插件、静态资源的引入方式、分包进行处理，处理后并验证自己的处理是否生效，如以下问题:</p><p><code>uni_modules引入的插件过大，导致打包一直很慢，一直卡,提醒某个包过大，跳过es6转化压缩。</code></p><p>主要是因为uni_modules会打包进主包，主包超过一定的体积，会自动跳过es6转化和压缩，这有时候会导致项目无法运行、甚至无法发布。</p><p>解决方式：</p><ol class=\"the-text-orderedList\"><li><p>按需引入。 寻找插件的定制化化包（如echart定制化），只引入需要的，减小它的体积，若不能再减了，又需要使用，就使用以下方式处理。</p></li><li><p>分包处理。把该插件当成分包里的组件进行引入,不放在uni_modules里，这样处理就不会打包在主包里，主包体积就会相应减小，就能顺利打包发布了。</p></li></ol><h4>16. 页面滚动问题</h4><p>遇到的问题：</p><p></p><p></p><p></p><p></p><p></p><p></p><ol class=\"the-text-orderedList\"><li><p>ios橡皮筋效果，导致页面滑动卡顿、不流畅，用户甚至会误触，细节上体验不佳</p></li><li><p>期望：顶部tab不动且无橡皮筋效果，仅中间内容能滑动、提高滑动流畅度，避免误触</p></li></ol><p>解决方式：</p><ol class=\"the-text-orderedList\"><li><p>关掉橡皮筋：在page.json,将\"disableScroll\"设置为false</p></li></ol><p></p>");
        // ArticleMg insert = mongoTemplate.insert(articleMg);
        // System.out.println(insert);
        MongoDatabase db = mongoTemplate.getDb();
        System.out.println(db.getName());
        // List<ArticleMg> all = mongoTemplate.findAll(ArticleMg.class);
        // all.forEach(System.out::println);

    }
    @Test
    void  testContextSave(){
        ApArticleContent content = new ApArticleContent();
        content.setContent("88888888888888888888888888");
        content.setId(9L);
        // apArticleContentService.contentSave(content);
    }


    @Test
    void  testUUID(){
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
    }
}
