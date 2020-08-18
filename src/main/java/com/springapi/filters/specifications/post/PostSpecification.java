package com.springapi.filters.specifications.post;

import com.springapi.domain.Post;
import com.springapi.filters.post.PostFilter;
import com.springapi.filters.specifications.BaseFilterSpecification;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;

public class PostSpecification extends BaseFilterSpecification<PostFilter, Post> {

    private static final String TITLE = "title";
    private static final String OWNER = "user";
    private static final String ID = "id";

    public PostSpecification(PostFilter postFilter)  {
        super(postFilter);
    }

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return defineFilter(root, criteriaQuery, criteriaBuilder);
    }

    private Predicate defineFilter(Root<Post> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return and(criteriaBuilder, addPostFilter(root, criteriaQuery, criteriaBuilder));
    }

    public Predicate addPostFilter(From<?, ?> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate result = null;
        if (StringUtils.isNotBlank(filter.getSearch())) {
            result = and(criteriaBuilder, addSearch(root, criteriaBuilder));
        }
        if (filter.getOwnerId() != null) {
            result = and(criteriaBuilder, result, criteriaBuilder.equal(root.get(OWNER).get(ID), filter.getOwnerId()));
        }
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdAt")));
        return result;
    }

    private Predicate addSearch(From<?, ?> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get(TITLE)),
                        "%" + filter.getSearch().toLowerCase() + "%")
        );
    }

}
