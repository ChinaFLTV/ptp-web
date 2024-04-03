package ptp.fltv.test;

import com.alibaba.fastjson2.JSON;
import pfp.fltv.common.model.po.info.AddressInfo;
import pfp.fltv.common.model.po.info.DeviceInfo;
import pfp.fltv.common.model.po.info.LoginInfo;
import pfp.fltv.common.model.po.info.NetworkInfo;
import pfp.fltv.common.model.vo.UserLoginVo;
import ptp.fltv.web.utils.JwtUtils;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/3 下午 8:03:06
 * @description 测试JWT工具类库的加解密功能
 * @filename JwtTest.java
 */

public class JwtTest {


    public static void main(String[] args) {

        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setNickname("IU");
        userLoginVo.setPassword("7758521");
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUid(520);
        loginInfo.setLoginDatetime(Timestamp.from(Instant.now()));
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setCountry("中国");
        addressInfo.setProvince("山东");
        addressInfo.setCity("临沂");
        addressInfo.setCounty("蒙阴");
        addressInfo.setLongitude("114.15769");
        addressInfo.setLatitude("22.28552");
        loginInfo.setAddressInfo(addressInfo);
        userLoginVo.setLoginInfo(loginInfo);
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setEmulator(false);
        deviceInfo.setManufacturer("Xiaomi");
        deviceInfo.setModel("Redmi K40 Gaming");
        deviceInfo.setVersionIID(33);
        loginInfo.setDeviceInfo(deviceInfo);
        NetworkInfo networkInfo = new NetworkInfo();
        networkInfo.setIpv4("101.44.80.228");
        networkInfo.setIsp("Huawei International Pte. Ltd.");
        loginInfo.setNetworkInfo(networkInfo);

        String encodedToken = JwtUtils.encode(userLoginVo);
        System.out.println("加密后的TOKEN : " + encodedToken);

        String decodedContent = JwtUtils.decode(encodedToken);
        UserLoginVo decodedUserLoginVo = JSON.parseObject(decodedContent, UserLoginVo.class);

        System.out.println("解密后的UserLoginVo : ");
        System.out.println(decodedUserLoginVo);

    }


}
