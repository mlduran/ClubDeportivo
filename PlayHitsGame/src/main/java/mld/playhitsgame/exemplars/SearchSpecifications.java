/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import mld.playhitsgame.exemplars.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author miguel
 */
public class SearchSpecifications <T> implements Specification<T> {
    private List<SearchCriteria> searchCriteriaList;
    public SearchSpecifications() {
        this.searchCriteriaList = new ArrayList<>();
    }
    public void add(SearchCriteria criteria) {
        searchCriteriaList.add(criteria);
    }
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria criteria : searchCriteriaList) {
            switch (criteria.getSearchOperation() ) {
                case GREATER_THAN:
                    predicates.add(builder.greaterThan(root.get(criteria.getAttr()), criteria.getValue().toString()));
                    break;
                case LESS_THAN:
                    predicates.add(builder.lessThan(root.get(criteria.getAttr()), criteria.getValue().toString()));
                    break;
                case GREATER_THAN_EQUAL:
                    predicates.add(builder.greaterThanOrEqualTo(root.get((criteria.getAttr())), criteria.getValue().toString()));
                    break;
                case LESS_THAN_EQUAL:
                    predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getAttr()), criteria.getValue().toString()));
                    break;
                case NOT_EQUAL:
                    predicates.add(builder.notEqual(root.get(criteria.getAttr()), criteria.getValue()));
                    break;
                case EQUAL:
                    predicates.add(builder.equal(root.get(criteria.getAttr()), criteria.getValue()));
                    break;
                case MATCH:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getAttr())),"%" + criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case MATCH_END:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getAttr())),criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case MATCH_START:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getAttr())), "%" + criteria.getValue().toString().toLowerCase()));
                    break;
                case IN:
                    predicates.add(builder.in(root.get(criteria.getAttr())).value(criteria.getValue()));
                    break;
                case NOT_IN:
                    predicates.add(builder.not(root.get(criteria.getAttr())).in(criteria.getValue()));
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}