package com.argusnft.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Objects;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Login {

    private String coseObject;

    public String getCoseObject() {
        return coseObject;
    }

    public void setCoseObject(String coseObject) {
        this.coseObject = coseObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return Objects.equals(coseObject, login.coseObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coseObject);
    }

}
