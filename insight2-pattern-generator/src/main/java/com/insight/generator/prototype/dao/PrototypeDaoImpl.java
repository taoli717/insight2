package com.insight.generator.prototype.dao;

import com.insight.model.PatternPrototype;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by PC on 3/23/2015.
 */
@Service
public class PrototypeDaoImpl implements PrototypeDao {

    @Autowired
    private MongoOperations mongoOperation;

    private static final Logger logger = Logger.getLogger(PrototypeDaoImpl.class);

    @Override
    public PatternPrototype get(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        PatternPrototype pp = (PatternPrototype) mongoOperation.findOne(query, PatternPrototype.class);
        return pp;
    }

    @Override
    public void save(PatternPrototype pp) {
        mongoOperation.save(pp);
    }

    @Override
    public void cleanAndReduce(int sizeThreshold ){
        Query query = new Query();
        CloseableIterator<PatternPrototype> pps =  mongoOperation.stream(query, PatternPrototype.class);
        while(pps.hasNext()){
            PatternPrototype pp = pps.next();
            logger.info(pp.getId() + " size: " + pp.getMembers().size());
            if(pp.getMembers().size() <= sizeThreshold){
                mongoOperation.remove(pp);
            }else{
                pp.setSize(Long.valueOf(pp.getMembers().size()));
                Query query1 = new Query();
                query1.addCriteria(Criteria.where("_id").is(pp.getId()));
                Update update = new Update();
                update.set("size", pp.getSize());
                mongoOperation.updateFirst(query1, update, PatternPrototype.class);
            }
        }
    }

    @Override
    public List<PatternPrototype> filter(long sizeThreshold) {
        List<PatternPrototype> result = new LinkedList<>();
        Query query = new Query();
        CloseableIterator<PatternPrototype> pps =  mongoOperation.stream(query, PatternPrototype.class);
        while(pps.hasNext()){
            PatternPrototype pp = pps.next();
            //logger.info(pp.getId() + " size: " + pp.getMembers().size());
            if(pp.getMembers().size() > sizeThreshold){
                pp.setSize(Long.valueOf(pp.getMembers().size()));
                result.add(pp);
            }
        }
        result.sort((p1,p2) -> p2.getSize().compareTo(p1.getSize()));
        return result;
    }

}
