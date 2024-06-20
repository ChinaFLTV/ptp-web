package pfp.fltv.common.utils;

import jakarta.annotation.Nonnull;

import java.util.concurrent.TimeUnit;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/19 PM 10:21:27
 * @description 与内容实体数据操作相关的工具类
 * @filename ContentUtils.java
 */

public class ContentUtils {


    /**
     * @param views                内容实体浏览量
     * @param likes                内容实体点赞量
     * @param unlikes              内容实体点踩量
     * @param collects             内容实体收藏量
     * @param comments             内容实体评论量
     * @param createTimestamp      内容实体创建时间戳
     * @param lastCollectTimestamp 内容实体最后收藏时间戳
     * @return Double 内容实体当前的实时得分
     * @author Lenovo/LiGuanda
     * @date 2024/6/19 PM 10:21:54
     * @version 1.0.0
     * @description 根据五项指标+文章发布时间和最后收藏时间来计算出当前文章的实时得分
     * @filename ContentUtils.java
     */
    public static Double calculateContentScore(@Nonnull Integer views, @Nonnull Integer likes, @Nonnull Integer unlikes, @Nonnull Integer collects,
                                               @Nonnull Integer comments, @Nonnull Long createTimestamp, @Nonnull Long lastCollectTimestamp) {

        // 2024-6-19  22:35-下面偷了个懒，让GPT帮我根据计算公式转写了一下计算代码~嘿嘿~~~
        // 计算各项指标的权重
        double logViews = Math.log10(views + 1); // 避免 log(0) 错误
        double logLikes = Math.log10(likes + 1);
        double logUnlikes = Math.log10(unlikes + 1);
        double logCollects = Math.log10(collects + 1);
        double logComments = Math.log10(comments + 1);

        // 计算加权总和
        double weightedSum = (2.0 / 15.0) * logViews +
                (3.0 / 15.0) * logLikes +
                (1.0 / 15.0) * logUnlikes +
                (5.0 / 15.0) * logCollects +
                (4.0 / 15.0) * logComments;

        // 计算 Q_scores，这里假设 Q_scores 为五项指标的加权总和乘以 4
        double Q_scores = 4 * weightedSum;

        // 计算时间因素
        long currentTime = System.currentTimeMillis();
        double Q_age = (currentTime - createTimestamp) / (double) TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS); // 文章创建时间到现在的天数
        double Q_updated = (currentTime - lastCollectTimestamp) / (double) TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS); // 最后收藏时间到现在的天数

        // 计算分母
        double denominator = Math.pow((Q_age / 2.0 + Q_updated / 2.0 + 1), 1.5); 

        // 2024-6-19  23:59-采用Math::max方法以避免分数值出现负值的偶然情况
        // 最终得分计算
        return (4 * weightedSum + (Math.max(likes - unlikes, 0)) / 5.0) / denominator;

    }


}