import DTO.Person;
import Enums.ProcedureOrQueryEnum;
import Enums.TypeActionEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainOracleSqlDeveloper {
    private static final String EMPTY_STRING = "";
    private static Mapper mapper = new Mapper();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String action = EMPTY_STRING;

        while (!action.equals("q")) {
            action = getActionToTake(reader);

            switch (action) {
                case "1":
                    getAllPersonInDb();
                    break;
                case "2":
                    saveNewPersonInDb(reader);
                    break;
                case "3":
                    searchPersonByMailInDb(reader);
                    break;
                case "4":
                    deletePersonByMailInDb(reader);
                    break;
                default:
                    System.out.println("\n\nNo action has been taken!\n\n\n\n");
            }
        }

    }

    public static String getActionToTake(BufferedReader reader) throws IOException {

        System.out.println("\n\n\n\n\nWitch action do you want take?");
        System.out.println("1 - Read data");
        System.out.println("2 - Add new person");
        System.out.println("3 - Update person");
        System.out.println("4 - Delete person");
        return reader.readLine();
    }

    public static List<Person> getDataResultsAfterConnectionToDb(ProcedureOrQueryEnum queryEnum, TypeActionEnum typeAction, Person person) throws ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@//localhost:1521/xe", "ROOT", "xxx123#")) {
            String query = ProcedureOrQueryEnum.convertToString(queryEnum);

            if (conn != null && query != null) {
                ResultSet results = null;
                if (TypeActionEnum.QUERY.equals(typeAction)) {
                    Statement statement = conn.createStatement();

                    if (person != null && person.getEmail() != null) {
                        query = query.replace("?mail", person.getEmail());
                    }
                    results = statement.executeQuery(query);
                } else if (TypeActionEnum.PROCEDURE.equals(typeAction)) {
                    CallableStatement callStatement = conn.prepareCall("{CALL " + query + "}");
                    if (person != null) {
                        if (person.getId() != null) {
                            callStatement.setString(":id", person.getId().toString());
                        }
                        if (person.getFirstname() != null) {
                            callStatement.setString(":firstname", person.getFirstname());
                        }
                        if (person.getLastname() != null) {
                            callStatement.setString(":lastname", person.getLastname());
                        }
                        if (person.getEmail() != null) {
                            callStatement.setString(":email", person.getEmail());
                        }
                    }
                    results = callStatement.executeQuery();
                }

                if (results != null) {
                    if (ProcedureOrQueryEnum.SELECT_ALL_PERSON.equals(queryEnum)) {
                        return getPersonFromResultSet(results);
                    } else if (ProcedureOrQueryEnum.CHECK_PERSON_EXIST_BY_EMAIL.equals(queryEnum)) {
                        List<Person> persons = getPersonFromResultSet(results);
                        updatePersonInDb(persons.get(0));
                    }
                }
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void getAllPersonInDb() throws ClassNotFoundException {

        List<Person> results = getDataResultsAfterConnectionToDb(ProcedureOrQueryEnum.SELECT_ALL_PERSON, TypeActionEnum.QUERY, null);

        if (results != null) {
            String leftAlignFormat = "| %-5s | %-12s | %-14s | %-23s |%n";

            System.out.format("+-------+--------------+----------------+-------------------------+%n");
            System.out.format("| ID    | FIRSTNAME    | LASTNAME       | EMAIL                   |%n");
            System.out.format("+-------+--------------+----------------+-------------------------+%n");
            for (Person person : results) {
                System.out.format(leftAlignFormat, person.getId(), person.getFirstname(), person.getLastname(), person.getEmail());
            }
            System.out.format("+-------+--------------+----------------+-------------------------+%n");
        } else {
            System.out.println("No persons registered!");
        }

    }

    private static List<Person> getPersonFromResultSet(ResultSet results) throws SQLException {
        List<Person> persons = new ArrayList<>();

        while (results.next()) {
            Person person = mapper.mapPersonByResultSet(results);
            persons.add(person);
        }
        return persons;
    }

    public static void saveNewPersonInDb(BufferedReader reader) throws IOException, ClassNotFoundException {
        String firstname = EMPTY_STRING, lastname = EMPTY_STRING, email = EMPTY_STRING;

        System.out.println("\n\nEnter your firstname");
        firstname = reader.readLine();
        System.out.println("\nEnter your lastname");
        lastname = reader.readLine();
        System.out.println("\nEnter your email");
        email = reader.readLine();

        Person newPerson = mapper.mapPersonByClearFields(firstname, lastname, email);

        List<Person> results = getDataResultsAfterConnectionToDb(ProcedureOrQueryEnum.SAVE_NEW_PERSON, TypeActionEnum.PROCEDURE, newPerson);

    }

    public static void updatePersonInDb(Person oldPerson) throws IOException, ClassNotFoundException {
        BufferedReader readerUpdate = new BufferedReader(new InputStreamReader(System.in));
        String firstname = EMPTY_STRING, lastname = EMPTY_STRING, email = EMPTY_STRING;

        System.out.println("\n\nHint: If you want keep the current value, press space!");
        System.out.println("\nEnter new firstname (old: " + oldPerson.getFirstname() + ") ");
        firstname = readerUpdate.readLine();
        System.out.println("\nEnter new lastname (old: " + oldPerson.getLastname() + ") ");
        lastname = readerUpdate.readLine();
        System.out.println("\nEnter new email (old: " + oldPerson.getEmail() + ") ");
        email = readerUpdate.readLine();

        Person updatedPerson = mapper.mapPersonWithOldPerson(firstname, lastname, email, oldPerson);

        List<Person> rsUpdate = getDataResultsAfterConnectionToDb(ProcedureOrQueryEnum.UPDATE_PERSON, TypeActionEnum.PROCEDURE, updatedPerson);

    }

    public static void searchPersonByMailInDb(BufferedReader reader) throws IOException, ClassNotFoundException {
        String email = EMPTY_STRING;
        Person oldPerson = new Person();

        System.out.println("\n\nWitch user do you want update?");
        System.out.println("Enter email");
        email = reader.readLine();

        if (!EMPTY_STRING.equals(email)) {
            oldPerson.setEmail("'%" + email + "%'");

            List<Person> results = getDataResultsAfterConnectionToDb(ProcedureOrQueryEnum.CHECK_PERSON_EXIST_BY_EMAIL, TypeActionEnum.QUERY, oldPerson);
        } else {
            System.out.println("\nNo mail entered!");
        }
    }

    public static void deletePersonByMailInDb(BufferedReader reader) throws IOException, ClassNotFoundException {
        String email = EMPTY_STRING;
        Person personToDelete = new Person();

        System.out.println("\n\nWitch user do you want delete?");
        System.out.println("Enter email");
        email = reader.readLine();

        if (!EMPTY_STRING.equals(email)) {
            personToDelete.setEmail(email);

            System.out.println("\n\nAre you sure you want to delete the user with mail: " + email + "?");
            System.out.println("Enter Y or N(any other key): ");
            String check = reader.readLine();
            if (!EMPTY_STRING.equals(check) && "y".equals(check.toLowerCase())) {
                List<Person> results = getDataResultsAfterConnectionToDb(ProcedureOrQueryEnum.DELETE_PERSON, TypeActionEnum.PROCEDURE, personToDelete);
            } else {
                System.out.println("\nAction stopped!");
            }
        } else {
            System.out.println("\nNo mail entered!");
        }
    }
}