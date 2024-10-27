import com.alibaba.fastjson2.JSON
import com.apifan.common.random.RandomSource
import org.junit.jupiter.api.Test
import kotlin.random.Random

/**
 *
 * @author Lenovo/LiGuanda
 * @date 2024/10/25 PM 9:45:59
 * @version 1.0.0
 * @description 专门用来生成SQL语句的测试
 * @filename SqlGenerateTest.kt
 *
 */

// @SpringBootTest
class SqlGenerateTest {


    /**
     *
     * @author Lenovo/LiGuanda
     * @date 2024/10/25 PM 11:58:39
     * @version 1.0.0
     * @description 生成批量插入评分记录的SQL语句的VALUES模拟数据部分
     * @filename SqlGenerateTest.kt
     *
     */
    @Test
    fun generateInsertRatesSql() {

        val random = Random(666)
        val builder = StringBuilder()

        for (i in 1..35) {


            val id = i
            val uid = random.nextInt(1, 11)
            val contentType = 2102
            val contentId = i

            val contentTagListSize = random.nextInt(3, 10)
            val contentTagList = mutableListOf<String>()
            for (j in 1..contentTagListSize) {

                contentTagList.add(RandomSource.languageSource().randomChineseIdiom())

            }
            val contentTags = JSON.toJSONString(contentTagList)

            val maxScore = random.nextDouble(5.0, 10.0)
            val minScore = random.nextDouble(0.0, 5.0)
            val averageScore = (minScore + maxScore) / 2
            val rateUserCount = random.nextLong(20, 10000)

            val rateUserCountMap = mutableMapOf<String, Long>()

            for (j in 0..9) {

                rateUserCountMap["$j.X"] = random.nextLong(0, 10000)

            }

            val rateMap = mutableMapOf<String, Double>()
            rateMap["AUTHENTICITY"] = String.format("%.1f", random.nextDouble(0.0, 10.0)).toDouble()
            rateMap["ACCURACY"] = String.format("%.1f", random.nextDouble(0.0, 10.0)).toDouble()
            rateMap["OBJECTIVITY"] = String.format("%.1f", random.nextDouble(0.0, 10.0)).toDouble()
            rateMap["DEPTH"] = String.format("%.1f", random.nextDouble(0.0, 10.0)).toDouble()
            rateMap["LOGICALITY"] = String.format("%.1f", random.nextDouble(0.0, 10.0)).toDouble()
            rateMap["TIMELINESS"] = String.format("%.1f", random.nextDouble(0.0, 10.0)).toDouble()

            val rateType = 2301

            builder.append("(")
                .append("$id,")
                .append("$uid,")
                .append("$contentType,")
                .append("$contentId,")
                .append("'$contentTags',")
                .append("${String.format("%.1f", averageScore)},")
                .append("${String.format("%.1f", maxScore)},")
                .append("${String.format("%.1f", minScore)},")
                .append("$rateUserCount,")
                .append("'${JSON.toJSONString(rateUserCountMap)}',")
                .append("'${JSON.toJSONString(rateMap)}',")
                .append("$rateType")
                .append(")")
                .append(if (i == 35) ";\n" else ",\n")

        }

        println("===========================generateInsertRatesSql=============================>")
        println(builder.toString())

    }


}