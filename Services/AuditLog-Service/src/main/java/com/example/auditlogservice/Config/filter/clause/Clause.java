package com.example.auditlogservice.Config.filter.clause;

import com.example.auditlogservice.Config.filter.enums.Operation;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Clause {
    private String filed;
    private Operation operation;


    public Clause(String id, String s, int i) {
    }

    public Clause(String filed, Operation operation) {
        this.filed = filed;
        this.operation = operation;
    }

}
