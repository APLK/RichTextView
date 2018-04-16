# RichTextView

## 仿微博输入框,支持话题,艾特好友,超链接富文本输入及显示,支持表情框输入,表情使用文字替换显示

## 参考https://github.com/CarGuo/RickText项目修改而来

![](https://github.com/APLK/RichTextView/blob/master/app/screenshot1.jpg)

![](https://github.com/APLK/RichTextView/blob/master/app/screenshot2.jpg)

![](https://github.com/APLK/RichTextView/blob/master/app/screenshot3.jpg)

## 1.RichEditText使用方法:
### 在布局文件中使用</br>
<example.ricktextview.view.richtext.RichEditText</br>
            android:id="@+id/et_content"</br>
            android:layout_width="match_parent"</br>
            android:layout_height="0dp"</br>
            android:layout_margin="5dp"</br>
            android:layout_weight="1.0"</br>
            android:background="@drawable/shape_publish_bg"</br>
            android:gravity="left"</br>
            android:hint="我也来说一说..."</br>
            android:imeOptions="actionDone"</br>
            android:padding="10dp"</br>
            android:textColor="@color/TextColorBlack"</br>
            android:textColorHint="#707070"</br>
            android:textSize="14sp"</br>
            RichEditText:richMaxLength="255"/></br>
            
### 在activity中设置
 RichEditBuilder richEditBuilder = new RichEditBuilder();</br>
        richEditBuilder.setEditText(mEtContent)</br>
                .setTopicModels(topicList)</br>
                .setUserModels(friendList)</br>
                .setColorAtUser("#FDA129")</br>
                .setColorTopic("#FF4081")</br>
                .setEditTextAtUtilJumpListener(new OnEditTextUtilJumpListener() {</br>
                    @Override</br>
                    public void notifyAt() {</br>
                        JumpUtil.goToUserList(MainActivity.this, MainActivity.REQUEST_USER_CODE_INPUT);</br>
                    }</br>

</br>
                    @Override</br>
                    public void notifyTopic() {</br>
                        JumpUtil.goToTopicList(MainActivity.this, MainActivity.REQUEST_TOPIC_CODE_INPUT);</br>
                    }</br>
                })</br>
                .builder();</br>
                
## 2.RichTextView使用方法</br>
### 在布局文件中使用</br>
<example.ricktextview.view.richtext.RichTextView</br>
        android:layout_below="@+id/tv1"</br>
        android:id="@+id/tv_content"</br>
        android:layout_width="match_parent"</br>
        android:layout_height="wrap_content"</br>
        android:lineSpacingExtra="15px"</br>
        android:padding="10dp"</br>
        android:textColor="@color/TextColorBlack"</br>
        android:textSize="14sp"</br>
        RichTextView:atColor="@color/hot"</br>
        RichTextView:emojiSize="18"</br>
        RichTextView:needNumberShow="true"</br>
        RichTextView:needUrlShow="true"</br>
        RichTextView:topicColor="@color/bg_bar_blue"/></br>
        
### 在activity中使用</br>
mTvContent.setSpanAtUserCallBackListener(new SpanAtUserCallBack() {</br>
            @Override</br>
            public void onClick(View view, UserModel userModel1) {</br>
                Toast.makeText(MainActivity.this, userModel1.getUser_name(), Toast.LENGTH_SHORT).show();</br>
            }</br>
        });</br>
        mTvContent.setSpanTopicCallBackListener(new SpanTopicCallBack() {</br>
            @Override</br>
            public void onClick(View view, TopicModel topicModel) {</br>
                Toast.makeText(MainActivity.this, topicModel.getTopicName(), Toast.LENGTH_SHORT).show();</br>
            }</br>
        });</br>
        mTvContent.setSpanUrlCallBackListener(new SpanUrlCallBack() {</br>
            @Override</br>
            public void phone(View view, String phone) {</br>
                if (phone != null && phone.length() > 0) {</br>
                    Toast.makeText(MainActivity.this, phone, Toast.LENGTH_SHORT).show();</br>
                }</br>
            }</br>
</br>
            @Override</br>
            public void url(View view, String url) {</br>
                Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();</br>
            }</br>
        });</br>
        content = "这是测试#话题话题# 文本哟 www.baidu.com " +</br>
                "来2个@某个人 @22222  @kkk " + "来2个电话 13245685478,0717225478" +</br>
                "来3个表情[发呆][眨眼][痛哭]，最后随便加点超过3行的数据就行了131657848785满3行了吗?还没有满吗?这下够了吧!";</br>
        mTvContent.setMaxLines(3);</br>
        mTvContent.setEllipsize(TextUtils.TruncateAt.END);</br>
        mTvContent.setRichText(content, nameModuleList, topicModuleList);</br>
