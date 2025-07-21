package com.example.auditlogservice.Config.filter.clause;

import com.example.auditlogservice.Config.filter.creator.AttribueCreator;
import com.example.auditlogservice.Config.filter.creator.JoinCreator;
import com.example.auditlogservice.Config.filter.enums.Operation;
import com.example.auditlogservice.Config.filter.factory.ValueFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ClauseTwoArgs extends Clause {
    private String arg1;
    private String arg2;


    public ClauseTwoArgs(String filed, Operation operation, String arg1, String arg2) {
        super(filed, operation);
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public static Predicate toPredicate(Root root, CriteriaBuilder criteriaBuilder, Clause clause) {
        ClauseTwoArgs clauseTwoArgsArg = (ClauseTwoArgs) clause;
        Join joinMap = JoinCreator.createJoin(root, clause.getFiled());
        String attribute = AttribueCreator.createAttribute(clause.getFiled());
        List<Predicate> predicates = new ArrayList<>();
        try {
            if (joinMap == null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(attribute), (Comparable) ValueFactory.toValue(root, attribute, clauseTwoArgsArg.getArg1())));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(attribute), (Comparable) ValueFactory.toValue(root, attribute, clauseTwoArgsArg.getArg2())));
            } else {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(joinMap.get(attribute), (Comparable) ValueFactory.toValue(joinMap, attribute, clauseTwoArgsArg.getArg1())));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(joinMap.get(attribute), (Comparable) ValueFactory.toValue(joinMap, attribute, clauseTwoArgsArg.getArg1())));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        } catch (IllegalArgumentException | ParseException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
