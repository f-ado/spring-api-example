package com.springapi.filters.specifications;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @param <F> Filter model
 * @param <M> Model
 *
 * @author Adnan Felic
 */
public abstract class BaseFilterSpecification<F, M> implements Specification<M> {

    protected F filter;

    public BaseFilterSpecification(final F filter) {
        this.filter = filter;
    }

    public abstract Predicate toPredicate(final Root<M> root,
                                          final CriteriaQuery<?> criteriaQuery,
                                          final CriteriaBuilder criteriaBuilder);

    protected static Predicate or(final CriteriaBuilder criteriaBuilder,
                                   final Predicate... predicate) {
        Predicate result = null;

        for (int i = 0; i < predicate.length; i++) {
            if (predicate[i] != null) {
                if (result == null) {
                    result = predicate[i];
                } else {
                    result = criteriaBuilder.or(result, predicate[i]);
                }
            }
        }

        return result;
    }

    protected static Predicate and(final CriteriaBuilder criteriaBuilder,
                                   final Predicate... predicate) {
        Predicate result = null;

        for (int i = 0; i < predicate.length; i++) {
            if (predicate[i] != null) {
                if (result == null) {
                    result = predicate[i];
                } else {
                    result = criteriaBuilder.and(result, predicate[i]);
                }
            }
        }

        return result;
    }
}
