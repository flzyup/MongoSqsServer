package com.ligux.mongo.sqs.server.model;

import com.mongodb.DB;
import com.mongodb.DBCollection;

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

public class MongoDb {

    private DB db;

    private Set<DBCollection> collections;

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public Set<DBCollection> getCollections() {
        return collections;
    }

    public void setCollections(Set<DBCollection> collections) {
        this.collections = collections;
    }
}
