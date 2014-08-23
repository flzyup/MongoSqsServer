package com.ligux.mongo.sqs.server.model;

import com.alibaba.fastjson.JSON;

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
 * This class contains several status about the mongo sqs
 */
public class MongoSqsStatus {

    private String name;

//    private long maxNumber;

    private long unreadNumber;

    private String version;

    private String mongoVersion;

    public MongoSqsStatus(String name, long unreadNumber, String version, String mongoVersion) {
        this.name = name;
//        this.maxNumber = maxNumber;
        this.unreadNumber = unreadNumber;
        this.version = version;
        this.mongoVersion = mongoVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public long getMaxNumber() {
//        return maxNumber;
//    }
//
//    public void setMaxNumber(long maxNumber) {
//        this.maxNumber = maxNumber;
//    }

    public long getUnreadNumber() {
        return unreadNumber;
    }

    public void setUnreadNumber(long unreadNumber) {
        this.unreadNumber = unreadNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMongoVersion() {
        return mongoVersion;
    }

    public void setMongoVersion(String mongoVersion) {
        this.mongoVersion = mongoVersion;
    }

    @Override
    public String toString() {
        return JSON.toJSON(this).toString();
    }

    public String toString(boolean toHumanReadable) {
        if (!toHumanReadable) {
            return this.toString();
        }
        return this.version + "\n" +
                "Mongo version: " + this.mongoVersion + "\n" +
                "\n" +
                "Quene name: " + this.name + "\n" +
//                "Maxium number of queues: " + this.maxNumber + "\n" +
                "Unread number of this queue: " + this.unreadNumber;
    }
}
