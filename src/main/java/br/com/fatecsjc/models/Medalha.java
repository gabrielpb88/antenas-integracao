package br.com.fatecsjc.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

@Data
public class Medalha {

    @BsonId
    @JsonIgnore
    private ObjectId id;

    private String competencia;
    private String medalha;

}
