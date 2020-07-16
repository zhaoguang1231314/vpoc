package io.vizit.vpoc.vkafka;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Article {
    private long id;
    private String title;
    private String author;
    private String content;
}
