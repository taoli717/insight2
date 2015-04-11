package com.insight.model;

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

    private long size;

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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = this.members==null ? 0 : this.members.size();
    }

}
