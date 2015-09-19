package ru.datagrabber.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by cobr123 on 19.09.15.
 */
public final class ProductCategory {
    private final Integer id;
    private final String name;
    private final Set<String> tags;

    /**
     * @param id   ID раздела
     * @param name Название
     * @param tags Ключевые слова для поиска
     */
    public ProductCategory(final Integer id, final String name, final String... tags) {
        this.id = id;
        this.name = name;
        if (tags != null) {
            this.tags = Arrays.stream(tags).collect(Collectors.toSet());
        } else {
            this.tags = new HashSet<String>();
        }
    }

    public ProductCategory(final Integer id, final String name) {
        this(id, name, null);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getNameTags() {
        return tags;
    }

    public boolean tagInNameExists(final String productName) {
        for (final String tag : tags) {
            if (productName.contains(tag)) {
                return true;
            }
        }
        return false;
    }
}
