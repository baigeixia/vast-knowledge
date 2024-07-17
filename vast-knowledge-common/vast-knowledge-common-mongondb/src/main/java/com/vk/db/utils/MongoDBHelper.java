package com.vk.db.utils;


import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MongoTemplate的帮助类
 */
@Component
public class MongoDBHelper {

    /**
     * 注入template
     */
    @Autowired
    public MongoTemplate mongoTemplate;


    /**
     * 功能描述: 创建一个集合
     * 同一个集合中可以存入多个不同类型的对象，我们为了方便维护和提升性能，
     * 后续将限制一个集合中存入的对象类型，即一个集合只能存放一个类型的数据
     *
     * @param name 集合名称，相当于传统数据库的表名
     */
    public void createCollection(String name) {
        mongoTemplate.createCollection(name);
    }

    /**
     * 功能描述: 创建索引
     * 索引是顺序排列，且唯一的索引
     *
     * @param collectionName 集合名称，相当于关系型数据库中的表名
     * @param filedName      对象中的某个属性名
     */
    public String createIndex(String collectionName, String filedName) {
        // 配置索引选项
        IndexOptions options = new IndexOptions();
        // 设置为唯一
        options.unique(true);
        // 创建按filedName升序排的索引
        return mongoTemplate.getCollection(collectionName).createIndex(Indexes.ascending(filedName), options);
    }


    /**
     * 功能描述: 获取当前集合对应的所有索引的名称
     *
     * @param collectionName 集合名称
     * @return 索引名称列表
     */
    public List<String> getAllIndexes(String collectionName) {
        ListIndexesIterable<Document> list = mongoTemplate.getCollection(collectionName).listIndexes();
        // 上面的list不能直接获取size，因此初始化arrayList就不设置初始化大小了
        List<String> indexes = new ArrayList<>();
        for (Document document : list) {
            document.entrySet().forEach((key) -> {
                // 提取出索引的名称
                if (key.getKey().equals("name")) {
                    indexes.add(key.getValue().toString());
                }
            });
        }
        return indexes;
    }

    /**
     * 插入文档到指定集合。
     *
     * @param info           要插入的文档
     * @param collectionName 集合名称
     * @param <T>            文档类型
     * @return 插入的文档或 null 如果插入失败
     */
    public <T> boolean insert(T info, String collectionName) {
        return !ObjectUtils.isEmpty(mongoTemplate.insert(info, collectionName));
    }

    /**
     * 功能描述: 往对应的集合中批量插入数据，注意批量的数据中不要包含重复的id
     *
     * @param infos 对象列表
     */
    public <T> boolean insertMulti(List<T> infos, String collectionName) {
        Collection<T> insert = mongoTemplate.insert(infos, collectionName);
        return insert.size() == infos.size();
    }

    public <T> boolean update(Query query, Update update, String collectionName, Class<T> entityClass) {
        return mongoTemplate.updateMulti(query, update, entityClass, collectionName).wasAcknowledged();
    }


    /**
     * 功能描述: 根据id删除集合中的内容
     *
     * @param id             序列id
     * @param collectionName 集合名称
     * @param clazz          集合中对象的类型
     */

    public <T> boolean deleteById(String id, Class<T> clazz, String collectionName) {
        // 设置查询条件，当id=#{id}
        Query query = new Query(Criteria.where("id").is(id));
        return deleteByQuery(collectionName, query, clazz);
    }

    /**
     * 功能描述: 根据id查询信息
     *
     * @param id             注解
     * @param clazz          类型
     * @param collectionName 集合名称
     */

    public <T> T selectById(String id, Class<T> clazz, String collectionName) {
        // 查询对象的时候，不仅需要传入id这个唯一键，还需要传入对象的类型，以及集合的名称
        return mongoTemplate.findById(id, clazz, collectionName);
    }


    public boolean deleteByQuery(String collectName, Query query, Class<?> clazz) {
        return mongoTemplate.remove(query, clazz, collectName).wasAcknowledged();
    }

    /**
     * 功能描述: 根据条件查询集合
     *
     * @param collectName 集合名称
     * @param query       查询条件
     * @param clazz       对象类型
     */
    public <T> List<T> selectByQuery(String collectName, Query query, Class<T> clazz) {
        return mongoTemplate.find(query, clazz, collectName);
    }


    public <T> T selectOneByQuery(String collectName, Query query, Class<T> clazz) {
        return mongoTemplate.findOne(query, clazz, collectName);
    }


    public long countByQuery(String collectName, Query query) {
        return mongoTemplate.count(query, collectName);
    }


    public <T> AggregationResults<T> aggregate(Aggregation aggregation, String collectionName, Class<T> outputType) {
        return mongoTemplate.aggregate(aggregation, collectionName, outputType);
    }


    public <T> List<T> aggregateData(Aggregation aggregation, String collectionName, Class<T> outputType) {
        return aggregate(aggregation, collectionName, outputType).getMappedResults();
    }


}
