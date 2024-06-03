package com.sg.bierkasse.utils;

import com.sg.bierkasse.dtos.PersonDTO;

public class PersonDTORankingWrapper {

    private PersonDTO personDTO;

    private int counter;

    public PersonDTORankingWrapper(PersonDTO personDTO, int counter) {
        this.personDTO = personDTO;
        this.counter = counter;
    }

    public PersonDTO getPersonDTO() {
        return personDTO;
    }

    public void setPersonDTO(PersonDTO personDTO) {
        this.personDTO = personDTO;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String toString() {
        return this.getPersonDTO().getState() + " " + this.getPersonDTO().getLastName() + ": " + getCounter();
    }
}
