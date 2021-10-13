package Enums;

public enum ProcedureOrQueryEnum {

    // PROCEDURE
    SAVE_NEW_PERSON,
    UPDATE_PERSON,
    DELETE_PERSON,

    // QUERY
    CHECK_PERSON_EXIST_BY_EMAIL,
    SELECT_ALL_PERSON;

    public static String convertToString(ProcedureOrQueryEnum enume) {
        String strEnum;
        switch (enume) {
            case SAVE_NEW_PERSON:
                strEnum = "SAVE_NEW_PERSON(:firstname,:lastname,:mail)";
                break;
            case UPDATE_PERSON:
                strEnum = "UPDATE_PERSON(:id,:firstname,:lastname,:mail)";
                break;
            case DELETE_PERSON:
                strEnum = "DELETE_PERSON(:mail)";
                break;
            case CHECK_PERSON_EXIST_BY_EMAIL:
                strEnum = "SELECT p.* FROM PERSON p WHERE p.email LIKE ?mail";
                break;
            case SELECT_ALL_PERSON:
                strEnum = "SELECT * FROM PERSON";
                break;
            default:
                strEnum = null;
                return null;
        }

        return strEnum;
    }
}
