package pfp.fltv.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/22 PM 10:43:29
 * @description 用于解决JavaBean的Map<String, Object>类型与JDBC中的String类型互转的问题
 * @filename Map2StringTypeHandler.java
 */

public class Map2StringTypeHandler extends BaseTypeHandler<Map<String, Object>> {


    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {

        try {

            ps.setString(i, objectMapper.writeValueAsString(parameter));

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);

        }

    }


    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {

        return string2Map(rs.getString(columnName));

    }


    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        return string2Map(rs.getString(columnIndex));

    }


    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

        return string2Map(cs.getString(columnIndex));

    }


    private Map<String, Object> string2Map(String content) throws SQLException {

        if (content == null || content.isEmpty()) {

            return null;

        }

        try {

            return objectMapper.readValue(content, new TypeReference<>() {


            });

        } catch (Exception e) {

            throw new SQLException("Failed to convert String to Map", e);

        }

    }


}