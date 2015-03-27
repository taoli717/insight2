package com.insight.generator.aggregate.model;

import com.sun.javafx.beans.IDProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Created by PC on 3/22/2015.
 */
@Document(collection="patternPrototype")
public class PatternPrototype  {

    @Id
    private String id;

    private Set<Aggregation> members;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Aggregation> getMembers() {
        return members;
    }

    public void setMembers(Set<Aggregation> members) {
        this.members = members;
    }
}
