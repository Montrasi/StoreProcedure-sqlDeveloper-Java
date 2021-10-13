import DTO.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mapper {
    private static final String EMPTY_STRING = "";

    public Person mapPersonByResultSet(ResultSet results) throws SQLException {
        Person person = new Person();

        person.setId(Long.parseLong(results.getString(1)));
        person.setFirstname(results.getString(2));
        person.setLastname(results.getString(3));
        person.setEmail(results.getString(4));

        return person;
    }

    public Person mapPersonByClearFields(String firstname, String lastname, String email) {
        Person person = new Person();

        person.setFirstname(firstname);
        person.setLastname(lastname);
        person.setEmail(email);

        return person;
    }

    // Map new person with also old value of person
    public Person mapPersonWithOldPerson(String firstname, String lastname, String email, Person oldPerson) {
        Person person = new Person();

        person.setId(oldPerson.getId());
        person.setFirstname(!EMPTY_STRING.equals(firstname) ? firstname : oldPerson.getFirstname());
        person.setLastname(!EMPTY_STRING.equals(lastname) ? lastname : oldPerson.getLastname());
        person.setEmail(!EMPTY_STRING.equals(email) ? email : oldPerson.getEmail());

        return person;
    }
}
