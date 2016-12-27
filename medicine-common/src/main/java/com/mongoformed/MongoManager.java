package com.mongoformed;

import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wubo5 on 2016/12/26.
 */
public class MongoManager {
    private static Logger log = LoggerFactory.getLogger(MongoManager.class);
    private static Mongo mongo;
    private DB db;
    static {
        try {
            init();
        } catch (UnknownHostException e) {
            log.error("MongoManager error is ",e);
        }
    }
    private static void init() throws UnknownHostException {
        if(MongoConfig.getHost() == null ||MongoConfig.getHost().length==0){
            throw new NumberFormatException("HOST IS NULL");
        }
        if (MongoConfig.getPort() == null || MongoConfig.getPort().length == 0) {
            throw new NumberFormatException("PORT IS NULL");
        }
        if (MongoConfig.getHost().length != MongoConfig.getPort().length) {
            throw new NumberFormatException("HOST'S LENGTH IS NOT EQUALS PORT'S LENGTH");
        }

        List<ServerAddress> replicaSetSeeds = new ArrayList<ServerAddress>();
        for (int i = 0; i < MongoConfig.getHost().length; i++) {
            replicaSetSeeds.add(new ServerAddress(MongoConfig.getHost()[i],MongoConfig.getPort()[i]));
        }

        MongoOptions options = new MongoOptions();
        options.threadsAllowedToBlockForConnectionMultiplier = MongoConfig.getThreadsAllowedToBlockForConnectionMultiplier();
        mongo = new Mongo(replicaSetSeeds, options);
        //从服务器可读  
        mongo.setReadPreference(ReadPreference.SECONDARY);
    }
    public MongoManager() {
        this(MongoConfig.getDbName(), MongoConfig.getUserName(), MongoConfig.getPwd());
    }

    public MongoManager(String dbName, String userName, String pwd) {
        if (dbName == null || "".equals(dbName)) {
            throw new NumberFormatException("dbName is null");
        }
        db = mongo.getDB(dbName);
        if(MongoConfig.isAuthentication() && !db.isAuthenticated()){
            if (userName == null || "".equals(userName)) {
                throw new NumberFormatException("userName is null");
            }
            if (pwd == null || "".equals(pwd)) {
                throw new NumberFormatException("pwd is null");
            }
            db.authenticate(userName, pwd.toCharArray());
        }
    }

    public DBCollection getDBCollection(String tableName) {
        return db.getCollection(tableName);
    }

}
