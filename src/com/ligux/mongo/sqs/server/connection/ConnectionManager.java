package com.ligux.mongo.sqs.server.connection;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import java.util.Map;

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
 * Connection Manager for the MongoDB
 */
public class ConnectionManager {
    private Mongo mongo;

    private DB db;

    private Integer sqsMaxNumber;

    private Map<String, DBCollection> collectionMap;

    public Mongo getMongo() {
        return mongo;
    }

    public void setMongo(Mongo mongo) {
        this.mongo = mongo;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public Integer getSqsMaxNumber() {
        return sqsMaxNumber;
    }

    public void setSqsMaxNumber(Integer sqsMaxNumber) {
        this.sqsMaxNumber = sqsMaxNumber;
    }

    public Map<String, DBCollection> getCollectionMap() {
        return collectionMap;
    }

    public void setCollectionMap(Map<String, DBCollection> collectionMap) {
        this.collectionMap = collectionMap;
    }
}
