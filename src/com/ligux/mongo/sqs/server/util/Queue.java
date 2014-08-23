package com.ligux.mongo.sqs.server.util;

import com.ligux.mongo.sqs.server.db.DbUtil;
import com.ligux.mongo.sqs.server.exception.MongoSqsException;
import com.ligux.mongo.sqs.server.model.MongoSqsStatus;
import com.mongodb.*;

import java.util.Date;

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
public class Queue {

    private static final String VERSION = "Mongo Simple Queue Service v1.0";

    /**
     * Mongo sqs structure
     * {
     *     "_id": xxx,
     *     "data": json string,
     *     "ts": timestamp
     * }
     *
     */
    /**
     *
     * @param collectionName
     * @param object
     * @throws Exception
     */
    public static String push(String collectionName, BasicDBObject object) throws Exception {
        if (object == null) {
            throw new MongoSqsException("Null object definition.");
        }
        object.put("ts", new Date().getTime());
        DBCollection collection = DbUtil.connectionManager.getCollectionMap().get(collectionName);
        if (collection == null) {
            System.out.println("init collection: " + collectionName);
            collection = DbUtil.initCollection(collectionName);
        }
//        WriteResult wr = collection.insert(object);
//        return wr.getLastError().toString();
        collection.insert(object, WriteConcern.NORMAL);
        return "SUCCESS";
    }

	private final static byte[] syn = new byte[0];

    public static synchronized DBObject pop(String collectionName) throws Exception {
        DBCollection collection = DbUtil.connectionManager.getCollectionMap().get(collectionName);
        if (collection == null) {
            throw new MongoSqsException("The collection doesn't exist in the queue.");
        }

		// Henry modified, to get god speed START
//        DBCursor cursor = collection.find().sort(new BasicDBObject("ts", 1)).limit(1);
//
//        DBObject obj = null;
//        if (cursor.hasNext()) {
//            obj = cursor.next();
//            collection.remove(new BasicDBObject("_id", obj.get("_id")));
//        }
//        DbUtil.freeCursor(cursor);
		DBObject obj;
		synchronized (syn) {
			obj = collection.findOne();
			if (obj != null) {
				collection.remove(new BasicDBObject("_id", obj.get("_id")));
			}
		}
		// Henry modified, to get god speed END
        return obj == null ? null : (DBObject)obj.get("data");
    }

    public static MongoSqsStatus getStatus(String collectionName) throws Exception {
        DBCollection collection = DbUtil.connectionManager.getCollectionMap().get(collectionName);
//        System.out.println("Collection: " + collectionName);
        if (collection == null) {
            /* for the connection doesn't  init */
            collection = DbUtil.getCollection(collectionName);
            if (collection != null) {
                DbUtil.connectionManager.getCollectionMap().put(collectionName, collection);
            } else {
                throw new MongoSqsException("The collection doesn't exist in the queue.");
            }
        }
        long unreadNumber = collection.getCount();
        String mongoVersion = DbUtil.connectionManager.getMongo().getVersion();
        return new MongoSqsStatus(collectionName,
                                    unreadNumber, Queue.VERSION, mongoVersion);
    }

    public static void dropCollection(String collectionName) throws Exception {
        DBCollection collection = DbUtil.connectionManager.getCollectionMap().get(collectionName);
        if (collection == null) {
            throw new MongoSqsException("The collection doesn't exist in the queue.");
        }
        collection.drop();
        DbUtil.connectionManager.getCollectionMap().remove(collectionName);
    }

}
