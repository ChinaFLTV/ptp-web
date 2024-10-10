// 2024-9-9  20:36-该目录下的脚本仅在容器第一次启动时会被执行 , 后续再次启动将不会被执行(首次启动容器前 , 记得清空数据目录 , 否则即使在第一次启动也不会执行脚本)
// 2024-9-8  23:17-初始化数据库和集合之前 , 先将旧的/已存在的数据库/集合给清除掉 , 以避免缓存带来的意外干扰
// db.dropDatabase("ptp_web");
// use ptp_web;


db.GroupMessage.drop()
// 2024-9-9  20:16-创建存放群聊消息的GroupMessage集合(表)
db.createCollection("GroupMessage", {

    timeseries: {

        timeField: "dateTime"

    }, expireAfterSeconds: 14 * 24 * 60 * 60 // 2024-9-8  22:16-该集合下的文档最长存活时间为14d

});
// 2024-10-10  15:26-传入对象类型的参数以解决 class java.lang.String cannot be cast to class java.util.Map (java.lang.String and java.util.Map are in module java.base of loader 'bootstrap') 的错误
// 2024-9-11  21:56-由于群聊消息写少读多(写操作顶多是插入操作) , 同时为了加速条件查询于是建立索引
db.GroupMessage.createIndex({

    "senderId": 1

});
db.GroupMessage.createIndex({

    "chatRoomId": 1

});
db.GroupMessage.createIndex({

    "dateTime": 1

});


// 2024-9-9  20:19-创建存放聊天房间信息的ChatRoom集合
db.ChatRoom.drop();
db.createCollection("ChatRoom", {

    capped: true, // 2024-9-9  20:20-规定该集合为固定大小
    size: 1024 * 1024 * 1024, // 2024-9-9  20:22-该集合最大占用内存1G , 超出则删除最旧的文档
    max: 50000, // 2024-9-9  20:22-该集合最多只能容纳5w个文档 , 超出则删除最旧的文档

});


// 2024-9-8  23:27-下面是模拟插入历史聊天数据
db.GroupMessage.insertOne({

    _id: 7758521, content: "能不能给我一首歌的时间？", senderId: 9, senderAvatarUrl: "heihei", receiverId: -1, chatRoomId: 666, dataUri: "Nothing~", dateTime: new Date(), contentType: 1801, messageType: 1701

});

// 2024-9-9  20:22-插入一条默认聊天房间信息数据
db.ChatRoom.insertOne({

    _id: 666, name: "达达利亚和他的朋友们", avatarUrl: "https://m.qqkw.com/d/tx/titlepic/c263a882a7ed7f099e6b48961af58b0b.jpg", rank: 6.0, onlineUsers: [], totalUsers: []

});