package com.ligux.mongo.sqs.server;

import com.alibaba.fastjson.JSON;
import com.ligux.mongo.sqs.server.db.DbUtil;
import com.ligux.mongo.sqs.server.model.MongoSqsStatus;
import com.ligux.mongo.sqs.server.util.Queue;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Version 1.0
 * <p/>
 * Date: 2014-08-23 21:03
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
 * Mongo sqs server servlet
 *
 * http://ip:port/mongosqs?name=xxx&opt=push&data=
 * opt: push, pop, status, status_json, clear
 * data: json string
 * name: mongo collection name, it also the sqs name
 **/
public class MongoSqsServerServlet extends HttpServlet {

    @Override
    public void init() {
        /**
         * initialize the mongo connection
         */
        try {
            DbUtil.initConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        DbUtil.freeConnection();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String opt = req.getParameter("opt");
        String name = req.getParameter("name");
        PrintWriter pw = resp.getWriter();
        if (opt == null || name == null) {
            pw.write("Please specify the name or the opt parameters.");
            return;
        }

        if (opt.equals("push")) {
            String data = req.getParameter("data");
            String jsonFlag = req.getParameter("json");

            Integer json = null;
            if (jsonFlag != null) {
                json = Integer.valueOf(jsonFlag);
            }

            if (data == null) {
                pw.write("Please specify the data parameter for push operation.");
                return;
            }

            try {
                Object obj = null;
                if ( json != null && json == 1) {
                    obj = JSON.parseObject(data);
                } else {
                    obj = data;
                }
                System.out.println("Json flag: " + jsonFlag);
                System.out.println("Hello: " + obj);
                BasicDBObject mongoData = new BasicDBObject().append("data", obj);
                try {
                    pw.write(Queue.push(name, mongoData));
                } catch (Exception e) {
                    e.printStackTrace();
                    pw.write("Server side got an exception: \n" + e.getMessage());
                }
            } catch (Exception e) {
                System.out.println("****JSON exception****");
                System.out.println("data is: " + data);
                e.printStackTrace();
            }
        } else if (opt.equals("pop")) {
            try {
                DBObject result = Queue.pop(name);
                pw.write(result == null ? "null" : JSON.toJSONString(result));
            } catch (Exception e) {
                e.printStackTrace();
                pw.write("Server side got an exception: \n" + e.getMessage());
            }
        } else if (opt.equals("status")) {
            try {
                MongoSqsStatus status = Queue.getStatus(name);
                pw.write(status.toString(true));
            } catch (Exception e) {
                e.printStackTrace();
                pw.write("Server side got an exception: \n" + e.getMessage());
            }
        } else if (opt.equals("status_json")) {
            try {
                MongoSqsStatus status = Queue.getStatus(name);
                pw.write(status.toString());
            } catch (Exception e) {
                e.printStackTrace();
                pw.write("Server side got an exception: \n" + e.getMessage());
            }
        } else if (opt.equals("clear")) {
            try {
                Queue.dropCollection(name);
                pw.write("STATUS: OK");
            } catch (Exception e) {
                e.printStackTrace();
                pw.write("Server side got an exception: \n" + e.getMessage());
            }
        }
        if (pw != null) {
            pw.close();
        }
    }

}
