package com.springapi.filters.specifications.user;

import com.springapi.domain.Gender;
import com.springapi.domain.User;
import com.springapi.filters.specifications.BaseFilterSpecification;
import com.springapi.filters.user.UserFilter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.util.List;

import static com.springapi.utils.StringUtils.convertStringToEnumList;

public class UserSpecification extends BaseFilterSpecification<UserFilter, User> {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String ACTIVE = "active";
    private static final String GENDER = "gender";

    public UserSpecification(UserFilter filter) {
        super(filter);
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return defineFilter(root, criteriaQuery, criteriaBuilder);
    }

    private Predicate defineFilter(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate result = null;

        result = and(criteriaBuilder, result, addUserFilter(root, criteriaQuery, criteriaBuilder));

        return result;
    }

    public Predicate addUserFilter(From<?, ?> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate result = null;

        if (filter.getActive() != null) {
            result = and(criteriaBuilder, result, root.get(ACTIVE).in(filter.getActive()));
        }

        if (filter.getGender() != null) {
            List<Gender> genders = convertStringToEnumList(filter.getGender(), Gender.class);
            result = and(criteriaBuilder, result, root.get(GENDER).in(genders));
        }

        if (StringUtils.isNotBlank(filter.getSearch())) {
            result = and(criteriaBuilder, result, addSearch(root, criteriaBuilder));
        }

        return result;
    }

    private Predicate addSearch(From<?, ?> root, CriteriaBuilder criteriaBuilder) {
        // TODO: This should be optimized and reduced
        return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get(FIRST_NAME)), "%" + filter.getSearch().toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get(LAST_NAME)), "%" + filter.getSearch().toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get(EMAIL)), "%" + filter.getSearch().toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get(USERNAME)), "%" + filter.getSearch().toLowerCase() + "%")
        );
    }
}
