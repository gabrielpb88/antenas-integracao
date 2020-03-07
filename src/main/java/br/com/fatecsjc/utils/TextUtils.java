package br.com.fatecsjc.utils;

import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TextUtils {

    /**
     * Converte o iterable para uma String contendo todos os objetos
     * @param iterable - Objeto a ser convertido para String
     * @return String contendo todos os objetos
     */
    public static String converter(FindIterable<Document> iterable){
        return StreamSupport.stream(iterable.spliterator(), false).map(Document::toJson)
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
