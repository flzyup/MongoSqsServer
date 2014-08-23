package com.ligux.mongo.sqs.server.db;

import com.ligux.mongo.sqs.server.connection.ConnectionManager;
import com.mongodb.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * Version 1.0
 * <p/>
 * Date: 2011-07-18 17:03
 * Author: flzyup@ligux.com
 * <p/>
 * Copyright © 2009-2014 LiGux.com.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Tools to manager the db collection or cursor
 */
public class DbUtil {

    public static ConnectionManager connectionManager;

    public static void initConnection() throws IOException {
        connectionManager = new ConnectionManager();
        InputStream is = DbUtil.class.getClassLoader().getResourceAsStream("mongo.properties");
        Properties prop = new Properties();

        try {
            prop.load(is);
            String ip = String.valueOf(prop.get("mongo_server_ip"));
            String db = String.valueOf(prop.get("mongo_sqs_db"));
            String connPerHost = String.valueOf(prop.get("mongo_sqs_connection_per_host"));
            MongoOptions mo = new MongoOptions();
            mo.connectionsPerHost = Integer.valueOf(connPerHost);
            Integer port = Integer.valueOf(String.valueOf(prop.get("mongo_server_port")));
//            Integer sqsMaxNumber = Integer.valueOf(String.valueOf(prop.get("mongo_sqs_max_num")));

            ServerAddress sa = new ServerAddress(ip, port);
            connectionManager.setMongo(new Mongo(sa, mo));
            connectionManager.setDb(connectionManager.getMongo().getDB(db));
//            connectionManager.setSqsMaxNumber(sqsMaxNumber);
            connectionManager.setCollectionMap(new HashMap<String, DBCollection>());

            Set<String> collections = connectionManager.getDb().getCollectionNames();
            System.out.println("Init: IP: " + ip + ". PORT: " + port + ". DB: " + db);
            for (String c : collections) {
                if (!c.matches("system.+")) {
                    connectionManager.getCollectionMap().put(c, connectionManager.getDb().getCollection(c));
                    System.out.println("Found collection: " + c);
                }
            }

        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }

    public static DBCollection initCollection(String collectionName) throws IOException {
        if (connectionManager == null) {
            initConnection();
        }
        DB db = connectionManager.getDb();
        DBCollection collection = db.getCollection(collectionName);
        if (collection == null) {
            collection = db.createCollection(collectionName, new BasicDBObject());
        }
        connectionManager.getCollectionMap().put(collectionName, collection);
        return collection;
    }

    public static DBCollection getCollection(String collectionName) throws IOException {
        if (connectionManager == null) {
            initConnection();
        }
        DB db = connectionManager.getDb();
        return db.collectionExists(collectionName) ? db.getCollection(collectionName) : null;
    }

    public static void freeConnection() {
        if (connectionManager != null) {
            Mongo mongo = connectionManager.getMongo();
            if (mongo != null) {
                mongo.close();
            }
        }
    }

    public static void freeCursor(DBCursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public static void dropCollection(String collectionName) throws Exception {
        if (connectionManager == null) {
            initConnection();
        }
        DB db = connectionManager.getDb();
        DBCollection collection = db.getCollection(collectionName);
        if (collection == null) {
            throw new NullPointerException("Collection is null, please check the collection exist.");
        }
        collection.drop();
    }



}
