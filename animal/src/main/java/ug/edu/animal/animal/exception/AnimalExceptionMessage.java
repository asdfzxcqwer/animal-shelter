package ug.edu.animal.animal.exception;

public enum AnimalExceptionMessage {
    DATE_OF_BIRTH_IS_IN_FUTURE("Date of birth is in the future"),
    I_THINK_YOUR_ANIMAL_COULD_BE_DEAD("Sorry to inform you but this animal is not present with us anymore"),
    BREEDER_NOT_PROVIDED("Breeder was not provided"),
    BREED_NOT_PROVIDED("Breed of animal not provided"),
    ANIMAL_TYPE_NOT_PROVIDED("Animal type not provided"),
    ANIMAL_NOT_FOUND("Animal not found"),
    ANIMAL_STATUS_DOES_NOT_EXIST("Animal status not found"),
    ANIMAL_IS_NOT_AVAILABLE("Animal is not available"),
    PRICE_NOT_PROVIDED("Price not provided");
    private final String message;

    AnimalExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
